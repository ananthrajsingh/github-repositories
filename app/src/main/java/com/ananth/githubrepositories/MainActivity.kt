package com.ananth.githubrepositories

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ananth.githubrepositories.list.ListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ListFragment.newInstance())
                    .commitNow()
        }
    }


}
