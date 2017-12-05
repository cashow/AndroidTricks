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
package com.cashow.presentation.userlist.presenter;

import android.support.annotation.NonNull;

import com.cashow.domain.User;
import com.cashow.domain.exception.DefaultErrorBundle;
import com.cashow.domain.exception.ErrorBundle;
import com.cashow.domain.interactor.DefaultObserver;
import com.cashow.domain.interactor.GetUserList;
import com.cashow.presentation.common.exception.ErrorMessageFactory;
import com.cashow.presentation.common.di.PerActivity;
import com.cashow.presentation.common.mapper.UserModelDataMapper;
import com.cashow.presentation.common.model.UserModel;
import com.cashow.presentation.common.presenter.Presenter;
import com.cashow.presentation.userlist.view.UserListView;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
@PerActivity
public class UserListPresenter implements Presenter {

    private UserListView viewListView;

    private final GetUserList getUserListUseCase;
    private final UserModelDataMapper userModelDataMapper;

    @Inject
    public UserListPresenter(GetUserList getUserListUserCase,
                             UserModelDataMapper userModelDataMapper) {
        this.getUserListUseCase = getUserListUserCase;
        this.userModelDataMapper = userModelDataMapper;
    }

    public void setView(@NonNull UserListView view) {
        this.viewListView = view;
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void destroy() {
        this.getUserListUseCase.dispose();
        this.viewListView = null;
    }

    /**
     * Initializes the presenter by start retrieving the user list.
     */
    public void initialize() {
        this.loadUserList();
    }

    /**
     * Loads all users.
     */
    private void loadUserList() {
        this.hideViewRetry();
        this.showViewLoading();
        this.getUserList();
    }

    public void onUserClicked(UserModel userModel) {
        this.viewListView.viewUser(userModel);
    }

    private void showViewLoading() {
        this.viewListView.showLoading();
    }

    private void hideViewLoading() {
        this.viewListView.hideLoading();
    }

    private void showViewRetry() {
        this.viewListView.showRetry();
    }

    private void hideViewRetry() {
        this.viewListView.hideRetry();
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(this.viewListView.context(),
                errorBundle.getException());
        this.viewListView.showError(errorMessage);
    }

    private void showUsersCollectionInView(Collection<User> usersCollection) {
        final Collection<UserModel> userModelsCollection =
                this.userModelDataMapper.transform(usersCollection);
        this.viewListView.renderUserList(userModelsCollection);
    }

    private void getUserList() {
        this.getUserListUseCase.execute(new UserListObserver(), null);
    }

    private final class UserListObserver extends DefaultObserver<List<User>> {

        @Override
        public void onComplete() {
            UserListPresenter.this.hideViewLoading();
        }

        @Override
        public void onError(Throwable e) {
            UserListPresenter.this.hideViewLoading();
            UserListPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
            UserListPresenter.this.showViewRetry();
        }

        @Override
        public void onNext(List<User> users) {
            UserListPresenter.this.showUsersCollectionInView(users);
        }
    }
}
