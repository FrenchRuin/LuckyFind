package com.example.luckyfind.utils

import com.example.luckyfind.model.UserRequest
import com.example.luckyfind.model.UserResponse
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Service
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.crypto.spec.SecretKeySpec

@Service
@PropertySource("classpath:jwt.yml")
class JWTUtils(
    @Value("\${secret-key}")
    private val secretKey: String,
    @Value("\${expiration-hours}")
    private val expirationHours: Long,
    @Value("\${issuer}")
    private val issuer: String,
) {

    // 토큰 생성 //
    fun createToken(response: UserResponse) = Jwts.builder()
        .signWith(SecretKeySpec(secretKey.toByteArray(), SignatureAlgorithm.HS512.jcaName))
        .setSubject("JWT 토큰 테스트")
        .claim("id", response.userId)
        .claim("username", response.username)
        .setIssuer(issuer)
        .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
        .setExpiration(Date.from(Instant.now().plus(expirationHours, ChronoUnit.HOURS)))
        .compact()!!

    fun checkTokenValid(token: String) : String? =
        Jwts.parserBuilder()
            .setSigningKey(secretKey.toByteArray())
            .build()
            .parseClaimsJws(token)
            .body
            .subject

}