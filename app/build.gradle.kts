plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.mytodos"
    //noinspection GradleDependency
    compileSdk = 33  // Ubah dari compileSdk { version = release(36) }

    defaultConfig {
        applicationId = "com.example.mytodos"
        minSdk = 24  // Turunkan dari 33 ke 24 agar lebih kompatibel
        //noinspection OldTargetApi
        targetSdk = 33  // Ubah dari 36 ke 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Tambahkan dependencies baru
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}