package com.example.mypharma.Views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mypharma.Controllers.AuthentificationController
import com.example.mypharma.R
import kotlinx.android.synthetic.main.fragment_login.*
import org.jetbrains.anko.support.v4.intentFor

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val auth = AuthentificationController()
        connect.setOnClickListener { view->
            val password=  password.text.toString()
            val telephone= "+213"+user_nss.text.toString()
            auth.connectUser(telephone,password,activity,view)
        }
    }
}
