buildscript {
    val kotlin_version = "1.5.30"
    extra.apply {
        set("kotlin_version", kotlin_version)
    }
    repositories {
        gradlePluginPortal()
        jcenter()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("com.android.tools.build:gradle:7.0.3")
        classpath ("org.jetbrains.kotlin:kotlin-serialization:$kotlin_version")
        classpath ("com.google.gms:google-services:4.3.4")
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.5")
        classpath ("com.google.firebase:firebase-crashlytics-gradle:2.5.2")
        classpath("com.squareup.sqldelight:gradle-plugin:1.5.0")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}