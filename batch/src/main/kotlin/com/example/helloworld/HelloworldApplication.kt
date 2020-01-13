package com.example.helloworld

import com.example.helloworld.service.GreetingService
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HelloWorldApplication(
		private val service: GreetingService
): CommandLineRunner {
	companion object {
		val log = LoggerFactory.getLogger(HelloWorldApplication::class.java)
	}

	override fun run(vararg args: String?) {
		service.saveGreeting("Hello, World!")
		service.getGreetingList().forEach {
			log.info(it.message)
		}
	}
}

fun main(args: Array<String>) {
	runApplication<HelloWorldApplication>(*args)
}
