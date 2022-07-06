package com.godelsoft.driverlogin.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.godelsoft.driverlogin.data.LoginDataSource
import com.godelsoft.driverlogin.data.LoginRepository

class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                loginRepository = LoginRepository(
                    dataSource = LoginDataSource(context)
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}