package com.mburakcakir.cryptopricetracker.ui.entry.login

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.EditText
import androidx.fragment.app.viewModels
import com.mburakcakir.cryptopricetracker.R
import com.mburakcakir.cryptopricetracker.databinding.FragmentLoginBinding
import com.mburakcakir.cryptopricetracker.ui.BaseFragment
import com.mburakcakir.cryptopricetracker.ui.entry.CustomTextWatcher
import com.mburakcakir.cryptopricetracker.util.enums.EntryState
import com.mburakcakir.cryptopricetracker.util.enums.EntryType
import com.mburakcakir.cryptopricetracker.util.navigate
import com.mburakcakir.cryptopricetracker.util.toast
import com.mburakcakir.cryptopricetracker.util.verifyEmail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun getFragmentView(): Int = R.layout.fragment_login

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setInputAndClick()

        observeData()
    }

    private fun setInputAndClick() {
        loginViewModel.setEntryType(EntryType.LOGIN)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.loginViewModel = loginViewModel

        binding.edtEmail.setText("muhburcak@gmail.com")
        binding.edtPassword.setText("aaaaA4")

        binding.edtEmail.afterTextChanged {
            loginViewModel.isDataChanged(
                EntryState.EMAIL,
                binding.edtEmail.text.toString()
            )
        }

        binding.btnRegister.setOnClickListener {
            this.navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }

    }

    private fun observeData() {
        loginViewModel.entryFormState.observe(viewLifecycleOwner, {
            binding.btnLogin.isEnabled = it.isDataValid

            if (it.passwordError.isNullOrEmpty().not())
                binding.edtPassword.error = it.passwordError
            if (it.emailError.isNullOrEmpty().not())
                binding.edtEmail.error = it.emailError
        })

        loginViewModel.isVerifiedSent.observe(viewLifecycleOwner) {
            if (it)
                loginViewModel.sendEmailVerify()
            else
                requireContext() toast getString(R.string.error_email_address)
        }

        loginViewModel.resultEntry.observe(viewLifecycleOwner) {
            var resultMessage = if (it) {
                checkUserVerifiedAndNavigate()
                getString(R.string.login_success)
            } else {
                getString(R.string.login_error)
            }

            requireContext() toast resultMessage
        }
    }

    private fun checkUserVerifiedAndNavigate() {
        val isUserVerified = loginViewModel.checkIfUserVerified()

        if (!isUserVerified)
            requireContext().verifyEmail()

        this.navigate(LoginFragmentDirections.actionLoginFragmentToCoinFragment())
    }

    private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        addTextChangedListener(object : CustomTextWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        })
    }
}