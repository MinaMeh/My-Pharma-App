package com.example.mypharma.Views

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mypharma.R
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main2.*
import org.jetbrains.anko.toast

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val navController= findNavController(R.id.mainNavigationFragment)
       setupActionBarWithNavController(navController)
        navigation.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
         return findNavController(R.id.mainNavigationFragment).navigateUp()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        onSupportNavigateUp()
    }

}
