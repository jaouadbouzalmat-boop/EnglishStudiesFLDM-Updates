plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

// =========================================================
// SIGNATURE RELEASE — configuration locale
// =========================================================

val signingPropertiesFile = rootProject.file("keystore.properties")

check(signingPropertiesFile.exists()) {
    "Fichier keystore.properties introuvable : ${signingPropertiesFile.absolutePath}"
}

val signingProperties: Map<String, String> =
    signingPropertiesFile
        .readLines()
        .asSequence()
        .map { it.trim() }
        .filter {
            it.isNotEmpty() &&
                    !it.startsWith("#") &&
                    it.contains("=")
        }
        .associate { line ->
            line.substringBefore("=").trim() to
                    line.substringAfter("=").trim()
        }

fun signingValue(name: String): String =
    signingProperties[name]
        ?: error("Propriété de signature manquante : $name")

// =========================================================
// ANDROID
// =========================================================

android {
    namespace = "ma.fldm.englishstudies"

    compileSdk = 37

    defaultConfig {
        applicationId = "ma.fldm.englishstudies"

        minSdk = 24
        targetSdk = 37

        // =================================================
        // VERSION ACTUELLE POUR LE TEST DE MISE À JOUR
        // =================================================
        versionCode = 4
        versionName = "1.3"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    // =====================================================
    // SIGNATURE
    // =====================================================

    signingConfigs {
        create("release") {
            storeFile =
                rootProject.file(
                    signingValue("storeFile")
                )

            storePassword =
                signingValue("storePassword")

            keyAlias =
                signingValue("keyAlias")

            keyPassword =
                signingValue("keyPassword")
        }
    }

    // =====================================================
    // BUILD TYPES
    // =====================================================

    buildTypes {
        release {
            isMinifyEnabled = false

            signingConfig =
                signingConfigs.getByName("release")
        }
    }

    // =====================================================
    // JAVA
    // =====================================================

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // =====================================================
    // COMPOSE
    // =====================================================

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

// =========================================================
// DEPENDENCIES
// =========================================================

dependencies {

    // =====================================================
    // FIREBASE
    // =====================================================

    implementation(
        platform("com.google.firebase:firebase-bom:34.19.0")
    )

    implementation(
        "com.google.firebase:firebase-firestore"
    )

    implementation(
        "com.google.firebase:firebase-auth"
    )

    // Firebase Cloud Messaging
    implementation(
        "com.google.firebase:firebase-messaging"
    )

    // =====================================================
    // ANDROIDX / COMPOSE
    // =====================================================

    implementation(
        platform(libs.androidx.compose.bom)
    )

    implementation(
        libs.androidx.activity.compose
    )

    implementation(
        libs.androidx.compose.material3
    )

    implementation(
        "androidx.compose.material:material-icons-extended"
    )

    implementation(
        libs.androidx.compose.ui
    )

    implementation(
        libs.androidx.compose.ui.graphics
    )

    implementation(
        libs.androidx.compose.ui.tooling.preview
    )

    implementation(
        libs.androidx.core.ktx
    )

    implementation(
        libs.androidx.lifecycle.runtime.ktx
    )

    // =====================================================
    // DATASTORE
    // =====================================================

    implementation(
        libs.androidx.datastore.preferences
    )

    // =====================================================
    // ML KIT — TRADUCTION
    // ANGLAIS → FRANÇAIS / ARABE
    // =====================================================

    implementation(
        "com.google.mlkit:translate:17.0.3"
    )

    // =====================================================
    // TESTS
    // =====================================================

    testImplementation(
        libs.junit
    )

    androidTestImplementation(
        platform(libs.androidx.compose.bom)
    )

    androidTestImplementation(
        libs.androidx.compose.ui.test.junit4
    )

    androidTestImplementation(
        libs.androidx.espresso.core
    )

    androidTestImplementation(
        libs.androidx.junit
    )

    // =====================================================
    // DEBUG
    // =====================================================

    debugImplementation(
        libs.androidx.compose.ui.test.manifest
    )

    debugImplementation(
        libs.androidx.compose.ui.tooling
    )
}