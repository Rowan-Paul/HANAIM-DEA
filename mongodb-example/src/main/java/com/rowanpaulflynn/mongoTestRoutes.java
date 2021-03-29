package com.rowanpaulflynn;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import java.util.Arrays;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

@Path("/")
public class mongoTestRoutes {
    MongoClient mongoClient = MongoClients.create();
    MongoDatabase database = mongoClient.getDatabase("onderzoek");
    MongoCollection<Document> collection = database.getCollection("test");

    @POST
    @Path("/")
    public Response createDocument() {
        Document doc = new Document("name", "A Test")
                .append("count", 1)
                .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
                .append("info", new Document("x", 203).append("y", 102));
        collection.insertOne(doc);

        return Response.status(201).build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFirstDocument() {
        Document myDoc = collection.find().first();

        return Response.status(200).entity(myDoc).build();
    }

    @PUT
    @Path("/")
    public Response updateDocument() {
        collection.updateOne(eq("name", "A Test"), set("name", "A test, changed"));

        return Response.status(200).build();
    }

    @DELETE
    @Path("/")
    public Response deleteDocument() {
        collection.deleteOne(eq("name", "A test, changed"));

        return Response.status(200).build();
    }
}
