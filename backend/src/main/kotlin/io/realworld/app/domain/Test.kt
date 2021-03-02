package io.realworld.app.domain

import org.joda.time.DateTime

data class TestDTO(val test: Test?)

data class TestsDTO(val tests: List<Test>, val testsCount: Int)

data class Test(
    val id: Long? = null,
    val title: String?,
    val description: String?,
    val subject: String?,
    val classNumber: Int?,

    val questions: String?, //JSON
    val answers: String?, //JSON
    val settings: String?, //JSON

    //val tagList: List<String> = listOf(),
    val createdAt: DateTime? = null,
    val author: User? = null
)

data class TestMeili(
    val id: Long? = null,
    val title: String?,
    val description: String?,
    val subject: String?,
    val classNumber: Int?,

    val questions: String?, //JSON
    val answers: String?, //JSON
    val settings: String?, //JSON

    //val tagList: List<String> = listOf(),
    val createdAt: DateTime? = null,
    val author: Long? = null
)

data class AnswerDTO(
    val answer: Any?,
    var score: Int?,
    var right: Boolean?
)

//================ QUESTIONS ===================

data class Questions(
    val questions: List<Question>?,
    val answers: List<Answer>?
)

data class Question(
    val answerType: String? = "text",
    val body: String? = ""
)

data class Answer(
    val answer: Any?
)

//================ SETTINGS ===================

data class Settings(
    val time_for_test: Long? = 0,
    val move_answers: Boolean? = true,
    val attempts: Int? = 0, //default = infinity
    val show_questions_as: String? = "scroll", //scroll, one_by_one
    val show_answers: Boolean? = true,
    val generate_random_variants: Boolean? = false,
    val gradation: GradationProperty? = GradationProperty(),
    val access_time: AccessTimeProperty?,
    val allow_return: Boolean? = false,
    val allow_miss: Boolean? = false,
    val Customization: CustomizationProperty? = CustomizationProperty()
)

data class GradationProperty(
    val show: Boolean? = true,
    val type: String? = "percent",
    val gradation: List<Int>? = listOf(42, 66, 87)
)

data class AccessTimeProperty(
    val start: DateTime?,
    val end: DateTime?
)

data class CustomizationProperty(val background_color: String? = "#ffffff00")