package com.github.fnunezkanut.util

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class UtilsTest {


    @Test
    fun `masked(), normal run`() {

        //given
        val test = "this is atest"
        val expected = "********atest"

        //when
        val actual = test.masked(5)

        //then
        assertThat(actual).isEqualTo(expected)
    }
}