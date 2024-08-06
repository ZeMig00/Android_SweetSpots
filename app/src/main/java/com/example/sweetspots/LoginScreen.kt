package com.example.sweetspots

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sweetspots.databinding.LoginScreenBinding
import com.vishnusivadas.advanced_httpurlconnection.PutData

class LoginScreen : Fragment()  {
    private var _binding: LoginScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = LoginScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginScreen_to_registerScreen)
        }

        binding.buttonLogin.setOnClickListener {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()

            if(username != "" && password != "") {
                binding.progressBar.visibility = View.VISIBLE

                val handler = Handler(Looper.getMainLooper())
                handler.post {
                    val field = arrayOfNulls<String>(2)
                    field[0] = "idUtilizador"
                    field[1] = "PalavraPasse"
                    val data = arrayOfNulls<String>(2)
                    data[0] = username
                    data[1] = password
                    val putData = PutData("http://10.0.2.2/SweetSpots/login.php", "POST", field, data)
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            binding.progressBar.visibility = View.GONE
                            val result = putData.result
                            if (result == "Login Success") {
                                (activity as MainActivity).username = username
                                Toast.makeText(activity, result, Toast.LENGTH_SHORT).show()
                                findNavController().navigate(R.id.action_loginScreen_to_mainScreen)
                            } else {
                                Toast.makeText(activity, result, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }else{
                Toast.makeText(activity,"Todos os campos são obrigatórios!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}