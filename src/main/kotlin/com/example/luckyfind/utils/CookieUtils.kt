package com.example.luckyfind.utils

import jakarta.servlet.http.Cookie
import org.springframework.stereotype.Service

@Service
class CookieUtils {


    // Get Cookie Value
    fun getCookieValue(cookies: Array<Cookie>, name: String): String? {
        for (c: Cookie in cookies) {
            if (c.name.equals(name)) {
                return c.value
            }
        }
        return null
    }

    // create Cookie
    fun createCookie(name : String, value : String) : Cookie{
        val cookie = Cookie(name, value)
        cookie.secure = true
        cookie.isHttpOnly = true
        return cookie
    }
}