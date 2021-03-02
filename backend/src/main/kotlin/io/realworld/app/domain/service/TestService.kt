package io.realworld.app.domain.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.javalin.http.BadRequestResponse
import io.javalin.http.InternalServerErrorResponse
import io.javalin.http.NotFoundResponse
import io.realworld.app.domain.Test
import io.realworld.app.domain.repository.TestRepository
import io.realworld.app.domain.repository.UserRepository
import io.realworld.app.ext.isClassNumberValid
import org.jetbrains.exposed.sql.SortOrder
import com.meilisearch.sdk.*
import java.text.SimpleDateFormat
import com.meilisearch.sdk.SearchRequest
import io.realworld.app.domain.TestMeili


class TestService(
    private val testRepository: TestRepository,
    private val userRepository: UserRepository,
    private val meiliClient: Client
) {

//    fun findBy(title: String?, subject: String?, classNumber: Int?, author: String?, limit: Int, offset: Int):
//            List<Test> {
//        return when {
//            !subject.isNullOrBlank() -> testRepository.findBySubject(subject, limit, offset)
//            !classNumber.isClassNumberValid() -> testRepository.findByClassNumber(classNumber, limit, offset)
//            !author.isNullOrBlank() -> testRepository.findByAuthor(author, limit, offset)
////            !favorited.isNullOrBlank() -> articleRepository.findByFavorited(favorited, limit, offset)
//            else -> testRepository.findAll(limit, offset)
//        }
//    }

    private val testsIndex: Index = meiliClient.getOrCreateIndex("tests")
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    private val mapper: ObjectMapper = jacksonObjectMapper()
        .registerModule(JodaModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true)
        .setDateFormat(dateFormat)

    fun create(email: String?, test: Test): Test {
        email ?: throw BadRequestResponse("invalid user to create article")
        val createdTest = userRepository.findByEmail(email).let { author ->
            author ?: throw BadRequestResponse("invalid user to create article")
            testRepository.create(test.copy(author = author))
                ?: throw InternalServerErrorResponse("При создании теста возникла ошибка.")
        }

        val meiliTest = mapper.writeValueAsString(
            arrayOf(
                TestMeili(
                    createdTest.id,
                    createdTest.title,
                    createdTest.description,
                    createdTest.subject,
                    createdTest.classNumber,
                    null,
                    null,
                    null,
                    createdTest.createdAt,
                    createdTest.author?.id
                )
            )
        )
//        println("================\n${mapper.writeValueAsString(testArray)}\n=================")

        testsIndex.addDocuments(meiliTest)

        return createdTest
    }

    fun findById(id: Long): Test {
        return testRepository.findById(id, null) ?: throw NotFoundResponse()
    }

    fun findByIdWithAnswers(email: String?, id: Long): Test {
        email ?: throw BadRequestResponse("invalid user to create article")
        userRepository.findByEmail(email).let { author ->
            author ?: throw BadRequestResponse("invalid user to create article")
            if(author.userRole != "editor") throw BadRequestResponse("invalid user to create article")
            if(author.id != findById(id).author?.id) throw BadRequestResponse("invalid user to create article")
        }
        return testRepository.findById(id, null) ?: throw NotFoundResponse()
    }

    fun search(test: Test?, limit: Int = 20, offset: Int = 0): String {
        val title = test?.title ?: ""
        println("===================== $title")

        val filters = mutableListOf<String>()
        if (test?.subject != null) filters += "subject = ${test.subject}"
        if (test?.classNumber != null) filters += "classNumber = ${test.classNumber}"
        if (test?.author?.id != null) filters += "author = ${test.author.id}"

        val res = testsIndex.search(
            SearchRequest(title, offset, limit)
                .setFilters(if (filters.size > 0) filters.joinToString(" AND ") else null)
                .setMatches(true)
                .setAttributesToHighlight(arrayOf("title"))
        )
        println("================\n\n${res}}\n\n=================")

        return res

        //return testRepository.search(test, sort, limit, offset)
    }

    fun update(email: String?, id: Long, test: Test): Test {
        email ?: throw BadRequestResponse("invalid user to create article")
        userRepository.findByEmail(email).let { author ->
            author ?: throw BadRequestResponse("invalid user to create article")
            if(author.userRole != "editor") throw BadRequestResponse("invalid user to create article")
            if(author.id != findById(id).author?.id) throw BadRequestResponse("invalid user to create article")
            testRepository.update(id, test)
        }

        val createdTest = findById(id)
        val meiliTest = mapper.writeValueAsString(
            arrayOf(
                TestMeili(
                    createdTest.id,
                    createdTest.title,
                    createdTest.description,
                    createdTest.subject,
                    createdTest.classNumber,
                    null,
                    null,
                    null,
                    createdAt = createdTest.createdAt,
                    createdTest.author?.id
                )
            )
        )
//        println("================\n${mapper.writeValueAsString(testArray)}\n=================")

        testsIndex.addDocuments(meiliTest)
        return createdTest
    }

//    fun findFeed(email: String?, limit: Int, offset: Int): List<Article> {
//        email ?: throw BadRequestResponse("invalid user to find feeds")
//        return articleRepository.findFeed(email, limit, offset)
//    }
//
//    fun favorite(email: String?, slug: String): Article {
//        email ?: throw BadRequestResponse("invalid user to favorite article")
//        val article = findBySlug(slug) ?: throw NotFoundResponse()
//        return userRepository.findByEmail(email).let { user ->
//            user ?: throw BadRequestResponse()
//            articleRepository.favorite(user.id!!, slug)
//                    .let { favoritesCount ->
//                        article.copy(favorited = true, favoritesCount = favoritesCount.toLong())
//                    }
//        }
//    }
//
//    fun unfavorite(email: String?, slug: String): Article {
//        email ?: throw BadRequestResponse("invalid user to unfavorite article")
//        val article = findBySlug(slug) ?: throw NotFoundResponse()
//        return userRepository.findByEmail(email).let { user ->
//            user ?: throw BadRequestResponse()
//            articleRepository.unfavorite(user.id!!, slug)
//                    .let { favoritesCount ->
//                        article.copy(favorited = false, favoritesCount = favoritesCount.toLong())
//                    }
//        }
//    }

    fun delete(email: String?, id: Long) {
        email ?: throw BadRequestResponse("invalid user to create article")
        userRepository.findByEmail(email).let { author ->
            author ?: throw BadRequestResponse("invalid user to create article")
            if(author.userRole != "editor") throw BadRequestResponse("invalid user to create article")
            if(author.id != findById(id).author?.id) throw BadRequestResponse("invalid user to create article")
        }

        testsIndex.deleteDocument(id.toString())
        return testRepository.delete(id)
    }
}