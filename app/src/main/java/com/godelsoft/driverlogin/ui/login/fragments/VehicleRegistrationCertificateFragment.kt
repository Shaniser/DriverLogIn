package com.godelsoft.driverlogin.ui.login.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.godelsoft.driverlogin.R
import com.godelsoft.driverlogin.databinding.FragmentVehicleRegistrationCertificateBinding
import com.godelsoft.driverlogin.ui.login.LoginStep
import com.godelsoft.driverlogin.ui.login.LoginViewModel

class VehicleRegistrationCertificateFragment  : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentVehicleRegistrationCertificateBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var manager: FragmentManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentVehicleRegistrationCertificateBinding
            .inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        manager = requireActivity().supportFragmentManager

        loginViewModel =
            ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)

        val vehicleRegistrationCertificateEditText = binding.vehicleRegistrationCertificateText
        val nextButton = binding.next
        val skipButton = binding.skip

        nextButton.isEnabled = false

        loginViewModel.loginState.observe(viewLifecycleOwner,
            Observer { loginState ->
                if (loginState == null) return@Observer

                nextButton.isEnabled = loginState.isDataValid
                loginState.vehicleRegistrationCertificateError?.let {
                    vehicleRegistrationCertificateEditText.error = getString(it)
                }
            })

        vehicleRegistrationCertificateEditText.addTextChangedListener {
            loginViewModel.loginDataChanged(
                null,
                vehicleRegistrationCertificateEditText.text.toString(),
                null,
                LoginStep.VEHICLE_REGISTRATION_CERTIFICATE
            )
        }

        nextButton.setOnClickListener {
            loginViewModel.saveTemp(
                vehicleRegistrationCertificateEditText.text.toString(),
                LoginStep.VEHICLE_REGISTRATION_CERTIFICATE
            )

            navController.navigate(R.id.driversLicence)
        }

        skipButton.setOnClickListener {
            CancellableActionDialog(
                getString(R.string.skip_alert_title),
                getString(R.string.skip_alert),
                getString(R.string.skip),
                getString(R.string.cancel)
            ) {
                loginViewModel.saveTemp(null, LoginStep.VEHICLE_REGISTRATION_CERTIFICATE)
                navController.navigate(R.id.driversLicence)

            }.show(manager, getString(R.string.dialog_tag))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}