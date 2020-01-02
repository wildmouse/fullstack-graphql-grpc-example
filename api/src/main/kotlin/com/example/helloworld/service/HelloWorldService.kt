package com.example.helloworld.service

import com.example.helloworld.entity.HelloWorld
import com.example.helloworld.repository.HelloWorldRepository
import org.springframework.stereotype.Service

@Service
class HelloWorldService(
        private val repository: HelloWorldRepository
) {

    fun getHelloWorld(id: Long) = repository.findById(id)

    fun getHelloWorldList() = repository.findAll()

    fun insertHelloWorld(message: String) = HelloWorld(message).let { repository.save(it) }
}