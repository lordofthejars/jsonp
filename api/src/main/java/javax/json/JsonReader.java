/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package javax.json;

import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;
import java.io.Closeable;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;

/**
 * A JSON reader that reads a JSON {@link JsonObject object} or
 * {@link JsonArray array} from an input source.
 *
 * <p>
 * <a id="JsonReaderExample1"/>
 * <b>For example</b>, an empty JSON array can be created as follows:
 * <pre>
 * <code>
 * JsonReader jsonReader = new JsonReader(new StringReader("[]"));
 * JsonArray array = jsonReader.readArray();
 * jsonReader.close();
 * </code>
 * </pre>
 *
 * It uses {@link javax.json.stream.JsonParser} internally for parsing. The
 * parser is created using one of the {@link Json}'s {@code createParser}
 * methods.
 *
 * @author Jitendra Kotamraju
 */
public interface JsonReader extends  /*Auto*/Closeable {

    /**
     * Returns a JSON array or object that is represented in
     * the input source. This method needs to be called
     * only once for a reader instance.
     *
     * @return a Json object or array
     * @throws JsonException if a JSON object or array cannot
     *     be created due to i/o error (IOException would be
     * cause of JsonException)
     * @throws javax.json.stream.JsonParsingException if a JSON object or array
     *     cannot be created due to incorrect representation
     * @throws IllegalStateException if this method, readObject, readArray or
     *     close method is already called
     */
    public JsonStructure read();

    /**
     * Returns a JSON object that is represented in
     * the input source. This method needs to be called
     * only once for a reader instance.
     *
     * @return a Json object
     * @throws JsonException if a JSON object cannot
     *     be created due to i/o error (IOException would be
     *     cause of JsonException)
     * @throws javax.json.stream.JsonParsingException if a JSON object cannot
     *     be created due to incorrect representation
     * @throws IllegalStateException if this method, readObject, readArray or
     *     close method is already called
     */
    public JsonObject readObject();

    /**
     * Returns a JSON array that is represented in
     * the input source. This method needs to be called
     * only once for a reader instance.
     *
     * @return a Json array
     * @throws JsonException if a JSON array cannot
     *     be created due to i/o error (IOException would be
     *     cause of JsonException)
     * @throws javax.json.stream.JsonParsingException if a JSON array cannot
     *     be created due to incorrect representation
     * @throws IllegalStateException if this method, readObject, readArray or
     *     close method is already called
     */
    public JsonArray readArray();

    /**
     * Closes this reader and frees any resources associated with the
     * reader. This closes the underlying input source.
     *
     * @throws JsonException if an i/o error occurs (IOException would be
     * cause of JsonException)
     */
    @Override
    public void close();

}
