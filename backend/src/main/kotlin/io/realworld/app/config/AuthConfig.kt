package io.realworld.app.config

import com.auth0.jwt.interfaces.DecodedJWT
import io.javalin.Javalin
import io.javalin.core.security.Role
import io.javalin.http.Context
import io.javalin.http.ForbiddenResponse
import io.realworld.app.utils.JwtProvider

internal enum class Roles : Role {
    ANYONE, AUTHENTICATED
}

private const val headerTokenName = "Authorization"

class AuthConfig(private val jwtProvider: JwtProvider) {
    fun configure(app: Javalin) {
        app.config.accessManager { handler, ctx, permittedRoles ->
            val jwtToken = getJwtTokenHeader(ctx)
            val userRole = getUserRole(jwtToken) ?: Roles.ANYONE
            permittedRoles.takeIf { !it.contains(userRole) }?.apply { throw ForbiddenResponse() }
            ctx.attribute("email", getEmail(jwtToken))
            handler.handle(ctx)
        }
    }

    private fun getJwtTokenHeader(ctx: Context): DecodedJWT? {
        println("header: " + ctx.header(headerTokenName))
        val tokenHeader = ctx.header(headerTokenName)?.substringAfter("Token")?.trim()
                ?: return null
        return jwtProvider.decodeJWT(tokenHeader)
    }

    private fun getEmail(jwtToken: DecodedJWT?): String? {
//        println("email: " + jwtToken?.subject)
        return jwtToken?.subject
    }

    private fun getUserRole(jwtToken: DecodedJWT?): Role? {
//        println("role: " + jwtToken?.getClaim("role")?.asString())
        val userRole = jwtToken?.getClaim("role")?.asString() ?: return null
        return Roles.valueOf(userRole)
    }
}