package io.realworld.app.domain

import org.joda.time.DateTime

data class ResultDTO(val result: Result?)

data class ResultsDTO(val results: List<Result>, val resultsCount: Int)

data class Result(
    val id: Long? = null,

    val user: User? = null, //OR Long
    val test: Test? = null,
    val score: Int?,
    val maxScore: Int?,
    val answers: String?, //JSON
    val createdAt: DateTime? = null,
)

data class ResultMeili(
    val id: Long? = null,
    val user: Long? = null, //OR Long
    val test: Long? = null,
    val score: Int?,
    val maxScore: Int?,

    val answers: String?, //JSON
    val createdAt: DateTime? = null,
)

//data class AnswerDTO(
//    val answer: Any?,
//    var score: Int?,
//    var right: Boolean?
//)
//
////================ QUESTIONS ===================
//
//data class Questions(
//    val questions: List<Question>?,
//    val answers: List<Answer>?
//)
//
//data class Question(
//    val answerType: String? = "text",
//    val body: String? = ""
//)
//
//data class Answer(
//    val value: Any?
//)
//
////================ SETTINGS ===================
//
//data class Settings(
//    val time_for_test: Long? = 0,
//    val move_answers: Boolean? = true,
//    val attempts: Int? = 0, //default = infinity
//    val show_questions_as: String? = "scroll", //scroll, one_by_one
//    val show_answers: Boolean? = true,
//    val generate_random_variants: Boolean? = false,
//    val gradation: GradationProperty? = GradationProperty(),
//    val access_time: AccessTimeProperty?,
//    val allow_return: Boolean? = false,
//    val allow_miss: Boolean? = false,
//    val Customization: CustomizationProperty? = CustomizationProperty()
//)
//
//data class GradationProperty(
//    val show: Boolean? = true,
//    val type: String? = "percent",
//    val gradation: List<Int>? = listOf(42, 66, 87)
//)
//
//data class AccessTimeProperty(
//    val start: DateTime?,
//    val end: DateTime?
//)
//
//data class CustomizationProperty(val background_color: String? = "#ffffff00")