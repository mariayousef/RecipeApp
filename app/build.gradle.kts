plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    id("kotlin-parcelize")
    alias(libs.plugins.safe.args)

}

android {
    namespace = "com.example.recipeapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.recipeapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11

    }

    kotlinOptions {
        jvmTarget = "11"

    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Android Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.recyclerview)

    // Navigation Component
    implementation(libs.nav.fragment.ktx)
    implementation(libs.nav.ui.ktx)

    // Retrofit & Gson
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.androidx.annotation)
    kapt(libs.room.compiler)

    // Glide
    implementation(libs.glide)
    kapt(libs.glide.compiler)

    // Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // Lifecycle ViewModel + LiveData
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.runtime)

    // Lottie (Splash Animation)
    implementation(libs.lottie)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //youtube
    implementation ("com.google.android.exoplayer:exoplayer:2.18.1")
    implementation ("com.google.android.exoplayer:exoplayer-ui:2.18.1")
    implementation ("com.pierfrancescosoffritti.androidyoutubeplayer:core:11.1.0")

}
