println(">>> Settings file location: ${settings.settingsDir}")
println(">>> Gradle version: ${gradle.gradleVersion}")

// 이건 plugins 블록에 영향을미치는 세팅임.
// 안 넣으면 gradlePluginPortal() 기본으로 동작함.
// 만약 plugins에서 사용할려는 것중 gradlePluginPortal()에서 제공하지 않는 것이 있다면 여기서 추가해줘야 함.
pluginManagement {
//    val springBootVersion: String by settings // settings.getProperty("springBootVersion")
//    val springBootDependencyManagementVersion: String by settings // settings.getProperty("springBootDependencyManagementVersion")
//    val dockerComposeVersion: String by settings // settings.getProperty("dockerComposeVersion")

    repositories {
        gradlePluginPortal()
        mavenCentral()
    }

    // 플러그인 버전 고정, 여기서는 버전만 지정하고, 실제로 사용하는건 아님.
//    plugins {
//        id("org.springframework.boot") version springBootVersion
//        id("io.spring.dependency-management") version springBootDependencyManagementVersion
//        id("com.avast.gradle.docker-compose") version dockerComposeVersion
//    }
}
rootProject.name = "learn-gradle"
