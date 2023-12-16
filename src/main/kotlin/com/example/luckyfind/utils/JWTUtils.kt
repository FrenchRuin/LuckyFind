package com.example.luckyfind.utils

import com.example.luckyfind.domain.repository.UserRepository
import com.example.luckyfind.model.TokenModel
import com.example.luckyfind.service.UserService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.security.Principal
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.stream.Collectors
import javax.crypto.spec.SecretKeySpec

@Component
@PropertySource("classpath:jwt.yml")
class JWTUtils(
    @Value("\${secret-key}")
    private val secretKey: String,
    @Value("\${expiration-hours}")
    private val expirationHours: Long,
    @Value("\${issuer}")
    private val issuer: String,
) {

    private val EXPIRE_TIME = Date.from(Instant.now().plus(expirationHours, ChronoUnit.HOURS))

    /* TODO
    *  1. REFRESH TOKEN 재발급
    *  2. JWT 예외처리
    *  3. token 만료 테스트
    *  4. token 만료시 로그아웃 처리
    *  5. token에 username 과 userId 를 추가할지 고려
    * */

    // JWT Claim Parse
    fun parseClaims(token: String): Claims =
        Jwts.parserBuilder()
            .setSigningKey(secretKey.toByteArray())
            .build()
            .parseClaimsJws(token)
            .body


    // JWT Token Expired check
    fun isExpired(token: String?): Boolean =
        (token == null || parseClaims(token).expiration.before(Date()))


    // Jwt generateRefreshToken
    fun generateRefreshToken(claim: Claims): String =
        Jwts.builder()
            .setIssuer(issuer)  // ISSUER
            .setSubject(claim.subject)  // username
            .setExpiration(EXPIRE_TIME) // EXPIRE
            .signWith(SecretKeySpec(secretKey.toByteArray(), SignatureAlgorithm.HS512.jcaName)) // KEY
            .compact()!!


    // generateAccessToken
    fun generateAccessToken(claim: Claims) = Jwts.builder()
        .setIssuer(issuer)
        .setSubject(claim.subject)  // USERNAME
        .claim("role", claim["role"]) // AUTHORITY
        .setExpiration(EXPIRE_TIME) // EXPIRE
        .signWith(SecretKeySpec(secretKey.toByteArray(), SignatureAlgorithm.HS512.jcaName)) // KEY
        .compact()!!

    fun generateAccessToken(authentication: Authentication) = Jwts.builder()
        .setIssuer(issuer)
        .setSubject(authentication.name)  // USERNAME
        .claim("role", authentication.authorities.toString()) // AUTHORITY
        .setExpiration(EXPIRE_TIME) // EXPIRE
        .signWith(SecretKeySpec(secretKey.toByteArray(), SignatureAlgorithm.HS512.jcaName)) // KEY
        .compact()!!
}