package com.loc.newsapp.presentation.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.loc.newsapp.domain.usecases.news.NewsUseCases
import com.loc.newsapp.util.Constants.NEWS_SOURCES
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsUseCases: NewsUseCases
): ViewModel() {

    var state = mutableStateOf(HomeState())
    private set

    val news = newsUseCases.getNews(
        sources = NEWS_SOURCES
    ).cachedIn(viewModelScope)
}