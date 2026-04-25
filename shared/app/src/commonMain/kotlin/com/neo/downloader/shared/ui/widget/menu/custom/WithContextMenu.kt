package com.neo.downloader.shared.ui.widget.menu.custom

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ir.neo.util.compose.action.MenuItem

@Composable
expect fun WithContextMenu(
    menuProvider: () -> List<MenuItem>,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
)
