package io.realworld.app.domain

import org.joda.time.DateTime
import java.util.*

data class TestDTO(val test: Test?)

data class TestsDTO(val tests: List<Test>, val testsCount: Int)

data class Test(
                   val id: Long? = null,
                   val title: String?,
                   val description: String?,
                   val subject: String?,
                   val classNumber: Int?,

                   val questions: String?, //JSON
                   val settings: String?, //JSON

                   //val tagList: List<String> = listOf(),
                   val createdAt: DateTime? = null,
                   val author: User? = null)