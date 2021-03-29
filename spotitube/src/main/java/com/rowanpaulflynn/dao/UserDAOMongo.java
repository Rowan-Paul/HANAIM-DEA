package com.rowanpaulflynn.dao;

import com.mongodb.client.*;
import com.rowanpaulflynn.domain.Token;
import com.rowanpaulflynn.domain.User;
import com.rowanpaulflynn.exceptions.AccessDeniedError;

import javax.enterprise.inject.Alternative;

import org.bson.Document;

import static com.mongodb.client.model.Filters.*;

@Alternative
public class UserDAOMongo implements IUserDAO {
    public static final String DATABASE_NAME = "spotitube";

    MongoClient mongoClient = MongoClients.create();
    MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
    MongoCollection<Document> usersCollection = database.getCollection("users");
    MongoCollection<Document> tokensCollection = database.getCollection("tokens");

    @Override
    public User getUser(String inputUser) throws AccessDeniedError {
        try {
            Document result = usersCollection.find(eq("user",inputUser)).first();

            User user = new User((String) result.get("user"));
            user.setPassword((String) result.get("password"));

            return user;
        } catch (Exception e) {
            throw new AccessDeniedError("No user found");
        }
    }

    @Override
    public Token createToken(String user) {
        try {
            Token token = new Token(user);

            Document doc = new Document().append("token", token.getToken()).append("user", user);
            tokensCollection.insertOne(doc);

            return token;
        } catch (Exception e) {
            throw new AccessDeniedError("Failed to make token");
        }
    }

    @Override
    public User verifyToken(String token) {
        MongoCursor<Document> cursor = tokensCollection.find(eq("token",token)).iterator();

        try {
            while (cursor.hasNext()) {
                User user = new User((String) cursor.next().get("user"));

                return user;
            }
            return null;
        } catch (Exception e) {
            throw new AccessDeniedError("No token found");
        } finally {
            cursor.close();
        }
    }
}
