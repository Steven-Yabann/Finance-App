// Root-level build.gradle.kts

plugins {
    // Your standard Android + Kotlin setup
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false


    // ✅ Add this line
    id("com.google.gms.google-services") version "4.4.2" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.2")
    }
}


        // ✅ Make sure this is present
        classpath("com.google.gms:google-services:4.4.2")
        }
}
