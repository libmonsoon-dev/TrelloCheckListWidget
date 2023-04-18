package com.github.libmonsoon.trellochecklistwidget

import android.util.Log
import com.github.libmonsoon.trelloapi.domain.*
import com.google.gson.*
import java.lang.reflect.Type
import java.net.URLEncoder


class TrelloClient(private val apiKey: String, private val token: String) {
    companion object {
        const val BASE_URL: String = "https://api.trello.com/1/"
    }

    fun loadMember(): Member = get(Member::class.java, "members/me")

    fun loadMemberBoards(userId: String): Array<Board> =
        get(Array<Board>::class.java, "members/$userId/boards")

    fun loadBoardLists(boardId: String): Array<CardsList> =
        get(Array<CardsList>::class.java, "board/$boardId/lists")

    fun loadBoardCards(boardId: String): Array<Card> =
        get(Array<Card>::class.java, "board/$boardId/cards")

    fun loadCardCheckLists(cardId: String): Array<CheckList> =
        get(Array<CheckList>::class.java, "cards/$cardId/checklists")

    private fun <T : Any> get(type: Type, path: String): T {
        return this.request("GET", path, type)
    }

    private fun <T : Any> request(method: String, path: String, type: Type): T {
        val response = khttp.request(method, getUrl(path))

        var httpErrorMsg: String? = null
        if (response.statusCode in 400..499) {
            httpErrorMsg =
                "${response.statusCode} Client Error for url: ${response.url}: ${response.text}"
        }

        if (response.statusCode in 500..599) {
            httpErrorMsg =
                "${response.statusCode} Server Error for url: ${response.url}: ${response.text}"
        }

        httpErrorMsg?.let {
            throw Exception(httpErrorMsg)
        }

        try {
            return JsonUtils.gson.fromJson(response.text, type)
        } catch (e: JsonSyntaxException) {
            Log.v(this.javaClass.name, response.request.toString())
            Log.v(this.javaClass.name, JsonUtils.instance.prettyPrintingJson(response.text))
            throw e
        }
    }

    private fun getUrl(request: String, vararg arguments: Pair<String, String>): String {
        val url = StringBuilder(BASE_URL)
        url.append(request)
        url.append("?key=").append(apiKey)
        url.append("&token=").append(token)

        for (argument in arguments) url.append("&").append(argument.first).append("=")
            .append(URLEncoder.encode(argument.second, "UTF-8"))

        return url.toString()
    }
}

