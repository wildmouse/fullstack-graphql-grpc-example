package com.example.helloworld.repository

import com.example.helloworld.entity.HelloWorld
import org.springframework.data.repository.CrudRepository

interface HelloWorldRepository : CrudRepository<HelloWorld, Long>