package com.example.recipeapp.ui.auth


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.HomeFragment
import com.example.recipeapp.R
import com.example.recipeapp.data.UserViewModel

class LoginFragment : Fragment() {

    private lateinit var viewModel: UserViewModel
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var switchToSignUpText : TextView

    private fun saveLoginState() {
        val sharedPref = requireActivity().getSharedPreferences("RecipeAppPrefs", Context.MODE_PRIVATE)
        sharedPref.edit()
            .putBoolean("isLoggedIn", true)
            .apply()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        emailEditText = view.findViewById(R.id.editTextEmail)
        passwordEditText = view.findViewById(R.id.editTextPassword)
        loginButton = view.findViewById(R.id.buttonLogin)
        switchToSignUpText = view.findViewById(R.id.Text5)


        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(UserViewModel::class.java)

        switchToSignUpText.setOnClickListener{
            val signUpFragment = SignUpFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.main,signUpFragment)
                .commit()
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.getUserByEmail(email) { user ->
                    if (user != null && user.password == password) {
                        Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show()

                        saveLoginState()

                        parentFragmentManager.beginTransaction()
                            .replace(R.id.main, HomeFragment())
                            .commit()
                        
                    } else {
                        Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}