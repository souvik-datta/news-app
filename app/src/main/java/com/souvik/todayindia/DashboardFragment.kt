package com.souvik.todayindia

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.souvik.todayindia.adapter.CategoryAdapter
import com.souvik.todayindia.adapter.FeedAdapter
import com.souvik.todayindia.databinding.FragmentDashboardBinding
import com.souvik.todayindia.model.Category
import com.souvik.todayindia.model.NewsData

class DashboardFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(DashboardViewModel::class.java)
    }
    private lateinit var feedAdapter: FeedAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private val list = ArrayList<NewsData>()
    private val categoryArrayList = ArrayList<Category>()
    private lateinit var binding: FragmentDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getHeadLineNews()
        setUpObserver()
        initView()
        initCategory()
    }

    private fun initView() {
        feedAdapter = FeedAdapter(list, object : FeedAdapter.OnClickListener {
            override fun onClick(data: NewsData) {
                data.url?.let {
                    findNavController().navigate(
                        DashboardFragmentDirections.actionDashboardFragmentToDetailsFragment(
                            data
                        )
                    )
                }
            }
        })
        binding.rvNewsList.adapter = feedAdapter
        categoryAdapter =
            CategoryAdapter(categoryArrayList, object : CategoryAdapter.OnClickListener {
                override fun onClick(data: Category) {
                    if (data.name.equals("Weather", true))
                        findNavController().navigate(
                            DashboardFragmentDirections.actionDashboardFragmentToWeatherFragment()
                        )
                    else
                        viewModel.getHeadLineNews(data.name)
                }
            })
        binding.rvCategories.adapter = categoryAdapter
    }

    private fun setUpObserver() {
        viewModel.list.observe(viewLifecycleOwner, Observer {
            Log.d("TAG", "setUpObserver: $it")
            if (it.size > 0) {
                list.clear()
                list.addAll(it)
                feedAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun initCategory() {
        categoryArrayList.clear()
        categoryArrayList.add(
            Category(
                "General",
                "https://images.unsplash.com/photo-1497008386681-a7941f08011e?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=80"
            )
        )
        categoryArrayList.add(
            Category(
                "Business",
                "https://images.unsplash.com/photo-1611095790444-1dfa35e37b52?ixid=MnwxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1951&q=80"
            )
        )
        categoryArrayList.add(
            Category(
                "Entertainment",
                "https://images.unsplash.com/photo-1486572788966-cfd3df1f5b42?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1052&q=80"
            )
        )
        categoryArrayList.add(
            Category(
                "Health",
                "https://images.unsplash.com/photo-1506126613408-eca07ce68773?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=431&q=80"
            )
        )
        categoryArrayList.add(
            Category(
                "Science",
                "https://images.unsplash.com/photo-1507668077129-56e32842fceb?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=334&q=80"
            )
        )
        categoryArrayList.add(
            Category(
                "Sports",
                "https://images.unsplash.com/photo-1499438075715-fc23ef376ab9?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=708&q=80"
            )
        )
        categoryArrayList.add(
            Category(
                "Technology",
                "https://images.unsplash.com/photo-1504384764586-bb4cdc1707b0?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80"
            )
        )
        categoryArrayList.add(
            Category(
                "Astrology",
                "https://images.unsplash.com/photo-1620654042883-715c3c509cf7?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=638&q=80"
            )
        )
        categoryArrayList.add(
            Category(
                "Weather",
                "https://images.unsplash.com/photo-1454789476662-53eb23ba5907?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=389&q=80"
            )
        )
        categoryAdapter.notifyDataSetChanged()
    }

}