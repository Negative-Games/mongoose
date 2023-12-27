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

package games.negative.mongoose;

import com.mongodb.Function;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Map;

/**
 * Represents the interface for interacting with a MongoDB database.
 */
public interface MongoDataManager {

    /**
     * Returns the {@link MongoDatabase} instance.
     *
     * @return the database
     * @throws NullPointerException if the database instance is null
     */
    @NotNull
    MongoDatabase database();

    /**
     * Returns the MongoClient instance.
     *
     * @return the MongoClient instance
     * @throws NullPointerException if the MongoClient instance is null
     */
    @NotNull
    MongoClient client();

    /**
     * Saves an object to the database with an optional query.
     *
     * @param <T>        the type of the object to be saved
     * @param collection the collection to save to
     * @param object     the object to be saved
     * @param query      the optional query
     * @throws NullPointerException if the type or object is null
     */
    <T extends BsonSerializable> void save(@NotNull MongoContainer collection, @NotNull T object, @Nullable Bson query);

    /**
     * Saves a collection of objects to the specified collection type in the database with an optional query.
     *
     * @param <T>        the type of the objects to be saved
     * @param collection the collection of objects to be saved
     * @param query      the optional query to filter the objects
     * @throws NullPointerException if the type or collection parameter is null
     */
    <T extends BsonSerializable> void save(@NotNull MongoContainer collection, @NotNull Iterator<T> iterator, @Nullable Function<T, Bson> query);

    /**
     * Deletes an object from the specified collection in the database with an optional query.
     *
     * @param <T>        the type of the object to be deleted
     * @param collection the collection to delete from
     * @param object     the object to be deleted
     * @param query      the optional query to filter the objects
     * @throws NullPointerException if the type or object is null
     */
    <T extends BsonSerializable> void delete(@NotNull MongoContainer collection, @NotNull T object, @Nullable Bson query);

    /**
     * Returns a map of database collections.
     *
     * @return the map of database collections
     */
    @NotNull
    Map<MongoContainer, MongoCollection<Document>> documents();
}
