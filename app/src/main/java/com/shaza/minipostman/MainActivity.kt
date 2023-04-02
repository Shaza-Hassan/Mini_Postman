package com.shaza.minipostman

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shaza.minipostman.home.view.HomeFragment
import com.shaza.minipostman.utils.PostmanSqlite

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            PostmanSqlite.getInstance(this)
            supportFragmentManager.beginTransaction()
                .add(R.id.main_layout, HomeFragment(), HomeFragment().tag).commit()
        }
    }
}