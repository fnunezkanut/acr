package com.github.fnunezkanut.repository

import com.github.fnunezkanut.model.Student
import com.github.fnunezkanut.util.kString
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class StudentsPostgresRepo(
    @Qualifier("jdbc") private val jdbc: NamedParameterJdbcTemplate
) {

    private val logger = KotlinLogging.logger {}


    //create new student entry, returns its uuid, null if something went wrong
    fun add(student: Student): Student? {

        val uuid = UUID.randomUUID()

        val sql = """
INSERT INTO students (
    uid,
    code,
    first_name,
    last_name
) VALUES (
    :uid,
    :code,
    :first_name,
    :last_name
);
""".trimIndent()

        val parameters = MapSqlParameterSource()
            .addValue("code", student.code)
            .addValue("first_name", student.firstName)
            .addValue("last_name", student.lastName)
            .addValue("uid", uuid)


        try {
            jdbc.update(sql, parameters)
        } catch (e: Exception) {
            logger.error("failed to add student", e)
            return null
        }

        return student.copy(
            uid = uuid.toString()
        )
    }


    //lookup a single entry given unique course code
    fun fetchByCode(code: String): Student? {

        val sql = """
SELECT uid, code, first_name, last_name  
FROM students 
WHERE students.code = :code
        """.trimIndent()
        val parameters = MapSqlParameterSource()
            .addValue("code", code)

        val results = jdbc.query(sql, parameters) { rs, _ ->

            //mapping from db row to dto
            Student(
                uid = rs.kString("uid"),
                code = rs.kString("code"),
                firstName = rs.kString("first_name"),
                lastName = rs.kString("last_name")
            )
        }
        return results.firstOrNull()
    }


    //lookup a single entry given primary key
    fun fetch(uid: String): Student? {

        val sql = """
SELECT uid, code, first_name, last_name  
FROM students 
WHERE students.uid = :uid
        """.trimIndent()
        val parameters = MapSqlParameterSource()
            .addValue("uid", UUID.fromString(uid))

        val results = jdbc.query(sql, parameters) { rs, _ ->

            //mapping from db row to dto
            Student(
                uid = rs.kString("uid"),
                code = rs.kString("code"),
                firstName = rs.kString("first_name"),
                lastName = rs.kString("last_name")
            )
        }
        return results.firstOrNull()
    }
}