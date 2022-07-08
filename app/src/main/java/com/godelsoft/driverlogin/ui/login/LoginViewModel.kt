package com.godelsoft.driverlogin.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.godelsoft.driverlogin.R
import com.godelsoft.driverlogin.data.LoginRepository
import com.godelsoft.driverlogin.data.Result
import com.godelsoft.driverlogin.data.model.LoggedInUser
import com.godelsoft.driverlogin.utils.InputUtils

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult


    /**
     * Try load last session data and login via saved user
     */
    fun tryLoadSavedSession(): Result<LoggedInUser> {
        val tempUserResult = loginRepository.tryLoadSavedSession()

        if (tempUserResult is Result.Success) {
            tempUserResult.let {
                login(
                    it.data.licencePlateNumber,
                    it.data.vehicleRegistrationCertificate,
                    it.data.driversLicenseNumber)
            }
        }

        return tempUserResult
    }

    /**
     * Save login step in temp user
     */
    fun saveLoginStep(value: String?, step: LoginStep) {
        when (step) {
            LoginStep.LICENCE_PLATE_NUMBER -> {
                loginRepository.loginInProcessUser.licencePlateNumber = value
                loginRepository.loginInProcessUser.vehicleRegistrationCertificate = null
            }

            LoginStep.VEHICLE_REGISTRATION_CERTIFICATE -> {
                loginRepository.loginInProcessUser.vehicleRegistrationCertificate = value

                if (value == null)
                    loginRepository.loginInProcessUser.licencePlateNumber = null

                if (loginRepository.loginInProcessUser.licencePlateNumber == null)
                    loginRepository.loginInProcessUser.vehicleRegistrationCertificate = null
            }

            LoginStep.DRIVERS_LICENCE -> {
                loginRepository.loginInProcessUser.driversLicenseNumber = value
                if (value != null) {
                    login(
                        loginRepository.loginInProcessUser.licencePlateNumber,
                        loginRepository.loginInProcessUser.vehicleRegistrationCertificate,
                        value
                    )
                }
            }
        }
    }

    fun login(licencePlateNumber: String?, vehicleRegistrationCertificate: String?, driversLicence: String) {
        val result = loginRepository.login(licencePlateNumber, vehicleRegistrationCertificate, driversLicence)

        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = LoggedInUserView(
                    result.data.licencePlateNumber,
                    result.data.vehicleRegistrationCertificate,
                    result.data.driversLicenseNumber))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(
        licencePlateNumber: String?,
        vehicleRegistrationCertificate: String?,
        driversLicence: String?,
        loginStep: LoginStep
    ) {
        _loginForm.value = when {
            loginStep == LoginStep.LICENCE_PLATE_NUMBER
                    && !isLicencePlateNumber(licencePlateNumber) ->
                LoginState(licencePlateNumberError = R.string.invalid_licence_plate_number)

            loginStep == LoginStep.VEHICLE_REGISTRATION_CERTIFICATE
                    && !isVehicleRegistrationCertificate(vehicleRegistrationCertificate) ->
                LoginState(vehicleRegistrationCertificateError = R.string.invalid_vehicle_registration_certificate)

            loginStep == LoginStep.DRIVERS_LICENCE
                    && !isDriversLicenseNumber(driversLicence) ->
                LoginState(driversLicenseNumberError = R.string.invalid_drivers_licence)

            else -> LoginState(isDataValid = true)
        }
    }

    private fun isLicencePlateNumber(licencePlateNumber: String?): Boolean {
        if (licencePlateNumber == null) return false

        return InputUtils.isCorrectLicencePlateNumber(licencePlateNumber)
    }

    private fun isVehicleRegistrationCertificate(vehicleRegistrationCertificate: String?): Boolean {
        if (vehicleRegistrationCertificate == null) return false

        return InputUtils.isCorrectVehicleRegistrationCertificate(vehicleRegistrationCertificate)
    }

    private fun isDriversLicenseNumber(driversLicenseNumber: String?): Boolean {
        if (driversLicenseNumber == null) return false

        return InputUtils.isCorrectDriversLicenseNumber(driversLicenseNumber)
    }

    fun logout() {
        loginRepository.logout()
    }
}