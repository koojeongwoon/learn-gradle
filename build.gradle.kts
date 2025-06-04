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
    archiveBaseName.set("my-app") // 생성되는 JAR 파일 이름이 my-app-<version>.jar로 바뀜
    from("extra-resources/") // extra-resources/ 디렉토리 내용이 JAR 안에 함께 패키징됨
    // project/					->	 my-app-1.0.0.jar
    // ├── build.gradle.kts		->	 ├── META-INF/
    // └── extra-resources/		->	 ├── com/...
    //     ├── config.yml		->	 ├── config.yml
    //     └── README.txt		->	 └── README.txt
    // 추후 연구 destinationDirectory, archiveClassifier, archiveVersion
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

// 존재하지 않는 Task를 A라는 이름만으로 등록
// 별도의 커스텀 task 필요X, doLast, doFirst 동작 추가
//tasks.register("A") {
//    dependsOn("B") // A는 B가 실행된 이후에 실행한다.
//    doLast {
//        println("A 실행")
//    }
//}

// 존재하지 않는 Task를 hello라는 이름으로, 타입을 명시(MyCustomTask)해서, 등록
// 커스텀 task 클래스 필요함.
// 구체적인 방법은 나중에 추가..
// 그냥 등록만 하는것뿐, 실행되지 않음.
// 실행하려면 ./gradlew hello 또는 dependOn 설정 필요.
//tasks.register<MyCustomTask>("hello") {
//    group = "custom"
//    description = "buildSrc에 정의된 인사 태스크"
//}
//
//tasks.named("build") {
//    dependsOn("hello") // "hello" 태스크가 build 전에 실행됨
//    // finalizedBy("hello") // build 끝난 뒤에 hello 실행
//}
//
//tasks.named("hello") {
//    mustRunAfter("build") // build가 먼저, hello가 나중에 실행
//}
//
//if (System.getenv("RUN_HELLO") == "true") {
//    tasks.named("build") {
//        dependsOn("hello")
//    }
//}
//
//// ./gradlew build -PrunHello
//if (project.hasProperty("runHello")) {
//    tasks.named("build") {
//        dependsOn("hello")
//    }
//}
//
//tasks.named("test") {
//    dependsOn("hello")
//}
//
//
//tasks.named("clean") {
//    finalizedBy("hello")
//}