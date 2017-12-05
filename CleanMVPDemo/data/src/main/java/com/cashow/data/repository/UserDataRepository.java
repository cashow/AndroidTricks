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
package com.cashow.data.repository;

import com.cashow.data.entity.mapper.UserEntityDataMapper;
import com.cashow.data.repository.datasource.UserDataStore;
import com.cashow.data.repository.datasource.UserDataStoreFactory;
import com.cashow.domain.User;
import com.cashow.domain.repository.UserRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * {@link UserRepository} for retrieving user data.
 */
@Singleton
public class UserDataRepository implements UserRepository {

    private final UserDataStoreFactory userDataStoreFactory;
    private final UserEntityDataMapper userEntityDataMapper;

    /**
     * Constructs a {@link UserRepository}.
     *
     * @param dataStoreFactory A factory to construct different data source implementations.
     * @param userEntityDataMapper {@link UserEntityDataMapper}.
     */
    @Inject
    UserDataRepository(UserDataStoreFactory dataStoreFactory,
                       UserEntityDataMapper userEntityDataMapper) {
        this.userDataStoreFactory = dataStoreFactory;
        this.userEntityDataMapper = userEntityDataMapper;
    }

    @Override
    public Observable<List<User>> users() {
        //we always get all users from the cloud
        final UserDataStore userDataStore = this.userDataStoreFactory.createCloudDataStore();
        return userDataStore.userEntityList().map(this.userEntityDataMapper::transform);
    }

    @Override
    public Observable<User> user(int userId) {
        final UserDataStore userDataStore = this.userDataStoreFactory.create(userId);
        return userDataStore.userEntityDetails(userId).map(this.userEntityDataMapper::transform);
    }
}
