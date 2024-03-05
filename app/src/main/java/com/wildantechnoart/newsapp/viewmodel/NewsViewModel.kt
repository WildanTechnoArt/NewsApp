package com.wildantechnoart.newsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wildantechnoart.newsapp.model.ArticlesModel
import com.wildantechnoart.newsapp.model.Sources
import com.wildantechnoart.newsapp.network.RetrofitClient
import com.wildantechnoart.newsapp.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {

    private val repository: NewsRepository by lazy { NewsRepository(RetrofitClient.instance) }

    private val _getSourcesList = MutableLiveData<List<Sources>>()
    val getSourcesList: LiveData<List<Sources>> = _getSourcesList

    private val _getArticlesList = MutableLiveData<ArticlesModel?>()
    val getArticlesList: LiveData<ArticlesModel?> = _getArticlesList

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> = _error

    fun getSourceList(category: String?) {
        viewModelScope.launch {
            try {
                repository.getSourceList(category)
                    .flowOn(Dispatchers.IO)
                    .onStart { _loading.value = true }
                    .onCompletion { _loading.postValue(false) }
                    .catch { errorHandle(it) }
                    .collect {
                        _getSourcesList.value = it.sources
                    }
            } catch (e: Exception) {
                errorHandle(e)
            }
        }
    }

    fun getArticleList(page: Int?, sources: String?, search: String?) {
        viewModelScope.launch {
            try {
                repository.getArticleList(page, sources, search)
                    .flowOn(Dispatchers.IO)
                    .onStart { _loading.value = true }
                    .onCompletion { _loading.postValue(false) }
                    .catch { errorHandle(it) }
                    .collect {
                        _getArticlesList.value = it
                    }
            } catch (e: Exception) {
                errorHandle(e)
            }
        }
    }

    private fun errorHandle(it: Throwable) {
        _loading.postValue(false)
        _error.postValue(it)
    }
}