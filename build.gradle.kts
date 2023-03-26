buildscript {
    val kotlin_version = "1.8.10"
    extra.apply {
        set("kotlin_version", kotlin_version)
    }
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("com.android.tools.build:gradle:7.4.1")
        classpath ("org.jetbrains.kotlin:kotlin-serialization:$kotlin_version")
        classpath ("com.google.gms:google-services:4.3.10")
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.4.1")
        classpath ("com.google.firebase:firebase-crashlytics-gradle:2.5.2")
        classpath("com.squareup.sqldelight:gradle-plugin:1.5.5")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}