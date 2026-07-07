package com.minhnbnt.domain

import io.quarkus.hibernate.reactive.panache.PanacheEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "app_user")
class User : PanacheEntity() {
    lateinit var username: String
    lateinit var password: String
}
