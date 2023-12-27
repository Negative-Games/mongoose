/*
 * MIT License
 *
 * Copyright (c) 2023 Negative Games
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package games.negative.mongoose.example;

import com.mongodb.client.model.Filters;
import games.negative.mongoose.*;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * This is an example class that shows how to use Mongoose, DO NOT ACTUALLY USE THIS!!!!
 */
public class Example {

    public Example() {
        throw new IllegalCallerException("This class cannot be instantiated");
    }

    private static void main(String[] args) {
        // Connecting to the database
        MongoCredentials credentials = new MongoCredentials("connection url", "database name");
        MongoDataManager manager = Mongoose.connect(credentials, Containers.values());

        // Printing out the collections
        manager.documents().forEach((container, collection) -> {
            System.out.println("Found collection '" + container.collection() + "'");
            System.out.println("Found " + collection.countDocuments() + " documents in the collection '" + container.collection() + "'");
        });

        // Saving something to the database using BsonSerializable
        UUID uuid = UUID.randomUUID();
        SavableObject savableObject = new SavableObject("username", uuid, "password");
        manager.save(Containers.USERS, savableObject, Filters.eq("uuid", uuid.toString()));

        // Getting something from the database using BsonSerializable
        Document document = manager.documents().get(Containers.USERS).find(Filters.eq("uuid", uuid.toString())).first();
        if (document == null) return; // No document with the provided uuid was found

        SavableObject loadedObject = new SavableObject(document);
        System.out.println("Loaded object with username '" + loadedObject.username + "' and password '" + loadedObject.password + "'");
    }

    private enum Containers implements MongoContainer {
        USERS("users"),
        GUILDS("guilds"),
        SERVERS("servers")
        ;

        private final String name;

        Containers(@NotNull String name) {
            this.name = name;
        }

        @Override
        public @NotNull String collection() {
            return name;
        }
    }

    private static class SavableObject implements BsonSerializable {

        private String username;
        private UUID uuid;
        private String password;

        public SavableObject(String username, UUID uuid, String password) {
            this.username = username;
            this.uuid = uuid;
            this.password = password;
        }

        public SavableObject(@NotNull Document document) {
            this.username = document.getString("username");
            this.uuid = UUID.fromString(document.getString("uuid"));
            this.password = document.getString("password");
        }

        @Override
        public @NotNull Document toDocument() {
            Document document = new Document();

            document.put("username", username);
            document.put("uuid", uuid.toString());
            document.put("password", password);
            return document;
        }
    }
}
