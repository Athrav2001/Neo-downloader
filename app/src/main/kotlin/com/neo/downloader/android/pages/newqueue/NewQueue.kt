package com.neo.downloader.android.pages.newqueue

import androidx.compose.runtime.Composable
import com.neo.downloader.android.ui.configurable.SheetInput
import com.neo.downloader.resources.Res
import com.neo.downloader.shared.ui.widget.MyTextField
import ir.amirab.util.compose.asStringSource
import ir.amirab.util.compose.resources.myStringResource

@Composable
fun NewQueueSheet(
    onQueueCreate: (String) -> Unit,
    isOpened: Boolean,
    onCloseRequest: () -> Unit,
) {
    SheetInput(
        title = Res.string.add_new_queue.asStringSource(),
        validate = { it.isNotEmpty() },
        isOpened = isOpened,
        initialValue = { "" },
        onDismiss = onCloseRequest,
        onConfirm = onQueueCreate,
        inputContent = {
            MyTextField(
                modifier = it.modifier,
                text = it.editingValue,
                onTextChange = it.setEditingValue,
                placeholder = myStringResource(Res.string.queue_name),
            )
        },
    )
}
