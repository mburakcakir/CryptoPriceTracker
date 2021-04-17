package com.mburakcakir.cryptopricetracker.ui.entry.login

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mburakcakir.cryptopricetracker.databinding.FragmentLoginBinding
import com.mburakcakir.cryptopricetracker.ui.entry.CustomTextWatcher
import com.mburakcakir.cryptopricetracker.util.enums.EntryState
import com.mburakcakir.cryptopricetracker.util.enums.EntryType
import com.mburakcakir.cryptopricetracker.util.navigate
import com.mburakcakir.cryptopricetracker.util.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel by viewModel<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {

        loginViewModel.setEntryType(EntryType.LOGIN)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.btnRegister.setOnClickListener {
            this.navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }

        binding.edtEmail.setText("muhburcak@gmail.com")
        binding.edtPassword.setText("aaaaA4")

//        binding.btnLogin.setOnClickListener {
//            loginViewModel.login(
//                binding.edtEmail.text.toString(),
//                binding.edtPassword.text.toString()
//            )
//        }

        binding.edtEmail.afterTextChanged {
            loginViewModel.isDataChanged(
                EntryState.EMAIL,
                binding.edtEmail.text.toString()
            )
        }

        binding.edtPassword.afterTextChanged {
            loginViewModel.isDataChanged(
                EntryState.PASSWORD,
                binding.edtPassword.text.toString()
            )
        }

        loginViewModel.entryFormState.observe(viewLifecycleOwner, {
            binding.btnLogin.isEnabled = it.isDataValid

            if (!it.passwordError.isNullOrEmpty())
                binding.edtPassword.error = it.passwordError
            if (!it.emailError.isNullOrEmpty())
                binding.edtEmail.error = it.emailError
        })

        loginViewModel.isVerifiedSent.observe(viewLifecycleOwner) {
            if (it)
                loginViewModel.sendEmailVerify()
            else
                requireContext() toast "Mail adresi bulunamadı."
        }
        loginViewModel.resultEntry.observe(viewLifecycleOwner) {
            when {
                !it.success.isNullOrEmpty() -> {
                    checkUserVerifiedAndNavigate()
                    it.success
                }
                !it.loading.isNullOrEmpty() -> it.loading
                !it.warning.isNullOrEmpty() -> it.warning
                else -> it.error
            }?.let { message ->
                requireContext() toast message
            }
        }
    }

    private fun checkUserVerifiedAndNavigate() {
        val isUserVerified = loginViewModel.checkIfUserVerified()
        if (isUserVerified)
            this.navigate(LoginFragmentDirections.actionLoginFragmentToCoinFragment())
        else {
            requireContext().verifyEmail()
        }

    }

    private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        addTextChangedListener(object : CustomTextWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        })
    }

    private fun Context.verifyEmail() {
        MaterialAlertDialogBuilder(this)
            .setTitle("WARNING")
            .setMessage("Check your email to verify your address")
            .setCancelable(false)
            .setPositiveButton("Check") { dialogInterface: DialogInterface, i: Int ->
                val intent = packageManager.getLaunchIntentForPackage("com.google.android.gm")
                openGmail(intent)
            }

            .setNegativeButton("Discard") { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
            }
            .show()
    }

    private fun openGmail(intent: Intent?) {
        if (intent != null)
            startActivity(intent)
        else
            requireContext() toast "Please Install Gmail."
    }
}