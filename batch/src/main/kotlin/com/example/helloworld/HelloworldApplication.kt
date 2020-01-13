package com.example.helloworld

import com.example.helloworld.service.GreetingService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import kotlin.system.exitProcess

data class FakeName(
		var name: String = ""
)

@SpringBootApplication
class HelloWorldApplication(
		private val service: GreetingService
): CommandLineRunner {
	companion object {
		val log = LoggerFactory.getLogger(HelloWorldApplication::class.java)
	}

	override fun run(vararg args: String?) {
		val request = Request.Builder().url("https://api.namefake.com/").build()
		val client = OkHttpClient()
		val objectMapper = ObjectMapper()
				.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        try {
			val response = client.newCall(request).execute()
            val body = response.body?.string() ?: ""
			val fakeName = objectMapper.readValue(body, FakeName::class.java)
			service.saveGreeting("Hello, ${fakeName.name}!")
			service.getGreetingList().forEach {
				log.info(it.message)
			}
		} catch (e: Exception) {
			log.error(e.message)
            exitProcess(1)
		}
	}
}

fun main(args: Array<String>) {
	SpringApplication.run(HelloWorldApplication::class.java, *args)
}
