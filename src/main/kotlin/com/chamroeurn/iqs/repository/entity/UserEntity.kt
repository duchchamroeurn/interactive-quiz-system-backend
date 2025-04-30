package com.chamroeurn.iqs.repository.entity

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "tbl_users")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    val userId: UUID? = null,

    @Column(length = 50, unique = true, nullable = false)
    val username: String,

    @Column(length = 100, unique = true, nullable = false)
    val email: String,

    @Column(nullable = false)
    var password: String,

    @Enumerated(EnumType.STRING)
    val role: UserRoles,

    @OneToMany(mappedBy = "presenter", cascade = [CascadeType.ALL], orphanRemoval = true)
    val quizzes: MutableList<QuizEntity> = mutableListOf()

)


enum class UserRoles { PRESENTER, AUDIENCE, ADMIN }