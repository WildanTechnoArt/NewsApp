package com.wildantechnoart.newsapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wildantechnoart.newsapp.databinding.FragmentSourceBinding
import com.wildantechnoart.newsapp.utils.Constant
import com.wildantechnoart.newsapp.viewmodel.NewsViewModel
import kotlin.properties.Delegates

class SourcesFragment : Fragment() {

    private var _binding: FragmentSourceBinding? = null
    private val binding get() = _binding
    private var mAdapter by Delegates.notNull<SourcesAdapter>()
    private val viewModel: NewsViewModel by viewModels()
    private var mCategoryId: String? = null
    private var mCategoryName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSourceBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        getLiveData()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() = with(binding) {
        val bundle = Bundle(arguments)
        mCategoryId = SourcesFragmentArgs.fromBundle(bundle).categoryId
        mCategoryName = SourcesFragmentArgs.fromBundle(bundle).categoryName
        this?.textCategory?.text = "Category $mCategoryName"

        mAdapter = SourcesAdapter(requireView())
        this?.rvSource?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
        }

        viewModel.getSourceList(mCategoryId)
        this?.swipeRefresh?.setOnRefreshListener {
            viewModel.getSourceList(mCategoryId)
        }

        this?.inputSearch?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                mAdapter.filter.filter(p0.toString())
            }
        })
    }

    private fun getLiveData() = with(binding) {
        viewModel.apply {
            getSourcesList.observe(viewLifecycleOwner) { data ->
                Constant.handleData(
                    null, false, data, mAdapter,
                    this@with?.rvSource, this@with?.textMessageNoData
                )
            }
            error.observe(viewLifecycleOwner) {
                Constant.handleErrorApi(requireActivity(), it)
            }
            loading.observe(viewLifecycleOwner) {
                this@with?.swipeRefresh?.isRefreshing = it
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}