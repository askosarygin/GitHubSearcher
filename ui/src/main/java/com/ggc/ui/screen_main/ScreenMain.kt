package com.ggc.ui.screen_main

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ggc.ui.R
import com.ggc.ui.R.drawable.icon_search
import com.ggc.ui.R.string.content_description_icon_search
import com.ggc.ui.R.string.fork
import com.ggc.ui.R.string.forks
import com.ggc.ui.R.string.try_again
import com.ggc.ui.common.ErrorBanner
import com.ggc.ui.common.ProgressBanner
import com.ggc.ui.navigation.NavRoutes.ScreenRepositoryContent
import com.ggc.ui.theme.ButtonSearchBackgroundDisabled
import com.ggc.ui.theme.ButtonSearchBackgroundEnabled
import com.ggc.ui.theme.ErrorBannerButton
import com.ggc.ui.theme.ErrorBannerButtonText
import com.ggc.ui.theme.ErrorBannerErrorText
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

    val modelState by viewModel.modelStateFlow.collectAsState()

    modelState.navEventData?.use { navRoutes, navArgs ->
        when (navRoutes) {
            ScreenRepositoryContent -> {
                navController.navigate(
                    route = navRoutes.route + "/${navArgs.owner}" + "/${navArgs.repo}"
                )
            }

            else -> {}
        }
    }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .padding(all = 10.dp)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(space = 10.dp)
        ) {
            SearchTextField(
                modifier = Modifier.weight(weight = 1f),
                value = modelState.textFieldSearch,
                onValueChange = remember {
                    { newTextFieldValue ->
                        viewModel.textFieldSearchChanged(newTextFieldValue = newTextFieldValue)
                    }
                },
                enabled = modelState.textFieldSearchEnabled,
                onSearch = remember {
                    {
                        if (modelState.buttonSearchEnabled) {
                            viewModel.buttonSearchClicked()
                            focusManager.clearFocus()
                        }
                    }
                }
            )

            SearchButton(
                enabled = modelState.buttonSearchEnabled,
                onClick = remember {
                    {
                        viewModel.buttonSearchClicked()
                        focusManager.clearFocus()
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(height = 20.dp))

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(space = 15.dp)
            ) {
                items(items = modelState.searchResults) { item ->
                    item.user?.let { userInfo ->
                        UserCard(
                            avatarUrl = userInfo.avatarUrl,
                            name = item.name,
                            score = userInfo.score,
                            onClick = remember(key1 = modelState.searchResults) {
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
                            onClick = remember(key1 = modelState.searchResults) {
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
            if (modelState.showProgressBar) {
                ProgressBanner()
            }
            if (modelState.showErrorBanner) {
                ErrorBanner(
                    errorText = modelState.errorBannerMessage,
                    buttonTryAgainOnClick = remember {
                        { viewModel.buttonTryAgainClicked() }
                    },
                    bannerOnClick = remember {
                        { viewModel.errorBannerCloseClicked() }
                    }
                )
            }
        }
    }
}

@Composable
private fun SearchTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    onSearch: (KeyboardActionScope.() -> Unit)
) {
    TextField(
        modifier = modifier,
        value = value,
        singleLine = true,
        enabled = enabled,
        onValueChange = onValueChange,
        label = {
            Text(text = "Введите запрос")
        },
        colors = TextFieldDefaults.colors().copy(
            unfocusedContainerColor = TextFieldBackground,
            focusedContainerColor = TextFieldBackground,
            focusedLabelColor = TextFieldFocused,
            focusedIndicatorColor = TextFieldFocused,
            disabledContainerColor = TextFieldBackground
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(onSearch = onSearch)
    )
}

@Composable
private fun SearchButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .size(width = 71.dp, height = 57.dp),
        enabled = enabled,
        onClick = onClick,
        border = BorderStroke(width = 3.dp, color = ButtonSearchBackgroundEnabled),
        shape = RoundedCornerShape(size = 3.dp),
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = ButtonSearchBackgroundEnabled,
            disabledContainerColor = ButtonSearchBackgroundDisabled
        )
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
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Layout(
            modifier = Modifier
                .clickable(onClick = onClick),
            content = {
                GlideImage(
                    modifier = Modifier.layoutId("USER_AVATAR"),
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop
                    ),
                    imageModel = { avatarUrl }
                )
                Text(
                    modifier = Modifier
                        .padding(vertical = 25.dp, horizontal = 20.dp)
                        .layoutId("USER_NAME"),
                    text = name,
                    color = UserCardTextName,
                    fontSize = 18.sp
                )
                Text(
                    modifier = Modifier.layoutId("USER_SCORE"),
                    text = score,
                    color = UserCardTextScore,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            measurePolicy = MeasurePolicy { measurables, constraints ->
                val userNameMeasurable = measurables.find { it.layoutId == "USER_NAME" }!!
                val userName = userNameMeasurable.measure(constraints)

                val userScoreMeasurable = measurables.find { it.layoutId == "USER_SCORE" }!!
                val userScore = userScoreMeasurable.measure(constraints)

                val newConstraints = constraints.copy(
                    minHeight = userName.height,
                    maxHeight = userName.height,
                    minWidth = userName.height,
                    maxWidth = userName.height
                )

                val userAvatarMeasurable = measurables.find { it.layoutId == "USER_AVATAR" }!!
                val userAvatar = userAvatarMeasurable.measure(newConstraints)

                layout(width = constraints.maxWidth, height = userName.height) {
                    userAvatar.placeRelative(x = 0, y = 0)
                    userName.placeRelative(x = userAvatar.width, y = 0)
                    userScore.placeRelative(
                        x = constraints.maxWidth - userScore.width,
                        y = (userName.height / 2) - (userScore.height / 2)
                    )
                }
            }
        )
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