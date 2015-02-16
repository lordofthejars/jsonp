/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
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

import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collector;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.BiConsumer;

/**
 * This class contains some implementations of {@code java.util.stream.Collector} for accumulating
 * {@link JsonValue}s into {@link JsonArray} and {@link JsonObject}.
 *
 * @author Kin-man Chung
 */

public class JsonCollectors {

    private JsonCollectors() {
    }

    /**
     * Constructs a {@code java.util.stream.Collector} that accumulates the input {@code JsonValue}
     * elements into a {@code JsonArray}.
     *
     * @return the constructed Collector
     */
    public static Collector<JsonValue, JsonArrayBuilder, JsonArray> toJsonArray() {
        return Collector.of(
                Json::createArrayBuilder,
                JsonArrayBuilder::add,
                JsonArrayBuilder::addAll,
                JsonArrayBuilder::build);
    }

    /**
     * Constructs a {@code java.util.stream.Collector} that accumulates the input {@code JsonValue}
     * elements into a {@code JsonObject}.  The name/value pairs of the {@code JsonObject} are computed
     * by applying the provided mapping functions.
     *
     * @param keyMapper a mapping function to produce names.
     * @param valueMapper a mapping function to produce values
     * @return the constructed Collector
     */
    public static Collector<JsonValue, JsonObjectBuilder, JsonObject>
                toJsonObject(Function<JsonValue, String> keyMapper,
                             Function<JsonValue, JsonValue> valueMapper) {
        return Collector.of(
                Json::createObjectBuilder,
                (b, v) -> b.add(keyMapper.apply(v), valueMapper.apply(v)),
                JsonObjectBuilder::addAll,
                JsonObjectBuilder::build,
                Collector.Characteristics.UNORDERED);
    }

    /**
     * Constructs a {@code java.util.stream.Collector} that implements a "group by" operation on the
     * input {@code JsonValue} elements. A classifier function maps the input {@code JsonValue}s to keys, and
     * the {@code JsonValue}s are partitioned into groups according to the value of the key.
     * A reduction operation is performed on the {@code JsonValue}s in each group, using the
     * downstream {@code Collector}. For each group, the key and the results of the reduction operation
     * become the name/value pairs of the resultant {@code JsonObject}.
     *
     * @param classifier a function mapping the input {@code JsonValue}s to a String, producing keys
     * @param downstream a {@code Collector} that implements a reduction operation on the
     *        {@code JsonValue}s in each group.
     * @return the constructed {@code Collector}
     */
    public static Collector<JsonValue, Map<String, JsonArrayBuilder>, JsonObject>
                groupingBy(Function<JsonValue, String> classifier,
                           Collector<JsonValue, JsonArrayBuilder, JsonArray> downstream) {

        BiConsumer<Map<String, JsonArrayBuilder>, JsonValue> accumulator =
            (map, value) -> {
                String key = classifier.apply(value);
                if (key == null) {
                    throw new JsonException("element cannot be mapped to a null key");
                }
                // Build a map of key to JsonArrayBuilder
                JsonArrayBuilder arrayBuilder = 
                    map.computeIfAbsent(key, v->downstream.supplier().get());
                // Add elements from downstream Collector to the arrayBuilder.
                downstream.accumulator().accept(arrayBuilder, value);
            };
        Function<Map<String, JsonArrayBuilder>, JsonObject> finisher =
            map -> {
                // transform the map of name: JsonArrayBuilder to
                //                      name: JsonArray
                // using the downstream collector for reducing the JsonArray
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                map.forEach((k, v) -> {
                    JsonArray array = downstream.finisher().apply(v);
                    objectBuilder.add(k, array);
                });
                return objectBuilder.build();
            };
        BinaryOperator<Map<String, JsonArrayBuilder>> combiner =
            (map1, map2) -> {
                map1.putAll(map2);
                return map1;
            };
        return Collector.of(HashMap::new, accumulator, combiner, finisher,
            Collector.Characteristics.UNORDERED);
    }

    /**
     * Constructs a {@code java.util.stream.Collector} that implements a "group by" operation on the
     * input {@code JsonValue} elements. A classifier function maps the input {@code JsonValue}s to keys, and
     * the {@code JsonValue}s are partitioned into groups according to the value of the key.
     * The {@code JsonValue}s in each group are added to a {@code JsonArray}.  The key and the
     * {@code JsonArray} in each group becomes the name/value pair of the resultant {@code JsonObject}.
     *
     * @param classifier a function mapping the input {@code JsonValue}s to a String, producing keys
     * @return the constructed {@code Collector}
     */
    public static Collector<JsonValue, Map<String, JsonArrayBuilder>, JsonObject>
                groupingBy(Function<JsonValue, String> classifier) {
        return groupingBy(classifier, toJsonArray());
    }
}
