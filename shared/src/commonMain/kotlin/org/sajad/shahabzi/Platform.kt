package org.sajad.shahabzi

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform