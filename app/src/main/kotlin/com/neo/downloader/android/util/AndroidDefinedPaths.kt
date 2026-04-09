package com.neo.downloader.android.util

import com.neo.downloader.shared.util.DefinedPaths
import okio.Path

class AndroidDefinedPaths(
    dataDir: Path,
) : DefinedPaths(
    dataDir = dataDir
) {
    val lastSavedLocationFile = pagesStateDir.resolve("lastSavedLocation.json")
    val onboardingFile = pagesStateDir.resolve("onboarding.json")
    val homePageFile = pagesStateDir.resolve("home.json")
    val browserBookmarksFile = pagesStateDir.resolve("browser_bookmarks.json")
    val browserHistoryFile = pagesStateDir.resolve("browser_history.json")
    val browserSessionFile = pagesStateDir.resolve("browser_session.json")
}
