package com.danis.android.todo_list.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.danis.android.todo_list.R
import com.danis.android.todo_list.databinding.ActivityMainBinding
import com.danis.android.todo_list.presentation.adapters.ViewPagerAdapter
import com.danis.android.todo_list.presentation.fragments.NotesFragment
import com.danis.android.todo_list.presentation.fragments.TodoFragment
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private val fragmentList: List<Fragment> =
        listOf(TodoFragment.newInstance(), NotesFragment.newInstance())
    private val fragmentListTitles = listOf(R.string.TODO, R.string.Notes)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewPagerAdapter = ViewPagerAdapter(this, fragmentList)
        binding.viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = getString(fragmentListTitles[position])
        }.attach()
    }

}