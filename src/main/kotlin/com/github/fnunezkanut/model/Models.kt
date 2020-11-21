/**
 * data models used by services and repos
 * thanks to kotlin we can keep all this boilerplate in one place
 */

package com.github.fnunezkanut.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Course(
    @JsonProperty(value = "uid", required = false) val uid: String = "",
    @JsonProperty(value = "code", required = true) val code: String,
    @JsonProperty(value = "name", required = false) val name: String = ""
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Student(
    @JsonProperty(value = "uid", required = false) val uid: String = "",
    @JsonProperty(value = "code", required = true) val code: String,
    @JsonProperty(value = "firstName", required = false) val firstName: String = "",
    @JsonProperty(value = "lastName", required = false) val lastName: String = ""
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Professor(
    @JsonProperty(value = "uid", required = false) val uid: String = "",
    @JsonProperty(value = "code", required = true) val code: String,
    @JsonProperty(value = "firstName", required = false) val firstName: String = "",
    @JsonProperty(value = "lastName", required = false) val lastName: String = ""
)