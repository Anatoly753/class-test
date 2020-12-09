package io.realworld.app.ext

const val MAIL_REGEX = ("^[a-zA-Z0-9.!#\$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*\$")
fun String.isEmailValid(): Boolean = Regex(MAIL_REGEX).matches(this)

const val NAME_REGEX = ("^(([a-zA-Z' -]{1,30})|([а-яА-ЯЁёІіЇїҐґЄє' -]{1,30}))$")
fun String.isNameValid(): Boolean = Regex(NAME_REGEX).matches(this)

const val SURNAME_REGEX = ("^(([a-zA-Z' -]{1,30})|([а-яА-ЯЁёІіЇїҐґЄє' -]{1,30}))$")
fun String.isSurnameValid(): Boolean = Regex(SURNAME_REGEX).matches(this)

const val PATRONYMIC_REGEX = ("^(([a-zA-Z' -]{1,30})|([а-яА-ЯЁёІіЇїҐґЄє' -]{1,30}))$")
fun String.isPatronymicValid(): Boolean = Regex(PATRONYMIC_REGEX).matches(this)

const val PASSWORD_REGEX = ("^(?=.*\\d)(?=.*[a-zA-Z]).{8,255}$")
fun String.isPasswordValid(): Boolean = Regex(PASSWORD_REGEX).matches(this)

//TODO: throw ex when not valid

const val TEST_TITLE_REGEX = ("^(([a-zA-Z' -]{1,30})|([а-яА-ЯЁёІіЇїҐґЄє' -]{1,30}))$")
fun String.isTestTitleValid(): Boolean = Regex(TEST_TITLE_REGEX).matches(this)