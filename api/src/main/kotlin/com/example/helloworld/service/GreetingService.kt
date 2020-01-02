package com.example.helloworld.service

import com.example.helloworld.grpc.greeting.*
import com.example.helloworld.entity.Greeting
import com.example.helloworld.repository.GreetingRepository
import io.grpc.stub.StreamObserver

class GreetingGrpcService(
        private val repository: GreetingRepository
) : GreetingServiceGrpc.GreetingServiceImplBase() {
    override fun getGreeting(request: GetGreetingRequest?, responseObserver: StreamObserver<GetGreetingResponse>?) {
        val id = request?.id ?: -1
        val greeting = repository.findById(id).get().let {
            com.example.helloworld.grpc.greeting.Greeting
                    .newBuilder()
                    .setMessage(it.message)
                    .setId(it.id)
                    .build()
        }
        val response = GetGreetingResponse
                .newBuilder()
                .setGreeting(greeting)
                .build()

        responseObserver?.onNext(response)
        responseObserver?.onCompleted()
    }

    override fun getGreetings(request: GetGreetingsRequest?, responseObserver: StreamObserver<GetGreetingsResponse>?) {
        val greetings = repository.findAll().map {
            com.example.helloworld.grpc.greeting.Greeting
                    .newBuilder()
                    .setMessage(it.message)
                    .setId(it.id)
                    .build()
        }

        val response = GetGreetingsResponse
                .newBuilder()
                .addAllGreetings(greetings)
                .build()

        responseObserver?.onNext(response)
        responseObserver?.onCompleted()
    }

    override fun saveGreeting(request: SaveGreetingRequest?, responseObserver: StreamObserver<SaveGreetingResponse>?) {
        val message = request?.message ?: "Hello, World!"
        val greeting = repository.save(Greeting(message)).let {
            com.example.helloworld.grpc.greeting.Greeting
                    .newBuilder()
                    .setMessage(it.message)
                    .setId(it.id)
                    .build()
        }

        val response = SaveGreetingResponse
                .newBuilder()
                .setGreeting(greeting)
                .build()

        responseObserver?.onNext(response)
        responseObserver?.onCompleted()
    }
}