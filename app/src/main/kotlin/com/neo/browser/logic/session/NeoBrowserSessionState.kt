package com.neo.browser.logic.session

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class NeoBrowserSessionTab(
    val tabId: String,
    val url: String?,
    val title: String?,
)

@Serializable
@Immutable
data class NeoBrowserSessionState(
    val tabs: List<NeoBrowserSessionTab> = emptyList(),
    val activeTabId: String? = null,
    val updatedAt: Long = 0L,
)
