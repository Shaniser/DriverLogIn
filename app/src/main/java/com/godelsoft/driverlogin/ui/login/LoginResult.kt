package com.godelsoft.driverlogin.ui.login

data class LoginResult(
    val success: LoggedInUserView? = null,

    // Error string id to display
    val error: Int? = null
)
