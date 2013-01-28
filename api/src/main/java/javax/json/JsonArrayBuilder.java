/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
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


import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * Builds a {@link JsonArray} from scratch. It uses builder pattern
 * to build the array model and the builder methods can be chained while
 * building the JSON array.
 *
 * <p>
 * <a id="JsonArrayBuilderExample1"/>
 * <b>For example</b>, for the following JSON array
 *
 * <pre>
 * <code>
 * [
 *     { "type": "home", "number": "212 555-1234" },
 *     { "type": "fax", "number": "646 555-4567" }
 * ]
 * </code>
 * </pre>
 *
 * a JsonArray instance can be built using:
 *
 * <p>
 * <pre>
 * <code>
 * JsonArray value = new JsonArrayBuilder()
 *     .add(new JsonObjectBuilder()
 *         .add("type", "home")
 *         .add("number", "212 555-1234"))
 *     .add(new JsonObjectBuilder()
 *         .add("type", "fax")
 *         .add("number", "646 555-4567"))
 *     .build();
 * </code>
 * </pre>
 *
 * @see JsonObjectBuilder
 */
public interface JsonArrayBuilder {

    /**
     * Adds the specified value to the array that is being built.
     *
     * @param value a JSON value
     * @return this array builder
     */
    public JsonArrayBuilder add(JsonValue value);

    /**
     * Adds the specified value as a JSON string value to the array
     * that is being built.
     *
     * @param value string
     * @return this array builder
     */
    public JsonArrayBuilder add(String value);

    /**
     * Adds the specified value as a JSON number value to the array
     * that is being built.
     *
     * @param value a number
     * @return this array builder
     *
     * @see JsonNumber
     */
    public JsonArrayBuilder add(BigDecimal value);

    /**
     * Adds the specified value as a JSON number value to the array
     * that is being built.
     *
     * @param value a number
     * @return this array builder
     *
     * @see JsonNumber
     */
    public JsonArrayBuilder add(BigInteger value);

    /**
     * Adds the specified value as a JSON number value to the array
     * that is being built.
     *
     * @param value a number
     * @return this array builder
     *
     * @see JsonNumber
     */
    public JsonArrayBuilder add(int value);

    /**
     * Adds the specified value as a JSON number value to the array
     * that is being built.
     *
     * @param value a number
     * @return this array builder
     *
     * @see JsonNumber
     */
    public JsonArrayBuilder add(long value);

    /**
     * Adds the specified value as a JSON number value to the array
     * that is being built.
     *
     * @param value a number
     * @return this array builder
     * @throws NumberFormatException if value is Not-a-Number(NaN) or infinity
     *
     * @see JsonNumber
     */
    public JsonArrayBuilder add(double value);

    /**
     * Adds a JSON true or false value to the array that is being built.
     *
     * @param value a boolean
     * @return this array builder
     */
    public JsonArrayBuilder add(boolean value);

    /**
     * Adds a JSON null value to the array that is being built.
     *
     * @return this array builder
     */
    public JsonArrayBuilder addNull();

    /**
     * Adds a JsonObject from the specified builder to the array that
     * is being built.
     *
     * @return this array builder
     */
    public JsonArrayBuilder add(JsonObjectBuilder builder);

    /**
     * Adds a JsonArray from the specified builder to the array that
     * is being built.
     *
     * @return this array builder
     */
    public JsonArrayBuilder add(JsonArrayBuilder builder);

    /**
     * Returns the array that is being built
     *
     * @return JSON array that is being built
     */
    public JsonArray build();

}

