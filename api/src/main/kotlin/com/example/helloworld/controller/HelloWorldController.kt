package com.example.helloworld.controller

import com.example.helloworld.grpc.HelloRequest
import com.example.helloworld.grpc.HelloServiceGrpc
import com.example.helloworld.service.HelloWorldService
import io.grpc.ManagedChannelBuilder
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
class HelloWorldController(
        private val service: HelloWorldService
) {

    // TODO: this point is just an example. remove after necessary gRPC services are implemented.
    @PostMapping("/hello-world/grpc")
    fun getHelloWorldGrpc(): Mono<String> {
        val channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build()
        val client = HelloServiceGrpc.newBlockingStub(channel)

        val request = HelloRequest.newBuilder().setName("gRPC").build()
        val reply = client.hello(request)
        return Mono.just(reply.message)
    }

    @GetMapping("/hello-worlds/{id}")
    fun getHelloWorld(@PathVariable("id") id: Int) = Mono.just(service.getHelloWorld(id))

    @GetMapping("/hello-worlds")
    fun getHelloWorldList() = Flux.just(service.getHelloWorldList())

    @PostMapping("/hello-worlds")
    fun saveHelloWorld(@RequestParam("message") message: String) =
            Mono.just(service.insertHelloWorld(message).id > 0)
}
