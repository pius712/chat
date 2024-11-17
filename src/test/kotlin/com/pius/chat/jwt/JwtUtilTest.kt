package com.pius.chat.jwt

import org.assertj.core.api.Assertions
import kotlin.test.Test

class JwtUtilTest {
    val secret = "lsadkfjladsjlsdakjfakldsjflaksjdflkadjslkfjld"
    val expiration = 86400000L
    val issuer = "pius"

    private val sut = JwtUtil(secret, expiration, issuer)

    @Test
    fun testGenerateToken() {


        val userId: Long = 1
        val token = sut.generateToken(userId)

        val result = sut.getUserId(token)
        Assertions.assertThat(result).isEqualTo(userId)
    }

    @Test
    fun decode() {

    }
}