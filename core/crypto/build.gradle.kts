plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hiltPlugin)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.apexvest.core.crypto"
    compileSdk = 35

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":core:common"))
    
    implementation(libs.hiltAndroidLib)
    ksp(libs.hiltCompilerLib)
    
    implementation(libs.bouncy.castle)
    implementation(libs.bitcoinj.core)
}
