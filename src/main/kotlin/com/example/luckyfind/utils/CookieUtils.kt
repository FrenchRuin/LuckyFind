package com.example.luckyfind.utils

import jakarta.servlet.http.Cookie
import org.springframework.stereotype.Service

@Service
class CookieUtils {


    // Get Cookie Value
    fun getRefreshTokenInCookie(cookies: Array<Cookie>): String {
        var value = ""
        for (c: Cookie in cookies) {
            if (c.name.equals("refreshToken")) {
                value = c.value
            }
        }
        return value
    }

    // create Cookie
    fun createCookie(name: String, value: String): Cookie {
        val cookie = Cookie(name, value)
        cookie.secure = true
        cookie.isHttpOnly = true
        return cookie
    }
}