package com.github.fnunezkanut.service

import com.github.fnunezkanut.config.ApiError
import com.github.fnunezkanut.model.Student
import com.github.fnunezkanut.repository.StudentsPostgresRepo
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class StudentsService(
    private val studentsRepo: StudentsPostgresRepo
) {

    private val logger = KotlinLogging.logger {}

    //validates user provided values, 4xx api errors if something not right
    fun validate(unvalidated: Student): Student {

        return Student(
            code = checkCode(unvalidated.code),
            firstName = checkName(unvalidated.firstName),
            lastName = checkName(unvalidated.lastName)
        )
    }

    //creates a student, returns created student
    fun create(validated: Student): Student {

        logger.info { "adding course to DB: $validated" }

        return studentsRepo.add(
            student = validated
        ) ?: throw ApiError(500, "Failed to create a new student")
    }


    //double check provided code value from user input
    fun checkCode(_code: String): String {

        val code = _code.toUpperCase().trim()
        if (!code.matches(Regex("[0-9]{8}"))) {  //students have an 8 digit unique code like 003344568
            throw ApiError(400, "Invalid student code provided")
        }

        //double check existance of the course as code is unique
        if (studentsRepo.fetchByCode(code = code) != null) {
            throw ApiError(409, "A student with this student code already exists")
        }

        return code
    }

    //double check provided name value from user input
    fun checkName(_name: String): String = _name.trim()

    //retrieve a single student using its main identifier
    fun fetch(uid: String): Student {

        return studentsRepo.fetch(
            uid = uid
        ) ?: throw ApiError(404, "No such student: $uid")
    }
}