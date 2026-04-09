package com.neo.browser.logic.history

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NeoBrowserHistoryManager(
    private val historyState: MutableStateFlow<List<NeoBrowserHistoryEntry>>,
    private val maxItems: Int = 500,
) {
    val history = historyState.asStateFlow()

    fun recordVisit(
        rawUrl: String?,
        title: String?,
    ) {
        val url = rawUrl?.trim().orEmpty()
        if (!url.shouldTrackInHistory()) return

        val now = System.currentTimeMillis()
        val resolvedTitle = title?.trim().takeUnless { it.isNullOrEmpty() } ?: url
        historyState.update { current ->
            val withoutUrl = current.filterNot { it.url == url }
            buildList(capacity = minOf(withoutUrl.size + 1, maxItems)) {
                add(
                    NeoBrowserHistoryEntry(
                        url = url,
                        title = resolvedTitle,
                        visitedAt = now,
                    )
                )
                addAll(withoutUrl.take(maxItems - 1))
            }
        }
    }

    private fun String.shouldTrackInHistory(): Boolean {
        if (isBlank() || equals("about:blank", ignoreCase = true)) return false
        return startsWith("http://", ignoreCase = true) || startsWith("https://", ignoreCase = true)
    }
}
