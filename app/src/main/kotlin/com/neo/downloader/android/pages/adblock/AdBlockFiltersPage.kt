package com.neo.downloader.android.pages.adblock

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.neo.downloader.android.ui.menu.RenderMenuInSheet
import com.neo.downloader.android.ui.page.PageHeader
import com.neo.downloader.android.ui.page.PageTitle
import com.neo.downloader.android.ui.page.PageUi
import com.neo.downloader.shared.ui.widget.CheckBox
import com.neo.downloader.shared.ui.widget.Text
import com.neo.downloader.shared.ui.widget.TransparentIconActionButton
import com.neo.downloader.shared.util.div
import com.neo.downloader.shared.util.ui.WithContentAlpha
import com.neo.downloader.shared.util.ui.icon.MyIcons
import com.neo.downloader.shared.util.ui.myColors
import com.neo.downloader.shared.util.ui.theme.myShapes
import com.neo.downloader.shared.util.ui.theme.mySpacings
import ir.amirab.util.compose.asStringSource
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdBlockFiltersPage(component: AndroidAdBlockFiltersComponent) {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current
    val sources by component.sources.collectAsState()
    val hosts by component.hosts.collectAsState()
    val showManage by component.showManageSources.collectAsState()
    val isUpdating by component.isUpdating.collectAsState()
    val lastUpdatedAt by component.lastUpdatedAt.collectAsState()
    val statusMessage by component.statusMessage.collectAsState()

    BackHandler {
        component.onBack()
    }

    PageUi(
        header = {
            PageHeader(
                leadingIcon = {
                    TransparentIconActionButton(
                        icon = MyIcons.back,
                        contentDescription = "Back".asStringSource(),
                        onClick = {
                            backDispatcher?.onBackPressedDispatcher?.onBackPressed()
                        }
                    )
                },
                headerTitle = {
                    PageTitle("Ad Block Filters")
                },
                headerActions = {
                    TransparentIconActionButton(
                        icon = MyIcons.menu,
                        contentDescription = "Menu".asStringSource(),
                        onClick = component::openMenu,
                    )
                }
            )
        },
        modifier = Modifier
            .systemBarsPadding()
            .navigationBarsPadding(),
    ) { insets ->
        Column(
            Modifier
                .padding(insets.paddingValues)
                .padding(horizontal = 12.dp, vertical = 10.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            SummaryLine("Hosts loaded", hosts.size.toString())
            SummaryLine(
                "Last update",
                if (lastUpdatedAt <= 0L) "Never" else formatDate(lastUpdatedAt),
            )
            SummaryLine(
                "Sources enabled",
                "${sources.count { it.enabled }}/${sources.size}",
            )
            if (isUpdating) {
                WithContentAlpha(0.8f) {
                    Text("Updating filters...")
                }
            }
            statusMessage?.let { text ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .border(1.dp, myColors.onBackground / 0.15f, myShapes.defaultRounded)
                        .background(myColors.surface / 0.5f, myShapes.defaultRounded)
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text, modifier = Modifier.weight(1f))
                    TransparentIconActionButton(
                        icon = MyIcons.close,
                        contentDescription = "Close".asStringSource(),
                        onClick = component::dismissStatusMessage,
                    )
                }
            }
            if (showManage) {
                sources.forEach { source ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .border(1.dp, myColors.onBackground / 0.1f, myShapes.defaultRounded)
                            .clip(myShapes.defaultRounded)
                            .clickable { component.setSourceEnabled(source.id, !source.enabled) }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        CheckBox(
                            value = source.enabled,
                            onValueChange = { component.setSourceEnabled(source.id, it) },
                            enabled = !isUpdating,
                        )
                        Spacer(Modifier.padding(horizontal = 4.dp))
                        Column(
                            Modifier
                                .padding(start = mySpacings.mediumSpace)
                                .weight(1f)
                        ) {
                            Text(source.name)
                            WithContentAlpha(0.7f) {
                                Text(
                                    source.url,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    RenderMenuInSheet(
        component.mainMenu.collectAsState().value,
        component::closeMenu,
    )
}

@Composable
private fun SummaryLine(title: String, value: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .border(1.dp, myColors.onBackground / 0.1f, myShapes.defaultRounded)
            .background(myColors.surface / 0.5f, myShapes.defaultRounded)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        WithContentAlpha(0.8f) {
            Text(title)
        }
        Text(value)
    }
}

private fun formatDate(epochMillis: Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
    return formatter.format(Date(epochMillis))
}
