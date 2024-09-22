package com.ggc.ui.navigation.nav_events

import com.ggc.ui.navigation.NavRoutes
import java.util.concurrent.atomic.AtomicBoolean

class NavSingleLifeEvent(
    private val navRoutes: NavRoutes
) {
    private val processed = AtomicBoolean(false)

    fun use(doEvent: (NavRoutes) -> Unit) {
        if (!processed.getAndSet(true)) {
            doEvent(navRoutes)
        }
    }
}