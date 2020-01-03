package com.example.helloworld.repository

import com.example.helloworld.entity.Greeting
import org.springframework.data.repository.CrudRepository

interface GreetingRepository : CrudRepository<Greeting, Int>