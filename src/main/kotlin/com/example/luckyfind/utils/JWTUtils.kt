package com.example.luckyfind.utils

import com.example.luckyfind.domain.entity.User
import com.example.luckyfind.domain.entity.UserAuthority
import com.example.luckyfind.domain.repository.UserRepository
import com.example.luckyfind.exception.TokenException
import com.example.luckyfind.model.TokenModel
import com.example.luckyfind.model.UserRequest
import com.example.luckyfind.model.UserResponse
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
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
class JWTUtils(
    @Value("\${secret-key}")
    private val secretKey: String,
    @Value("\${expiration-hours}")
    private val expirationHours: Long,
    @Value("\${issuer}")
    private val issuer: String,
    private val userRepository: UserRepository,
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

    fun generateToken(authentication: Authentication): TokenModel {
        val authorities = authentication.authorities.stream().map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","))

        val accessTokenExpireTime = Date.from(Instant.now().plus(expirationHours, ChronoUnit.HOURS))
        val accessToken = Jwts.builder()
            .setSubject(authentication.name)
            .claim("auth", authorities)
            .setExpiration(accessTokenExpireTime)
            .signWith(SecretKeySpec(secretKey.toByteArray(), SignatureAlgorithm.HS512.jcaName))
            .compact()!!

        val refreshToken = Jwts.builder()
            .setExpiration(accessTokenExpireTime)
            .signWith(SecretKeySpec(secretKey.toByteArray(), SignatureAlgorithm.HS512.jcaName))
            .compact()!!

        return TokenModel(
            refreshToken = refreshToken,
            accessToken = accessToken,
            grantType = "Bearer"
        )
    }

    fun getAuthentication(accessToken: String): UsernamePasswordAuthenticationToken {
        val claims = parseClaims(accessToken) ?: throw TokenException("권한 정보가 없는 토근입니다.")

        val user = userRepository.findByUsername(claims.subject)

        return UsernamePasswordAuthenticationToken(user, "", user?.authorities)
    }

    fun parseClaims(accessToken: String): Claims? =
        Jwts.parserBuilder()
            .setSigningKey(secretKey.toByteArray())
            .build()
            .parseClaimsJws(accessToken)
            .body

}