package com.example.helloworld.controller

import com.example.helloworld.service.HelloWorldService
import org.springframework.web.bind.annotation.*

@RestController
class HelloWorldController(private val service: HelloWorldService) {

    @GetMapping("/hello-worlds/{id}")
    fun getHelloWorld(@PathVariable("id") id: Int) = service.getHelloWorld(id)

    @GetMapping("/hello-worlds")
    fun getHelloWorldList() = service.getHelloWorldList()

    @PostMapping("/hello-worlds")
    fun saveHelloWorld(@RequestParam("message") message: String) =
            service.insertHelloWorld(message)
}
