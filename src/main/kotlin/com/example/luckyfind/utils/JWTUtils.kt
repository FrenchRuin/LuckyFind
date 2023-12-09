package com.example.luckyfind.utils

import com.example.luckyfind.domain.repository.UserRepository
import com.example.luckyfind.model.TokenModel
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.stream.Collectors
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
    private val userRepository: UserRepository,
) {

    private val EXPIRE_TIME = Date.from(Instant.now().plus(expirationHours, ChronoUnit.HOURS))

    /* TODO
    *  1. REFRESH TOKEN 재발급
    *  2. JWT 예외처리
    *  3. token 만료 테스트
    *  4. token 만료시 로그아웃 처리
    *  5. token에 username 과 userId 를 추가할지 고려
    * */

    // Generate JWT for first Login
    fun generateToken(authentication: Authentication): TokenModel {
        val authorities = authentication.authorities.stream().map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","))

        val accessToken = Jwts.builder()
            .setIssuer(issuer)
            .setSubject(authentication.name)
            .claim("role", authorities)
            .setExpiration(EXPIRE_TIME)
            .signWith(SecretKeySpec(secretKey.toByteArray(), SignatureAlgorithm.HS512.jcaName))
            .compact()!!

        val refreshToken = Jwts.builder()
            .setExpiration(EXPIRE_TIME)
            .signWith(SecretKeySpec(secretKey.toByteArray(), SignatureAlgorithm.HS512.jcaName))
            .compact()!!

        return TokenModel(
            refreshToken = refreshToken,
            accessToken = accessToken,
            grantType = "Bearer"
        )
    }

    // JWT Claim Parse
    fun parseClaims(token: String): Claims =
        Jwts.parserBuilder()
            .setSigningKey(secretKey.toByteArray())
            .build()
            .parseClaimsJws(token)
            .body


    // JWT Token Expired check
    fun isExpired(token: String): Boolean =
        parseClaims(token).expiration.before(Date())

    // Jwt generateRefreshToken
    fun genereateRefreshToken(claim: Claims): String =
        Jwts.builder()
            .setIssuer(issuer)  // ISSUER
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


    // get Authentication info in Token
    fun getAuthentication(token: String): UsernamePasswordAuthenticationToken {
        val claim = parseClaims(token)
        val user = userRepository.findByUsername(claim.subject) // claim.subject == username
        return UsernamePasswordAuthenticationToken( // UsernamePasswordAuthenticationToken return
            user, user!!.password, user.authorities
        )
    }
}