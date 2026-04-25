package com.neo.downloader.android.pages.credits.translators

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import com.neo.downloader.android.di.Di
import com.neo.downloader.resources.NDMResources
import com.neo.downloader.shared.ui.widget.MaybeLinkText

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.neo.downloader.android.ui.page.FooterFade
import com.neo.downloader.android.ui.page.PageHeader
import com.neo.downloader.android.ui.page.PageTitle
import com.neo.downloader.android.ui.page.PageUi
import com.neo.downloader.android.ui.page.rememberHeaderAlpha
import com.neo.downloader.android.util.compose.useBack
import com.neo.downloader.shared.util.ui.myColors
import com.neo.downloader.shared.util.ui.theme.myTextSizes
import com.neo.downloader.shared.ui.widget.Text
import com.neo.downloader.shared.util.div
import com.neo.downloader.resources.Res
import com.neo.downloader.shared.pages.credits.translators.LanguageTranslationInfo
import com.neo.downloader.shared.pages.credits.translators.TranslatorData
import com.neo.downloader.shared.ui.widget.PrimaryMainActionButton
import com.neo.downloader.shared.ui.widget.TransparentIconActionButton
import com.neo.downloader.shared.util.SharedConstants
import com.neo.downloader.shared.util.ui.LocalContentColor
import com.neo.downloader.shared.util.ui.WithContentAlpha
import com.neo.downloader.shared.util.ui.icon.MyIcons
import com.neo.downloader.shared.util.ui.theme.mySpacings
import ir.neo.util.URLOpener
import ir.neo.util.compose.asStringSource
import ir.neo.util.compose.dpToPx
import ir.neo.util.compose.localizationmanager.LanguageNameProvider
import ir.neo.util.compose.localizationmanager.MyLocale
import ir.neo.util.compose.resources.myStringResource
import ir.neo.util.ifThen
import kotlinx.serialization.json.Json
import org.koin.core.component.get

@Composable
fun TranslatorsPage(onBack: () -> Unit) {
    Translators(
        Modifier
            .fillMaxSize()
            .background(myColors.background)
    )
}

@Composable
internal fun Translators(modifier: Modifier) {
    val listState = rememberLazyListState()
    var contentPadding by remember {
        mutableStateOf(PaddingValues.Zero)
    }
    val topPadding = contentPadding.calculateTopPadding()
    val bottomPadding = contentPadding.calculateBottomPadding()
    val density = LocalDensity.current
    val headerAlpha by rememberHeaderAlpha(listState, topPadding.dpToPx(density))
    PageUi(
        modifier = modifier,
        header = {
            val onBack = useBack()
            PageHeader(
                leadingIcon = {
                    TransparentIconActionButton(
                        MyIcons.back,
                        Res.string.back.asStringSource(),
                    ) {
                        onBack?.onBackPressed()
                    }
                },
                headerTitle = {
                    PageTitle(
                        myStringResource(Res.string.meet_the_translators)
                    )
                },
                modifier = Modifier
                    .background(
                        myColors.background.copy(
                            alpha = headerAlpha * 0.75f
                        )
                    )
                    .statusBarsPadding()
            )
        },
        footer = {
            AnimatedContent(
                headerAlpha == 0f,
                transitionSpec = {
                    fadeIn() + expandVertically() togetherWith fadeOut() + shrinkVertically()
                },
                contentAlignment = Alignment.BottomCenter,
            ) {
                if (it) {
                    ContributionNotice(
                        modifier = Modifier,
                        onUserWantsToContribute = {
                            URLOpener.openUrl(SharedConstants.projectTranslations)
                        }
                    )
                } else {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                    )
                }
            }
//            AnimatedVisibility(
//                headerAlpha == 0f,
//                enter = expandVertically() + fadeIn(),
//                exit = shrinkVertically() + fadeOut(),
//            ) {
//
//            }
        },
    ) {
        contentPadding = it.paddingValues
        Box {
            DearTranslators(
                Modifier
                    .fillMaxWidth(),
                state = listState,
                contentPadding = it.paddingValues,
            )
            FooterFade(bottomPadding)
        }
    }
}

@Composable
private fun ContributionNotice(
    modifier: Modifier,
    onUserWantsToContribute: () -> Unit,
) {
    Column(
        modifier
            .fillMaxWidth()
            .background(myColors.surface),
    ) {
        Spacer(
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(myColors.onSurface / 0.15f)
        )
        Column(
            Modifier
                .padding(mySpacings.largeSpace)
                .navigationBarsPadding()
        ) {
            Text(
                myStringResource(Res.string.translators_page_thanks),
                modifier = Modifier,
                fontSize = myTextSizes.lg,
                fontWeight = FontWeight.Bold,
            )
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(1.dp)
                    .background(myColors.surface)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    Modifier.weight(1f)
                ) {
                    Text(
                        myStringResource(Res.string.translators_contribute_title),
                        fontSize = myTextSizes.lg,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        myStringResource(Res.string.translators_contribute_description),
                        fontSize = myTextSizes.base,
                        color = LocalContentColor.current / 0.75f
                    )
                }
            }
            Spacer(Modifier.height(mySpacings.largeSpace))
            PrimaryMainActionButton(
                text = myStringResource(Res.string.contribute),
                onClick = onUserWantsToContribute,
                modifier = Modifier.fillMaxWidth(),
                enabled = true,
            )
        }
    }
}

@Composable
private fun DearTranslators(
    modifier: Modifier,
    state: LazyListState,
    contentPadding: PaddingValues,
) {
    val itemHorizontalPadding = 16.dp
    val list = rememberLanguageTranslationInfo()

    LazyColumn(
        modifier,
        state = state,
        contentPadding = contentPadding,
    ) {
        itemsIndexed(list) { index, item ->
            TranslatedLanguageItem(
                item,
                Modifier
                    .fillMaxWidth()
                    .ifThen(index % 2 == 1) {
                        background(myColors.surface)
                    }
                    .padding(16.dp, itemHorizontalPadding)
            )
        }
    }
}

@Composable
private fun TranslatedLanguageItem(
    translationInfo: LanguageTranslationInfo,
    modifier: Modifier,
) {
    Column(modifier) {
        Column {
            WithContentAlpha(1f) {
                Text(
                    translationInfo.nativeName,
                    fontSize = myTextSizes.base,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
            Spacer(Modifier.height(mySpacings.smallSpace))
            WithContentAlpha(0.75f) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        translationInfo.englishName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = myTextSizes.base,
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        translationInfo.locale,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = myTextSizes.base,
                        color = myColors.primary,
                        modifier = Modifier
                            .background(myColors.primary / 10)
                            .padding(vertical = 0.dp, horizontal = 4.dp)
                    )
                }
            }
        }
        Spacer(Modifier.height(mySpacings.mediumSpace))
        Column(
            verticalArrangement = Arrangement.spacedBy(mySpacings.smallSpace)
        ) {
            translationInfo.translators.forEach {
                MaybeLinkText(
                    it.name,
                    it.link,
                )
            }
        }
    }
}

private fun convertLanguageToMyLocale(language: String): MyLocale {
    return language.split("-").run {
        MyLocale(
            languageCode = get(0),
            countryCode = getOrNull(1)
        )
    }
}

@Composable
private fun rememberLanguageTranslationInfo(): List<LanguageTranslationInfo> {
    val json = remember { Di.get<Json>() }
    val list by produceState(initialValue = emptyList<LanguageTranslationInfo>(), json) {
        value = runCatching {
            NDMResources.getTranslatorsContent()
                .let { json.decodeFromString<TranslatorData>(it) }
                .map {
                    val name = LanguageNameProvider.getName(convertLanguageToMyLocale(it.key))
                    LanguageTranslationInfo(
                        locale = it.key,
                        englishName = name.englishName,
                        nativeName = name.nativeName,
                        translators = it.value,
                    )
                }
        }.getOrDefault(emptyList())
    }
    return list
}
