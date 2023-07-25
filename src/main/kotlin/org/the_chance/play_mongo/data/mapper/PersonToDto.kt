package org.the_chance.play_mongo.data.mapper

import org.the_chance.play_mongo.data.Person
import org.the_chance.play_mongo.data.PersonDto

fun Person.toDto():PersonDto=
    PersonDto(
        id = this.id.toString(),
        name=this.name,
        age=this.age,
        score=this.score
    )