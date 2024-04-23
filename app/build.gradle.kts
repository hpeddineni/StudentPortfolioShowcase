plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
    id ("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}

android {
    namespace = "uk.ac.tees.mad.w9643793"
    compileSdk = 34

    defaultConfig {
        applicationId = "uk.ac.tees.mad.w9643793"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        //Good practice - secure ServerClient key
        buildConfigField("String", "ServerClient", "\"${property("ServerClient")}\"")
    }

    buildFeatures {
        buildConfig = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.database)

    //viewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    //lottie
    implementation(libs.lottie.compose)
    //navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")

    implementation ("androidx.compose.material:material-icons-extended:1.6.5")

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation("com.google.android.gms:play-services-auth:21.1.0")
    implementation(libs.firebase.auth)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.firestore.ktx)

    // firebase storage
    implementation("com.google.firebase:firebase-storage-ktx")

    // coil for asyncImage
    implementation("io.coil-kt:coil-compose:2.6.0")

    implementation ("androidx.compose.runtime:runtime-livedata:1.6.6")

    // Gson library to convert json to object
    implementation ("com.google.code.gson:gson:2.10.1")

    //Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.50")

    ksp ("com.google.dagger:hilt-compiler:2.49")
    implementation ("androidx.hilt:hilt-navigation-compose:1.2.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}