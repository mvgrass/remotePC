package com.mvgrass.remotepc

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mvgrass.remotepc.UI.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val fragment: Fragment = when (item.itemId) {
            R.id.navigation_desktop -> {
                DesktopFragment()
            }
            R.id.navigation_mouse -> {
                MouseFragment()
            }
            R.id.navigation_keyboard->{
                KeyboardFragment()
            }
            R.id.navigation_files -> {
                FilemanagerFragment()
            }

            R.id.navigation_connection->{
                ConnectionFragment()
            }
            else->{
                return@OnNavigationItemSelectedListener false
            }
        }

        val mFragmentTransaction = supportFragmentManager.beginTransaction()
        mFragmentTransaction.replace(R.id.main_fragment_container, fragment)
        mFragmentTransaction.commit()

        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.menu.getItem(4).setChecked(true)
        val mFragmentTransaction = supportFragmentManager.beginTransaction()
        mFragmentTransaction.replace(R.id.main_fragment_container, ConnectionFragment())
        mFragmentTransaction.commit()
    }
}
