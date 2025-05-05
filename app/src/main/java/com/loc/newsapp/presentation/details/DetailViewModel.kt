package com.loc.newsapp.presentation.details

import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.newsapp.domain.model.Article
import com.loc.newsapp.domain.usecases.news.NewsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val newsUseCases: NewsUseCases,
    @ApplicationContext private val context: Context // Added context injection

) : ViewModel() {

    private var textToSpeech: TextToSpeech? = null
    private var tts: TextToSpeech? = null
    var ttsError by mutableStateOf<String?>(null)

    var isSpeaking by mutableStateOf(false)
        private set

    init {
        initializeTTS()
    }

    private fun initializeTTS() {
        tts = TextToSpeech(context) { status ->
            when {
                status == TextToSpeech.SUCCESS -> {
                    val result = tts?.setLanguage(Locale.getDefault())
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                        result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        ttsError = "Language not supported"
                    }
                }
                status == TextToSpeech.ERROR -> {
                    ttsError = "TTS engine initialization failed"
                }
                else -> {
                    ttsError = "Unknown TTS error"
                }
            }
        }
    }

    fun toggleSpeech(content: String) {
        if (isSpeaking) {
            textToSpeech?.stop()
        } else {
            textToSpeech?.speak(content, TextToSpeech.QUEUE_FLUSH, null, null)
        }
        isSpeaking = !isSpeaking
    }

    override fun onCleared() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        super.onCleared()
    }

    var sideEffect by mutableStateOf<String?>(null)
        private set

    fun onEvent(event: DetailsEvent) {
        when (event) {
            is DetailsEvent.UpsertDeleteArticle -> {
                viewModelScope.launch {
                    val article = newsUseCases.selectArticle(event.article.url)
                    if (article == null) {
                        upsertArticles(event.article)
                    } else {
                        deleteArticle(event.article)
                    }
                }
            }

            is DetailsEvent.RemoveSideEffect -> {
                sideEffect = null

            }
        }
    }

    fun checkTTSAvailability() {
        val intent = Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA)
        context.startActivity(intent)
    }

    private suspend fun deleteArticle(article: Article) {
        newsUseCases.deleteArticle(article)
        sideEffect = "Article Deleted"
    }

    private suspend fun upsertArticles(article: Article) {
        newsUseCases.upsertArticle(article)
        sideEffect = "Article Saved"
    }
}