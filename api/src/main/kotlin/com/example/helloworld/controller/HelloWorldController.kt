package com.example.helloworld.controller

import com.example.helloworld.entity.Greeting
import com.example.helloworld.service.GreetingService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/greetings")
class HelloWorldController(
        private val service: GreetingService
) {

    @GetMapping("/{id}")
    fun getGreeting(@PathVariable("id") id: Int): Mono<Greeting> =
            Mono.just(service.getGreeting(id).get())

    @GetMapping
    fun getHelloWorldList(): Flux<Greeting> = Flux.fromIterable(service.getGreetingList())

    @PostMapping
    fun saveHelloWorld(@RequestParam("message") message: String): Mono<Greeting> =
            Mono.just(service.saveGreeting(message))
}
