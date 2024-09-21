package com.ggc.ui.navigation.nav_events

import com.ggc.ui.navigation.NavRoutes

class NavEventData<T>(
    route: NavRoutes,
    val data: T
) : NavEvent(route)