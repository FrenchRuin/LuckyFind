package com.example.luckyfind.config

import com.example.luckyfind.exception.TokenException
import com.example.luckyfind.model.TokenModel
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import mu.KotlinLogging
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.stream.Collectors
import javax.crypto.spec.SecretKeySpec


@Service
@PropertySource("classpath:jwt.yml")
class TokenProvider(
    @Value("\${secret-key}")
    private val secretKey: String,
    @Value("\${expiration-hours}")
    private val expirationHours: Long,
    @Value("\${issuer}")
    private val issuer: String,
) {

    // 토큰생성
    fun createToken(userSpec: String) = Jwts.builder()
        .signWith(SecretKeySpec(secretKey.toByteArray(), SignatureAlgorithm.HS512.jcaName))
        .setSubject(userSpec)
        .setIssuer(issuer)
        .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
        .setExpiration(Date.from(Instant.now().plus(expirationHours, ChronoUnit.HOURS)))
        .compact()!!

    // 토큰정보 return
    fun validateTokenAndGetSubject(token: String): String? = Jwts.parserBuilder()
        .setSigningKey(secretKey.toByteArray())
        .build()
        .parseClaimsJws(token)
        .body
        .subject

}