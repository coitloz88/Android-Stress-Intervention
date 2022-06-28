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
}

dependencies {
    val work_version = "2.7.1"
    implementation("androidx.work:work-runtime-ktx:$work_version")

    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation ("com.android.support:support-compat:28.0.0")

    // 파일 경로를 libs\monkeybrains-sdk-release.aar이 있는 경로로 바꿔주어야 함
    // aar 파일 추가 방법: https://developer.android.com/studio/projects/android-library?hl=ko
    implementation(files("C:\\Users\\loveg\\Documents\\GitHub\\Stress-Intervention\\Android-Companion-App\\Comm Android\\app\\libs\\monkeybrains-sdk-release.aar"))

    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.preference:preference:1.1.1")

    val roomVersion = "2.3.0"

    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")

    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$roomVersion")
}