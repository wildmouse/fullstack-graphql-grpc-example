package com.example.helloworld.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class HelloWorld(
        var message: String = "Hello, World!",
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Int = 0
)