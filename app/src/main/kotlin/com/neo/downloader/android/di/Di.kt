package com.neo.downloader.android.di

import AndroidDirectLinkUpdateApplier
import android.app.Application
import android.content.Context
import com.neo.downloader.github.GithubApi
import com.neo.downloader.UpdateDownloadLocationProvider
import com.neo.downloader.UpdateManager
import com.neo.downloader.android.NDMApp
import com.neo.downloader.android.pages.home.HomePageStateToPersist
import com.neo.downloader.android.pages.onboarding.permissions.NDMPermissions
import com.neo.downloader.android.pages.onboarding.permissions.PermissionManager
import com.neo.downloader.android.pages.browser.adblock.AdBlockFilterSource
import com.neo.downloader.android.pages.browser.adblock.AdBlockFiltersManager
import com.neo.downloader.android.pages.browser.adblock.AdBlockSourceDatastoreStorage
import com.neo.downloader.android.pages.browser.adblock.IAdBlockSourceStorage
import com.neo.downloader.android.pages.browser.adblock.defaultAdBlockSources
import com.neo.downloader.android.receiver.StartOnBootBroadcastReceiver
import com.neo.downloader.android.repository.AppRepository
import com.neo.downloader.android.storage.AndroidExtraDownloadItemSettings
import com.neo.downloader.android.storage.AndroidExtraQueueSettings
import com.neo.downloader.android.storage.AndroidOnBoardingStorage
import com.neo.downloader.android.storage.AppSettingsStorage
import com.neo.downloader.android.storage.BrowserBookmarksStorage
import com.neo.downloader.android.storage.BrowserHistoryStorage
import com.neo.downloader.android.storage.BrowserSessionStorage
import com.neo.downloader.android.storage.HomePageStorage
import com.neo.downloader.android.storage.OnBoardingData
import com.neo.downloader.android.util.NDMAppManager
import com.neo.downloader.android.util.NDMServiceNotificationManager
import com.neo.downloader.android.util.AndroidDefinedPaths
import com.neo.downloader.android.util.AndroidDownloadItemOpener
import com.neo.downloader.android.util.AppInfo
import com.neo.downloader.shared.util.SharedConstants
import com.neo.downloader.shared.ui.theme.ThemeManager
import com.neo.downloader.queue.QueueManager
import com.neo.downloader.shared.util.ui.icon.MyIcons
import com.neo.downloader.shared.util.ui.theme.ISystemThemeDetector
import com.neo.downloader.DownloadManagerMinimalControl
import com.neo.downloader.DownloadSettings
import com.neo.downloader.connection.HttpDownloaderClient
import com.neo.downloader.connection.OkHttpHttpDownloaderClient
import com.neo.downloader.db.*
import com.neo.downloader.monitor.DownloadMonitor
import com.neo.downloader.utils.IDiskStat
import com.neo.downloader.resources.NDMLanguageResources
import com.neo.downloader.shared.downloaderinui.DownloaderInUiRegistry
import com.neo.downloader.shared.downloaderinui.hls.HLSDownloaderInUi
import com.neo.downloader.shared.downloaderinui.http.HttpDownloaderInUi
import com.neo.downloader.shared.repository.BaseAppRepository
import com.neo.downloader.shared.storage.BaseAppSettingsStorage
import com.neo.downloader.shared.storage.ExtraDownloadSettingsStorage
import com.neo.downloader.shared.storage.ExtraQueueSettingsStorage
import com.neo.downloader.shared.storage.IExtraDownloadSettingsStorage
import com.neo.downloader.shared.storage.IExtraQueueSettingsStorage
import com.neo.downloader.shared.storage.ILastSavedLocationsStorage
import com.neo.downloader.shared.storage.PerHostSettingsDatastoreStorage
import com.neo.downloader.shared.storage.ProxyDatastoreStorage
import com.neo.downloader.shared.storage.impl.LastSavedLocationStorage
import com.neo.downloader.shared.ui.theme.ThemeSettingsStorage
import com.neo.downloader.shared.ui.widget.NotificationManager
import com.neo.downloader.shared.updater.UpdateDownloaderViaDownloadSystem
import com.neo.downloader.shared.util.AndroidDiskStat
import com.neo.downloader.shared.util.AndroidSystemThemeDetector
import com.neo.downloader.shared.util.AppVersion
import com.neo.downloader.shared.util.DefinedPaths
import com.neo.downloader.shared.util.SizeAndSpeedUnitProvider
import com.neo.downloader.shared.util.UserAgentProviderFromSettings
import com.neo.downloader.shared.util.*
import com.neo.downloader.updateapplier.UpdateApplier
import com.neo.downloader.DownloadManager
import ir.amirab.util.config.datastore.createMapConfigDatastore
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.module
import com.neo.downloader.updatechecker.GithubUpdateChecker
import com.neo.downloader.updatechecker.UpdateChecker
import ir.amirab.util.AppVersionTracker
import com.neo.downloader.shared.util.appinfo.PreviousVersion
import com.neo.downloader.shared.util.autoremove.RemovedDownloadsFromDiskTracker
import com.neo.downloader.shared.util.category.*
import com.neo.downloader.shared.util.ondownloadcompletion.NoOpOnDownloadCompletionActionProvider
import com.neo.downloader.shared.util.ondownloadcompletion.OnDownloadCompletionActionProvider
import com.neo.downloader.shared.util.ondownloadcompletion.OnDownloadCompletionActionRunner
import com.neo.downloader.shared.util.onqueuecompletion.NoopOnQueueCompletionActionProvider
import com.neo.downloader.shared.util.onqueuecompletion.OnQueueEventActionRunner
import com.neo.downloader.shared.util.onqueuecompletion.OnQueueCompletionActionProvider
import com.neo.downloader.shared.util.perhostsettings.IPerHostSettingsStorage
import com.neo.downloader.shared.util.perhostsettings.PerHostSettingsItem
import com.neo.downloader.shared.util.perhostsettings.PerHostSettingsManager
import com.neo.downloader.shared.util.ui.IMyIcons
import com.neo.downloader.shared.util.proxy.IProxyStorage
import com.neo.downloader.shared.util.proxy.ProxyData
import com.neo.downloader.shared.util.proxy.ProxyManager
import com.neo.browser.logic.session.NeoBrowserSessionState
import com.neo.downloader.DownloaderRegistry
import com.neo.downloader.connection.UserAgentProvider
import com.neo.downloader.connection.proxy.AutoConfigurableProxyProvider
import com.neo.downloader.connection.proxy.NoopSystemProxySelectorProvider
import com.neo.downloader.connection.proxy.ProxyStrategyProvider
import com.neo.downloader.connection.proxy.SystemProxySelectorProvider
import com.neo.downloader.downloaditem.DownloadJob
import com.neo.downloader.downloaditem.IDownloadCredentials
import com.neo.downloader.downloaditem.IDownloadItem
import com.neo.downloader.downloaditem.hls.HLSDownloader
import com.neo.downloader.downloaditem.http.HttpDownloadCredentials
import com.neo.downloader.downloaditem.http.HttpDownloadItem
import com.neo.downloader.downloaditem.http.HttpDownloader
import com.neo.downloader.monitor.DownloadItemStateFactory
import com.neo.downloader.monitor.IDownloadMonitor
import com.neo.downloader.queue.ManualDownloadQueue
import com.neo.downloader.utils.EmptyFileCreator
import ir.amirab.util.compose.IIconResolver
import ir.amirab.util.compose.localizationmanager.LanguageManager
import ir.amirab.util.compose.localizationmanager.LanguageSourceProvider
import ir.amirab.util.compose.localizationmanager.LanguageStorage
import ir.amirab.util.config.datastore.kotlinxSerializationDataStore
import ir.amirab.util.startup.AbstractStartupManager
import ir.amirab.util.startup.Startup
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import okhttp3.Protocol
import okhttp3.internal.tls.OkHostnameVerifier

val downloaderModule = module {
    single<IDownloadQueueDatabase> {
        val definedPaths = get<DefinedPaths>()

        DownloadQueueFileStorageDatabase(
            queueFolder = get<DownloadFoldersRegistry>().registerAndGet(
                definedPaths.queuesDir
            ),
            fileSaver = get(),
        )
    }
    single<IDownloadListDb> {
        val definedPaths = get<DefinedPaths>()
        DownloadListFileStorage(
            downloadListFolder = get<DownloadFoldersRegistry>().registerAndGet(
                definedPaths.downloadListDir
            ),
            fileSaver = get(),
        )
    }
    single {
        TransactionalFileSaver(get())
    }
    single<IDownloadPartListDb> {
        val definedPaths = get<DefinedPaths>()
        PartListFileStorage(
            get<DownloadFoldersRegistry>().registerAndGet(
                definedPaths.partsDir
            ),
            get()
        )
    }
    single<IDiskStat> {
        AndroidDiskStat()
    }
    single<ISystemThemeDetector> {
        AndroidSystemThemeDetector(get())
    }
    single {
        QueueManager(get(), get())
    }
    single {
        DownloadFoldersRegistry()
    }
    single {
        DownloadSettings(
            8,
        )
    }
    single {
        ProxyManager(
            get()
        )
    }.bind<ProxyStrategyProvider>()
    single<SystemProxySelectorProvider> {
        NoopSystemProxySelectorProvider()
    }
    single<AutoConfigurableProxyProvider> {
        AutoConfigurableProxyProvider.NoOp()
    }
    single<UserAgentProvider> {
        UserAgentProviderFromSettings(get())
    }
    single<HttpDownloaderClient> {
        OkHttpHttpDownloaderClient(
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }
    single {
        val downloadSettings: DownloadSettings = get()
        EmptyFileCreator(
            diskStat = get(),
            useSparseFile = { downloadSettings.useSparseFileAllocation }
        )
    }
    single {
        HLSDownloader(inject())
    }
    single {
        HLSDownloaderInUi(get(), get())
    }
    single {
        HttpDownloader(inject())
    }
    single {
        HttpDownloaderInUi(get(), get())
    }
    single {
        DownloaderInUiRegistry().apply {
            add(get<HttpDownloaderInUi>())
            add(get<HLSDownloaderInUi>())
        }
    }.bind<DownloadItemStateFactory<IDownloadItem, DownloadJob>>()
    single {
        DownloaderRegistry().apply {
            add(get<HttpDownloader>())
            add(get<HLSDownloader>())
        }
    }
    single {
        val definedPaths = get<DefinedPaths>()
        DownloadManager(
            get(),
            get(),
            get(),
            get(),
            get(),
            get<DownloadFoldersRegistry>().registerAndGet(
                definedPaths.downloadDataDir
            )
        )
    }.bind(DownloadManagerMinimalControl::class)
    single {
        ManualDownloadQueue(get(), get())
    }
    single<IDownloadMonitor> {
        DownloadMonitor(
            downloadManager = get(),
            manualDownloadQueue = get(),
            downloadItemStateFactory = inject(),
        )
    }
}
val downloadSystemModule = module {
    single {
        val definedPaths = get<DefinedPaths>()
        get<DownloadFoldersRegistry>().registerAndGet(definedPaths.categoriesDir)
        CategoryFileStorage(
            file = definedPaths.categoriesFile.toFile(),
            fileSaver = get()
        )
    }.bind<CategoryStorage>()
    single {
        FileIconProviderUsingCategoryIcons(
            get(),
            get(),
            get(),
            get(),
        )
    }.bind<FileIconProvider>()
    single {
        DefaultCategories(
            icons = get(),
            getDefaultDownloadFolder = {
                get<BaseAppSettingsStorage>().defaultDownloadFolder.value
            }
        )
    }
    single {
        DownloadManagerCategoryItemProvider(get())
    }.bind<ICategoryItemProvider>()
    single {
        CategoryManager(
            categoryStorage = get(),
            scope = get(),
            defaultCategoriesFactory = get(),
            categoryItemProvider = get(),
        )
    }

    single {
        DownloadSystem(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }
    single {
        val definedPaths = get<DefinedPaths>()
        val extraDownloadSettingsStorageFolder = get<DownloadFoldersRegistry>().registerAndGet(
            definedPaths.extraDownloadSettings
        )
        ExtraDownloadSettingsStorage(
            extraDownloadSettingsStorageFolder,
            get(),
            AndroidExtraDownloadItemSettings
        )
    }.bind<IExtraDownloadSettingsStorage<*>>()
    single {
        val definedPaths = get<DefinedPaths>()
        val extraQueueSettingsStorageFolder = get<DownloadFoldersRegistry>().registerAndGet(
            definedPaths.extraQueueSettings
        )
        ExtraQueueSettingsStorage(
            extraQueueSettingsStorageFolder,
            get(),
            AndroidExtraQueueSettings
        )
    }.apply {
        bind<IExtraQueueSettingsStorage<*>>()
    }
    single<OnDownloadCompletionActionProvider> {
        NoOpOnDownloadCompletionActionProvider()
    }
    single<OnQueueCompletionActionProvider> {
        NoopOnQueueCompletionActionProvider()
    }
    single {
        OnDownloadCompletionActionRunner(
            downloadManagerMinimalControl = get(),
            scope = get(),
            onDownloadCompletionActionProvider = get(),
        )
    }
    single {
        OnQueueEventActionRunner(
            queueManager = get(),
            scope = get(),
            onQueueCompletionActionProvider = get(),
        )
    }
    single {
        PermissionManager(
            NDMPermissions.importantPermissions,
            get(),
        )
    }
}
val coroutineModule = module {
    single {
        CoroutineScope(SupervisorJob())
    }
}
val jsonModule = module {
    single {
        val downloaderRegistry: DownloaderRegistry by inject()
        Json {
            this.encodeDefaults = true
            this.prettyPrint = true
            this.ignoreUnknownKeys = true
            this.serializersModule = SerializersModule {
                polymorphic(IDownloadItem::class) {
                    downloaderRegistry.getAll().forEach {
                        subclass(it.downloadItemClass, it.downloadItemSerializer)
                    }
                    defaultDeserializer {
                        HttpDownloadItem.serializer()
                    }
                }
                polymorphic(IDownloadCredentials::class) {
                    downloaderRegistry.getAll().forEach {
                        subclass(it.downloadCredentialsClass, it.downloadCredentialsSerializer)
                    }
                    defaultDeserializer {
                        HttpDownloadCredentials.serializer()
                    }
                }
            }
        }
    }
}
val updaterModule = module {
    single {
        val definedPaths = get<DefinedPaths>()
        UpdateDownloadLocationProvider {
            definedPaths.updateDownloadLocation.toFile()
        }
    }
    single<UpdateApplier> {
        val definedPaths = get<DefinedPaths>()
        definedPaths.updateDownloadLocation
        AndroidDirectLinkUpdateApplier(
            updateDownloader = UpdateDownloaderViaDownloadSystem(
                get(),
                get(),
            ),
        )
    }
    single<UpdateChecker> {
        GithubUpdateChecker(
            AppVersion.get(),
            githubApi = GithubApi(
                owner = SharedConstants.projectGithubOwner,
                repo = SharedConstants.projectGithubRepo,
                client = OkHttpClient
                    .Builder()
                    .build()
            )
        )
    }
    single {
        UpdateManager(
            updateChecker = get(),
            updateApplier = get(),
            appVersionTracker = get(),
        )
    }
}
val startUpModule = module {
    single {
        Startup.getStartUpManager(get(), StartOnBootBroadcastReceiver::class.java)
    }.apply {
        bind<AbstractStartupManager>()
    }
}

fun getAppModule(context: NDMApp) = module {
    includes(downloaderModule)
    includes(downloadSystemModule)
    includes(coroutineModule)
    includes(jsonModule)
    includes(updaterModule)
    includes(startUpModule)
//    single {
//        NetworkChecker(get())
//    }
    single {
        AppInfo.definedPaths
    }.apply {
        bind<DefinedPaths>()
        bind<AndroidDefinedPaths>()
    }
    single {
        AppRepository(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }.apply {
        bind<BaseAppRepository>()
        bind<SizeAndSpeedUnitProvider>()
    }
    single {
        ThemeManager(get(), get(), get())
    }
//    single {
//        FontManager(get())
//    }
    single {
        LanguageManager(
            get(),
            LanguageSourceProvider(
                NDMLanguageResources.defaultLanguageResource,
                NDMLanguageResources.languages,
            )
        )
    }
    single {
        MyIcons
    }.apply {
        bind<IMyIcons>()
        bind<IIconResolver>()
    }
    single {
        val definedPaths = get<DefinedPaths>()
        ProxyDatastoreStorage(
            kotlinxSerializationDataStore(
                definedPaths.proxySettingsFile.toFile(),
                get(),
                ProxyData::default,
            )
        )
    }.bind<IProxyStorage>()
    single {
        val definedPaths = get<DefinedPaths>()
        AppSettingsStorage(
            createMapConfigDatastore(
                definedPaths.appSettingsFile.toFile(),
                get(),
            )
        )
    }.apply {
        bind<BaseAppSettingsStorage>()
        bind<LanguageStorage>()
        bind<ThemeSettingsStorage>()
    }
    single {
        RemovedDownloadsFromDiskTracker(
            get(), get(), get(),
        )
    }
    single {
        val definedPaths = get<DefinedPaths>()
        PreviousVersion(
            systemPath = definedPaths.systemDir.toFile(),
            currentVersion = AppVersion.get(),
        )
    }
    single {
        AppVersionTracker(
            previousVersion = {
                // it MUST be booted first
                get<PreviousVersion>().get()
            },
            currentVersion = AppVersion.get(),
        )
    }

    single {
        val appSettingsStorage: BaseAppSettingsStorage = get()
        AppSSLFactoryProvider(
            ignoreSSLCertificates = appSettingsStorage.ignoreSSLCertificates
        )
    }
    single {
        val appSettingsStorage: BaseAppSettingsStorage = get()
        AppHostNameVerifier(
            delegateHostnameVerifier = OkHostnameVerifier,
            ignoreHostNameVerification = appSettingsStorage.ignoreSSLCertificates
        )
    }
    single<OkHttpClient> {
        val appSSLFactoryProvider: AppSSLFactoryProvider = get()
        val appHostNameVerifier: AppHostNameVerifier = get()
        OkHttpClient
            .Builder()
            .protocols(listOf(Protocol.HTTP_1_1))
            .dispatcher(Dispatcher().apply {
                //bypass limit on concurrent connections!
                maxRequests = Int.MAX_VALUE
                maxRequestsPerHost = Int.MAX_VALUE
            })
            .sslSocketFactory(
                appSSLFactoryProvider.createSSLSocketFactory(),
                appSSLFactoryProvider.trustManager,
            )
            .hostnameVerifier(appHostNameVerifier)
            .build()
    }
    single<ILastSavedLocationsStorage> {
        val definedPaths = get<AndroidDefinedPaths>()
        LastSavedLocationStorage(
            kotlinxSerializationDataStore<List<String>>(
                definedPaths.lastSavedLocationFile.toFile(),
                get(),
                ::emptyList,
            )
        )
    }
    single<IPerHostSettingsStorage> {
        val definedPaths = get<DefinedPaths>()
        PerHostSettingsDatastoreStorage(
            kotlinxSerializationDataStore<List<PerHostSettingsItem>>(
                definedPaths.perHostSettingsFile.toFile(),
                get(),
                ::emptyList,
            )
        )
    }
    single {
        PerHostSettingsManager(get())
    }
    single<IAdBlockSourceStorage> {
        AdBlockSourceDatastoreStorage(
            kotlinxSerializationDataStore<List<AdBlockFilterSource>>(
                context.filesDir
                    .resolve("adblock")
                    .resolve("adBlockSources.json"),
                get(),
                ::defaultAdBlockSources,
            )
        )
    }
    single {
        AdBlockFiltersManager(
            appScope = get(),
            appSettingsStorage = get(),
            sourceStorage = get(),
        )
    }
    single { context }.apply {
        bind<NDMApp>()
        bind<Application>()
        bind<Context>()
    }
    single {
        NDMAppManager(get(), get(), get(), get(), get(), get(), get())
    }
    single {
        NDMServiceNotificationManager(get(), get(), get(), get(), get())
    }
    single {
        AndroidDownloadItemOpener(get())
    }.apply {
        bind<DownloadItemOpener>()
    }
    single { NotificationManager() }
    single {
        val paths = get<AndroidDefinedPaths>()
        AndroidOnBoardingStorage(
            kotlinxSerializationDataStore(
                paths.onboardingFile.toFile(),
                get(),
                ::OnBoardingData,
            )
        )
    }
    single {
        val paths = get<AndroidDefinedPaths>()
        HomePageStorage(
            kotlinxSerializationDataStore(
                paths.homePageFile.toFile(),
                get(),
                ::HomePageStateToPersist,
            )
        )
    }
    single {
        val paths = get<AndroidDefinedPaths>()
        BrowserBookmarksStorage(
            kotlinxSerializationDataStore(
                paths.browserBookmarksFile.toFile(),
                get(),
                ::emptyList,
            )
        )
    }
    single {
        val paths = get<AndroidDefinedPaths>()
        BrowserHistoryStorage(
            kotlinxSerializationDataStore(
                paths.browserHistoryFile.toFile(),
                get(),
                ::emptyList,
            )
        )
    }
    single {
        val paths = get<AndroidDefinedPaths>()
        BrowserSessionStorage(
            kotlinxSerializationDataStore(
                paths.browserSessionFile.toFile(),
                get(),
                ::NeoBrowserSessionState,
            )
        )
    }
}


object Di : KoinComponent {
    fun boot(applicationContext: NDMApp) {
        startKoin {
            modules(getAppModule(applicationContext))
        }
    }
}
