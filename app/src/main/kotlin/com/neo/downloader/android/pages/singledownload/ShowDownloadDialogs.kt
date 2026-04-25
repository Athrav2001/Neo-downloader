package com.neo.downloader.android.pages.singledownload

import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.*
import com.neo.downloader.android.ui.SheetHeader
import com.neo.downloader.android.ui.SheetTitle
import com.neo.downloader.android.ui.SheetUI
import com.neo.downloader.resources.Res
import com.neo.downloader.shared.singledownloadpage.createStatusString
import com.neo.downloader.shared.ui.widget.TransparentIconActionButton
import com.neo.downloader.shared.util.OnFullyDismissed
import com.neo.downloader.shared.util.ResponsiveDialog
import com.neo.downloader.shared.util.rememberResponsiveDialogState
import com.neo.downloader.shared.util.ui.icon.MyIcons
import com.neo.downloader.monitor.CompletedDownloadItemState
import com.neo.downloader.monitor.IDownloadItemState
import com.neo.downloader.monitor.ProcessingDownloadItemState
import ir.neo.util.compose.asStringSource
import kotlinx.coroutines.delay

@Composable
private fun getDownloadTitle(itemState: IDownloadItemState): String {
    return buildString {
        if (itemState is ProcessingDownloadItemState && itemState.percent != null) {
            append("${itemState.percent}%")
            append(" ")
        }
        append(createStatusString(itemState).rememberString())
    }
}


@Composable
fun ShowDownloadDialog(
    singleDownloadComponent: AndroidSingleDownloadComponent,
    onRequestShowInDownloads: () -> Unit,
) {
    val itemState by singleDownloadComponent.itemStateFlow.collectAsState()
    val dialogState = rememberResponsiveDialogState(false)
    dialogState.OnFullyDismissed {
        singleDownloadComponent.close()
    }
    LaunchedEffect(Unit) {
        // animate open after activity becomes fully open
        // is there a better way?
        delay(10)
        dialogState.show()
    }
    val closeDialog = dialogState::hide
    ResponsiveDialog(
        dialogState, closeDialog
    ) {
        itemState?.let { downloadItemState ->
            SheetUI(header = {
                SheetHeader(
                    headerTitle = {
                        SheetTitle(getDownloadTitle(downloadItemState))
                    },
                    headerActions = {
                        if (singleDownloadComponent.comesFromExternalApplication) {
                            TransparentIconActionButton(
                                MyIcons.externalLink,
                                contentDescription = Res.string.show_downloads.asStringSource(),
                                onClick = onRequestShowInDownloads,
                            )
                        }
                        TransparentIconActionButton(
                            MyIcons.close,
                            contentDescription = Res.string.close.asStringSource(),
                            onClick = closeDialog
                        )
                    }
                )
            }) {
                AnimatedContent(
                    targetState = downloadItemState,
                    contentKey = {
                        when (it) {
                            is CompletedDownloadItemState -> 0
                            is ProcessingDownloadItemState -> 1
                        }
                    }
                ) { downloadItemState ->
                    when (downloadItemState) {
                        is CompletedDownloadItemState -> {
                            CompletedDownloadPage(
                                singleDownloadComponent,
                                downloadItemState,
                            )
                        }

                        is ProcessingDownloadItemState -> {
                            ProgressDownloadPage(
                                singleDownloadComponent,
                                downloadItemState,
                            )
                        }
                    }
                }

            }
        }
    }

}



