package com.github.libmonsoon.trelloapi.domain

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class CardsList(
    val id: String,
    val name: String,
    val closed: Boolean,
    val pos: Double?,
    val softLimit: JsonElement?,
    val creationMethod: Any?,
    @SerializedName("idBoard")
    val boardId: String,
    val limits: JsonElement?,
    val subscribed: Boolean?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CardsList

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String =
        "TList(id='$id', name='$name', closed=$closed, pos=$pos, softLimit=$softLimit, creationMethod=$creationMethod, boardId='$boardId', limits=$limits, subscribed=$subscribed)"

}