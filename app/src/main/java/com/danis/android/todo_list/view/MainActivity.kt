package com.danis.android.todo_list.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.danis.android.todo_list.R
import com.danis.android.todo_list.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var viewPagerAdapter:ViewPagerAdapter
    private val fragmentList:List<Fragment> = listOf(TodoFragment.newInstance(),NotesFragment.newInstance())
    private val fragmentListTitles = listOf(R.string.TODO,R.string.Notes)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        viewPagerAdapter = ViewPagerAdapter(this,fragmentList)
        binding.viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayout,binding.viewPager){ tab,position ->
            tab.text = getString(fragmentListTitles[position])
        }.attach()

    }
}