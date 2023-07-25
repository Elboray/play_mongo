package org.the_chance.play_mongo.mongodb_service

import org.bson.types.ObjectId
import org.litote.kmongo.Id
import org.litote.kmongo.id.toId
import org.the_chance.play_mongo.data.Person
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.regex


class PersonService {
    private val client =
        KMongo.createClient("mongodb+srv://elboray2002:Aa1234@atlascluster.ndf3ewu.mongodb.net/").coroutine
    private val dataBase = client.getDatabase("person")
    private val personCollection = dataBase.getCollection<Person>("person")

    suspend fun create(person: Person): Id<Person>? {
        personCollection.insertOne(person)
        return person.id
    }

    suspend fun findAll(): List<Person> = personCollection.find().toList()

    suspend fun findById(id: String): Person? {
        val bsonId: Id<Person> = ObjectId(id).toId()
        return personCollection.findOne(Person::id eq bsonId)
    }

    suspend fun findByName(name: String): List<Person> {
        val caseSensitiveTypeSafeFilter = Person::name regex name
        return personCollection.find(caseSensitiveTypeSafeFilter).toList()
    }

    suspend fun updateById(id: String, request: Person): Boolean {
        val bsonId: Id<Person> = ObjectId(id).toId()
        val filter = Person::id eq bsonId

        val updateResult = personCollection.replaceOne(
            filter,
            request.copy(id = bsonId) // Ensure the id remains unchanged in the updated document
        )

        return updateResult.modifiedCount == 1L
    }


    suspend fun deleteById(id: String): Boolean {
        val deletePerson = personCollection.deleteOneById(ObjectId(id))
        return deletePerson.deletedCount == 1L
    }
}
