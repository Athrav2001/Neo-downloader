package com.neo.downloader.android.pages.adblock

import com.neo.downloader.android.pages.browser.adblock.AdBlockFiltersManager
import com.neo.downloader.shared.util.BaseComponent
import com.arkivanov.decompose.ComponentContext
import ir.amirab.util.compose.action.MenuItem
import ir.amirab.util.compose.action.buildMenu
import ir.amirab.util.compose.action.simpleAction
import ir.amirab.util.compose.asStringSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AndroidAdBlockFiltersComponent(
    ctx: ComponentContext,
    private val adBlockFiltersManager: AdBlockFiltersManager,
    private val closeRequested: () -> Unit,
) : BaseComponent(ctx) {
    val sources = adBlockFiltersManager.sourcesFlow
    val hosts = adBlockFiltersManager.hostsFlow
    val isUpdating = adBlockFiltersManager.isUpdating
    val autoUpdateEnabled = adBlockFiltersManager.autoUpdateEnabledFlow
    val lastUpdatedAt = adBlockFiltersManager.lastUpdatedAtFlow

    private val _showManageSources = MutableStateFlow(false)
    val showManageSources = _showManageSources.asStateFlow()

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    private val _mainMenu: MutableStateFlow<MenuItem.SubMenu?> = MutableStateFlow(null)
    val mainMenu = _mainMenu.asStateFlow()

    fun onBack() = closeRequested()

    fun openMenu() {
        val autoUpdate = autoUpdateEnabled.value
        _mainMenu.value = MenuItem.SubMenu(
            title = "Ad Block".asStringSource(),
            items = buildMenu {
                +simpleAction(
                    if (autoUpdate) "Auto Update: ON".asStringSource() else "Auto Update: OFF".asStringSource()
                ) {
                    adBlockFiltersManager.setAutoUpdateEnabled(!autoUpdate)
                    closeMenu()
                }
                +simpleAction("Force Update".asStringSource()) {
                    forceUpdate()
                    closeMenu()
                }
                +simpleAction("Manage Sources".asStringSource()) {
                    _showManageSources.value = !_showManageSources.value
                    closeMenu()
                }
            }
        )
    }

    fun closeMenu() {
        _mainMenu.value = null
    }

    fun setSourceEnabled(sourceId: String, enabled: Boolean) {
        adBlockFiltersManager.setSourceEnabled(sourceId, enabled)
    }

    fun forceUpdate() {
        adBlockFiltersManager.forceUpdateInBackground { result ->
            _statusMessage.value = result.fold(
                onSuccess = { "Updated ${it} hosts" },
                onFailure = { "Update failed: ${it.message ?: "unknown error"}" }
            )
        }
    }

    fun dismissStatusMessage() {
        _statusMessage.value = null
    }
}
