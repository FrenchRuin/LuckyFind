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
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.stream.Collectors
import javax.crypto.spec.SecretKeySpec


//@Component
@PropertySource("classpath:jwt.yml")
class TokenProvider(
    @Value("\${secret-key}")
    private val secretKey: String,
    @Value("\${expiration-hours}")
    private val expirationHours: Long,
    @Value("\${issuer}")
    private val issuer: String,
) {

    private val key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))

    private val log = KotlinLogging.logger {}

    // JWT 토큰 생성
    fun createToken(authentication: Authentication): TokenModel {
        val authorities = authentication.authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","))

        val token = Jwts.builder()
            .signWith(
                SecretKeySpec(
                    secretKey.toByteArray(),
                    SignatureAlgorithm.HS512.jcaName
                )
            ) // HS512 알고리즘을 사용하여 secretKey를 이용해 서명
            .setSubject(authentication.name)   // JWT 토큰 제목
            .setIssuer(issuer)    // JWT 토큰 발급자
            .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))    // JWT 토큰 발급 시간
            .setExpiration(Date.from(Instant.now().plus(expirationHours, ChronoUnit.HOURS)))    // JWT 토큰의 만료시간 설정
            .claim("auth", authorities)
            .compact()!!

        return TokenModel("Bearer", token)
    }


    // JWT토큰 복호화하여서 토큰에 들어있는 정보를 꺼내는 메소드
    fun getAuthentication(token: String): Authentication {
        val claims: Claims = parseClaims(token)

        if (claims["auth"] == null) {
            throw TokenException("권한이 존재하지 않는 토큰입니다.")
        }

        val authorities = claims["auth"].toString().split(",").map {
            SimpleGrantedAuthority(it)
        }.stream().collect(Collectors.toList())

        val principal: UserDetails = User(claims.subject, "", authorities)
        return UsernamePasswordAuthenticationToken(principal, "", authorities)
    }


    // 토큰 검증
    fun validateToken(token: String): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            return true
        } catch (e: SecurityException) {
            log.info("Invalid JWT Token", e)
        } catch (e: MalformedJwtException) {
            log.info("Invalid JWT Token", e)
        } catch (e: ExpiredJwtException) {
            log.info("Expired JWT Token", e)
        } catch (e: UnsupportedJwtException) {
            log.info("Unsupported JWT Token", e)
        } catch (e: IllegalArgumentException) {
            log.info("JWT claims string is empty.", e)
        }
        return false
    }

    fun parseClaims(token: String): Claims {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
        } catch (e: ExpiredJwtException) {
            e.claims
        }
    }


}