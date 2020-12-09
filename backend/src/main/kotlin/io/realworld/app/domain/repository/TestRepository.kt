package io.realworld.app.domain.repository

import io.realworld.app.domain.Test
import io.realworld.app.domain.User
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

private object Tests : LongIdTable() {
    //DELETE    override val id: Column<EntityID<Long>> = long("id").autoIncrement().primaryKey().entityId()
    val title: Column<String> = varchar("title", 150)
    val description: Column<String?> = varchar("description", 255).nullable()
    val subject: Column<String> = varchar("subject", 20)
    val classNumber: Column<Int> = integer("classNumber")
    val questions: Column<String?> = text("questions").nullable() //JSON
    val settings: Column<String?> = text("settings").nullable() //JSON
    val createdAt: Column<DateTime> = datetime("created_at")
    val author: Column<Long> = long("author")

    fun toDomain(row: ResultRow, author: User?): Test {
//        val fmt: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
//            .withZone(DateTimeZone.forID("Europe/Moscow"))
//        val dateTime: DateTime = fmt.parseDateTime(row[createdAt].toString("yyyy-MM-dd HH:mm:ss"))
        return Test(
            id = row[Tests.id].value,
            title = row[title],
            description = row[description],
            subject = row[subject],
            classNumber = row[classNumber],
            questions = row[questions]!!,
            settings = row[settings]!!,
            createdAt = row[createdAt],
            author = author
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

class TestRepository(private val dataSource: DataSource) {

    init {
        transaction(Database.connect(dataSource)) {
            SchemaUtils.create(Tests)
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

    fun create(test: Test): Test {
        val date = DateTime()
        val id = transaction(Database.connect(dataSource)) {
            Tests.insertAndGetId { row ->
                row[title] = test.title!!
                row[description] = test.description
                row[subject] = test.subject!!
                row[classNumber] = test.classNumber!!
                row[questions] = test.questions
                row[settings] = test.settings
                row[createdAt] = date
                row[author] = test.author?.id!!
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
        return findById(id, null)!!
    }

    fun findById(id: Long, author: User?): Test? {
        return transaction(Database.connect(dataSource)) {
            Tests.join(Users, JoinType.INNER, additionalConstraint = { Tests.author eq Users.id })
                .select { Tests.id eq id }
                .map { row ->
                    Tests.toDomain(row, Users.toDomain(row).copy(password = null))
                }.firstOrNull()
        }
    }

    // DELETE
//    fun search(test: Test, author: User?): Test? {
//        return transaction(Database.connect(dataSource)) {
//            Tests.select { Tests.id eq id }
//                .map { Tests.toDomain(it, author) }
//                .firstOrNull()
//        }
//    }

    private fun findWithConditional(where: Op<Boolean>, limit: Int, offset: Int): List<Test> {
        return transaction(Database.connect(dataSource)) {
            Tests.join(Users, JoinType.INNER, additionalConstraint = { Tests.author eq Users.id })
                .select { where }
                .limit(limit, offset)
                .orderBy(Tests.createdAt, true)
                .map { row ->
                    val id = row[Tests.id]
                    val title = row[Tests.title]
                    val classNumber = row[Tests.classNumber]
                    val subject = row[Tests.subject]
                    //val favoritesCount = Favorites.select { Favorites.slug eq slug }.count()
//                        val tagList = Tags.join(ArticlesTags, JoinType.INNER,
//                                additionalConstraint = { Tags.id eq ArticlesTags.tag })
//                                .select { ArticlesTags.slug eq slug }
//                                .map { it[Tags.name] }
                    Tests.toDomain(row, Users.toDomain(row))
//                                .copy(favorited = favoritesCount > 0,
//                                        favoritesCount = favoritesCount.toLong(),
//                                        tagList = tagList)
                }
        }
    }

    //searchParams EX: title, description, subject, classNumber, sort, offset, limit
    fun search(test: Test?, sort: SortOrder = SortOrder.DESC, limit: Int, offset: Int): List<Test> {
        return transaction(Database.connect(dataSource)) {
            val query = Tests.join(Users, JoinType.INNER, additionalConstraint = { Tests.author eq Users.id })
                .selectAll()
            test?.id?.let {
                query.andWhere { Tests.id eq it }
            }
            test?.title?.let {
                query.andWhere { Tests.title eq it }
            }
            test?.classNumber?.let {
                query.andWhere { Tests.classNumber eq it }
            }
            test?.subject?.let {
                query.andWhere { Tests.subject eq it }
            }
            test?.author?.id?.let {
                query.andWhere { Tests.author eq it }
            }
            if (limit > -1 || offset > -1) query.limit(limit, offset)
            query.orderBy(Tests.id to sort) //createdAt
            query.map { row -> Tests.toDomain(row, Users.toDomain(row)) }
        }
    }

    fun findAll(limit: Int, offset: Int): List<Test> {
        return transaction(Database.connect(dataSource)) {
            Tests.join(Users, JoinType.INNER, additionalConstraint = { Tests.author eq Users.id })
                .selectAll()
                .limit(limit, offset)
                .orderBy(Tests.createdAt, true)
                .map { row ->
//                        val favoritesCount = Favorites.select { Favorites.slug eq row[Articles.slug] }.count()
                    Tests.toDomain(row, Users.toDomain(row))
//                                .copy(favoritesCount = favoritesCount.toLong(), tagList =
//                                Tags.join(ArticlesTags, JoinType.INNER,
//                                        additionalConstraint = { Tags.id eq ArticlesTags.tag })
//                                        .select { ArticlesTags.slug eq row[Articles.slug] }
//                                        .map { it[Tags.name] })
                }
        }
    }

//    fun findFeed(email: String, limit: Int, offset: Int): List<Article> {
//        val authors = transaction(Database.connect(dataSource)) {
//            Follows.join(Users, JoinType.INNER, additionalConstraint = { Follows.follower eq Users.id })
//                    .slice(Follows.user)
//                    .select { Users.email eq email }
//                    .map { it[Follows.user] }
//        }
//        return findWithConditional((Articles.author inList authors), limit, offset)
//    }

//    fun findBySlug(slug: String): Article? {
//        return findWithConditional((Articles.slug eq slug), 1, 0).firstOrNull()
//    }
//
//    fun findByTitle(title: String): Test? {
//        return findWithConditional((Tests.title eq title), 1, 0).firstOrNull()
//    }

//    fun findBySubject(subject: String, limit: Int, offset: Int): List<Test> {
//        return findWithConditional((Users.name eq subject), limit, offset)
//    }
//
//    fun findByClassNumber(classNumber: Int, limit: Int, offset: Int): List<Test> {
//        return findWithConditional((Users.name eq classNumber), limit, offset)
//    }
//
//    fun findByAuthor(author: String, limit: Int, offset: Int): List<Test> {
//        return findWithConditional((Users.name eq author), limit, offset)
//    }

    fun update(id: Long, test: Test): Test? {
        return transaction(Database.connect(dataSource)) {
            Tests.update({ Tests.id eq id }) { row ->
                if (test.title != null)
                    row[title] = test.title
                if (test.description != null)
                    row[description] = test.description
                if (test.subject != null)
                    row[Tests.subject] = test.subject
                if (test.classNumber != null)
                    row[Tests.classNumber] = test.classNumber
                if (test.questions != null)
                    row[questions] = test.questions
                if (test.settings != null)
                    row[settings] = test.settings
            }
            findById(id, null)
        }
    }

    //    fun favorite(userId: Long, slug: String): Int {
//        return transaction(Database.connect(dataSource)) {
//            Favorites.insert { row ->
//                row[Favorites.slug] = slug
//                row[Favorites.user] = userId
//            }.let {
//                Favorites.select { Favorites.slug eq slug }.count()
//            }
//        }
//    }
//
//    fun unfavorite(userId: Long, slug: String): Int {
//        val article = findBySlug(slug) ?: throw NotFoundResponse()
//        return transaction(Database.connect(dataSource)) {
//            Favorites.deleteWhere {
//                Favorites.slug eq article.slug!! and (Favorites.user eq userId)
//            }.let {
//                Favorites.select { Favorites.slug eq article.slug!! }.count()
//            }
//        }
//    }
//
    fun delete(id: Long?) {
        transaction(Database.connect(dataSource)) {
            Tests.deleteWhere { Tests.id eq id }
//            Favorites.deleteWhere { Favorites.slug eq slug }
        }
    }
}
