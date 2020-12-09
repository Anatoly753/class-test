package io.realworld.app.web.controllers

import io.javalin.http.Context
import io.realworld.app.domain.UserDTO
import io.realworld.app.domain.service.UserService
import io.realworld.app.ext.*
import kotlin.reflect.typeOf

class UserController(private val userService: UserService) {

    fun debug(ctx: Context) {
        ctx.result(ctx.attribute("email") ?: "NULL!")
    }

    fun login(ctx: Context) {
        ctx.bodyValidator<UserDTO>()
            .check({ it.user?.email != null && it.user.email.isEmailValid() })
            .check({ it.user?.password != null && it.user.password!!.isPasswordValid() })
            .get().user?.also { user ->
                userService.authenticate(user).apply {
                    ctx.json(UserDTO(this.copy(password=null)))
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
            //.check({ it.user?.role != null /*&& it.user.role.roleValid()*/ }) //FIXME: check it
            .get().user?.also { user ->
                userService.create(user).apply {
                    ctx.json(UserDTO(this.copy(password=null)))
                }
            }
    }

    fun getCurrent(ctx: Context) {
        userService.getByEmail(ctx.attribute<String>("email")).also { user ->
            ctx.json(UserDTO(user.copy(password=null)))
        }
    }

    fun update(ctx: Context) {
        val email = ctx.attribute<String>("email")
        ctx.bodyValidator<UserDTO>()
            .check({ it.user != null })
            .check({ it.user?.email?.isEmailValid() ?: true })
            .check({ it.user?.name?.isNotBlank() ?: true })
            .check({ it.user?.surname?.isNotBlank() ?: true })
            .check({ it.user?.patronymic.isNullOrBlank() ?: true })
            .check({ it.user?.password?.isNotBlank() ?: true })
            .check({ it.user?.classNumber?.isClassNumberValid() ?: true })
//                .check({ it.user?.role?.isNotBlank() ?: true })
            .get()
            .user?.also { user ->
                userService.update(email, user)?.apply {
                    ctx.json(UserDTO(this.copy(password=null)))
                }
            }

    }
}