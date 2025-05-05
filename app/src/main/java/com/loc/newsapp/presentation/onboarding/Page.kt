package com.loc.newsapp.presentation.onboarding

import androidx.annotation.DrawableRes
import com.loc.newsapp.R

data class Page (
    val title: String,
    val description: String,
    @DrawableRes val image: Int
)

val pages = listOf(
    Page(
        title = "Welcome to Times.Then",
        description = "Your go-to source for breaking news, discovering content, and in-depth stories",
        image = R.drawable.onboarding6
    ),
    Page(
        title = "Never Miss a Story",
        description = "Save articles to read later and access your favorites anytime.",
        image = R.drawable.onboarding7
    ),
    Page(
        title = "Listen or Speak Your News",
        description = "Listen to articles with Text-to-Speech or Search them using your voice.",
        image = R.drawable.onboarding8_
    )
)