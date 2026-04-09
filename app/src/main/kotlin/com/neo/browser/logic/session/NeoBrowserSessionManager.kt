package com.neo.browser.logic.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NeoBrowserSessionManager(
    private val sessionState: MutableStateFlow<NeoBrowserSessionState>,
) {
    val session = sessionState.asStateFlow()

    fun current(): NeoBrowserSessionState {
        return sessionState.value
    }

    fun persist(
        tabs: List<NeoBrowserSessionTab>,
        activeTabId: String?,
    ) {
        sessionState.value = NeoBrowserSessionState(
            tabs = tabs,
            activeTabId = activeTabId,
            updatedAt = System.currentTimeMillis(),
        )
    }
}
