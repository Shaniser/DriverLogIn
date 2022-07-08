package com.godelsoft.driverlogin.ui.login

/**
 * View of successfully logged in user
 */
data class LoggedInUserView(
    val licencePlateNumber: String?,
    val vehicleRegistrationCertificate: String?,
    val driversLicenseNumber: String
)