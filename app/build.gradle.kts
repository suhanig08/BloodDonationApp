plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.adas.redconnect"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.adas.redconnect"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
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
        viewBinding = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.firebase.auth)
    implementation(libs.play.services.cast.framework)
    implementation(libs.firebase.storage.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //retrofit
    implementation (libs.retrofit)
    //gson
    implementation (libs.converter.gson)
    implementation(libs.places.ktx)

    implementation (libs.androidx.navigation.fragment.ktx.v260)
    implementation (libs.androidx.navigation.ui.ktx.v260)
    
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.google.maps.android:places-ktx:3.2.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.google.maps.android:maps-ktx:5.1.1")
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation ("com.google.firebase:firebase-database-ktx")

    implementation ("com.hbb20:ccp:2.5.0")
    implementation (libs.firebase.firestore.v2470)
    implementation (libs.firebase.messaging)
    implementation (libs.android.volley)
    implementation (libs.kotlinx.coroutines.core)
    implementation (libs.kotlinx.coroutines.android)
    implementation(libs.dexter)
    implementation(libs.google.auth.library.oauth2.http.v1190)

    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation ("com.github.dhaval2404:imagepicker:2.1")


    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.google.firebase:firebase-database:20.3.0")
    implementation ("com.github.dhaval2404:imagepicker:2.1")


    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation ("com.github.dhaval2404:imagepicker:2.1")
}