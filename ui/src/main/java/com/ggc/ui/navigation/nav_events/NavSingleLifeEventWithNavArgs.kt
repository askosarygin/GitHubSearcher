package com.ggc.ui.navigation.nav_events

import com.ggc.ui.navigation.NavRoutes
import java.util.concurrent.atomic.AtomicBoolean

class NavSingleLifeEventWithNavArgs<T>(
    private val navRoutes: NavRoutes,
    private val navArgs: T
) {
    private val processed = AtomicBoolean(false)

    fun use(doEvent: (NavRoutes, T) -> Unit) {
        if (!processed.getAndSet(true)) {
            doEvent(navRoutes, navArgs)
        }
    }
}