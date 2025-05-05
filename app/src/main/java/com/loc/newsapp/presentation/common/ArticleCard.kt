package com.loc.newsapp.presentation.common

import android.content.DialogInterface.OnClickListener
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.loc.newsapp.R
import com.loc.newsapp.domain.model.Article
import com.loc.newsapp.domain.model.Source
import com.loc.newsapp.presentation.onboarding.Dimens.ArticleCardSize
import com.loc.newsapp.presentation.onboarding.Dimens.ExtraSmallPadding
import com.loc.newsapp.presentation.onboarding.Dimens.ExtraSmallPadding2
import com.loc.newsapp.presentation.onboarding.Dimens.SmallIconSize
import com.loc.newsapp.presentation.onboarding.Dimens.SmallPadding

@Composable
fun ArticleCard(
    modifier: Modifier = Modifier,
//    showSummaries: Boolean,
//    summarizer: GemmaSummarizer?,
    article: Article,
    onClick: (() -> Unit)? = null
) {
    val context = LocalContext.current
//    var summaryText by remember { mutableStateOf("Generating summary...") }

//    LaunchedEffect(showSummaries) {
//        if (showSummaries && summarizer != null) {
//            try {
//                val summary = summarizer.summarize(article.content)
//                summaryText = summary.take(70) // Limit to ~7-8 words
//            } catch (e: Exception) {
//                summaryText = "Summary unavailable"
//            }
//        }
//    }

    Row(modifier = modifier.clickable { onClick?.invoke() }) {


        if (article.urlToImage != null && article.urlToImage.isNotEmpty()) {
            AsyncImage(
                modifier = Modifier
                    .size(ArticleCardSize)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop,
                model = ImageRequest.Builder(context).data(article.urlToImage).build(),
                contentDescription = null
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.image_not_found),
                contentDescription = null,
                modifier = Modifier
                    .size(ArticleCardSize)
                    .clip(MaterialTheme.shapes.medium)
            )
        }

        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .padding(horizontal = SmallPadding)
                .height(ArticleCardSize)
        ) {
            Text(
                text = article.title,
                style = MaterialTheme.typography.bodyMedium.copy(),
                color = colorResource(
                    id = R.color.text_title
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = article.source.name,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = colorResource(
                        id = R.color.body
                    )
                )

                Spacer(modifier = Modifier.size(SmallPadding))

                Icon(
                    painter = painterResource(id = R.drawable.ic_time), contentDescription = null,
                    modifier = Modifier.size(SmallIconSize),
                    tint = colorResource(
                        id = R.color.body
                    )
                )

                Spacer(modifier = Modifier.size(ExtraSmallPadding2))

                Text(
                    text = article.publishedAt,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = colorResource(
                        id = R.color.body
                    )
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ArticleCardPreview() {
    ArticleCard(
        article = Article(
            author = "",
            content = "",
            description = "",
            publishedAt = "2 hrs ago",
            source = Source(id = "", name = "BBC News"),
            title = "Title of the news",
            url = "",
            urlToImage = ""
        ),
        modifier = TODO(),
//        showSummaries = TODO(),
//        summarizer = TODO(),
    ) {

    }
}