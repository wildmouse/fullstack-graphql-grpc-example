package com.example.helloworld.service

import com.example.helloworld.grpc.greeting.*
import io.grpc.stub.StreamObserver

class GreetingService : GreetingServiceGrpc.GreetingServiceImplBase() {
    override fun getGreetings(request: GetGreetingsRequest?, responseObserver: StreamObserver<GetGreetingsResponse>?) {
        val response = GetGreetingsResponse
                .newBuilder()
                .addAllMessages(listOf(
                        "Hello, World!",
                        "Hello, gRPC!",
                        "Hello, GraphQL!"
                ))
                .build()

        responseObserver?.onNext(response)
        responseObserver?.onCompleted()
    }

    override fun saveGreeting(request: SaveGreetingRequest?, responseObserver: StreamObserver<SaveGreetingResponse>?) {
        val response = SaveGreetingResponse
                .newBuilder()
                .setIsSaved(true)
                .build()

        responseObserver?.onNext(response)
        responseObserver?.onCompleted()
    }
}