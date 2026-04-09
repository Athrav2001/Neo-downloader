package com.neo.browser.logic.history

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class NeoBrowserHistoryEntry(
    val url: String,
    val title: String,
    val visitedAt: Long,
)
