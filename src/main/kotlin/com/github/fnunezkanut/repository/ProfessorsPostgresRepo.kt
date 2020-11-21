package com.github.fnunezkanut.repository

import com.github.fnunezkanut.model.Professor
import com.github.fnunezkanut.util.kString
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ProfessorsPostgresRepo(
    @Qualifier("jdbc") private val jdbc: NamedParameterJdbcTemplate
) {

    private val logger = KotlinLogging.logger {}


    //create new professor entry, returns its uuid, null if something went wrong
    fun add(professor: Professor): Professor? {

        val uuid = UUID.randomUUID()

        val sql = """
INSERT INTO professors (
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
            .addValue("code", professor.code)
            .addValue("first_name", professor.firstName)
            .addValue("last_name", professor.lastName)
            .addValue("uid", uuid)


        try {
            jdbc.update(sql, parameters)
        } catch (e: Exception) {
            logger.error("failed to add professor", e)
            return null
        }

        return professor.copy(
            uid = uuid.toString()
        )
    }


    //lookup a single entry given unique course code
    fun fetchByCode(code: String): Professor? {

        val sql = """
SELECT uid, code, first_name, last_name  
FROM professors 
WHERE professors.code = :code
        """.trimIndent()
        val parameters = MapSqlParameterSource()
            .addValue("code", code)

        val results = jdbc.query(sql, parameters) { rs, _ ->

            //mapping from db row to dto
            Professor(
                uid = rs.kString("uid"),
                code = rs.kString("code"),
                firstName = rs.kString("first_name"),
                lastName = rs.kString("last_name")
            )
        }
        return results.firstOrNull()
    }
}