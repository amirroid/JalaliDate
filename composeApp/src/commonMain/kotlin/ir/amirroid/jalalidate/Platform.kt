package ir.amirroid.jalalidate

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform