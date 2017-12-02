# LiveDataDemo

LiveData 是一个可以感知生命周期的组件，当指定的 LifeCycle 在 STARTED 或者 RESUMED 状态时，LiveData 会将观察者视为活动状态，并通知其数据的变化。当指定的 LifeCycle 在 DESTROYED 状态时，LiveData 会自动进行清理。


官方文档：<https://developer.android.com/topic/libraries/architecture/livedata.html>

------

### 引入

root目录的 build.gradle加上：

<pre>
allprojects {
    repositories {
        jcenter()
        maven { url 'https://maven.google.com' }
    }
}
</pre>

项目里的build.gradle加上：

<pre>
implementation "android.arch.lifecycle:extensions:1.0.0"

// 如果你需要使用 ReactiveStreams 的 API，需要引入 reactivestreams
implementation "android.arch.lifecycle:reactivestreams:1.0.0"
</pre>

------

### 创建 LiveData 对象

LiveData 对象一般是存储在 ViewModel 里，通过 getter 方法获取，如下方所示：
```java
public class NameViewModel extends ViewModel {
    private MutableLiveData<String> mCurrentName;

    public MutableLiveData<String> getCurrentName() {
        if (mCurrentName == null) {
            mCurrentName = new MutableLiveData<>();
        }
        return mCurrentName;
    }
}
```

### 监测数据变化

LiveData 只在数据发生变化时对仍然活跃的观察者发送更新。当观察者从不活跃状态变成活跃状态时，观察者也会收到数据更新的提醒。以下代码是观察 LiveData 对象的示例：

```java
public class MainActivity extends AppCompatActivity {
    private TextView textview;
    private Button buttonChangeName;

    private NameViewModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        mModel = ViewModelProviders.of(this).get(NameViewModel.class);
        mModel.getCurrentName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String newName) {
                textview.setText(newName);
            }
        });
    }

    private void initView() {
        textview = findViewById(R.id.textview);
        buttonChangeName = findViewById(R.id.button_change_name);

        buttonChangeName.setOnClickListener(v -> {
            String newName = "name_" + System.currentTimeMillis() % 1000;
            mModel.getCurrentName().setValue(newName);
        });
    }
}
```

在 observe() 方法被调用时，onChanged() 会立即被调用，并将最新的 newName 传过来。如果 LiveData 还没有设置过值，onChanged() 不会被调用。

### 更新 LiveData 的数据

LiveData 没有公开的方法去更新数据。如果你需要更新 LiveData 的数据，你必须使用 MutableLiveData 提供的 setValue(T) 和 postValue(T) 方法。

```java
buttonChangeName.setOnClickListener(v -> {
    String newName = "name_" + System.currentTimeMillis() % 1000;
    mModel.getCurrentName().setValue(newName);
});
```

### 实现自定义的 LiveData

以下代码实现了一个自定义的 LiveData：

```java
public class StockLiveData extends LiveData<Long> {
    private StockManager mStockManager;

    private static StockLiveData sInstance;

    private SimplePriceListener mListener = price -> setValue(price);

    public static StockLiveData get() {
        if (sInstance == null) {
            sInstance = new StockLiveData();
        }
        return sInstance;
    }

    private StockLiveData() {
        mStockManager = new StockManager();
    }

    @Override
    protected void onActive() {
        mStockManager.requestPriceUpdates(mListener);
    }

    @Override
    protected void onInactive() {
        mStockManager.removeUpdates();
    }
}
```

当 LiveData 有了一个活跃的观察者时，onActive() 会被调用。这个时候你应该去监控数据的变化。

当 LiveData 没有任何活跃的观察者时，onInactive() 会被调用。这时你可以去做一些清理工作。

你可以通过调用 setValue(T) 方法去更新 LiveData 里的数据。

StockLiveData 的使用示例：

```java
StockLiveData.get().observe(this, integer -> textview.setText(String.valueOf(integer)));
```
