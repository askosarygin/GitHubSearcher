package com.ggc.ui.screen_main

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ggc.ui.R.drawable.icon_search
import com.ggc.ui.R.string.content_description_icon_search
import com.ggc.ui.R.string.fork
import com.ggc.ui.R.string.forks
import com.ggc.ui.navigation.NavRoutes.ScreenRepositoryContent
import com.ggc.ui.theme.ButtonSearchBackground
import com.ggc.ui.theme.RepositoryCardTextDescription
import com.ggc.ui.theme.RepositoryCardTextName
import com.ggc.ui.theme.TextFieldBackground
import com.ggc.ui.theme.TextFieldFocused
import com.ggc.ui.theme.UserCardBackground
import com.ggc.ui.theme.UserCardTextName
import com.ggc.ui.theme.UserCardTextScore
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ScreenMain(
    viewModel: ScreenMainViewModel,
    navController: NavController
) {

    val modelState by viewModel.modelState.collectAsState()

    modelState.navEventData?.let { navEvent ->
        when (navEvent.navRoutes) {
            ScreenRepositoryContent -> {
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    "NAV_EVENT_KEY_OWNER_REPO",
                    navEvent.data
                )
                navController.navigate(route = navEvent.navRoutes.route)
            }

            else -> {}
        }
    }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(all = 10.dp)
            .fillMaxSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(space = 10.dp)
        ) {
            SearchTextField(
                value = modelState.textFieldSearch,
                onValueChange = remember {
                    { newTextFieldValue ->
                        viewModel.textFieldSearchChanged(newTextFieldValue = newTextFieldValue)
                    }
                }
            )

            SearchButton(
                onClick = remember { { viewModel.buttonSearchPressed() } }
            )
        }

        Spacer(modifier = Modifier.height(height = 20.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(space = 15.dp)
        ) {
            items(items = modelState.searchResults) { item ->
                item.user?.let { userInfo ->
                    UserCard(
                        avatarUrl = userInfo.avatarUrl,
                        name = item.name,
                        score = userInfo.score,
                        onClick = remember {
                            {
                                context.startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(userInfo.htmlUrl)
                                    )
                                )
                            }
                        }
                    )
                }
                item.repository?.let { repositoryInfo ->
                    RepositoryCard(
                        name = item.name,
                        forksCount = repositoryInfo.forksCount,
                        description = repositoryInfo.description,
                        onClick = remember {
                            {
                                viewModel.repositoryClicked(
                                    repositoryInfo.owner,
                                    repositoryInfo.repo
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = "Введите запрос")
        },
        colors = TextFieldDefaults.colors().copy(
            unfocusedContainerColor = TextFieldBackground,
            focusedContainerColor = TextFieldBackground,
            focusedLabelColor = TextFieldFocused,
            focusedIndicatorColor = TextFieldFocused
        )
    )
}

@Composable
private fun SearchButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(ButtonSearchBackground)
            .size(width = 71.dp, height = 57.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = icon_search),
            contentScale = ContentScale.None,
            contentDescription = stringResource(id = content_description_icon_search)
        )
    }
}

@Composable
private fun UserCard(
    avatarUrl: String,
    name: String,
    score: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(size = 5.dp))
            .background(color = UserCardBackground, shape = RoundedCornerShape(size = 5.dp))
            .padding(start = 25.dp, end = 15.dp, top = 25.dp, bottom = 25.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        GlideImage(
            modifier = Modifier.layout { measurable, constraints ->
                val imageWidth =
                    (minOf(constraints.maxWidth, constraints.maxHeight) * 0.16f).toInt()
                val newConstraints = constraints.copy(
                    minHeight = imageWidth,
                    minWidth = imageWidth,
                    maxHeight = imageWidth,
                    maxWidth = imageWidth
                )
                val image = measurable.measure(newConstraints)
                layout(width = imageWidth, height = imageWidth) {
                    image.placeRelative(x = 0, y = 0)
                }
            },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop
            ),
            imageModel = { avatarUrl }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = name,
                color = UserCardTextName,
                fontSize = 18.sp
            )
            Text(
                text = score,
                color = UserCardTextScore,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun RepositoryCard(
    name: String,
    forksCount: Int,
    description: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(size = 5.dp))
            .background(color = UserCardBackground, shape = RoundedCornerShape(size = 5.dp))
            .padding(start = 15.dp, end = 15.dp, top = 15.dp, bottom = 15.dp)
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                color = RepositoryCardTextName,
                fontSize = 18.sp
            )
            Text(
                text = if (forksCount <= 1) {
                    "$forksCount\n${stringResource(id = fork)}"
                } else {
                    "$forksCount\n${stringResource(id = forks)}"
                },
                color = RepositoryCardTextName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
        Text(
            text = description,
            color = RepositoryCardTextDescription,
            fontSize = 18.sp
        )
    }
}