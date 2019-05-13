package com.mvgrass.remotepc.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.mvgrass.remotepc.UI.LanConnectionFragment
import com.mvgrass.remotepc.UI.WebConnectionFragment

class ConnectionPagerAdapter: FragmentPagerAdapter{



    constructor(fm: FragmentManager?) : super(fm)

    override fun getItem(position: Int): Fragment =
         when (position) {
            0 -> {
                LanConnectionFragment()
            }
            else -> {
                WebConnectionFragment()
            }
        }



    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? =
            when(position){
                0->{
                    "LAN"
                }
                else->{
                    "Web"
                }
            }

}