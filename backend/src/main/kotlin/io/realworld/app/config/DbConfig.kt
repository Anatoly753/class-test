package io.realworld.app.config

import com.zaxxer.hikari.*
import javax.sql.*

class DbConfig(jdbcUrl: String, username: String, password: String) {
    private val dataSource: DataSource

    init {
        dataSource = HikariConfig().let { config ->
            config.jdbcUrl = jdbcUrl
            config.driverClassName = "com.mysql.cj.jdbc.Driver"
            config.username = username
            config.password = password
            config.maximumPoolSize = 10
            HikariDataSource(config)
        }
    }

    fun getDataSource(): DataSource {
        return dataSource
    }
}