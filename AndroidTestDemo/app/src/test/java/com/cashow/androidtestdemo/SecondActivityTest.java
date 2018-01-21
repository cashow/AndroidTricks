package com.cashow.androidtestdemo;

import android.content.Intent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowApplication;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class SecondActivityTest {

    @Test
    public void clickingLogin_shouldStartLoginActivity() {
        SecondActivity secondActivity = Robolectric.setupActivity(SecondActivity.class);
        secondActivity.findViewById(R.id.login).performClick();

        Intent expectedIntent = new Intent(secondActivity, RecyclerViewActivity.class);
        Intent actual = ShadowApplication.getInstance().getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actual.getComponent());
    }
}