/*
 * The MIT License
 *
 * Copyright 2016 Ahseya.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.horrorho.inflatabledonkey.chunk.engine;

import com.github.horrorho.inflatabledonkey.chunk.Chunk;
import com.github.horrorho.inflatabledonkey.chunk.store.ChunkStore;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer;
import com.github.horrorho.inflatabledonkey.requests.ChunkListRequestFactory;
import com.github.horrorho.inflatabledonkey.responsehandler.InputStreamResponseHandler;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import net.jcip.annotations.Immutable;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class ChunkClient {

    public static ChunkClient defaultInstance() {
        return DEFAULT_INSTANCE;
    }

    private static final Logger logger = LoggerFactory.getLogger(ChunkClient.class);

    private static final ChunkClient DEFAULT_INSTANCE = new ChunkClient();

    private final ChunkListDecrypterFactory chunkDecrypterFactory;
    private final Function<ChunkServer.StorageHostChunkList, HttpUriRequest> requestFactory;

    public ChunkClient(
            ChunkListDecrypterFactory chunkDecrypterFactory,
            Function<ChunkServer.StorageHostChunkList, HttpUriRequest> requestFactory) {
        this.chunkDecrypterFactory = Objects.requireNonNull(chunkDecrypterFactory);
        this.requestFactory = Objects.requireNonNull(requestFactory);
    }

    ChunkClient() {
        this(ChunkListDecrypterFactory.defaults(), ChunkListRequestFactory.instance());
    }

    public Optional<Map<ChunkServer.ChunkReference, Chunk>>
            apply(HttpClient client, SHCLContainer container, ChunkStore store) {
        try {
            ChunkListDecrypter decrypter = chunkDecrypterFactory.apply(store, container);
            InputStreamResponseHandler<Map<ChunkServer.ChunkReference, Chunk>> handler
                    = new InputStreamResponseHandler<>(decrypter);

            HttpUriRequest request = requestFactory.apply(container.storageHostChunkList());

            Map<ChunkServer.ChunkReference, Chunk> chunks = client.execute(request, handler);
            return Optional.of(chunks);

        } catch (IOException ex) {
            logger.warn("-- apply() - IOException: ", ex);
            return Optional.empty();
        }
    }
}
