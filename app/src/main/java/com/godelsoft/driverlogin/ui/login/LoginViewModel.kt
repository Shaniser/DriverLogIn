package com.godelsoft.driverlogin.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.godelsoft.driverlogin.R
import com.godelsoft.driverlogin.data.LoginRepository
import com.godelsoft.driverlogin.data.Result
import com.godelsoft.driverlogin.data.model.LoggedInUser
import com.godelsoft.driverlogin.utils.InputHandling

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun tryLoadSavedSession(): Result<LoggedInUser> {
        val logInResult = loginRepository.tryLoadSavedSession()

        if (logInResult is Result.Success) {
            logInResult.let {
                login(
                    it.data.licencePlateNumber,
                    it.data.vehicleRegistrationCertificate,
                    it.data.driversLicenseNumber)
            }
        }

        return logInResult
    }

    fun saveTemp(value: String?, step: LoginStep) {
        when (step) {
            LoginStep.LICENCE_PLATE_NUMBER -> {
                loginRepository.loggingInUser.licencePlateNumber = value
                loginRepository.loggingInUser.vehicleRegistrationCertificate = null
            }

            LoginStep.VEHICLE_REGISTRATION_CERTIFICATE -> {
                loginRepository.loggingInUser.vehicleRegistrationCertificate = value

                if (value == null)
                    loginRepository.loggingInUser.licencePlateNumber = null

                if (loginRepository.loggingInUser.licencePlateNumber == null)
                    loginRepository.loggingInUser.vehicleRegistrationCertificate = null
            }

            LoginStep.DRIVERS_LICENCE -> {
                loginRepository.loggingInUser.driversLicenseNumber = value
                if (value != null) {
                    login(
                        loginRepository.loggingInUser.licencePlateNumber,
                        loginRepository.loggingInUser.vehicleRegistrationCertificate,
                        value
                    )
                }
            }
            else -> {}
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

        return InputHandling.isCorrectLicencePlateNumber(licencePlateNumber)
    }

    private fun isVehicleRegistrationCertificate(vehicleRegistrationCertificate: String?): Boolean {
        if (vehicleRegistrationCertificate == null) return false

        return InputHandling.isCorrectVehicleRegistrationCertificate(vehicleRegistrationCertificate)
    }

    private fun isDriversLicenseNumber(driversLicenseNumber: String?): Boolean {
        if (driversLicenseNumber == null) return false

        return InputHandling.isCorrectDriversLicenseNumber(driversLicenseNumber)
    }

    fun logout() {
        loginRepository.logout()
    }
}