package com.example.androidapp.home.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.androidapp.databinding.FragmentHomeBinding
import com.example.androidapp.home.model.RvModalClass
import com.example.androidapp.networkService.ApiResult
import com.example.androidapp.home.util.HomeViewModel
import com.example.androidapp.home.util.HomeScreenAdapter
import com.example.androidapp.home.util.WrapContentLinearLayoutManager
import com.example.androidapp.roomDb.NewsModel

class HomeFragment : Fragment() {
private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val viewModel: HomeViewModel by activityViewModels()
    private val parentAdapter by lazy {
        HomeScreenAdapter(list)
    }
    private lateinit var list:MutableList<RvModalClass>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setUpObservers()
    }

    private fun setUpViews() {
        viewModel.fetchNewsData()
        list=ArrayList()
        binding.apply {
            progresbar.visibility= View.GONE
            rvHome.layoutManager= WrapContentLinearLayoutManager(requireContext())
            rvHome.adapter=parentAdapter
        }

    }
    private fun setUpObservers() {
        viewModel.newsData.observe(viewLifecycleOwner
        ){
            when(it){
                ApiResult.Loading->{
                    binding.progresbar.visibility= View.VISIBLE
                }
                is ApiResult.Success->{
                    binding.progresbar.visibility= View.GONE
                    val data=it.data
                    list= ArrayList()
                    if (data != null) {
                        for (item in data.articles){
                            item.description?.let { it1 -> item.urlToImage?.let { it2 -> RvModalClass(publishedAt = "${
                                item.publishedAt?.split("T")?.get(0)
                            } by ${item.source?.name}", heading = item.title.toString(), description = it1, url = it2) } }
                                ?.let { it2 -> list.add(it2) }
                        }
                        parentAdapter.updateData(list)
                        viewModel.deleteSaved()
                        viewModel.saveData(data)
                    }
                }
                is ApiResult.Error->{
                    binding.progresbar.visibility= View.GONE
                    Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



}