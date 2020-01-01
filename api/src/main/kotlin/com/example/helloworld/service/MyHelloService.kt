package com.example.helloworld.service

import com.example.helloworld.grpc.HelloReply
import com.example.helloworld.grpc.HelloRequest
import com.example.helloworld.grpc.HelloServiceGrpc
import io.grpc.stub.StreamObserver

class HelloServiceImpl : HelloServiceGrpc.HelloServiceImplBase() {
    override fun hello(request: HelloRequest?, responseObserver: StreamObserver<HelloReply>?) {
        val name = request?.name ?: ""

        val result = "Hello, $name"
        val reply = HelloReply.newBuilder().setMessage(result).build()

        responseObserver?.onNext(reply)
        responseObserver?.onCompleted()
    }
}