import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("kotlinx-serialization")
    id("com.squareup.sqldelight")
}

version = "1.0"

kotlin {
    android()

    val iosTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64

    iosTarget("ios") {}

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        frameworkName = "shared"
        podfile = project.file("../iosApp/Podfile")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:runtime:1.5.0")
                implementation("com.russhwolf:multiplatform-settings:0.7.7")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0-native-mt")
                implementation("io.insert-koin:koin-core:3.0.2")
                implementation("com.squareup.sqldelight:coroutines-extensions:1.5.0")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.2.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation("com.russhwolf:multiplatform-settings-test:0.7.7")
                implementation("io.insert-koin:koin-test:3.0.2")
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation("app.cash.turbine:turbine:0.5.1")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:android-driver:1.5.0")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-junit:1.5.10")
                implementation("org.jetbrains.kotlin:kotlin-test:1.5.10")
                implementation("androidx.test.ext:junit:1.1.3")
                implementation("androidx.test:core:1.4.0")
                implementation("androidx.test:runner:1.4.0")
                implementation("org.robolectric:robolectric:4.5.1")
                // Koin for JUnit 4
                implementation("io.insert-koin:koin-test-junit4:3.0.2")
                implementation("org.junit.jupiter:junit-jupiter:5.7.0")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:native-driver:1.5.0")
            }
        }
        val iosTest by getting
    }
}

android {
    compileSdkVersion(30)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(22)
        targetSdkVersion(30)
    }
}

sqldelight {
    database("Drink") {
        packageName = "drinkdb"
        sourceFolders = listOf("sqldelight")
    }
}
