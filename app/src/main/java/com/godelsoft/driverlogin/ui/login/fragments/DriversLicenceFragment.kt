package com.godelsoft.driverlogin.ui.login.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.godelsoft.driverlogin.R
import com.godelsoft.driverlogin.databinding.FragmentDriversLicenceBinding
import com.godelsoft.driverlogin.ui.login.LoginStep
import com.godelsoft.driverlogin.ui.login.LoginViewModel

class DriversLicenceFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentDriversLicenceBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDriversLicenceBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        loginViewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)

        val driversLicenseNumberEditText = binding.driversLicenseNumberText
        val nextButton = binding.next

        nextButton.isEnabled = false

        loginViewModel.loginState.observe(viewLifecycleOwner,
            Observer { loginState ->
                if (loginState == null) return@Observer

                nextButton.isEnabled = loginState.isDataValid
                loginState.driversLicenseNumberError?.let {
                    driversLicenseNumberEditText.error = getString(it)
                }
            })

        driversLicenseNumberEditText.addTextChangedListener {
            loginViewModel.loginDataChanged(
                null,
                null,
                driversLicenseNumberEditText.text.toString(),
                LoginStep.DRIVERS_LICENCE
            )
        }

        nextButton.setOnClickListener {
            loginViewModel.saveTemp(
                driversLicenseNumberEditText.text.toString(),
                LoginStep.DRIVERS_LICENCE)

            navController.navigate(R.id.mainMenu)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}