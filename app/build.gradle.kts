plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.gs.moneybook"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.gs.moneybook"
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
    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("com.airbnb.android:lottie:6.6.1")

    implementation ("com.google.android.material:material:1.8.0")

    implementation ("androidx.recyclerview:recyclerview:1.3.1")

    implementation ("androidx.core:core-ktx:1.9.0")
    implementation ("androidx.documentfile:documentfile:1.0.1")




}