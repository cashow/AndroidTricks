package com.cashow.androidtestdemo;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityInstrumentationTest {
    private static final String STRING_TO_BE_TYPED = "Peter";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void sayHello() {
        // 首先，找到ID为editText的view，输入Peter，然后关闭键盘
        onView(withId(R.id.editText)).perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());
        // 接下来，点击Say hello!的View
        onView(withText("Say hello!")).perform(click());
        String expectedText = "Hello, " + STRING_TO_BE_TYPED + "!";
        // 最后，将TextView上的文本同预期结果对比，如果一致则测试通过
        onView(withId(R.id.textView)).check(matches(withText(expectedText)));
    }
}
