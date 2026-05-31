plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hiltPlugin)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.apexvest.feature.onboarding"
    compileSdk = 35

    defaultConfig {
        minSdk = 26
    }

    buildFeatures {
        compose = true
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
    implementation(project(":core:designsystem"))
    implementation(project(":core:navigation"))

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hiltAndroidLib)
    ksp(libs.hiltCompilerLib)
    
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)

    // ML Kit for AI Onboarding
    implementation(libs.mlkitTextRecognition)
    implementation(libs.mlkitFaceDetection)
}
