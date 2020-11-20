/**
 * json response data objects used by endpoints (when they need custom output classes)
 * thanks to kotlin we can keep all this boilerplate in one place
 */

package com.github.fnunezkanut.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


@JsonIgnoreProperties(ignoreUnknown = true)
data class V1MiscVersionResponse(
    @JsonProperty(value = "version", required = true) val version: String = ""
)
