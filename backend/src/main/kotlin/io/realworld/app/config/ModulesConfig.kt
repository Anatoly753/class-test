package io.realworld.app.config

import io.realworld.app.domain.Result
import io.realworld.app.domain.repository.*
import io.realworld.app.domain.service.*
import io.realworld.app.utils.JwtProvider
import io.realworld.app.web.Router
import io.realworld.app.web.controllers.*
import org.koin.dsl.module.module

object ModulesConfig {
    private val configModule = module {
        single { AppConfig() }
        single { JwtProvider() }
        single { AuthConfig(get()) }
        single {
            DbConfig(getProperty("jdbc.url"), getProperty("db.username"), getProperty("db.password")).getDataSource()
        }
        single { MeiliSearchConfig(getProperty("meiliSearch.url"), getProperty("meiliSearch.key")).getClient() }
        single { Router(get(), get(), get(), get(), get(), get(), get()) }
    }
    private val userModule = module {
        single { UserController(get()) }
        single { UserService(get(), get()) }
        single { UserRepository(get()) }
    }
    private val testModule = module {
        single { TestController(get(), get(), get()) }
        single { TestService(get(), get(), get()) }
        single { TestRepository(get()) }
    }
    private val resultModule = module {
        single { ResultController(get(), get(), get()) }
        single { ResultService(get(), get(), get(), get()) }
        single { ResultRepository(get()) }
    }
    private val articleModule = module {
        single { ArticleController(get()) }
        single { ArticleService(get(), get()) }
        single { ArticleRepository(get()) }
    }
    private val profileModule = module {
        single { ProfileController(get()) }
    }
    private val commentModule = module {
        single { CommentController(get()) }
        single { CommentService(get()) }
        single { CommentRepository(get()) }
    }
    private val tagModule = module {
        single { TagController(get()) }
        single { TagService(get()) }
        single { TagRepository(get()) }
    }
    internal val allModules = listOf(ModulesConfig.configModule, ModulesConfig.userModule, ModulesConfig.testModule, ModulesConfig.resultModule,
            ModulesConfig.articleModule, ModulesConfig.profileModule, ModulesConfig.commentModule,
            ModulesConfig.tagModule)
}
