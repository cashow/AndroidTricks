# CancelDisposable

实现了一个自定义的编译时注解，只要给 Activity 或 Fragment 加上 @DisposableList 注解，在页面销毁时通过一行代码就可以把 Activity 或 Fragment 里所有的 Disposable 都取消掉。

假如你的页面里有多个 Disposable，正常情况下你需要在页面销毁时把每个 Disposable 都手动取消掉：

```java
public class MainActivity extends AppCompatActivity {
    Disposable disposable1;
    Disposable disposable2;
    Disposable disposable3;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (disposable1 != null && !disposable1.isDisposed()) {
            disposable1.dispose();
        }
        if (disposable2 != null && !disposable2.isDisposed()) {
            disposable2.dispose();
        }
        if (disposable3 != null && !disposable3.isDisposed()) {
            disposable3.dispose();
        }
    }
}
```

在给 MainActivity 加上 @DisposableList 注解后，注解解释器会自动帮你生成取消 Disposable 的代码，你只需要调用一行代码即可：

```java
@DisposableList
public class MainActivity extends AppCompatActivity {
    Disposable disposable1;
    Disposable disposable2;
    Disposable disposable3;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainActivityDisposableList.cancelAll(this);
    }
}
```

这是注解解释器生成的 MainActivityDisposableList.java 的代码：

```java
import io.reactivex.disposables.Disposable;

public class MainActivityDisposableList {
    public static void cancelAll(MainActivity mainActivity) {
        cancalDisposable(mainActivity.disposable1);
        cancalDisposable(mainActivity.disposable2);
        cancalDisposable(mainActivity.disposable3);
    }

    private static void cancalDisposable(Object object) {
        if (!(object instanceof Disposable)) {
            return;
        }
        Disposable disposable = (Disposable) object;
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
```
