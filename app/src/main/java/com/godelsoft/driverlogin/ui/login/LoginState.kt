package com.godelsoft.driverlogin.ui.login

data class LoginState(
    // Error string ids to display
    val licencePlateNumberError: Int? = null,
    val vehicleRegistrationCertificateError: Int? = null,
    val driversLicenseNumberError: Int? = null,

    val currentStep: LoginStep = LoginStep.LICENCE_PLATE_NUMBER,
    val isDataValid: Boolean = false
)
