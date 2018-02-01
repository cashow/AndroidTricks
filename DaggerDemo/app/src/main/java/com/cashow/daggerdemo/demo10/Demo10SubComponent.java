package com.cashow.daggerdemo.demo10;

import dagger.Subcomponent;

@Subcomponent()
public interface Demo10SubComponent {
    void inject(Demo10Activity activity);
}
