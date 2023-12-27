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

package games.negative.mongoose.impl;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import games.negative.mongoose.BsonSerializable;
import games.negative.mongoose.MongoContainer;
import games.negative.mongoose.MongoCredentials;
import games.negative.mongoose.MongoDataManager;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MongoDataManagerImpl implements MongoDataManager {

    private final MongoDatabase database;
    private final MongoClient client;
    private final Map<MongoContainer, MongoCollection<Document>> documents;

    @SuppressWarnings("ConstantValue")
    public MongoDataManagerImpl(@NotNull MongoCredentials credentials, @NotNull MongoContainer... containers) {
        String url = credentials.url();
        if (url == null) throw new NullPointerException("The 'connection-url' value is null");

        String databaseName = credentials.database();
        if (databaseName == null) throw new NullPointerException("The 'database-name' value is null");

        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(url))
                .serverApi(serverApi)
                .build();

        this.client = MongoClients.create(settings);

        this.database = client.getDatabase(databaseName);
        this.documents = new HashMap<>();

        // Load documents
        for (MongoContainer type : containers) {
            MongoCollection<Document> collection;
            try {
                collection = database.getCollection(type.collection());
            } catch (MongoException exception) {
                database.createCollection(type.collection());
                collection = database.getCollection(type.collection());
            }

            documents.put(type, collection);
        }
    }

    @Override
    public @NotNull MongoDatabase database() {
        return database;
    }

    @Override
    public @NotNull MongoClient client() {
        return client;
    }

    @Override
    public <T extends BsonSerializable> void save(@NotNull MongoContainer type, @NotNull T object, @Nullable Bson query) {
        MongoCollection<Document> collection = documents.get(type);
        if (collection == null) throw new NullPointerException("The collection '" + type.collection() + "' is null");

        Document document = object.toDocument();

        if (query == null) {
            collection.insertOne(document);
            return;
        }

        Document result = collection.find(query).first();
        if (result == null) {
            collection.insertOne(document);
        } else {
            collection.replaceOne(result, document);
        }
    }

    @Override
    public <T extends BsonSerializable> void save(@NotNull MongoContainer type, @NotNull Iterator<T> iterator, @Nullable Function<T, Bson> query) {
        MongoCollection<Document> documents = this.documents.get(type);
        if (documents == null) throw new NullPointerException("The collection '" + type.collection() + "' is null");

        while (iterator.hasNext()) {
            T object = iterator.next();
            Document document = object.toDocument();

            if (query == null) {
                documents.insertOne(document);
                return;
            }

            Bson bsonQuery = query.apply(object);
            Document result = documents.find(bsonQuery).first();

            if (result == null) {
                documents.insertOne(document);
            } else {
                documents.replaceOne(result, document);
            }
        }
    }

    @Override
    public <T extends BsonSerializable> void delete(@NotNull MongoContainer type, @NotNull T object, @Nullable Bson query) {
        MongoCollection<Document> collection = documents.get(type);
        if (collection == null) throw new NullPointerException("The collection '" + type.collection() + "' is null");

        Document document = object.toDocument();

        if (query == null) {
            collection.deleteOne(document);
            return;
        }

        Document result = collection.find(query).first();
        if (result == null) return;

        collection.deleteOne(result);
    }

    @Override
    public @NotNull Map<MongoContainer, MongoCollection<Document>> documents() {
        return documents;
    }
}
