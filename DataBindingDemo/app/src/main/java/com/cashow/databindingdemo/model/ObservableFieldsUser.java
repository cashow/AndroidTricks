package com.cashow.databindingdemo.model;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

public class ObservableFieldsUser {
    public final ObservableField<String> firstName = new ObservableField<>();
    public final ObservableField<String> lastName = new ObservableField<>();
    public final ObservableInt age = new ObservableInt();
}
