package com.github.fnunezkanut.repository

import com.github.fnunezkanut.model.Course
import com.github.fnunezkanut.util.kString
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CoursesPostgresRepo(
    @Qualifier("jdbc") private val jdbc: NamedParameterJdbcTemplate
) {

    private val logger = KotlinLogging.logger {}


    //create new course entry, returns its uuid, null if something went wrong
    fun add(course: Course): Course? {


        val uuid = UUID.randomUUID()

        val sql = """
INSERT INTO courses (
    uid,
    code,
    name
) VALUES (
    :uid,
    :code,
    :name
);
""".trimIndent()

        val parameters = MapSqlParameterSource()
            .addValue("code", course.code)
            .addValue("name", course.name)
            .addValue("uid", uuid)


        try {
            jdbc.update(sql, parameters)
        } catch (e: Exception) {
            logger.error("failed to add course", e)
            return null
        }

        return course.copy(
            uid = uuid.toString()
        )
    }


    //lookup a single entry given unique course code
    fun fetchByCode(code: String): Course? {

        val sql = """
SELECT uid, code, name 
FROM courses 
WHERE courses.code = :code
        """.trimIndent()
        val parameters = MapSqlParameterSource()
            .addValue("code", code)

        val results = jdbc.query(sql, parameters) { rs, _ ->

            //mapping from db row to dto
            Course(
                uid = rs.kString("uid"),
                code = rs.kString("code"),
                name = rs.kString("name")
            )
        }
        return results.firstOrNull()
    }
}