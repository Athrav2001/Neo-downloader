package com.neo.downloader.android.pages.crashreport

import android.os.Build
import com.neo.downloader.shared.util.ui.myColors
import com.neo.downloader.shared.util.ui.theme.myTextSizes
import com.neo.downloader.shared.ui.widget.ActionButton
import com.neo.downloader.shared.util.ClipboardUtil
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import com.neo.downloader.shared.ui.widget.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.neo.downloader.android.ui.SheetHeader
import com.neo.downloader.android.ui.SheetTitle
import com.neo.downloader.android.ui.SheetUI
import com.neo.downloader.android.util.AppInfo
import com.neo.downloader.resources.Res
import com.neo.downloader.shared.util.OnFullyDismissed
import com.neo.downloader.shared.util.ResponsiveDialog
import com.neo.downloader.shared.util.rememberResponsiveDialogState
import com.neo.downloader.shared.util.ui.theme.myShapes
import com.neo.downloader.shared.util.ui.theme.mySpacings
import ir.amirab.util.compose.resources.myStringResource

@Composable
fun ErrorWindow(
    throwable: ThrowableData,
    close: () -> Unit,
) {
    val state = rememberResponsiveDialogState(true)
    state.OnFullyDismissed(close)
    ResponsiveDialog(state, state::hide) {
        SheetUI(
            header = {
                SheetHeader(
                    headerTitle = {
                        SheetTitle("Application Crash")
                    }
                )
            }
        ) {
            ErrorUi(throwable, state::hide)
        }
    }
}

@Composable
private fun ErrorUi(
    e: ThrowableData,
    close: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = mySpacings.largeSpace)
            .padding(bottom = mySpacings.largeSpace),
    ) {
        Header(
            modifier = Modifier
                .fillMaxWidth(),
            e
        )
        Spacer(Modifier.height(8.dp))
        RenderException(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 380.dp)
                .weight(1f, false),
            e = e
        )
        Spacer(Modifier.height(8.dp))
        Actions(
            modifier = Modifier
                .fillMaxWidth(),
            close = close,
            copyInformation = {
                ClipboardUtil.copy(createInformation(e))
            }
        )
        Spacer(Modifier.height(8.dp))
    }
}
fun createInformation(
    exceptionString: ThrowableData,
): String {

    val version = AppInfo.version
    val platform = AppInfo.platform.name
    return """
### Application Runtime Error
###### App Info
```
appVersion = $version
platform = $platform
Brand: ${Build.BRAND}
Manufacturer: ${Build.MANUFACTURER}
Model: ${Build.MODEL}
Android Version: ${Build.VERSION.RELEASE}
SDK: ${Build.VERSION.SDK_INT}
```
###### Exception
```
$exceptionString
```
""".trimIndent()
}

@Composable
private fun Header(modifier: Modifier = Modifier, e: ThrowableData) {
    Text(
        text = "We got an error in the application (\"${e.title}\")", modifier = modifier,
        fontSize = myTextSizes.xl
    )
}

@Composable
private fun RenderException(modifier: Modifier, e: ThrowableData) {
    val errorText = e.stacktrace
    Box(
        modifier = modifier
            .background(myColors.background)
            .clip(myShapes.defaultRounded)
            .horizontalScroll(rememberScrollState())
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {
        SelectionContainer {
            Text(
                text = errorText,
                color = myColors.error,
                fontSize = myTextSizes.base,
            )
        }
    }
}

@Composable
private fun Actions(
    modifier: Modifier = Modifier,
    close: () -> Unit,
    copyInformation: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
    ) {
        ActionButton(
            text = myStringResource(Res.string.close),
            onClick = close,
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(mySpacings.mediumSpace))
        ActionButton(
            text = myStringResource(Res.string.copy),
            onClick = copyInformation,
            modifier = Modifier.weight(1f)
        )
    }
}
