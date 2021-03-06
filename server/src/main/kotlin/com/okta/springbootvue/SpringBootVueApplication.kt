package com.okta.springbootvue

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.core.Ordered
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import java.util.function.Consumer

@SpringBootApplication
class SpringBootVueApplication {
    // Bootstrap some test data into the in-memory database
    @Bean
    fun init(repository: TodoRepository): ApplicationRunner {
        return ApplicationRunner { _: ApplicationArguments? ->
            arrayOf("Buy milk", "Eat pizza", "Write tutorial", "Study Vue.js", "Go kayaking").forEach {
                val todo = Todo(it, false)
                repository.save(todo)
            }
            repository.findAll().forEach(Consumer { x: Todo? -> println(x) })
        }
    }

    // Fix the CORS errors
    @Bean
    fun simpleCorsFilter(): FilterRegistrationBean<*> {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        // *** URL below needs to match the Vue client URL and port ***
        config.allowedOrigins = listOf("http://localhost:8080")
        config.allowedMethods = listOf("*")
        config.allowedHeaders = listOf("*")
        source.registerCorsConfiguration("/**", config)
        val bean: FilterRegistrationBean<*> = FilterRegistrationBean(CorsFilter(source))
        bean.order = Ordered.HIGHEST_PRECEDENCE
        return bean
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(SpringBootVueApplication::class.java, *args)
        }
    }
}
