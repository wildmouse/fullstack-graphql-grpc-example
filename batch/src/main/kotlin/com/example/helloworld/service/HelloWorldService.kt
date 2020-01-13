package com.example.helloworld.service

import com.example.helloworld.entity.Greeting
import com.example.helloworld.repository.GreetingRepository
import org.springframework.stereotype.Service

@Service
class GreetingService(
        private val repository: GreetingRepository
) {

    fun getGreeting(id: Int) = repository.findById(id)

    fun getGreetingList() = repository.findAll()

    fun saveGreeting(message: String) = Greeting(message).let { repository.save(it) }
}