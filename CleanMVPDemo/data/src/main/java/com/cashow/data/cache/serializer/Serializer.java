/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cashow.data.cache.serializer;

import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Serializer 负责 Json 字符串与 Json 对象的转换
 */
@Singleton
public class Serializer {

    private final Gson gson = new Gson();

    @Inject
    Serializer() {
    }

    /**
     * 将 Json 对象转换成 Json 字符串
     */
    public String serialize(Object object, Class clazz) {
        return gson.toJson(object, clazz);
    }

    /**
     * 将 Json 字符串转换成 Json 对象
     */
    public <T> T deserialize(String string, Class<T> clazz) {
        return gson.fromJson(string, clazz);
    }
}
