package com.github.fnunezkanut.service

import com.github.fnunezkanut.config.ApiError
import com.github.fnunezkanut.model.Professor
import com.github.fnunezkanut.repository.ProfessorsPostgresRepo
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class ProfessorsService(
    private val professorsRepo: ProfessorsPostgresRepo
) {

    private val logger = KotlinLogging.logger {}

    //validates user provided values, 4xx api errors if something not right
    fun validate(unvalidated: Professor): Professor {

        return Professor(
            code = checkCode(unvalidated.code),
            firstName = checkName(unvalidated.firstName),
            lastName = checkName(unvalidated.lastName)
        )
    }

    //creates a professor, returns created professor
    fun create(validated: Professor): Professor {

        logger.info { "adding professor to DB: $validated" }

        return professorsRepo.add(
            professor = validated
        ) ?: throw ApiError(500, "Failed to create a new professor")
    }


    //double check provided code value from user input
    fun checkCode(_code: String): String {

        val code = _code.toUpperCase().trim()
        if (!code.matches(Regex("[0-9]{7}"))) { //professors have a 7 digit unique code like 0011234
            throw ApiError(400, "Invalid professor code provided")
        }

        //double check existance of the professor as code is unique
        if (professorsRepo.fetchByCode(code = code) != null) {
            throw ApiError(409, "A professor with this professor code already exists")
        }

        return code
    }

    //double check provided name value from user input
    fun checkName(_name: String): String = _name.trim()


    //retrieve a single professor using its main identifier
    fun fetch(uid: String): Professor {

        return professorsRepo.fetch(
            uid = uid
        ) ?: throw ApiError(404, "No such professor: $uid")
    }
}