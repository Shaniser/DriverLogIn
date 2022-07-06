package com.godelsoft.driverlogin.ui.login.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.godelsoft.driverlogin.R
import com.godelsoft.driverlogin.data.Result
import com.godelsoft.driverlogin.databinding.FragmentMainMenuBinding
import com.godelsoft.driverlogin.ui.login.LoginViewModel
import com.godelsoft.driverlogin.ui.login.LoginViewModelFactory

class MainMenuFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentMainMenuBinding? = null

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

        _binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        manager = requireActivity().supportFragmentManager

        val licencePlateNumberText = binding.licencePlateNumberEditText
        val vehicleRegistrationCertificateText = binding.vehicleRegistrationCertificateEditText
        val driversLicenceText = binding.driversLicenceEditText
        val logout = binding.logout

        loginViewModel = ViewModelProvider(requireActivity(), LoginViewModelFactory(requireActivity()))
            .get(LoginViewModel::class.java)

        loginViewModel.loginResult.observe(viewLifecycleOwner,
            Observer { loginResult ->
                if (loginResult == null) return@Observer

                val noInfoString = getString(R.string.no_info)

                licencePlateNumberText.text =
                    loginResult.success?.licencePlateNumber ?: noInfoString
                vehicleRegistrationCertificateText.text =
                    loginResult.success?.vehicleRegistrationCertificate ?: noInfoString
                driversLicenceText.text = loginResult.success?.driversLicenseNumber
            })

        // Prev session load
        val loadLastSessionResult = loginViewModel.tryLoadSavedSession()
        if (loadLastSessionResult !is Result.Success)  {
            navController.navigate(R.id.licencePlateNumberInput)
        }

        logout.setOnClickListener {
            loginViewModel.logout()
            navController.navigate(R.id.licencePlateNumberInput)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}