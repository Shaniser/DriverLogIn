package com.godelsoft.driverlogin.data

import com.godelsoft.driverlogin.data.model.LoggedInUser
import com.godelsoft.driverlogin.data.model.TempUser

/**
 * In-memory current session data storage
 */
class LoginRepository(private val dataSource: LoginDataSource) {

    /**
     * Temp user to save data between login steps
     */
    var loginInProcessUser = TempUser()

    /**
     * In-memory cache of the loggedInUser object
     */
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    /**
     * Loading last saved user info
     */
    fun tryLoadSavedSession(): Result<LoggedInUser> = dataSource.tryLoadSavedSession()

    /**
     * Setup in-memory cache user and save data
     */
    fun login(licencePlateNumber: String?, vehicleRegistrationCertificate: String?, driversLicence: String): Result<LoggedInUser> {
        // handle login
        val result = dataSource.login(licencePlateNumber, vehicleRegistrationCertificate, driversLicence)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    /**
     * Remove in-memory cache user and clear data
     */
    fun logout() {
        user = null
        dataSource.logout()
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
    }
}