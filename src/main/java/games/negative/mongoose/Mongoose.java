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

import games.negative.mongoose.impl.MongoDataManagerImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a class to connect to a MongoDB database and interact with it.
 */
public interface Mongoose {

    /**
     * Connects to the MongoDB database with the specified credentials and containers.
     * @param credentials the credentials to connect to the database
     * @param containers the containers to collect once connected.
     * @return the data manager instance
     */
    static MongoDataManager connect(@NotNull MongoCredentials credentials, @NotNull MongoContainer... containers) {
        return new MongoDataManagerImpl(credentials, containers);
    }

}
