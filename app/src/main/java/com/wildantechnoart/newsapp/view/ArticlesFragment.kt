package com.wildantechnoart.newsapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wildantechnoart.newsapp.databinding.FragmentArticlesBinding
import com.wildantechnoart.newsapp.utils.Constant
import com.wildantechnoart.newsapp.viewmodel.NewsViewModel
import kotlin.properties.Delegates

class ArticlesFragment : Fragment() {

    private var _binding: FragmentArticlesBinding? = null
    private val binding get() = _binding
    private var mAdapter by Delegates.notNull<ArticlesAdapter>()
    private val viewModel: NewsViewModel by viewModels()
    private var mSourceId: String? = null
    private var mSearching = ""
    private var mSourceName: String? = null
    private var page: Int = 1
    private var totalResult: Long = 0
    private var isOnRefreshPage = false
    private var isLoading = false
    private val mHandler = Handler(Looper.getMainLooper())
    private lateinit var translateAnimation: TranslateAnimation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentArticlesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        getLiveData()
        initListener()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() = with(binding) {
        val bundle = Bundle(arguments)
        mSourceId = ArticlesFragmentArgs.fromBundle(bundle).sourceId
        mSourceName = ArticlesFragmentArgs.fromBundle(bundle).sourceName
        this?.textSource?.text = mSourceName

        translateAnimation = TranslateAnimation(0F, 0F, 0F, 0F).apply {
            duration = 200
            fillAfter = true
            isFillEnabled = true
        }

        mAdapter = ArticlesAdapter()
        this?.rvArticles?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
        }

        viewModel.getArticleList(page, mSourceId, mSearching)
        this?.swipeRefresh?.setOnRefreshListener {
            isOnRefreshPage = false
            page = 1
            viewModel.getArticleList(page, mSourceId, mSearching)
        }

        val runnable = Runnable {
            viewModel.getArticleList(page, mSourceId, mSearching)
        }

        this?.inputSearch?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                mSearching = p0.toString()
                mHandler.removeCallbacks(runnable)
                isOnRefreshPage = false
                page = 1
                if (mSearching.length >= 4) {
                    mHandler.postDelayed(runnable, 500)
                } else if (mSearching.isEmpty()) {
                    viewModel.getArticleList(page, mSourceId, "")
                }
            }
        })
    }

    private fun initListener() {
        binding?.rvArticles?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val countItem = linearLayoutManager.itemCount
                val lastVisiblePosition =
                    linearLayoutManager.findLastCompletelyVisibleItemPosition()
                val isLastPosition = countItem.minus(1) == lastVisiblePosition
                if (!isLoading && isLastPosition && mAdapter.currentList.size < totalResult) {
                    isOnRefreshPage = true
                    page = page.plus(1)
                    viewModel.getArticleList(page, mSourceId, mSearching)
                }
            }
        })
    }

    private fun getLiveData() = with(binding) {
        viewModel.apply {
            getArticlesList.observe(viewLifecycleOwner) { data ->
                totalResult = data?.totalResults ?: 0L

                Constant.handleData(
                    page, false, data?.articles, mAdapter,
                    this@with?.rvArticles, this@with?.textMessageNoData
                )
            }
            error.observe(viewLifecycleOwner) {
                isLoading = false
                isOnRefreshPage = false
                Constant.handleErrorApi(requireActivity(), it)
            }
            loading.observe(viewLifecycleOwner) {
                if (it) {
                    isLoading = true
                    if (isOnRefreshPage) {
                        this@with?.progressBar?.startAnimation(translateAnimation)
                        this@with?.progressBar?.visibility = View.VISIBLE
                    } else {
                        this@with?.swipeRefresh?.isRefreshing = true
                    }
                } else {
                    isLoading = false
                    isOnRefreshPage = false
                    this@with?.swipeRefresh?.isRefreshing = false
                    this@with?.progressBar?.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}