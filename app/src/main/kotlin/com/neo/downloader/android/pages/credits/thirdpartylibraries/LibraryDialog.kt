package com.neo.downloader.android.pages.credits.thirdpartylibraries

import com.neo.downloader.shared.util.ui.myColors
import com.neo.downloader.shared.util.ui.theme.myTextSizes
import com.neo.downloader.shared.ui.widget.ActionButton
import com.neo.downloader.shared.util.ui.WithContentAlpha
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.neo.downloader.shared.ui.widget.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.neo.downloader.android.ui.SheetHeader
import com.neo.downloader.android.ui.SheetTitle
import com.neo.downloader.android.ui.SheetUI
import com.neo.downloader.resources.Res
import com.neo.downloader.shared.ui.widget.MaybeLinkText
import com.neo.downloader.shared.util.OnFullyDismissed
import com.neo.downloader.shared.util.ResponsiveDialog
import com.neo.downloader.shared.util.div
import com.neo.downloader.shared.util.rememberResponsiveDialogState
import com.neo.downloader.shared.util.ui.theme.myShapes
import com.neo.downloader.shared.util.ui.theme.mySpacings
import com.mikepenz.aboutlibraries.entity.Developer
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.entity.License
import com.mikepenz.aboutlibraries.entity.Organization
import ir.neo.util.compose.resources.myStringResource
import ir.neo.util.compose.StringSource
import ir.neo.util.compose.asStringSource
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun LibraryDialog(
    library: Library?,
    onCloseRequest: () -> Unit,
) {
    val state = rememberResponsiveDialogState(false)
    state.OnFullyDismissed(onCloseRequest)
    LaunchedEffect(library) {
        if (library == null) {
            state.hide()
        } else {
            state.show()
        }
    }
    val hideDialog: () -> Unit = state::hide
    ResponsiveDialog(
        state = state,
        onDismiss = hideDialog
    ) {
        Column {
            library?.let { library ->
                SheetUI(
                    header = {
                        SheetHeader(
                            headerTitle = {
                                SheetTitle(myStringResource(Res.string.info))
                            }
                        )
                    }
                ) {
                    Column(
                        modifier = Modifier.padding(mySpacings.largeSpace)
                    ) {
                        Column(
                            Modifier
                                .weight(1f, false)
                                .verticalScroll(rememberScrollState())
                        ) {
                            LibraryNameAndVersion(library.name, library.artifactVersion, library.artifactId)
                            Spacer(Modifier.height(16.dp))
                            library.description?.let {
                                LibraryDescription(it)
                                Spacer(Modifier.height(16.dp))
                            }
                            library.developers.takeIf { it.isNotEmpty() }?.let {
                                LibraryDevelopers(it)
                                Spacer(Modifier.height(8.dp))
                            }
                            library.organization?.let {
                                LibraryOrganization(it)
                                Spacer(Modifier.height(8.dp))
                            }
                            val links = buildList {
                                library.scm?.url?.let {
                                    add(Res.string.source_code.asStringSource() to it)
                                }
                                library.website?.let {
                                    add(Res.string.website.asStringSource() to it)
                                }
                            }
                            links.takeIf { it.isNotEmpty() }?.let {
                                LibraryLinks(links)
                                Spacer(Modifier.height(8.dp))
                            }
                            LibraryLicenseInfo(library.licenses)
                        }
                        Spacer(Modifier.height(8.dp))
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                        ) {
                            ActionButton(
                                text = myStringResource(Res.string.close),
                                onClick = {
                                    hideDialog()
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LibraryLinks(links: List<Pair<StringSource, String>>) {
    KeyValue(myStringResource(Res.string.links)) {
        ListOfNamesWithLinks(links)
    }
}

@Composable
private fun LibraryDescription(description: String) {
    Text(
        description,
        modifier = Modifier
            .fillMaxWidth()
            .clip(myShapes.defaultRounded)
            .background(myColors.onSurface / 0.1f)
            .padding(8.dp),
        color = myColors.onSurface,
    )
}

@Composable
private fun LibraryLicenseInfo(licenses: ImmutableSet<License>) {
    KeyValue(myStringResource(Res.string.license)) {
        val l = licenses.map {
            it.name.asStringSource() to it.url
        }
        if (l.isEmpty()) {
            Text(myStringResource(Res.string.no_license_found))
        } else {
            ListOfNamesWithLinks(l)
        }
    }
}

@Composable
private fun LibraryDevelopers(devs: List<Developer>) {
    KeyValue(myStringResource(Res.string.developers)) {
        ListOfNamesWithLinks(
            devs
                .filter { it.name != null }
                .map {
                    it.name!!.asStringSource() to it.organisationUrl
                }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ListOfNamesWithLinks(map: List<Pair<StringSource, String?>>) {
    FlowRow {
        for ((i, v) in map.withIndex()) {
            val (name, link) = v
            MaybeLinkText(name.rememberString(), link)
            if (i < map.lastIndex) {
                Text(", ")
            }
        }
    }
}

@Composable
fun LibraryOrganization(organization: Organization) {
    KeyValue(myStringResource(Res.string.organization)) {
        MaybeLinkText(organization.name, organization.url)
    }
}

@Composable
private fun LibraryNameAndVersion(
    name: String, version: String?,
    artifactId: String,
) {
    val nameWithVersion = name + (version?.let { " $it" }.orEmpty())
    Column {
        Row {
            Text(
                "$nameWithVersion",
                fontWeight = FontWeight.Bold,
                fontSize = myTextSizes.base,
            )
        }
        Spacer(Modifier.height(4.dp))
        WithContentAlpha(0.75f) {
            Row {
                Text(
                    "($artifactId)",
                    fontSize = myTextSizes.sm,
                )
            }
        }
    }
}

@Composable
private fun KeyValue(
    key: String,
    value: @Composable () -> Unit,
) {
    Row {
        WithContentAlpha(0.75f) {
            Text(
                "$key:",
                maxLines = 1,
            )
        }
        Spacer(Modifier.width(8.dp))
        value()
    }
}

