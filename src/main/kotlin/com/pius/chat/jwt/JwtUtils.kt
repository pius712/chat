package com.pius.chat.jwt

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.jackson.io.JacksonDeserializer
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*


@Component
class JwtUtil(
    @Value("\${jwt.secret}")
    private val secretKey: String,
    @Value("\${jwt.expiration}")
    private val expirationTime: Long,
    @Value("\${jwt.issuer}")
    private val issuer: String,
) {

    val signingKey = Keys.hmacShaKeyFor(secretKey.toByteArray())

    fun generateToken(userId: Long): String {
        val claims: Claims = Jwts
            .claims()
            .add("userId", userId.toString())
            .build()

        return Jwts.builder()
            .claims(claims)
            .signWith(signingKey)
            .issuer(issuer)
            .subject(userId.toString())
            .expiration(Date(System.currentTimeMillis() + expirationTime))
            .compact()
    }

    /**
     * 토큰 유효성 검증 + 만료일 검증
     * */
    fun validateToken(token: String): Boolean {
        val claims = getClaims(token) ?: return false
        return !claims.expiration.before(Date())
    }

    /**
     * 토큰 파싱: userId 반환
     * */
    fun getUserId(token: String): Long {
        val claims = getClaims(token) ?: throw RuntimeException("invalid token")
        return claims.get("userId", String::class.java)?.toLong()
            ?: throw RuntimeException("invalid token")
    }

    /**
     * 토큰 파싱: 토큰 검증 + 클레임 반환
     * */
    private fun getClaims(token: String): Claims? {
        return try {
            Jwts.parser()
                .json(JacksonDeserializer(jacksonObjectMapper()))
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: Exception) {
            null
        }
    }
}