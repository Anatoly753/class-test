package io.realworld.app.web.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.javalin.http.Context
import io.realworld.app.domain.*
import io.realworld.app.domain.service.*
import io.realworld.app.ext.*
import org.jetbrains.exposed.sql.SortOrder
import org.joda.time.DateTime
import java.text.SimpleDateFormat

class ResultController(
    private val testService: TestService,
    private val userService: UserService,
    private val resultService: ResultService
) {

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

    val mapper = ObjectMapper().registerKotlinModule()

//    fun create(ctx: Context) {
//        ctx.bodyValidator<TestDTO>()
//            .check({ !it.test?.title.isNullOrBlank() })
////            .check({ !it.test?.description.isNullOrBlank() })
//            .check({ !it.test?.subject.isNullOrBlank() })
//            .check({ it.test?.classNumber != null && it.test.classNumber.isClassNumberValid() })
////            .check({ !it.test?.questions.isNullOrBlank() }) // not null AND parse as Array with objects
////            .check({ !it.test?.settings.isNullOrBlank() }) //null = default settings?
//            .get().test?.also { test ->
//                testService.create(ctx.attribute("email"), test).apply {
//                    ctx.json(TestDTO(this))
//                }
//            }
//    }

    fun getById(ctx: Context) {
        ctx.pathParam<Long>("id")
            .check({ it.isIdValid() })
            .get().also { id ->
                println(id)
                resultService.findById(id).apply {
//                    if (this.answers != null) {
//                        val answers: List<List<AnswerDTO?>?> = mapper.readValue(this.answers)
//                        answers.forEach {
//                            it?.forEach { answerDTO ->
//                                if (answerDTO != null) {
//                                    answerDTO.score = 0
//                                    answerDTO.right = false
//                                    println("${answerDTO.score}; ${answerDTO.right}")
//                                }
//                            }
//                        }
//
//                    }

                    ctx.json(this)

                }
            }
    }

//    fun getByIdWithAnswers(ctx: Context) {
//        ctx.pathParam<Long>("id")
//            .check({ it.isIdValid() })
//            .get().also { id ->
//                testService.findByIdWithAnswers(ctx.attribute("email"), id).apply {
//                    ctx.json(TestDTO(this))
//                }
//            }
//    }

    fun search(ctx: Context) {
        ctx.bodyValidator<ResultDTO>()
//            .check({ !it.test?.title.isNullOrBlank() })
//            .check({ !it.test?.subject.isNullOrBlank() })
//            .check({ it.test?.classNumber == null || it.test.classNumber.isClassNumberValid() })
//            .check({ it.test?.author != null})
//            .check({ !it.test?.questions.isNullOrBlank() })
//            .check({ !it.test?.settings.isNullOrBlank() })
            .get().result?.also { result ->
                val sort = SortOrder.valueOf(ctx.queryParam("sort", "DESC")!!) //SortOrder.DESC
                val limit = ctx.queryParam("limit", "10")!!.toInt()
                val offset = ctx.queryParam("offset", "0")!!.toInt()
                resultService.search(result, limit, offset).apply {
                    ctx.result(this)
                    //ctx.json(this.let { TestsDTO(it, it.size) })
                }
            }
    }

//    fun update(ctx: Context) {
//        val id = ctx.pathParam<String>("id").get().toLong()
//        ctx.bodyValidator<TestDTO>()
////            .check({ !it.test?.questions.isNullOrBlank() }) //FIXME: can be null OR validJSON
//            .get().test?.also { test ->
//                testService.update(ctx.attribute("email"), id, test).apply {
//                    ctx.json(TestDTO(this))
//                }
//            }
//    }

//    fun delete(ctx: Context) {
//        ctx.pathParam<String>("id").get().toLong().also { id ->
//            testService.delete(ctx.attribute("email"), id)
//        }
//    }

//    fun testCheck(ctx: Context) {
//
//        val id = ctx.pathParam<String>("id").get().toLong()
//
//        var score = 0
//
////        ctx.bodyValidator<List<List<AnswerDTO>>>()
////            .get().also { userAnswers ->
//        val userAnswers: List<List<Answer?>?> = mapper.readValue(ctx.body())
//        println(userAnswers[0]?.get(0)?.answer)
//
//        val user = userService.getByEmail(ctx.attribute<String>("email"))
//
//        val test = testService.findById(id).apply {
//            if (this.answers != null) {
//                val testAnswers: List<List<AnswerDTO?>?> = mapper.readValue(this.answers)
//
////         result depends on settings(show right answers)
//
//                userAnswers.forEachIndexed { index, user_answer ->
//                    user_answer?.forEach { answerDTO ->
//                        println(answerDTO)
//
//                        if (answerDTO != null) {
//                            testAnswers[index]?.forEach { test_answer ->
////                                println("user: ${answerDTO.answer}; test: ${test_answer?.answer}; score: ${test_answer?.score};;; ${answerDTO.answer == test_answer?.answer}")
//                                if (answerDTO.answer.toString() == test_answer?.answer.toString()) score += test_answer?.score!!
//                            }
//                        }
//
//                    }
//
//                }
//
////                ctx.json(TestDTO(this.copy(answers = mapper.writeValueAsString(answers))))
//
//            }
//        }
//
////        private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
////        private val mapper: ObjectMapper = jacksonObjectMapper()
////            .registerModule(JodaModule())
////            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
////            .configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true)
////            .setDateFormat(dateFormat)
//
//        val result = Result(null, user, test, score, mapper.writeValueAsString(userAnswers), DateTime())
//
//        ctx.json(resultService.create(result))
////        ctx.json(result)
//
//    }

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
