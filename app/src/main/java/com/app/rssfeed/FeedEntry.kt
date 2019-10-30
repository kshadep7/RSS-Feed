package com.app.rssfeed

class FeedEntry {

    var name: String = ""
    var artist: String = ""
    var releaseDate: String = ""
    var imageURl: String = ""
    var summary: String = ""

    override fun toString(): String {
        return """
            Name: $name
            Artist: $artist
            Release Date: $releaseDate
            Image URL: $imageURl
            Summary: $summary
        """.trimIndent()
    }

}
