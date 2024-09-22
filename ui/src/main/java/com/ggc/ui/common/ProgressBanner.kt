package com.ggc.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ggc.ui.theme.ErrorBannerBackground

@Composable
fun ProgressBanner() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = ErrorBannerBackground),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}