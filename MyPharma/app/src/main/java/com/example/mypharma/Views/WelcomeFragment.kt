package com.example.mypharma.Views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.mypharma.R
import kotlinx.android.synthetic.main.fragment_welcome.*
import org.jetbrains.anko.support.v4.intentFor


class WelcomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signup.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_welcomeFragment_to_signupFragment)
        }
        signin.setOnClickListener { view->
            view.findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)

        }
        guest.setOnClickListener { view->
            startActivity(intentFor<Main2Activity>())
        }
    }
}
