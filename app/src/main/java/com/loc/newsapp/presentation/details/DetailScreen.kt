package com.loc.newsapp.presentation.details

import android.content.Intent
import android.net.Uri
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.loc.newsapp.R
import com.loc.newsapp.domain.model.Article
import com.loc.newsapp.domain.model.Source
import com.loc.newsapp.presentation.details.components.DetailsTopBar
import com.loc.newsapp.presentation.onboarding.Dimens.ArticleImageHeight
import com.loc.newsapp.presentation.onboarding.Dimens.IconSize
import com.loc.newsapp.presentation.onboarding.Dimens.MediumPadding0
import com.loc.newsapp.presentation.onboarding.Dimens.MediumPadding1
import com.loc.newsapp.presentation.onboarding.Dimens.TopPadding
import com.loc.newsapp.ui.theme.NewsAppTheme
import java.util.Locale

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailsScreen(
    article: Article,
    event: (DetailsEvent) -> Unit,
    navigateUp: () -> Unit
) {
    val context = LocalContext.current

    // TextToSpeech initialization
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var isTtsReady by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.getDefault()
                isTtsReady = true
            }
        }
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    val dismissState = rememberDismissState(
        confirmStateChange = {
            if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart) {
                navigateUp()
            }
            true
        }
    )

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
        background = { },
        dismissContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            ) {
                // Top bar with TTS button
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DetailsTopBar(
                        onBrowsingClick = {
                            Intent(Intent.ACTION_VIEW).also {
                                it.data = Uri.parse(article.url)
                                if (it.resolveActivity(context.packageManager) != null) {
                                    context.startActivity(it)
                                }
                            }
                        },
                        onShareClick = {
                            Intent(Intent.ACTION_SEND).also {
                                it.putExtra(Intent.EXTRA_TEXT, article.url)
                                it.type = "text/plain"
                                if (it.resolveActivity(context.packageManager) != null)
                                    context.startActivity(it)
                            }
                        },
                        onBookmarkClick = { event(DetailsEvent.UpsertDeleteArticle(article)) },
                        onBackClick = navigateUp
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MediumPadding0)
                ) {
                    item {
                        if (article.urlToImage != null && article.urlToImage.isNotEmpty()) {
                            AsyncImage(
                                model = ImageRequest.Builder(context = context)
                                    .data(article.urlToImage)
                                    .build(),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(ArticleImageHeight)
                                    .clip(MaterialTheme.shapes.medium),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.image_not_found),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(MaterialTheme.shapes.medium),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.requiredHeight(MediumPadding1))

                        SelectionContainer {
                            Text(
                                text = article.title ?: "No Title Available",
                                style = MaterialTheme.typography.displaySmall,
                                color = colorResource(id = R.color.text_title)
                            )
                        }

                        SelectionContainer {
                            Text(
                                text = article.content ?: "No Content Available",
                                style = MaterialTheme.typography.bodyMedium,
                                color = colorResource(id = R.color.body)
                            )
                        }
                    }
                }
                // TTS Speaker Icon
                IconButton(
                    onClick = {
                        if (isTtsReady) {
                            val textToRead = "${article.title ?: ""}. ${article.content ?: ""}"
                            tts?.speak(textToRead, TextToSpeech.QUEUE_FLUSH, null, null)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = TopPadding)
                        .requiredSize(48.dp)

                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_volume_up), // Add this icon to your drawable resources
                        contentDescription = "Listen to article",
                        tint = colorResource(id = R.color.body),
                        modifier = Modifier.requiredSize(36.dp)
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DetaitsScreenPreview() {
    NewsAppTheme {
        DetailsScreen(
            article = Article(
                author = "",
                title = "Coinbase says Apple blocked its tast app release on NFTs in Wallet",
                description = "Coinbase says Apple blocked its tast app release on NFTs in Wallet",
                content = "We use cookies and data to Deliver and maintain Google services Track",
                publishedAt = "3252423",
                source = Source(
                    id = "", name = "bbc",
                ),
                url = "http",
                urlToImage = "http"
            ),
            event = {}
        ) {

        }
    }
}