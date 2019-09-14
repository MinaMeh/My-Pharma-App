package com.example.mypharma.Views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.mypharma.Controllers.AuthentificationController
import com.example.mypharma.Models.NewPass
import com.example.mypharma.R
import kotlinx.android.synthetic.main.fragment_changepasswd.*
import org.jetbrains.anko.support.v4.intentFor


class changePasswdFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_changepasswd, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nss     = arguments?.getInt("id",0)



        suivant.setOnClickListener { view ->
            val auth= AuthentificationController()
            val password = password.text.toString()
            if (nss !=null) {
                var newPass = NewPass(nss, password)
                auth.updatePassword(newPass, activity, view)
            }

            }

    }
}
