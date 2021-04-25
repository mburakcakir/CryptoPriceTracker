package com.mburakcakir.cryptopricetracker.ui.entry.register

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mburakcakir.cryptopricetracker.R
import com.mburakcakir.cryptopricetracker.databinding.FragmentRegisterBinding
import com.mburakcakir.cryptopricetracker.ui.entry.CustomTextWatcher
import com.mburakcakir.cryptopricetracker.util.enums.EntryState
import com.mburakcakir.cryptopricetracker.util.enums.EntryType
import com.mburakcakir.cryptopricetracker.util.navigate
import com.mburakcakir.cryptopricetracker.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private val registerViewModel: RegisterViewModel by viewModels()

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {

        checkInputAndClick()

        observeData()
    }

    private fun checkInputAndClick() {
        registerViewModel.setEntryType(EntryType.REGISTER)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.registerViewModel = registerViewModel

        binding.edtMail.afterTextChanged {
            registerViewModel.isDataChanged(
                EntryState.EMAIL,
                binding.edtMail.text.toString()
            )
        }
    }

    private fun observeData() {
        registerViewModel.entryFormState.observe(viewLifecycleOwner, {
            binding.btnRegister.isEnabled = it.isDataValid

            if (it.emailError.isNullOrEmpty().not())
                binding.edtMail.error = it.emailError

            if (!it.passwordError.isNullOrEmpty().not())
                binding.edtPassword.error = it.passwordError

        })

        registerViewModel.resultEntry.observe(viewLifecycleOwner) {
            val resultMessage = if (it) {
                this.navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
                getString(R.string.register_success)
            } else {
                getString(R.string.register_error)
            }

            requireContext() toast resultMessage
        }
    }

    private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : CustomTextWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        })
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}