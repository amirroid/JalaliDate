@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.maven.publish)
}

version = "1.0.0-alpha1"
group = "io.github.amirroid"
val projectName = "JalaliDate"

kotlin {
    explicitApi()

    jvm()
    androidTarget()
    iosArm64()
    iosX64()
    iosSimulatorArm64()
    js(IR) {
        browser()
        nodejs()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.datetime)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}


android {
    namespace = "ir.amirroid.jalalidate"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}


mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates(
        groupId = group.toString(),
        artifactId = projectName.lowercase(),
        version = version.toString()
    )

    pom {
        name.set(projectName)
        inceptionYear = "2025"
        description.set("A Kotlin Multiplatform library for Jalali (Persian) date handling, conversion, formatting and parsing.")
        url.set("https://github.com/amirroid/JalaliDate")

        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/licenses/MIT")
            }
        }

        developers {
            developer {
                id.set("amirroid")
                name.set("Amirreza Gholami")
                url.set("https://github.com/amirroid")
            }
        }

        scm {
            connection.set("scm:git:git://github.com/amirroid/JalaliDate.git")
            developerConnection.set("scm:git:ssh://git@github.com/amirroid/JalaliDate.git")
            url.set("https://github.com/amirroid/JalaliDate")
        }
    }
}