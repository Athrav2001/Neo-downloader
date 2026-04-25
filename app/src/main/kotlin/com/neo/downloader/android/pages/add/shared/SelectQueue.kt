package com.neo.downloader.android.pages.add.shared

import com.neo.downloader.shared.util.ui.icon.MyIcons
import com.neo.downloader.shared.util.ui.myColors
import com.neo.downloader.shared.util.ui.theme.myTextSizes
import com.neo.downloader.shared.ui.widget.ActionButton
import com.neo.downloader.shared.ui.widget.IconActionButton
import com.neo.downloader.shared.ui.widget.Text
import com.neo.downloader.shared.util.ui.WithContentColor
import com.neo.downloader.shared.util.div
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.neo.downloader.android.ui.SheetHeader
import com.neo.downloader.android.ui.SheetTitle
import com.neo.downloader.android.ui.SheetUI
import com.neo.downloader.shared.util.ui.theme.LocalUiScale
import com.neo.downloader.resources.Res
import com.neo.downloader.shared.ui.widget.CheckBox
import com.neo.downloader.shared.util.OnFullyDismissed
import com.neo.downloader.shared.util.ResponsiveDialog
import com.neo.downloader.shared.util.rememberResponsiveDialogState
import com.neo.downloader.shared.util.ui.MultiplatformVerticalScrollbar
import com.neo.downloader.shared.util.ui.VerticalScrollableContent
import com.neo.downloader.shared.util.ui.theme.myShapes
import com.neo.downloader.shared.util.ui.theme.mySpacings
import io.github.oikvpqya.compose.fastscroller.rememberScrollbarAdapter
import ir.neo.util.compose.resources.myStringResource
import com.neo.downloader.queue.DownloadQueue
import ir.neo.util.compose.action.AnAction
import ir.neo.util.compose.asStringSource

@Composable
fun ShowAddToQueueDialog(
    queueList: List<DownloadQueue>,
    isOpened: Boolean,
    onQueueSelected: (Long?, Boolean) -> Unit,
    newQueueAction: AnAction,
    onClose: () -> Unit,
) {
    val state = rememberResponsiveDialogState(false)
    LaunchedEffect(isOpened) {
        if (isOpened) {
            state.show()
        } else {
            state.hide()
        }
    }
    state.OnFullyDismissed {
        onClose()
    }
    val (startQueue, setStartQueue) = remember {
        mutableStateOf(false)
    }
    ResponsiveDialog(
        onDismiss = state::hide,
        state = state,
    ) {
        SheetUI(
            header = {
                SheetHeader(
                    headerTitle = {
                        SheetTitle(
                            myStringResource(Res.string.select_queue)
                        )
                    }
                )
            }
        ) {
            WithContentColor(myColors.onBackground) {
                Column(
                    Modifier.fillMaxWidth()
                ) {
                    Column(
                        Modifier
                            .padding(horizontal = 8.dp)
                            .padding(bottom = 8.dp)
                    ) {
                        val addToQueueModifier = Modifier.fillMaxWidth()
                        Spacer(Modifier.height(8.dp))
                            val scrollState = rememberScrollState()
                        VerticalScrollableContent(
                            scrollState,
                            Modifier
                                .border(1.dp, myColors.onBackground / 5, myShapes.defaultRounded)
                                .padding(1.dp),
                        ) {
                            Column(
                                modifier = Modifier
                                    .verticalScroll(scrollState)
                            ) {
                                for (q in queueList) {
                                    key(q.id) {
                                        val queueModel by q.queueModel.collectAsState()
                                        QueueItemToSelect(
                                            modifier = addToQueueModifier,
                                            name = queueModel.name,
                                            onSelect = {
                                                onQueueSelected(queueModel.id, startQueue)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    setStartQueue(!startQueue)
                                }
                                .padding(vertical = 4.dp)
                                .padding(start = 2.dp)
                        ) {
                            CheckBox(
                                size = 24.dp,
                                value = startQueue,
                                onValueChange = setStartQueue
                            )
                            Spacer(Modifier.width(mySpacings.mediumSpace))
                            Text(myStringResource(Res.string.start_queue))
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconActionButton(
                                MyIcons.add,
                                contentDescription = Res.string.add_new_queue.asStringSource(),
                                onClick = newQueueAction
                            )
                            Spacer(Modifier.width(mySpacings.mediumSpace))
                            ActionButton(
                                text = myStringResource(Res.string.without_queue),
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    onQueueSelected(null, startQueue)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QueueItemToSelect(
    modifier: Modifier,
    name: String,
    onSelect: () -> Unit,
) {
    Row(
        modifier
            .clickable(onClick = onSelect)
            .heightIn(mySpacings.thumbSize)
            .padding(vertical = 4.dp)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            name,
            fontSize = myTextSizes.base,
        )
    }
}

@Composable
private fun Divider() {
    Spacer(
        Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(myColors.onBackground / 10),
    )
}
