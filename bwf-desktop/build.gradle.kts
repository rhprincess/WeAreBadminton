import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "io.rhprincess"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("com.github.kittinunf.fuel:fuel:2.3.1")
                implementation("com.google.code.gson:gson:2.10.1")
                // Ktor latest 2.2.4
                implementation("io.ktor:ktor-client-core:1.6.6")
                implementation("io.ktor:ktor-client-cio:1.6.6")
                implementation("androidx.datastore:datastore-preferences-core:1.1.0-dev01")
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "bwf-desktop"
            packageVersion = "1.0.0"
        }
    }
}
