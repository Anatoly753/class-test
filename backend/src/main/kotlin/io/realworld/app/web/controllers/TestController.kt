package io.realworld.app.web.controllers

import io.javalin.http.Context
import io.realworld.app.domain.TestDTO
import io.realworld.app.domain.TestsDTO
import io.realworld.app.domain.service.TestService
import io.realworld.app.ext.isClassNumberValid
import io.realworld.app.ext.isIdValid
import org.jetbrains.exposed.sql.SortOrder

class TestController(private val testService: TestService) {

//    fun findBy(ctx: Context) {
//        val tag = ctx.queryParam("tag")
//        val author = ctx.queryParam("author")
//        val favorited = ctx.queryParam("favorited")
//        val limit = ctx.queryParam("limit") ?: "20"
//        val offset = ctx.queryParam("offset") ?: "0"
//        articleService.findBy(tag, author, favorited, limit.toInt(), offset.toInt()).also { articles ->
//            ctx.json(ArticlesDTO(articles, articles.size))
//        }
//    }

//    fun feed(ctx: Context) {
//        val limit = ctx.queryParam("limit") ?: "20"
//        val offset = ctx.queryParam("offset") ?: "0"
//        articleService.findFeed(ctx.attribute("email"), limit.toInt(), offset.toInt()).also { articles ->
//            ctx.json(ArticlesDTO(articles, articles.size))
//        }
//    }

//    fun get(ctx: Context) {
//        ctx.pathParam<String>("subject")
//                .check({ it.isNotBlank() })
//                .get().also { subject ->
//                    testService.search(subject).apply {
//                        ctx.json(TestDTO(this))
//                    }
//                }
//    }

    fun create(ctx: Context) {
        ctx.bodyValidator<TestDTO>()
            .check({ !it.test?.title.isNullOrBlank() })
//            .check({ !it.test?.description.isNullOrBlank() })
            .check({ !it.test?.subject.isNullOrBlank() })
            .check({ it.test?.classNumber != null && it.test.classNumber.isClassNumberValid() })
//            .check({ !it.test?.questions.isNullOrBlank() }) // not null AND parse as Array with objects
//            .check({ !it.test?.settings.isNullOrBlank() }) //null = default settings?
            .get().test?.also { test ->
                testService.create(ctx.attribute("email"), test).apply {
                    ctx.json(TestDTO(this))
                }
            }
    }

    fun getById(ctx: Context) {
        ctx.pathParam<Long>("id")
            .check({ it.isIdValid() })
            .get().also { id ->
                testService.findById(id).apply {
                    ctx.json(TestDTO(this))
                }
            }
    }

    fun search(ctx: Context) {
        ctx.bodyValidator<TestDTO>()
//            .check({ !it.test?.title.isNullOrBlank() })
//            .check({ !it.test?.subject.isNullOrBlank() })
//            .check({ it.test?.classNumber == null || it.test.classNumber.isClassNumberValid() })
//            .check({ it.test?.author != null})
//            .check({ !it.test?.questions.isNullOrBlank() })
//            .check({ !it.test?.settings.isNullOrBlank() })
            .get().test?.also { test ->
                val sort = SortOrder.valueOf(ctx.queryParam("sort", "DESC")!!) //SortOrder.DESC
                val limit = ctx.queryParam("limit", "-1")!!.toInt()
                val offset = ctx.queryParam("offset", "-1")!!.toInt()
                testService.search(test, sort, limit, offset).apply {
                    ctx.json(this.let { TestsDTO(it, it.size) })
                }
            }
    }

    fun update(ctx: Context) {
        val id = ctx.pathParam<String>("id").get().toLong()
        ctx.bodyValidator<TestDTO>()
//            .check({ !it.test?.questions.isNullOrBlank() }) //FIXME: can be null OR validJSON
            .get().test?.also { test ->
                testService.update(id, test).apply {
                    ctx.json(TestDTO(this))
                }
            }
    }

    fun delete(ctx: Context) {
        ctx.pathParam<String>("id").get().toLong().also { id ->
            testService.delete(id)
        }
    }

//    fun favorite(ctx: Context) {
//        ctx.pathParam<String>("slug").get().also { slug ->
//            articleService.favorite(ctx.attribute("email"), slug).apply {
//                ctx.json(ArticleDTO(this))
//            }
//        }
//    }
//
//    fun unfavorite(ctx: Context) {
//        ctx.pathParam<String>("slug").get().also { slug ->
//            articleService.unfavorite(ctx.attribute("email"), slug).apply {
//                ctx.json(ArticleDTO(this))
//            }
//        }
//    }
}
