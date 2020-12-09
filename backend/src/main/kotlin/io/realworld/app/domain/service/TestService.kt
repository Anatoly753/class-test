package io.realworld.app.domain.service

import io.javalin.http.BadRequestResponse
import io.javalin.http.InternalServerErrorResponse
import io.javalin.http.NotFoundResponse
import io.realworld.app.domain.Test
import io.realworld.app.domain.repository.TestRepository
import io.realworld.app.domain.repository.UserRepository
import io.realworld.app.ext.isClassNumberValid
import org.jetbrains.exposed.sql.SortOrder

class TestService(private val testRepository: TestRepository, private val userRepository: UserRepository) {

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

    fun create(email: String?, test: Test): Test {
        email ?: throw BadRequestResponse("invalid user to create article")
        return userRepository.findByEmail(email).let { author ->
            author ?: throw BadRequestResponse("invalid user to create article")
            testRepository.create(test.copy(author = author))
                ?: throw InternalServerErrorResponse("При создании теста возникла ошибка.")
        }
    }

    fun findById(id: Long): Test {
        return testRepository.findById(id, null) ?: throw NotFoundResponse()
    }

    fun search(test: Test?, sort: SortOrder = SortOrder.DESC, limit: Int = -1, offset: Int = -1): List<Test> {
        return testRepository.search(test, sort, limit, offset)
    }

    fun update(id: Long, test: Test): Test {
        testRepository.update(id, test)
        return findById(id)
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

    fun delete(id: Long?) {
        return testRepository.delete(id)
    }
}