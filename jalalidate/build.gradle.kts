@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.maven.publish)
}

version = "1.0.1"
group = "io.github.amirroid"
val projectName = project.name

kotlin {
    explicitApi()

    jvm("desktop")
    androidTarget {
        publishLibraryVariants("release", "debug")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    js {
        browser()
        nodejs()
    }

    wasmJs {
        outputModuleName.set(projectName.lowercase())
        binaries.executable()
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "${projectName.lowercase()}.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = projectName
            isStatic = true
        }
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
    namespace = group.toString()
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
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
        artifactId = projectName,
        version = version.toString()
    )

    pom {
        name.set(projectName)
        inceptionYear.set("2025")
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