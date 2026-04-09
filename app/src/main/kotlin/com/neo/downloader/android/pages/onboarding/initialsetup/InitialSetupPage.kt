package com.neo.downloader.android.pages.onboarding.initialsetup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.neo.downloader.android.pages.onboarding.StartUpPageActions
import com.neo.downloader.android.pages.onboarding.StartUpPageHeader
import com.neo.downloader.android.pages.onboarding.StartUpPageTemplate
import com.neo.downloader.resources.Res
import com.neo.downloader.shared.ui.configurable.ConfigurableUiProps
import com.neo.downloader.shared.ui.configurable.RenderConfigurable
import com.neo.downloader.shared.ui.widget.PrimaryMainActionButton
import com.neo.downloader.shared.ui.widget.Text
import com.neo.downloader.shared.util.ui.LocalContentAlpha
import com.neo.downloader.shared.util.ui.LocalContentColor
import com.neo.downloader.shared.util.ui.myColors
import com.neo.downloader.shared.util.ui.theme.myShapes
import com.neo.downloader.shared.util.ui.theme.mySpacings
import com.neo.downloader.shared.util.ui.theme.myTextSizes
import ir.amirab.util.compose.asStringSource
import ir.amirab.util.compose.resources.myStringResource

@Composable
fun InitialSetupPage(
    component: InitialSetupComponent,
) {
    StartUpPageTemplate(
        header = {
            StartUpPageHeader(
                title = Res.string.app_title.asStringSource(),
                onBackPressed = null
            )
        },
        actions = {
            StartUpPageActions {
                Column {
                    Text(
                        text = myStringResource(Res.string.initial_setup_notice),
                        color = LocalContentColor.current.copy(alpha = 0.75f),
                        modifier = Modifier
                            .padding(mySpacings.smallSpace)
                    )
                    Spacer(modifier = Modifier.height(mySpacings.mediumSpace))
                    Row {
                        PrimaryMainActionButton(
                            onClick = component::onUserPressFinish,
                            text = myStringResource(Res.string.next),
                            modifier = Modifier
                                .fillMaxWidth(),
                        )
                    }
                }
            }
        },
        content = {
            Column {
                Column(
                    Modifier
                        .weight(1f)
                        .wrapContentHeight()
                ) {
                    Spacer(Modifier.height(mySpacings.largeSpace))
                    Spacer(Modifier.height(mySpacings.largeSpace))
                    Text(
                        text = myStringResource(Res.string.welcome),
                        fontWeight = FontWeight.Bold,
                        fontSize = myTextSizes.x2l,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(),
                    )
                    Spacer(Modifier.height(mySpacings.mediumSpace))
                    Text(
                        text = myStringResource(Res.string.initial_setup_description),
                        fontSize = myTextSizes.lg,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(),
                    )
                }
                Column(
                    Modifier
                        .weight(1f)
                        .wrapContentHeight(Alignment.Bottom)
                        .padding(vertical = mySpacings.largeSpace),
                    verticalArrangement = Arrangement.spacedBy(mySpacings.largeSpace),
                ) {
                    for (configurable in component.configurables) {
                        RenderConfigurable(
                            cfg = configurable,
                            configurableUiProps = ConfigurableUiProps(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = mySpacings.largeSpace)
                                    .clip(myShapes.defaultRounded)
                                    .background(myColors.surface),
                                itemPaddingValues = PaddingValues(
                                    horizontal = mySpacings.largeSpace,
                                    vertical = mySpacings.mediumSpace,
                                ),
                            )
                        )
                    }
                }
            }
        }
    )
}
