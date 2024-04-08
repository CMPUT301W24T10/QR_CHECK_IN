plugins {
    id("com.android.application")

    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")

    id("androidx.navigation.safeargs")
}

android {
    namespace = "com.example.qr_check_in"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.qr_check_in"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")

    // dependency for QR code generation and scanning
    implementation("com.google.zxing:core:3.5.3")
    implementation("com.google.zxing:javase:3.5.3")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")

    //dependency for diaplying images
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")



    implementation ("com.google.zxing:core:3.3.0")
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.test.uiautomator:uiautomator:2.3.0")

    // testing dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    testImplementation ("org.mockito:mockito-core:3.11.2")
    androidTestImplementation ("androidx.navigation:navigation-testing:2.7.7")
    androidTestImplementation ("org.mockito:mockito-android:4.0.0")

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))

    // Add the dependencies for the Firebase Cloud Messaging and Analytics libraries
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-analytics")

    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    //geolocation
    implementation ("org.osmdroid:osmdroid-android:6.1.10")
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")
    implementation ("com.google.android.gms:play-services-location:21.2.0")
    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.karumi:dexter:6.2.1")


    // Test Runner and Rules for JUnit
    androidTestImplementation ("androidx.test:runner:1.4.0")
    androidTestImplementation ("androidx.test:rules:1.4.0")




    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-database")

    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries

    debugImplementation ("androidx.fragment:fragment-testing:1.4.1")

    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

    implementation("de.hdodenhof:circleimageview:3.1.0")



}