package com.prac.cabstone.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.prac.cabstone.MainViewModel
import com.prac.cabstone.R
import com.prac.cabstone.search.SearchActivity

class HomeFragment : Fragment {

    val viewModel: MainViewModel
    private lateinit var mClSearch: ConstraintLayout

    constructor(vm: MainViewModel) {
        viewModel = vm
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView : View = inflater.inflate(R.layout.fragment_home, container, false);

        mClSearch = rootView.findViewById(R.id.home_fg_cl_search)
        mClSearch.setOnClickListener {
            var intent = Intent(context, SearchActivity::class.java)
            startActivity(intent)
        }

        return rootView
    }
}