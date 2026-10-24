package com.meulauncher.tv

import android.content.Intent
import android.content.pm.PackageManager

/** Lista somente apps realmente instalados. Nenhum conteúdo promocional. */
object AppLoader {

    @Suppress("DEPRECATION")
    fun load(pm: PackageManager, selfPackage: String): List<AppInfo> {
        val result = LinkedHashMap<String, AppInfo>()

        fun query(category: String) {
            val probe = Intent(Intent.ACTION_MAIN).addCategory(category)
            for (info in pm.queryIntentActivities(probe, 0)) {
                val pkg = info.activityInfo.packageName
                if (pkg == selfPackage || result.containsKey(pkg)) continue

                val launch = Intent(Intent.ACTION_MAIN)
                    .addCategory(category)
                    .setClassName(pkg, info.activityInfo.name)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)

                result[pkg] = AppInfo(
                    label = info.loadLabel(pm).toString(),
                    packageName = pkg,
                    icon = info.loadIcon(pm),
                    intent = launch
                )
            }
        }

        query(Intent.CATEGORY_LEANBACK_LAUNCHER) // apps feitos para TV
        query(Intent.CATEGORY_LAUNCHER)          // demais apps instalados

        return result.values.sortedBy { it.label.lowercase() }
    }
}
