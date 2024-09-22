package com.ggc.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ggc.ui.R.string.try_again
import com.ggc.ui.theme.ErrorBannerBackground
import com.ggc.ui.theme.ErrorBannerBackgroundSmall
import com.ggc.ui.theme.ErrorBannerButton
import com.ggc.ui.theme.ErrorBannerButtonText
import com.ggc.ui.theme.ErrorBannerErrorText

@Composable
fun ErrorBanner(
    errorText: String,
    buttonTryAgainOnClick: () -> Unit,
    bannerOnClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = ErrorBannerBackground)
            .clickable(
                interactionSource = null,
                indication = null,
                onClick = bannerOnClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(size = 5.dp))
                .background(color = ErrorBannerBackgroundSmall)
                .padding(horizontal = 50.dp, vertical = 40.dp),
            verticalArrangement = Arrangement.spacedBy(space = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = errorText,
                color = ErrorBannerErrorText,
                fontSize = 18.sp
            )
            Button(
                shape = RoundedCornerShape(size = 3.dp),
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = ErrorBannerButton
                ),
                onClick = buttonTryAgainOnClick
            ) {
                Text(
                    text = stringResource(id = try_again),
                    color = ErrorBannerButtonText,
                    fontSize = 16.sp
                )
            }
        }
    }
}