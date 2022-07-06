package com.godelsoft.driverlogin.data

import com.godelsoft.driverlogin.data.model.LoggedInUser
import com.godelsoft.driverlogin.data.model.TempUser

class LoginRepository(private val dataSource: LoginDataSource) {

    var loggingInUser = TempUser()

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    fun tryLoadSavedSession(): Result<LoggedInUser> {
        val result = dataSource.tryLoadSavedSession()
        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }
        return result
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    fun login(licencePlateNumber: String?, vehicleRegistrationCertificate: String?, driversLicence: String): Result<LoggedInUser> {
        // handle login
        val result = dataSource.login(licencePlateNumber, vehicleRegistrationCertificate, driversLicence)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
    }
}