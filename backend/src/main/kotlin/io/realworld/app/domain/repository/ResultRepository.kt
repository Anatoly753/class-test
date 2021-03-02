package io.realworld.app.domain.repository

import io.realworld.app.domain.*
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import java.time.LocalTime
import javax.sql.DataSource
import org.joda.time.DateTimeZone

import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

private object Results : LongIdTable() {

    val user: Column<Long> = long("user")
    val test: Column<Long> = long("test")
    val score: Column<Int> = integer("score")
    val maxScore: Column<Int> = integer("max_score")
    val answers: Column<String?> = text("answers").nullable() //JSON
    val createdAt: Column<DateTime> = datetime("created_at")

    fun toDomain(row: ResultRow, user: User?, test: Test?): Result {
//        val fmt: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
//            .withZone(DateTimeZone.forID("Europe/Moscow"))
//        val dateTime: DateTime = fmt.parseDateTime(row[createdAt].toString("yyyy-MM-dd HH:mm:ss"))
        return Result(
            id = row[Results.id].value,
            user = user,
            test = test,
            score = row[score],
            maxScore = row[maxScore],
            answers = row[answers]!!,
            createdAt = row[createdAt],
        )
    }

}

//private object Favorites : Table() {
//    val slug: Column<String> = varchar("slug", 100).primaryKey()
//    val user: Column<Long> = long("user").primaryKey()
//}
//
//private object ArticlesTags : Table() {
//    val tag: Column<Long> = long("tag").primaryKey()
//    val slug: Column<String> = varchar("slug", 100).primaryKey()
//}

class ResultRepository(private val dataSource: DataSource) {

    init {
        transaction(Database.connect(dataSource)) {
            SchemaUtils.create(Results)
        }
    }

//    private fun findWithConditional(where: Op<Boolean>, limit: Int, offset: Int): List<Test> {
//        return transaction(Database.connect(dataSource)) {
//            Tests.join(Users, JoinType.INNER, additionalConstraint = { Tests.author eq Users.id })
//                    .select { where }
//                    .limit(limit, offset)
//                    .orderBy(Tests.createdAt, true)
//                    .map { row ->
//                        val slug = row[Tests.slug]
//                        val favoritesCount = Favorites.select { Favorites.slug eq slug }.count()
//                        val tagList = Tags.join(ArticlesTags, JoinType.INNER,
//                                additionalConstraint = { Tags.id eq ArticlesTags.tag })
//                                .select { ArticlesTags.slug eq slug }
//                                .map { it[Tags.name] }
//                        Tests.toDomain(row, Users.toDomain(row))
//                                .copy(favorited = favoritesCount > 0,
//                                        favoritesCount = favoritesCount.toLong(),
//                                        tagList = tagList)
//                    }
//        }
//    }

//    fun findByTag(tag: String, limit: Int, offset: Int): List<Article> {
//        val slugs = transaction(Database.connect(dataSource)) {
//            Tags.join(ArticlesTags, JoinType.INNER, additionalConstraint = { Tags.id eq ArticlesTags.tag })
//                    .select { Tags.name eq tag }
//                    .map { it[ArticlesTags.slug] }
//        }
//        return findWithConditional((Articles.slug inList slugs), limit, offset)
//
//    }
//
//    fun findByFavorited(favorited: String, limit: Int, offset: Int): List<Article> {
//        val slugs = transaction(Database.connect(dataSource)) {
//            Favorites.join(Users, JoinType.INNER, additionalConstraint = { Favorites.user eq Users.id })
//                    .slice(Favorites.slug)
//                    .select { Users.name eq favorited }
//                    .map { it[Favorites.slug] }
//        }
//        return findWithConditional((Articles.slug inList slugs), limit, offset)
//    }

    fun create(result: Result): Result {
        val date = DateTime()
        val id = transaction(Database.connect(dataSource)) {
            Results.insertAndGetId { row ->
                row[user] = result.user?.id!!
                row[test] = result.test?.id!!
                row[score] = result.score!!
                row[maxScore] = result.maxScore!!
                row[answers] = result.answers
                row[createdAt] = result.createdAt!!
            }.value
//            test.tagList.map { tag ->
//                Tags.slice(Tags.id).select { Tags.name eq tag }.map { row -> row[Tags.id].value }.firstOrNull()
//                        ?: Tags.insertAndGetId { it[name] = tag }.value
//            }.also {
//                ArticlesTags.batchInsert(it) { tagId ->
//                    this[ArticlesTags.tag] = tagId
//                    this[ArticlesTags.slug] = article.slug!!
//                }
//            }
        }
        return findById(id, null, null)!!
    }

    fun findById(id: Long, author: User?, test: Test?): Result? {
        return transaction(Database.connect(dataSource)) {
            Results.join(Users, JoinType.INNER, additionalConstraint = { Results.user eq Users.id })
                .join(Tests, JoinType.INNER, additionalConstraint = { Results.user eq Tests.id })
                .select { Results.id eq id }
                .map { row ->
                    Results.toDomain(row, Users.toDomain(row).copy(password = null), Tests.toDomain(row, Users.toDomain(row).copy(password = null))) //test answers forEach
                }.firstOrNull()
        }
    }

//    // DELETE
////    fun search(test: Test, author: User?): Test? {
////        return transaction(Database.connect(dataSource)) {
////            Tests.select { Tests.id eq id }
////                .map { Tests.toDomain(it, author) }
////                .firstOrNull()
////        }
////    }
//
//    private fun findWithConditional(where: Op<Boolean>, limit: Int, offset: Int): List<Result> {
//        return transaction(Database.connect(dataSource)) {
//            Results.join(Users, JoinType.INNER, additionalConstraint = { Results.author eq Users.id })
//                .select { where }
//                .limit(limit, offset)
//                .orderBy(Results.createdAt, true)
//                .map { row ->
//                    val id = row[Results.id]
//                    val title = row[Results.title]
//                    val classNumber = row[Results.classNumber]
//                    val subject = row[Results.subject]
//                    //val favoritesCount = Favorites.select { Favorites.slug eq slug }.count()
////                        val tagList = Tags.join(ArticlesTags, JoinType.INNER,
////                                additionalConstraint = { Tags.id eq ArticlesTags.tag })
////                                .select { ArticlesTags.slug eq slug }
////                                .map { it[Tags.name] }
//                    Results.toDomain(row, Users.toDomain(row))
////                                .copy(favorited = favoritesCount > 0,
////                                        favoritesCount = favoritesCount.toLong(),
////                                        tagList = tagList)
//                }
//        }
//    }

    //searchParams EX: title, description, subject, classNumber, sort, offset, limit
//    fun search(test: Test?, sort: SortOrder = SortOrder.DESC, limit: Int, offset: Int): List<Result> {
//        return transaction(Database.connect(dataSource)) {
//            val query = Results.join(Users, JoinType.INNER, additionalConstraint = { Results.author eq Users.id })
//                .selectAll()
//            test?.id?.let {
//                query.andWhere { Results.id eq it }
//            }
//            test?.title?.let {
//                query.andWhere { Results.title eq it }
//            }
//            test?.classNumber?.let {
//                query.andWhere { Results.classNumber eq it }
//            }
//            test?.subject?.let {
//                query.andWhere { Results.subject eq it }
//            }
//            test?.author?.id?.let {
//                query.andWhere { Results.author eq it }
//            }
////            if (limit > -1 || offset > -1) query.limit(limit, offset)
//            query.orderBy(Results.id to sort) //createdAt
//            query.map { row -> Results.toDomain(row, Users.toDomain(row)) }
//        }
//    }

//    fun findAll(limit: Int, offset: Int): List<Result> {
//        return transaction(Database.connect(dataSource)) {
//            Results.join(Users, JoinType.INNER, additionalConstraint = { Results.author eq Users.id })
//                .selectAll()
//                .limit(limit, offset)
//                .orderBy(Results.createdAt, true)
//                .map { row ->
////                        val favoritesCount = Favorites.select { Favorites.slug eq row[Articles.slug] }.count()
//                    Results.toDomain(row, Users.toDomain(row))
////                                .copy(favoritesCount = favoritesCount.toLong(), tagList =
////                                Tags.join(ArticlesTags, JoinType.INNER,
////                                        additionalConstraint = { Tags.id eq ArticlesTags.tag })
////                                        .select { ArticlesTags.slug eq row[Articles.slug] }
////                                        .map { it[Tags.name] })
//                }
//        }
//    }

////////////////
//    fun update(id: Long, test: Test): Test? {
//        return transaction(Database.connect(dataSource)) {
//            Results.update({ Results.id eq id }) { row ->
//                if (test.title != null)
//                    row[title] = test.title
//                if (test.description != null)
//                    row[description] = test.description
//                if (test.subject != null)
//                    row[Results.subject] = test.subject
//                if (test.classNumber != null)
//                    row[Results.classNumber] = test.classNumber
//                if (test.questions != null)
//                    row[questions] = test.questions
//                if (test.answers != null)
//                    row[answers] = test.answers
//                if (test.settings != null)
//                    row[settings] = test.settings
//            }
//            findById(id, null)
//        }
//    }

    fun delete(id: Long?) {
        transaction(Database.connect(dataSource)) {
            Results.deleteWhere { Results.id eq id }
//            Favorites.deleteWhere { Favorites.slug eq slug }
        }
    }
}
