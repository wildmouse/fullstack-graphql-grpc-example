package com.example.helloworld.controller

import com.example.helloworld.entity.Greeting
import com.example.helloworld.service.GreetingService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
class HelloWorldController(
        private val service: GreetingService
) {

    @GetMapping("/greetings/{id}")
    fun getGreeting(@PathVariable("id") id: Int): Mono<Greeting> =
            Mono.just(service.getGreeting(id).get())

    @GetMapping("/hello-worlds")
    fun getHelloWorldList(): Flux<Greeting> = Flux.fromIterable(service.getGreetingList())

    @PostMapping("/hello-worlds")
    fun saveHelloWorld(@RequestParam("message") message: String): Mono<Greeting> =
            Mono.just(service.saveGreeting(message))
}
