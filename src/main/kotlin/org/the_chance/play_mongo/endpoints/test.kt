package org.the_chance.play_mongo.endpoints

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.testRoutes(){
    get("/test"){
        call.respond("Ready")
    }
}
