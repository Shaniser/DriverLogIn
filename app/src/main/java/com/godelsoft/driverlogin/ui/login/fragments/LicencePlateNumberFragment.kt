package com.godelsoft.driverlogin.ui.login.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.godelsoft.driverlogin.R
import com.godelsoft.driverlogin.databinding.FragmentLicencePlateNumberBinding
import com.godelsoft.driverlogin.ui.login.LoginStep
import com.godelsoft.driverlogin.ui.login.LoginViewModel

class LicencePlateNumberFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentLicencePlateNumberBinding? = null

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

        _binding = FragmentLicencePlateNumberBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        manager = requireActivity().supportFragmentManager

        loginViewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)

        val licencePlateNumberEditText = binding.licencePlateNumberText
        val nextButton = binding.next
        val skipButton = binding.skip

        loginViewModel.loginState.observe(viewLifecycleOwner,
        Observer { loginState ->
            if (loginState == null) return@Observer

            nextButton.isEnabled = loginState.isDataValid
            loginState.licencePlateNumberError?.let {
                licencePlateNumberEditText.error = getString(it)
            }
        })

        licencePlateNumberEditText.addTextChangedListener {
            loginViewModel.loginDataChanged(
                licencePlateNumberEditText.text.toString(),
                null,
                null,
                LoginStep.LICENCE_PLATE_NUMBER
            )
        }

        nextButton.setOnClickListener {
            loginViewModel.saveTemp(
                licencePlateNumberEditText.text.toString(),
                LoginStep.LICENCE_PLATE_NUMBER)

            navController.navigate(R.id.vehicleRegistrationCertificate)
        }

        skipButton.setOnClickListener {
            CancellableActionDialog(
                getString(R.string.skip_alert_title),
                getString(R.string.skip_alert),
                getString(R.string.skip),
                getString(R.string.cancel)
            ) {
                loginViewModel.saveTemp(null, LoginStep.LICENCE_PLATE_NUMBER)
                navController.navigate(R.id.driversLicence)

            }.show(manager, getString(R.string.dialog_tag))
        }
    }

    override fun onResume() {
        super.onResume()

        if (binding.licencePlateNumberText.text.isEmpty()) {
            binding.next.isEnabled = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}