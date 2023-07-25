package org.the_chance.play_mongo.endpoints

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.the_chance.play_mongo.data.Person
import org.the_chance.play_mongo.data.PersonDto
import org.the_chance.play_mongo.data.mapper.toDto
import org.the_chance.play_mongo.data.mapper.toPerson
import org.the_chance.play_mongo.mongodb_service.PersonService
import org.the_chance.play_mongo.util.ErrorResponse


fun Route.testRoutes() {
    get("/test") {
        call.respond("Ready")
    }
    val service = PersonService()
    route("/person") {
        post {
            val request = call.receive<PersonDto>()
            val person = request.toPerson()
            service.create(person)?.let { userId ->
                call.response.headers.append("My-User-Id-Header", userId.toString())
                call.respond(HttpStatusCode.Created)
            } ?: call.respond(HttpStatusCode.BadRequest, ErrorResponse.BAD_REQUEST_RESPONSE)
        }

        get {
            val peopleList = service.findAll().map(Person::toDto)
            call.respond(peopleList)
        }
        get("/{id}") {
            val id = call.parameters["id"].toString()
            service.findById(id)?.let { foundPerson ->
                call.respond(foundPerson.toDto())
            } ?: call.respond(HttpStatusCode.NotFound, ErrorResponse.NOT_FOUND_RESPONSE)
        }
        put("/{id}") {
            val id = call.parameters["id"].toString()
            val personRequest = call.receive<PersonDto>()
            val person = personRequest.toPerson()
            val updatedSuccessfully = service.updateById(id, person)
            if (updatedSuccessfully) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse.BAD_REQUEST_RESPONSE)
            }
        }
        delete("/{id}") {
            val deleteId = call.parameters["id"].toString()
            val deleteRequest = service.deleteById(deleteId)
            if (deleteRequest) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, ErrorResponse.NOT_FOUND_RESPONSE)
            }
        }
        get("/search") {
            val name = call.request.queryParameters["name"].toString()
            val searchRequest = service.findByName(name)
            call.respond(searchRequest)
        }

    }

}
