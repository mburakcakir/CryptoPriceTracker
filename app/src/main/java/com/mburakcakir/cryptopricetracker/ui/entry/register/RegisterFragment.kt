package com.mburakcakir.cryptopricetracker.ui.entry.register

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mburakcakir.cryptopricetracker.databinding.FragmentRegisterBinding
import com.mburakcakir.cryptopricetracker.ui.entry.CustomTextWatcher
import com.mburakcakir.cryptopricetracker.utils.EntryState
import com.mburakcakir.cryptopricetracker.utils.EntryType
import com.mburakcakir.cryptopricetracker.utils.navigate
import com.mburakcakir.cryptopricetracker.utils.toast

class RegisterFragment : Fragment() {
    private lateinit var registerViewModel: RegisterViewModel
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

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun init() {
        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        registerViewModel.setEntryType(EntryType.REGISTER)

        binding.btnRegister.setOnClickListener {
            registerViewModel.insertUser(
                binding.edtMail.text.toString(),
                binding.edtPassword.text.toString()
            )
        }

        binding.edtMail.setText("muhburcak@gmail.com")
        binding.edtPassword.setText("aaaaA4")


        registerViewModel.entryFormState.observe(viewLifecycleOwner, {
            binding.btnRegister.isEnabled = it.isDataValid

            if (!it.emailError.isNullOrEmpty())
                binding.edtMail.error = it.emailError

            if (!it.passwordError.isNullOrEmpty())
                binding.edtPassword.error = it.passwordError

        })

        registerViewModel.resultEntry.observe(viewLifecycleOwner, {
            it.error?.let { error ->
                requireContext() toast error
            }
            it.success?.let { success ->
                this.navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
            }
        })

        binding.edtMail.afterTextChanged {
            registerViewModel.isDataChanged(
                EntryState.EMAIL,
                binding.edtMail.text.toString()
            )
        }

        binding.edtPassword.afterTextChanged {
            registerViewModel.isDataChanged(
                EntryState.PASSWORD,
                binding.edtPassword.text.toString()
            )
        }

    }

//    private fun insertUser() {
//        val email = binding.edtMail.text.toString()
//        val password = binding.edtPassword.text.toString()
//
//        val userModel = UserModel(
//            email,
//            password
//        )
//
//        registerViewModel.insertUser(email,password)
//    }

    private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : CustomTextWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        })
    }
}