package io.realworld.app.domain

data class UserDTO(val user: User? = null)

data class User(val id: Long? = null,
                val email: String? = null,
                val token: String? = null,
                val name: String? = null,
                val surname: String? = null,
                val patronymic: String? = null,
                var password: String? = null,
                var oldPassword: String? = null,
                val classNumber: Int? = null,
                val classId: Long?  = null,
                val userRole: String? = null)