CORRECTION v1.2

Le problème de compilation venait de app/build.gradle.kts : les plugins
Android et Kotlin Compose n'étaient pas appliqués au module app.

La correction ajoutée est :
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

Version : versionCode 3 / versionName 1.2

Lancer GENERER_APK_1_2.bat après extraction complète du ZIP.
