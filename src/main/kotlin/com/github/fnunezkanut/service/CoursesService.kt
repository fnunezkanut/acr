package com.github.fnunezkanut.service

import com.github.fnunezkanut.config.ApiError
import com.github.fnunezkanut.model.Course
import com.github.fnunezkanut.repository.CoursesPostgresRepo
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class CoursesService(
    private val coursesRepo: CoursesPostgresRepo
) {

    private val logger = KotlinLogging.logger {}

    //validates user provided values, 4xx api errors if something not right
    fun validate(unvalidated: Course): Course {

        val filteredCode = checkCode(unvalidated.code)
        val filteredName = checkName(unvalidated.name)

        return Course(
            name = filteredName,
            code = filteredCode
        )
    }

    //creates a course, returns created course
    fun create(validated: Course): Course {

        logger.info { "adding course to DB: $validated" }

        return coursesRepo.add(
            course = validated
        ) ?: throw ApiError(500, "Failed to create a new course")
    }


    //double check provided code value from user input
    fun checkCode(_code: String): String {

        val code = _code.toUpperCase().trim()
        if (!code.matches(Regex("[A-Z0-9-]{3,10}"))) {
            throw ApiError(400, "Invalid course code provided")
        }

        //double check existance of the course as code is unique
        if (coursesRepo.fetchByCode(code = code) != null) {
            throw ApiError(409, "A course with this course code already exists")
        }

        return code
    }

    //double check provided name value from user input
    fun checkName(_name: String): String = _name.trim()


    //creates a course<>professor relation
    fun assignProfessor(courseUid: String, professorUid: String) {
        val added = coursesRepo.addProfessorRelation(courseUid, professorUid)
        if (!added) {
            throw ApiError(500, "Failed to assign a professor to a course")
        }
    }


    //creates a course<>student relation
    fun registerStudent(courseUid: String, studentUid: String) {
        val added = coursesRepo.addStudentRelation(courseUid, studentUid)
        if (!added) {
            throw ApiError(500, "Failed to register a student to a course")
        }
    }
}