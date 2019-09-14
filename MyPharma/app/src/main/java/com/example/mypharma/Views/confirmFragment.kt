package com.example.mypharma.Views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.example.mypharma.R
import kotlinx.android.synthetic.main.fragment_confirm.*


class confirmFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm, container, false)
    }
     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         super.onViewCreated(view, savedInstanceState)
         suivant.setOnClickListener { view ->

             val passwd= arguments?.getString("password")
             val nss= arguments?.getInt("nss")
             val bundle= bundleOf("id" to nss)


             if (password.text.toString()==passwd) {
                 view.findNavController().navigate(R.id.action_confirmFragment_to_changePasswdFragment,bundle)
             }
             else{
                 Toast.makeText(activity,"Veuillez confirmer le code d'activation", Toast.LENGTH_SHORT).show()            }
         }

     }


}
