package com.godelsoft.driverlogin.data

import android.content.Context
import com.godelsoft.driverlogin.R
import com.godelsoft.driverlogin.data.model.LoggedInUser
import com.godelsoft.driverlogin.data.model.TempUser
import java.io.IOException

/**
 * Data source
 * In this case using SharedPreferences
 */
class LoginDataSource(private val context: Context) {

    private val licencePlateNumberKey = context.getString(R.string.preference_key_licence_plate_number)
    private val vehicleRegistrationCertificateKey = context.getString(R.string.preference_key_vehicle_registration_certificate)
    private val driversLicenceKey = context.getString(R.string.preference_key_drivers_license_number)

    /**
     * Loading last user info via SharedPreferences
     */
    fun tryLoadSavedSession(): Result<LoggedInUser> {
        val lastSessionData = context.getSharedPreferences(
            context.getString(R.string.preference_key_last_session_data), Context.MODE_PRIVATE)

        val licencePlateNumber = lastSessionData.getString(licencePlateNumberKey, null)
        val vehicleRegistrationCertificate = lastSessionData.getString(vehicleRegistrationCertificateKey, null)
        val driversLicence = lastSessionData.getString(driversLicenceKey, null)

        return if (driversLicence == null)
                Result.Error(Exception("No driver's licence data"))
            else
                Result.Success(LoggedInUser(licencePlateNumber, vehicleRegistrationCertificate, driversLicence))
    }

    /**
     * Login with saving data in SharedPreferences
     */
    fun login(licencePlateNumber: String?, vehicleRegistrationCertificate: String?, driversLicence: String?): Result<LoggedInUser> {
        return try {
            if (driversLicence == null) throw Exception("Driver's licence must be not null")
            val user = LoggedInUser(licencePlateNumber, vehicleRegistrationCertificate, driversLicence)

            val lastSessionData = context.getSharedPreferences(
                context.getString(R.string.preference_key_last_session_data), Context.MODE_PRIVATE)

            lastSessionData
                .edit()
                .putString(licencePlateNumberKey, licencePlateNumber)
                .putString(vehicleRegistrationCertificateKey, vehicleRegistrationCertificate)
                .putString(driversLicenceKey, driversLicence)
                .apply()

            Result.Success(user)
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    /**
     * Logout via SharedPrefs data deletion
     */
    fun logout() {
        context.getSharedPreferences(
            context.getString(R.string.preference_key_last_session_data), Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }
}