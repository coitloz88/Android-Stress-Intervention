plugins {
    id("com.android.application")
    kotlin("android")
}

val compileSdkVersion: String by project
val minSdkVersion: String by project
val targetSdkVersion: String by project
val applicationId = "com.garmin.android.apps.connectiq.sample.comm"

android {
    compileSdk = this@Build_gradle.compileSdkVersion.toInt()

    defaultConfig {
        applicationId = this@Build_gradle.applicationId
        minSdk = minSdkVersion.toInt()
        targetSdk = targetSdkVersion.toInt()
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")

//    implementation("com.garmin.connectiq:monkeybrains:1.0.2")
//    implementation(files("libs\\connectiq.jar"))

    // 파일 경로를 libs\monkeybrains-sdk-release.aar이 있는 경로로 바꿔주어야 함
    // aar 파일 추가 방법: https://developer.android.com/studio/projects/android-library?hl=ko
    implementation(files("C:\\Users\\loveg\\Documents\\GitHub\\connectiq-android-sdk\\Comm Android\\app\\libs\\monkeybrains-sdk-release.aar"))
}