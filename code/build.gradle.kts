plugins {
    id("com.android.application") version "8.3.1" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
    id("androidx.navigation.safeargs") version "2.5.3" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
