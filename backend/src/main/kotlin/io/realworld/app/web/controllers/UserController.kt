package io.realworld.app.web.controllers

import io.javalin.http.Context
import io.realworld.app.domain.UserDTO
import io.realworld.app.domain.service.UserService
import io.realworld.app.ext.*
import io.realworld.app.utils.Cipher
import java.util.*
import kotlin.reflect.typeOf

class UserController(private val userService: UserService) {
    private val base64Encoder = Base64.getEncoder()

    fun debug(ctx: Context) {
        ctx.result(ctx.attribute("email") ?: "NULL!")
    }

    fun login(ctx: Context) {
        ctx.bodyValidator<UserDTO>()
            .check({ it.user?.email != null && it.user.email.isEmailValid() })
            .check({ it.user?.password != null && it.user.password!!.isPasswordValid() })
            .get().user?.also { user ->
                userService.authenticate(user).apply {
                    ctx.json(UserDTO(this.copy(password = null)))
                }
            }
    }

    fun register(ctx: Context) {
        ctx.bodyValidator<UserDTO>()
            .check({ it.user?.email != null && it.user.email.isEmailValid() })
            .check({ it.user?.password != null && it.user.password!!.isPasswordValid() })
            .check({ it.user?.name != null && it.user.name.isNameValid() })
            .check({ it.user?.surname != null && it.user.surname.isSurnameValid() })
            .check({ it.user?.patronymic == null || it.user.patronymic.isPatronymicValid() })
            .check({ it.user?.classNumber != null && it.user.classNumber.isClassNumberValid() })
            .get().user?.also { user ->
                userService.create(user).apply {
                    ctx.json(UserDTO(this.copy(password = null)))
                }
            }
    }

    fun getCurrent(ctx: Context) {
        userService.getByEmail(ctx.attribute<String>("email")).also { user ->
            ctx.json(UserDTO(user.copy(password = null)))
        }
    }

    fun update(ctx: Context) {
        val email = ctx.attribute<String>("email")
        ctx.bodyValidator<UserDTO>()
            .check({ it.user != null })
            .check({ it.user?.email != null && it.user.email.isEmailValid() })
            .check({ it.user?.name != null && it.user.name.isNameValid() })
            .check({ it.user?.surname != null && it.user.surname.isSurnameValid() })
            .check({ it.user?.patronymic == null || it.user.patronymic.isPatronymicValid() })
            .check({ it.user?.classNumber != null && it.user.classNumber.isClassNumberValid() })
            .get()
            .user?.also { user ->
                userService.update(email, user)?.apply {
                    ctx.json(UserDTO(this.copy(password = null)))
                }
            }
    }

    fun changePassword(ctx: Context) {
        val email = ctx.attribute<String>("email")
        ctx.bodyValidator<UserDTO>()
            .check({ it.user != null })
            .check({ it.user?.email != null && it.user.email.isEmailValid() })
            .check({ it.user?.password == null || it.user.password!!.isPasswordValid() })
            .check({ it.user?.oldPassword != null && it.user.oldPassword!!.isPasswordValid() })
            .get()
            .user?.also { user ->
                userService.getByEmail(email).also { userWithOldPassword ->
                    if (String(base64Encoder.encode(Cipher.encrypt(user.oldPassword))) == userWithOldPassword.password)
                        userService.update(email, user.copy(password = String(base64Encoder.encode(Cipher.encrypt(user.password)))))?.apply {
                            ctx.json(UserDTO(this.copy(password = null)))
                        }
                }
            }
    }
}