package com.example.mypharma.Views

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mypharma.R
import kotlinx.android.synthetic.main.activity_main2.*
import org.jetbrains.anko.intentFor

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
      //  val nav = findNavController(R.id.navigation)
        //NavigationUI.setupActionBarWithNavController(this,nav)
        val navController= findNavController(R.id.fragment)
        setupActionBarWithNavController(navController)

        val pref = getSharedPreferences("fileName", Context.MODE_PRIVATE)

        val connected= pref.getBoolean("connected",false)
        if (connected==true){
            startActivity(intentFor<Main2Activity>())
        }




    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment)
        return navController.navigateUp()
    }
}
