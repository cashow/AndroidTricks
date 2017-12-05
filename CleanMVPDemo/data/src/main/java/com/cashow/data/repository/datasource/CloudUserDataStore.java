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
package com.cashow.data.repository.datasource;

import com.cashow.data.cache.UserCache;
import com.cashow.data.entity.UserEntity;
import com.cashow.data.net.RestApi;

import java.util.List;

import io.reactivex.Observable;

/**
 * {@link UserDataStore} implementation based on connections to the api (Cloud).
 */
class CloudUserDataStore implements UserDataStore {

    private final RestApi restApi;
    private final UserCache userCache;

    /**
     * Construct a {@link UserDataStore} based on connections to the api (Cloud).
     *
     * @param restApi The {@link RestApi} implementation to use.
     * @param userCache A {@link UserCache} to cache data retrieved from the api.
     */
    CloudUserDataStore(RestApi restApi, UserCache userCache) {
        this.restApi = restApi;
        this.userCache = userCache;
    }

    @Override
    public Observable<List<UserEntity>> userEntityList() {
        return this.restApi.userEntityList();
    }

    @Override
    public Observable<UserEntity> userEntityDetails(final int userId) {
        return this.restApi.userEntityById(userId).doOnNext(CloudUserDataStore.this.userCache::put);
    }
}
