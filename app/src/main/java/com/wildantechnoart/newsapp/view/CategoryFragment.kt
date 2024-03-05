package com.wildantechnoart.newsapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.wildantechnoart.newsapp.databinding.FragmentCategoryBinding
import com.wildantechnoart.newsapp.model.CategoryModel
import kotlin.properties.Delegates

class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding
    private var mAdapter by Delegates.notNull<CategoryAdapter>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {
        mAdapter = CategoryAdapter(requireView())
        this?.rvGenres?.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = mAdapter
        }

        val categoryList = ArrayList<CategoryModel>()
        categoryList.add(CategoryModel("business", "Business"))
        categoryList.add(CategoryModel("entertainment", "Entertainment"))
        categoryList.add(CategoryModel("general", "General"))
        categoryList.add(CategoryModel("health", "Health"))
        categoryList.add(CategoryModel("science", "Science"))
        categoryList.add(CategoryModel("sports", "Sports"))
        categoryList.add(CategoryModel("technology", "Technology"))

        mAdapter.submitList(categoryList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}