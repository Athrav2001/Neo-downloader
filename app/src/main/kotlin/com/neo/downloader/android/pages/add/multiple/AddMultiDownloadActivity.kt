package com.neo.downloader.android.pages.add.multiple

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.neo.downloader.android.pages.category.CategorySheet
import com.neo.downloader.android.pages.newqueue.NewQueueSheet
import com.neo.downloader.android.util.NDMAppManager
import com.neo.downloader.android.util.activity.NDMActivity
import com.neo.downloader.android.util.activity.HandleActivityEffects
import com.neo.downloader.android.util.activity.getSerializedExtra
import com.neo.downloader.android.util.activity.putSerializedExtra
import com.neo.downloader.shared.downloaderinui.DownloaderInUiRegistry
import com.neo.downloader.shared.pages.adddownload.AddDownloadConfig
import com.neo.downloader.shared.storage.ILastSavedLocationsStorage
import com.neo.downloader.shared.util.DownloadSystem
import com.neo.downloader.shared.util.FileIconProvider
import com.neo.downloader.shared.util.OnFullyDismissed
import com.neo.downloader.shared.util.ResponsiveDialog
import com.neo.downloader.shared.util.category.CategoryManager
import com.neo.downloader.shared.util.rememberChild
import com.neo.downloader.shared.util.rememberResponsiveDialogState
import com.neo.downloader.queue.QueueManager
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import org.koin.core.component.inject

class AddMultiDownloadActivity : NDMActivity() {
    private val json: Json by inject()
    private val downloadSystem: DownloadSystem by inject()
    private val appManager: NDMAppManager by inject()
    private val downloaderInUiRegistry: DownloaderInUiRegistry by inject()
    private val lastSavedLocationsStorage: ILastSavedLocationsStorage by inject()
    private val queueManager: QueueManager by inject()
    private val categoryManager: CategoryManager by inject()
    private val iconProvider: FileIconProvider by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val myRetainedComponent = myRetainedComponent {
            val config = getComponentConfig(intent)
            val appManager = appManager
            val closeAddDownloadDialog = {
                this@myRetainedComponent.finishActivityAction()
            }
            AndroidAddMultiDownloadComponent(
                ctx = it,
                onRequestClose = closeAddDownloadDialog,
                lastSavedLocationsStorage = lastSavedLocationsStorage,
                id = config.id,
                queueManager = queueManager,
                categoryManager = categoryManager,
                downloadSystem = downloadSystem,
                onRequestAdd = { items, queueId, categorySelectionMode ->
                    appManager.addDownloads(
                        items = items,
                        categorySelectionMode = categorySelectionMode,
                        queueId = queueId,
                    )
                },
                perHostSettingsManager = perHostSettingsManager,
                fileIconProvider = iconProvider,
                appRepository = appRepository,
                downloaderInUiRegistry = downloaderInUiRegistry,
            ).apply { addItems(config.newDownloads) }
        }
        val addDownloadComponent = myRetainedComponent.component
        setNDMContent {
            myRetainedComponent.HandleActivityEffects()
            AddMultiItemPage(addDownloadComponent)
            CategorySheet(
                categoryComponent = addDownloadComponent.categorySlot.rememberChild(),
                onDismiss = addDownloadComponent::closeCategoryDialog
            )
            NewQueueSheet(
                onQueueCreate = addDownloadComponent::createQueueWithName,
                isOpened = addDownloadComponent.showAddQueue.collectAsState().value,
                onCloseRequest = { addDownloadComponent.setShowAddQueue(false) },
            )
        }
    }

    private fun getComponentConfig(intent: Intent): AddDownloadConfig.MultipleAddConfig {
        runCatching {
            with(json) {
                intent.getSerializedExtra<AddDownloadConfig.MultipleAddConfig>(COMPONENT_CONFIG_KEY)
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()?.let {
            return it
        }
        return AddDownloadConfig.MultipleAddConfig()
    }

    companion object {
        const val COMPONENT_CONFIG_KEY = "ComponentConfig"
        fun createIntent(
            context: Context,
            multipleAddConfig: AddDownloadConfig.MultipleAddConfig,
            json: Json,
        ): Intent {
            val intent = Intent(
                context,
                AddMultiDownloadActivity::class.java,
            )
            with(json) {
                intent.putSerializedExtra(COMPONENT_CONFIG_KEY, multipleAddConfig)
            }
            return intent
        }
    }
}
