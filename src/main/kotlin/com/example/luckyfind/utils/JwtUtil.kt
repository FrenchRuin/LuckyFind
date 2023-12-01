package com.example.luckyfind.utils

import com.example.luckyfind.model.UserRequest
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.crypto.SecretKey

@PropertySource("classpath:jwt.yml")
@Component
class JwtUtil(
    @Value("\${secret-key}")
    private val secretKey: String,
) {
    private val key: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))
    private val accessTokenValidity: Long = 60 * 60 * 1000


    fun createToken(userRequest: UserRequest): String {
        val claims = Jwts.claims().setSubject(userRequest.username)
        val tokenCreateTime = Date()
        val tokenValidity = Date(tokenCreateTime.time + TimeUnit.MICROSECONDS.toMillis(accessTokenValidity))
        return Jwts.builder()
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(tokenValidity)
            .setClaims(claims)
            .compact()
    }

    fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        if (bearerToken != null && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring("Bearer".length)
        }
        return null
    }

    fun resolveClaims(request: HttpServletRequest): String {
        val token = resolveToken(request)
        return Jwts.parserBuilder().build().parseClaimsJwt(token)!!.toString()
    }


}