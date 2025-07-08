import mytask.MyCustomTask

plugins {
    java
}


val javaVersion: String by project
val jacksonVersion: String by project
val junitVersion: String by project

// JDK 버전을 사용자가 직접 명시할 수 있음.
// JDK 버전을 바꾼다고, 빌드 스크립트를 따로 수정하지 않음.
// java plugin 필수!
java {
    toolchain {
        // languageVersion.set(JavaLanguageVersion.of(17))
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}


group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")

    // 여기가 junit5 표준 라이브러리라고 보면 됨. 아래 4줄.
    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher") // *******
    // EngineDiscoveryRequest가 생성될때, 반드시, OutputDirectoryProvider**를 인자로 받음.
    // 이 Provider는 테스트 중 생성되는 아티팩트(예: 리포트, 스냅샷 등)의 출력을 위한 디렉토리 관리를 담당
    // 런처가 없으면 Gradle이 자동으로 프로바이더를 넘길수 없음.
    // JUnit Jupiter 엔진이 discovery 과정에서 OutputDirectoryProvider not available 예외
    // junit-platform-launcher이걸 넣으면 JUnit Platform Launcher의 SPI(Service Provider Interface) 구현을 찾을 수 있음
    // Launcher가 정상 동작 → EngineDiscoveryRequest에 OutputDirectoryProvider를 포함해서 엔진에게 넘겨줌
    // TODO : 추후 소스 레벨로 파악 필요함.

    // 아래 3개 다 쓸거면 이거 하나만..
    // testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")

    // 이건 테스트 파일 컴파일을 위해서..
    // @Test, @BeforeEach, Assertions.assertEquals() 등 테스트 코드 작성을 위한 API
    // testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    // 이건 파라미터라이즈드 테스트 할꺼면 넣고.. 아님 빼고..
    // @ParameterizedTest, @ValueSource, @CsvSource 등 사용 시 필요
    // testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    // 이건 테스트 실행기 (테스트 실행 시 필요)
    // testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
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