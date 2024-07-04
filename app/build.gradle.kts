plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 27
        targetSdk = 34
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

    // Biblioteca de compatibilidade AppCompat
    implementation ("androidx.appcompat:appcompat:1.4.0")

    // Retrofit para requisições HTTP
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")

    // Conversor Gson para Retrofit
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // RecyclerView para listas
    implementation ("androidx.recyclerview:recyclerview:1.3.2")

    // Gson para parsing JSON
    implementation ("com.google.code.gson:gson:2.10.1")

    // Coroutines para programação assíncrona
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Glide para carregamento de imagens
    implementation ("com.github.bumptech.glide:glide:4.12.0")

    // Arquitetura de Componentes do Android (ViewModel e LiveData)
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.3")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.8.3")

    // Material Design Components
    implementation ("com.google.android.material:material:1.12.0")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}