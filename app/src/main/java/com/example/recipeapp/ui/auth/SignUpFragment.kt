package com.example.recipeapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.HomeFragment
import com.example.recipeapp.R
import com.example.recipeapp.data.User
import com.example.recipeapp.data.UserViewModel

class SignUpFragment : Fragment() {

    private lateinit var viewModel: UserViewModel
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signUpButton: Button
    private lateinit var switchToLoginText : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        nameEditText = view.findViewById(R.id.editTextName)
        emailEditText = view.findViewById(R.id.editTextEmailSignUp)
        passwordEditText = view.findViewById(R.id.editTextPasswordSignUp)
        signUpButton = view.findViewById(R.id.buttonSignUp)
        switchToLoginText = view.findViewById(R.id.textViewSwitchToLogin)

        switchToLoginText.setOnClickListener {
            val loginFragment = LoginFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.main, loginFragment)
                .commit()
        }

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(UserViewModel::class.java)

        signUpButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!email.endsWith("@gmail.com")) {
                Toast.makeText(requireContext(), "Email must end with @gmail.com", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!password.any { it.isDigit() }) {
                Toast.makeText(requireContext(), "Password must contain at least one number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.getUserByEmail(email) { existingUser ->
                if (existingUser != null) {
                    Toast.makeText(requireContext(), "Email already registered", Toast.LENGTH_SHORT).show()
                } else {
                    val newUser = User(username = name, email = email, password = password)
                    viewModel.addUser(newUser)
                    Toast.makeText(requireContext(), "Sign up successful!", Toast.LENGTH_SHORT).show()

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.main, LoginFragment())
                        .commit()
                }
            }
        }

        return view
    }

}