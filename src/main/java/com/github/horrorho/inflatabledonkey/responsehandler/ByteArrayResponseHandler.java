/* 
 * The MIT License
 *
 * Copyright 2015 Ahseya.
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
package com.github.horrorho.inflatabledonkey.responsehandler;

import java.io.IOException;
import net.jcip.annotations.Immutable;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

/**
 * ByteArray ResponseHandler.
 *
 * @author Ahseya
 */
@Immutable
public final class ByteArrayResponseHandler extends DonkeyResponseHandler<byte[]> {

    public static ByteArrayResponseHandler instance() {
        return INSTANCE;
    }

    private static final ByteArrayResponseHandler INSTANCE = new ByteArrayResponseHandler();

    ByteArrayResponseHandler() {
    }

    @Override
    public byte[] handleEntity(HttpEntity entity) throws IOException {
        return EntityUtils.toByteArray(entity);
    }
}
