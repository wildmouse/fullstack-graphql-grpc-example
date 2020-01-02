package com.example.helloworld.service

import com.example.helloworld.entity.HelloWorld
import com.example.helloworld.grpc.greeting.*
import com.example.helloworld.repository.HelloWorldRepository
import io.grpc.stub.StreamObserver

class GreetingService(
        private val repository: HelloWorldRepository
) : GreetingServiceGrpc.GreetingServiceImplBase() {
    override fun getGreeting(request: GetGreetingRequest?, responseObserver: StreamObserver<GetGreetingResponse>?) {
        val id = request?.id ?: -1
        val message = repository.findById(id).get().message
        val response = GetGreetingResponse
                .newBuilder()
                .setMessage(message)
                .build()

        responseObserver?.onNext(response)
        responseObserver?.onCompleted()
    }

    override fun getGreetings(request: GetGreetingsRequest?, responseObserver: StreamObserver<GetGreetingsResponse>?) {
        val messages = repository.findAll().map { it.message }
        val response = GetGreetingsResponse
                .newBuilder()
                .addAllMessages(messages)
                .build()

        responseObserver?.onNext(response)
        responseObserver?.onCompleted()
    }

    override fun saveGreeting(request: SaveGreetingRequest?, responseObserver: StreamObserver<SaveGreetingResponse>?) {
        val message = request?.message ?: "Hello, World!"
        val greeting = repository.save(HelloWorld(message))
        val response = SaveGreetingResponse
                .newBuilder()
                .setIsSaved(greeting.id > 0)
                .build()

        responseObserver?.onNext(response)
        responseObserver?.onCompleted()
    }
}