plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
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
    buildFeatures {
        mlModelBinding = true
    }
}

dependencies {
    implementation("org.tensorflow:tensorflow-lite-support:0.4.1")
    implementation("org.tensorflow:tensorflow-lite-metadata:0.4.1")
    implementation("org.tensorflow:tensorflow-lite-gpu:2.9.0")
    val workVersion = "2.7.1"
    implementation("androidx.work:work-runtime-ktx:$workVersion")

    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("com.android.support:support-compat:28.0.0")

    // 파일 경로를 libs\monkeybrains-sdk-release.aar이 있는 경로로 바꿔주어야 함
    // aar 파일 추가 방법: https://developer.android.com/studio/projects/android-library?hl=ko
    implementation(files("\\libs\\monkeybrains-sdk-release.aar"))

    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("com.google.android.material:material:1.6.1")
    implementation("androidx.preference:preference:1.2.0")

    val roomVersion = "2.4.2"

    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")

    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$roomVersion")
    kapt("org.xerial:sqlite-jdbc:3.36.0.3")

    //location information
    implementation("com.google.android.gms:play-services-location:20.0.0")
}
