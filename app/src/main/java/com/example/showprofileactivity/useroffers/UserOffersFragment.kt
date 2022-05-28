package com.example.showprofileactivity.useroffers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.showprofileactivity.R
import com.google.android.material.tabs.TabLayout


class UserOffersFragment : Fragment() {
    private var viewPager: ViewPager? = null
    private var tabLayout: TabLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_user_offers, container, false)
        tabLayout = v.findViewById(R.id.tabs)
        viewPager = v.findViewById(R.id.viewpager)
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(AcceptedOffersFragment(), "Accepted")
        adapter.addFragment(AssignedOffersFragment(), "Requested")
        viewPager?.adapter = adapter
        tabLayout?.setupWithViewPager(viewPager)
        return v
    }
    internal class ViewPagerAdapter(supportFragmentManager: FragmentManager?) :
        FragmentStatePagerAdapter(supportFragmentManager!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val mList: MutableList<Fragment> = ArrayList()
        private val mTitleList: MutableList<String> = ArrayList()
        override fun getItem(i: Int): Fragment {
            return mList[i]
        }

        override fun getCount(): Int {
            return mList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mList.add(fragment)
            mTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mTitleList[position]
        }
    }
}