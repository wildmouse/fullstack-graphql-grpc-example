package com.example.helloworld.controller

import com.example.helloworld.service.HelloWorldService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.websocket.server.PathParam

@RestController
class HelloWorldController(private val service: HelloWorldService) {

    @GetMapping("/hello-worlds/{id}")
    fun getHelloWorld(
            @PathParam("id") id: Int
    ) = service.getHelloWorld(id)

    @GetMapping("/hello-worlds")
    fun getHelloWorldList() = service.getHelloWorldList()

    @PostMapping("/hello-worlds")
    fun saveHelloWorld(@RequestParam("message") message: String) =
            service.insertHelloWorld(message)
}
