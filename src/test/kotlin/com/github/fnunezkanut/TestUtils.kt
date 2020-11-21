package com.github.fnunezkanut

/**
 * test utility methods to cut down on boilerplate
 */

import com.github.fnunezkanut.config.ApiError
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.fail


inline fun expectApiError(code: Int, action: () -> Unit) {

    try {
        action()
        fail("expecting ApiError $code")
    } catch (e: ApiError) {
        assertThat(e.status.value()).isEqualTo(code)
    }
}