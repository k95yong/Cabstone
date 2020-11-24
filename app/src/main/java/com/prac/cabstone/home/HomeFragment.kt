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
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment {

    companion object{

        fun newInstance():HomeFragment{
            val args: Bundle = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    constructor() {
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView : View = inflater.inflate(R.layout.fragment_home, container, false);
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        home_fg_cl_search.setOnClickListener {
            val searchActivity = SearchActivity()
            var intent = Intent(context, searchActivity::class.java)
            startActivity(intent)
        }
    }
}