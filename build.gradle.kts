import mytask.MyCustomTask

plugins {
    id("java")
}



// JDK 버전을 사용자가 직접 명시할 수 있음.
// JDK 버전을 바꾼다고, 빌드 스크립트를 따로 수정하지 않음.
// java plugin 필수!
java {
    toolchain {
        // languageVersion.set(JavaLanguageVersion.of(17))
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}


group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
    implementation("com.fasterxml.jackson.core:jackson-databind:2.19.0")
}

// jar task 커스텀
// java plugin 필수!
tasks.named<Jar>("jar") {
    archiveBaseName.set("my-app")
    from("extra-resources/")
}

// test task 커스텀
// java plugin 필수!
tasks.named<Test>("test") {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed") // 테스트 로그 이벤트 지정
        showStandardStreams = true
    }

    // 커스텀 설정 예시: 특정 테스트만 실행
    // filter {
    //     includeTestsMatching("com.example.*")
    // }
}


// 존재하지 않는 Task를 hello라는 이름으로, 타입을 명시(MyCustomTask)해서, 등록
// 커스텀 task 클래스 필요함.
// 구체적인 방법은 나중에 추가..
// 그냥 등록만 하는것뿐, 실행되지 않음.
// 실행하려면 ./gradlew hello 또는 dependOn 설정 필요.
tasks.register<MyCustomTask>("hello") {
    group = "custom"
    description = "buildSrc에 정의된 인사 태스크"
}