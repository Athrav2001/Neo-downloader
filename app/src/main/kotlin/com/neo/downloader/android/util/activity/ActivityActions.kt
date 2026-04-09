package com.neo.downloader.android.util.activity

import android.content.Intent
import com.neo.downloader.shared.util.mvi.ContainsEffects

interface ActivityActions {
    fun startActivityAction(intent: Intent)
    fun finishActivityAction()
}
