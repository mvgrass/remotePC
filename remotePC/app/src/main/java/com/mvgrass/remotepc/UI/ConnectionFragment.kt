package com.mvgrass.remotepc.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.mvgrass.remotepc.Adapters.ConnectionPagerAdapter

import com.mvgrass.remotepc.R


class ConnectionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewPager = view.findViewById<ViewPager>(R.id.viewPager_connection)
        viewPager.adapter = ConnectionPagerAdapter(childFragmentManager)


        val tabLayout = view.findViewById<TabLayout>(R.id.tabs_connection)
        tabLayout.setupWithViewPager(viewPager)

    }
}
