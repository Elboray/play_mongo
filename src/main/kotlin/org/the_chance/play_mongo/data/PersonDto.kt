package org.the_chance.play_mongo.data

import kotlinx.serialization.Serializable

@Serializable
data class PersonDto(
    val id: String? = null,
    val name: String,
    val age: Int,
    val score:Int,
)
