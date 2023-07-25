package org.the_chance.play_mongo.data.mapper

import org.the_chance.play_mongo.data.Person
import org.the_chance.play_mongo.data.PersonDto

fun PersonDto.toPerson(): Person =
    Person(
        name = this.name,
        age = this.age,
        score = this.score
    )