package mytask

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class MyCustomTask : DefaultTask() {
    @TaskAction
    fun greet() {
        println("👋 안녕하세요, buildSrc에서 왔어요!")
    }
}