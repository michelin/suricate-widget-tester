/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.michelin.suricate.widget.tester.util.exception;

import com.michelin.suricate.widget.tester.model.enumeration.ApiErrorEnum;
import java.text.MessageFormat;

/** Object not found exception. */
public class ObjectNotFoundException extends ApiException {
    private static final String MSG_OBJECT_NOT_FOUND = "{0} ''{1}'' not found";

    /**
     * Constructor.
     *
     * @param entity The entity class
     * @param id The object id
     */
    public ObjectNotFoundException(Class<?> entity, Object id) {
        super(
                MessageFormat.format(MSG_OBJECT_NOT_FOUND, entity.getSimpleName(), id.toString()),
                ApiErrorEnum.OBJECT_NOT_FOUND);
    }
}
