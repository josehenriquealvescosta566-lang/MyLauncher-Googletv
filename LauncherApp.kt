package com.meulauncher.tv

import android.app.Application
import com.google.android.material.color.DynamicColors

class LauncherApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Material You: usa cores dinâmicas quando o sistema oferece; senão, o tema padrão.
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
