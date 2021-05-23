buildscript {
    val kotlin_version = "1.5.0"
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
        classpath("com.android.tools.build:gradle:4.2.0")
        classpath ("org.jetbrains.kotlin:kotlin-serialization:$kotlin_version")
        classpath ("com.google.gms:google-services:4.3.4")
        classpath ("android.arch.navigation:navigation-safe-args-gradle-plugin:1.0.0")
        classpath ("com.google.firebase:firebase-crashlytics-gradle:2.4.1")
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