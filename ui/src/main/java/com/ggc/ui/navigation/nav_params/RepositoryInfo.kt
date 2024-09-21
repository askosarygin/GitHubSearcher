package com.ggc.ui.navigation.nav_params

import java.io.Serializable

data class RepositoryInfo(
    val owner: String,
    val repo: String
) : Serializable