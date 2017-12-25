package com.cashow.databindingdemo.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.cashow.databindingdemo.BR;

public class ObservableObjectUser extends BaseObservable {
    public String firstName;
    public String lastName;

    @Bindable
    public String getFirstName() {
        return firstName;
    }

    @Bindable
    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        notifyPropertyChanged(BR.firstName);
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        notifyPropertyChanged(BR.lastName);
    }
}
