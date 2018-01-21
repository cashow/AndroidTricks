# AndroidTestDemo

单元测试和 UI 测试的学习笔记

相关链接：

<https://developer.android.com/training/testing/espresso/setup.html>  
<https://developer.android.com/training/testing/espresso/cheat-sheet.html>  
<https://codelabs.developers.google.com/codelabs/android-testing/index.html#0>  
<https://blog.aritraroy.in/understanding-and-mastering-the-world-of-android-testing-part-1-32f6a1a06d3b>  
<https://www.jianshu.com/p/03118c11c199>  
<http://robolectric.org/>  

---

## 单元测试

### 创建测试类

打开要测试的类文件，在类的声明处右键，选择 "Go to > Test"，然后 "Create a new test…"，生成的测试文件在 `src/test/java/` 下。

要运行测试，选中文件，右键选择 Run 'CalculatorTest' 即可。

```java
public class CalculatorTest {
    private Calculator mCalculator;

    @Before
    public void setUp() throws Exception {
        mCalculator = new Calculator();
    }

    @Test
    public void sum() throws Exception {
        Assert.assertEquals(6d, mCalculator.sum(1d, 5d), 0);
    }

    @Test
    public void substract() throws Exception {
        Assert.assertEquals(1d, mCalculator.substract(5d, 4d), 0);
    }

    @Test
    public void divide() throws Exception {
        Assert.assertEquals(4d, mCalculator.divide(20d, 5d), 0);
    }

    @Test
    public void multiply() throws Exception {
        Assert.assertEquals(10d, mCalculator.multiply(2d, 5d), 0);
    }
}
```

---

## Espresso

### 找一个 view

通过 id 查找：

```java
onView(withId(R.id.my_view))
```

如果有多个 view 有同一个 id，可以通过 view 的 text 去过滤。

找 text 是 "Hello!" 的 view：

```java
onView(allOf(withId(R.id.my_view), withText("Hello!")))
```

找 text 不是 "Hello!" 的 view：

```java
onView(allOf(withId(R.id.my_view), not(withText("Hello!"))))
```

### 对 view 进行操作

点击：

```java
onView(...).perform(click())
```

perform 里可以放多个操作：

```java
onView(...).perform(typeText("Hello"), click())
```

需要注意的是，我在测试的时候用的是搜狗输入法，调用 `typeText()` 后填入的是小写的字符串 "hello"。建议在做测试时使用系统自带的输入法。

如果你的 view 是在 ScrollView 里，建议先调用 `scrollTo()`

```java
onView(...).perform(scrollTo(), click())
```

### 对 view 进行检查

检查 view 的 text 是不是 "Hello!"：

```java
onView(...).check(matches(withText("Hello!")))
```

### 获取 List 里的 item

```java
onData(allOf(is(instanceOf(String.class)), is("Americano"))).perform(click())
```

### 通过相邻的 view 过滤要找的 view

```java
onView(allOf(withText("7"), hasSibling(withText("item: 0")))).perform(click())
```

### 检查 view 是否显示

```java
onView(withId(R.id.bottom_left)).check(matches(not(isDisplayed())))
```

### 检查 view 是否在 view hierarchy 里

```java
onView(withId(R.id.bottom_left)).check(doesNotExist())
```

### 检查 view 是否在 list 内

```java
private static Matcher<View> withAdaptedData(final Matcher<Object> dataMatcher) {
    return new TypeSafeMatcher<View>() {

        @Override
        public void describeTo(Description description) {
            description.appendText("with class name: ");
            dataMatcher.describeTo(description);
        }

        @Override
        public boolean matchesSafely(View view) {
            if (!(view instanceof AdapterView)) {
                return false;
            }

            @SuppressWarnings("rawtypes")
            Adapter adapter = ((AdapterView) view).getAdapter();
            for (int i = 0; i < adapter.getCount(); i++) {
                if (dataMatcher.matches(adapter.getItem(i))) {
                    return true;
                }
            }

            return false;
        }
    };
}

public void testDataItemNotInAdapter(){
    onView(withId(R.id.list))
          .check(matches(not(withAdaptedData(withItemContent("item: 168")))));
    }
}
```

### 自定义错误信息

```java
private static class CustomFailureHandler implements FailureHandler {
    private final FailureHandler delegate;

    public CustomFailureHandler(Context targetContext) {
        delegate = new DefaultFailureHandler(targetContext);
    }

    @Override
    public void handle(Throwable error, Matcher<View> viewMatcher) {
        try {
            delegate.handle(error, viewMatcher);
        } catch (NoMatchingViewException e) {
            throw new MySpecialException(e);
        }
    }
}

@Override
public void setUp() throws Exception {
    super.setUp();
    getActivity();
    setFailureHandler(new CustomFailureHandler(getInstrumentation()
                                              .getTargetContext()));
}
```

### 指定 root view

```java
onView(withText("South China Sea"))
    .inRoot(withDecorView(not(is(getActivity().getWindow().getDecorView()))))
    .perform(click());
```

### 匹配 list 的 header 和 footer

```java
public static final String FOOTER = "FOOTER";
...
View footerView = layoutInflater.inflate(R.layout.list_item, listView, false);
listView.addFooterView(footerView, FOOTER, true);

public static Matcher<Object> isFooter() {
    return allOf(is(instanceOf(String.class)), is(LongListActivity.FOOTER));
}

public void testClickFooter() {
    onData(isFooter())
        .perform(click());
    // ...
}
```

### 自定义 Matcher 过滤 list 中的 view

在 list 中过滤 view：

```java
onData(allOf(is(instanceOf(Map.class)), hasEntry(equalTo("STR"), is("item: 50")))
    .perform(click());
```

为了方便复用，可以把上面的逻辑写成一个自定义 Matcher：

```java
public static Matcher<Object> withItemContent(final Matcher<String> itemTextMatcher) {
    checkNotNull(itemTextMatcher);
    return new BoundedMatcher<Object, Map>(Map.class) {
      @Override
      public boolean matchesSafely(Map map) {
        return hasEntry(equalTo("STR"), itemTextMatcher).matches(map);
      }
      @Override
      public void describeTo(Description description) {
        description.appendText("with item content: ");
        itemTextMatcher.describeTo(description);
      }
    };
  }

public static Matcher<Object> withItemContent(String expectedText) {
    checkNotNull(expectedText);
    return withItemContent(equalTo(expectedText));
}

onData(withItemContent("item: 50")).perform(click());
```

### 找到 list 中 view 的子 view

```java
onData(withItemContent("item: 60"))
    .onChildView(withId(R.id.item_size))
    .perform(click());
```

### 与 RecyclerView 交互

* scrollTo()：滚动到对应的 view
* scrollToHolder()：滚动到对应的 ViewHolder
* scrollToPosition()：滚动到对应的位置
* actionOnHolderItem()： 对匹配到的 ViewHolder 执行操作
* actionOnItem()：对匹配到的 View 执行操作
* actionOnItemAtPosition()： 对指定位置的 View 执行操作

```java
@Test
public void scrollToItemBelowFold_checkItsText() {
    // First, scroll to the position that needs to be matched and click on it.
    onView(ViewMatchers.withId(R.id.recyclerView))
            .perform(RecyclerViewActions.actionOnItemAtPosition(ITEM_BELOW_THE_FOLD,
            click()));

    // Match the text in an item below the fold and check that it's displayed.
    String itemElementText = mActivityRule.getActivity().getResources()
            .getString(R.string.item_element_text)
            + String.valueOf(ITEM_BELOW_THE_FOLD);
    onView(withText(itemElementText)).check(matches(isDisplayed()));
}

@Test
public void itemInMiddleOfList_hasSpecialText() {
    // First, scroll to the view holder using the isInTheMiddle() matcher.
    onView(ViewMatchers.withId(R.id.recyclerView))
            .perform(RecyclerViewActions.scrollToHolder(isInTheMiddle()));

    // Check that the item has the special text.
    String middleElementText =
            mActivityRule.getActivity().getResources()
            .getString(R.string.middle);
    onView(withText(middleElementText)).check(matches(isDisplayed()));
}
```

### 检验 intent

```java
intended(allOf(
    hasAction(equalTo(Intent.ACTION_VIEW)),
    hasCategories(hasItem(equalTo(Intent.CATEGORY_BROWSABLE))),
    hasData(hasHost(equalTo("www.google.com"))),
    hasExtras(allOf(
        hasEntry(equalTo("key1"), equalTo("value1")),
        hasEntry(equalTo("key2"), equalTo("value2")))),
        toPackage("com.android.browser")));
```

### 设置 ActivityResult

```java
// Build the result to return when the activity is launched.
Intent resultData = new Intent();
String phoneNumber = "123-345-6789";
resultData.putExtra("phone", phoneNumber);
ActivityResult result =
    new ActivityResult(Activity.RESULT_OK, resultData);

// Set up result stubbing when an intent sent to "contacts" is seen.
intending(toPackage("com.android.contacts")).respondWith(result);

// User action that results in "contacts" activity being launched.
// Launching activity expects phoneNumber to be returned and displayed.
onView(withId(R.id.pickButton)).perform(click());

// Assert that the data we set up above is shown.
onView(withId(R.id.phoneNumber)).check(matches(withText(phoneNumber)));
```

### 和 Webview 交互

```java
onWebView()
    .withElement(findElement(Locator.ID, "link_2")) // similar to onView(withId(...))
    .perform(webClick()) // Similar to perform(click())
    // Similar to check(matches(...))
    .check(webMatches(getCurrentUrl(), containsString("navigation_2.html")));

onWebView()
    .withElement(findElement(Locator.ID, "teacher"))
    .withContextualElement(findElement(Locator.ID, "person_name"));

onWebView()
    .withElement(...)
    .perform(...)
    .reset();
```

---

## Robolectric

### demo

检查点击 login 按钮后是不是跳转到了 RecyclerViewActivity：

```java
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
```

### 为某个类或者方法设置单独的配置

```java
@Config(sdk=JELLYBEAN_MR1,
    manifest="some/build/path/AndroidManifest.xml",
    shadows={ShadowFoo.class, ShadowBar.class})
public class SandwichTest {
}
```

你可以在基类里设置 config，所有继承该基类的类都会有这个 config

### 设置 package 的配置

在 `src/test/resources/包名/` 目录下，创建一个 robolectric.properties 文件，该包名下所有类都会使用这个配置，如果有类使用了 `@Config` ，那么 `@Config` 里的配置会覆盖掉 robolectric.properties 里的配置。

```
# src/test/resources/com/mycompany/app/robolectric.properties
sdk=18
manifest=some/build/path/AndroidManifest.xml
shadows=my.package.ShadowFoo,my.package.ShadowBar
```

### 设置全局配置

如果想要设置全局的配置，可以继承 RobolectricTestRunner 并重写 `buildGlobalConfig()` 方法。

### 设置 sdk 版本

默认情况下，Robolectric 使用的是 targetSdkVersion 的版本。可以通过 `sdk`、`minSdk` 和 `maxSdk` 指定 Robolectric 使用的版本号。

```
@Config(sdk = KITKAT)

@Config(minSdk=LOLLIPOP)
```

### 设置 Application 类

Robolectric 会创建一个你在 manifest 里指定的 Application 类的实例，你可以通过 `application` 指定要使用的 Application 类。

```java
@Config(application = CustomApplication.class)
public class SandwichTest {

    @Config(application = CustomApplicationOverride.class)
    public void getSandwich_shouldReturnHamSandwich() {
    }
}
```

### 设置资源文件位置

```java
@Config(resourceDir = "other/build/path/ham-sandwich/res")
public void getSandwich_shouldReturnHamSandwich() {
}
```

### 设置资源文件类型

```
@Config(qualifiers = "fr-xlarge")
public void getSandwichName() {
    assertThat(sandwich.getName()).isEqualTo("Grande Croque Monégasque");
}
```

### 设置 Robolectric 的配置

* robolectric.enabledSdks: 运行的时候只测试在list中指定的sdk
* robolectric.offline: 禁止运行时获取新的 jars
* robolectric.dependency.dir: 在 offline 模式，指定依赖库的位置
* robolectric.dependency.repo.id: 设置 Maven 依赖库的 id (默认是 sonatype)
* robolectric.dependency.repo.url: 设置 Maven 库的 url (默认是 https://oss.sonatype.org/content/groups/public/).
* robolectric.logging.enabled: 打印 log

你还可以在 Gradle 里设置上面这些配置：

```
android {
    testOptions {
        unitTests.all {
            systemProperty 'robolectric.dependency.repo.url', 'https://local-mirror/repo'
            systemProperty 'robolectric.dependency.repo.id', 'local'
        }
    }
}
```

### 修改配置

```java
@Config(qualifiers = "xlarge-port")
class MyTest {
  public void testItWithXlargePort() { ... } // config is "xlarge-port"

  @Config(qualifiers = "+land")
  public void testItWithXlargeLand() { ... } // config is "xlarge-land"

  @Config(qualifiers = "land")
  public void testItWithLand() { ... } // config is "normal-land"
}
```

### 在代码中修改配置

```java
@Test @Config(qualifiers = "+port")
public void testOrientationChange() {
  controller = Robolectric.buildActivity(MyActivity.class);
  controller.setup();
  // assert that activity is in portrait mode
  RuntimeEnvironment.setQualifiers("+land");
  controller.configurationChange();
  // assert that activity is in landscape mode
}
```

### 创建 Activity 并调用 onCreate()

```java
Activity activity = Robolectric.buildActivity(MyAwesomeActivity.class).create().get();
```

### 检查某些操作是否在 onResume() 里被执行

```java
ActivityController controller = Robolectric.buildActivity(MyAwesomeActivity.class).create().start();
Activity activity = controller.get();
// assert that something hasn't happened
activityController.resume();
// assert it happened!
```

### Activity 的每个阶段

```java
Activity activity = Robolectric.buildActivity(MyAwesomeActivity.class).create().start().resume().visible().get();
```

### 创建 Activity 时传入 Intent

```java
Intent intent = new Intent(Intent.ACTION_VIEW);
Activity activity = Robolectric.buildActivity(MyAwesomeActivity.class, intent).create().get();
```

### restoreInstanceState

```java
Bundle savedInstanceState = new Bundle();
Activity activity = Robolectric.buildActivity(MyAwesomeActivity.class)
    .create()
    .restoreInstanceState(savedInstanceState)
    .get();
```

### visible()

如果你想要和 Activity 交互，例如点击按钮，你需要在 `create()` 之后调用 `visible()`，之后才能进行点击操作。

### 注意

如果用到以下库，需要在 build.gradle 添加对应的 robolectric 的依赖。

com.android.support.support-v4 -> org.robolectric:shadows-supportv4  
com.android.support.multidex -> org.robolectric:shadows-multidex  
com.google.android.gms:play-services -> org.robolectric:shadows-playservices  
org.apache.httpcomponents:httpclient -> org.robolectric:shadows-httpclient

### 创建自定义的 Shadow

```java
@Implements(Bitmap.class)
public class MyShadowBitmap {
    @RealObject
    private Bitmap realBitmap;
    private int bitmapQuality = -1;

    @Implementation
    public boolean compress(Bitmap.CompressFormat format, int quality, OutputStream stream) {
        bitmapQuality = quality;
        return realBitmap.compress(format, quality, stream);
    }

    public int getQuality() {
        return bitmapQuality;
    }
}
```

### 使用自定义的 Shadow

```java
@Config(shadows={MyShadowBitmap.class})

@Config(shadows={MyShadowBitmap.class, MyOtherCustomShadow.class})
```
