# RxJavaLearning

本文是学习RxJava过程中做的一些笔记，内容是从以下几个博客和项目整理而来。  
[ReactiveX官网](http://reactivex.io/)  
[深入浅出RxJava(一：基础篇)](http://blog.csdn.net/lzyzsd/article/details/41833541>)  
[深入浅出RxJava(二：操作符)](http://blog.csdn.net/lzyzsd/article/details/44094895)  
[深入浅出RxJava三--响应式的好处](http://blog.csdn.net/lzyzsd/article/details/44891933)  
[深入浅出RxJava四-在Android中使用响应式编程](http://blog.csdn.net/lzyzsd/article/details/45033611)  
[给 Android 开发者的 RxJava 详解](http://gank.io/post/560e15be2dca930e00da1083)  
[Github项目：RxJava-Android-Samples](https://github.com/kaushikgopal/RxJava-Android-Samples)  
[101 Rx Samples](http://rxwiki.wikidot.com/101samples)  
[RxJava操作符（一）Creating Observables ](http://blog.chinaunix.net/uid-20771867-id-5187376.html)  
[知乎：谁来讲讲Rxjava、rxandroid中的操作符的作用?](https://www.zhihu.com/question/32209660)  

***
RxJava最核心的是Observables（被观察者，事件源）和Subscribers（观察者）。Observables发出一系列事件，Subscribers处理这些事件。  

### 创建一个Observable
```java
Observable<String> myObservable = Observable.create(
    new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> sub) {
            sub.onNext("Hello, world!");
            sub.onCompleted();
        }
    }
);
```
这里定义的myObservable是给所有的Subscriber发出一个Hello World字符串。  

### 创建一个Subscriber
```java
Subscriber<String> mySubscriber = new Subscriber<String>() {
    @Override
    public void onNext(String s) { System.out.println(s); }

    @Override
    public void onCompleted() { }

    @Override
    public void onError(Throwable e) { }
};
```
这里的mySubscriber仅仅就是打印observable发出的字符串。  
如果只需要在onNext，onError或者onCompleted的时候做一些处理，可以使用Action0和Action1类。  
Action0是RxJava的一个接口，它只有一个方法call()，这个方法是无参无返回值的。  
Action1也是一个接口，它同样只有一个方法call(T param)，这个方法也无返回值，但有一个参数。  
```java
Action1<String> onNextAction = new Action1<String>() {
    // onNext()
    @Override
    public void call(String s) {
        Log.d(tag, s);
    }
};
Action1<Throwable> onErrorAction = new Action1<Throwable>() {
    // onError()
    @Override
    public void call(Throwable throwable) {
        // Error handling
    }
};
Action0 onCompletedAction = new Action0() {
    // onCompleted()
    @Override
    public void call() {
        Log.d(tag, "completed");
    }
};

```

### mySubscriber订阅myObservable
mySubscriber订阅myObservable后，Observable每发出一个事件，就会调用它的Subscriber的onNext()。如果出错，会调用Subscriber的onError()。当不会再有新的onNext()发出时，会调用onCompleted()方法。  
subscribe方法有一个重载版本，接受Action0和Action1类型的参数，分别对应OnNext，OnComplete， OnError函数。  
```java
// mySubscriber订阅myObservable
myObservable.subscribe(mySubscriber);
// 自动创建Subscriber，并使用onNextAction来定义onNext()
observable.subscribe(onNextAction);
// 自动创建Subscriber，并使用onNextAction和onErrorAction来定义onNext()和onError()
observable.subscribe(onNextAction, onErrorAction);
// 自动创建Subscriber，并使用onNextAction、onErrorAction和onCompletedAction来定义onNext()、onError()和onCompleted()
observable.subscribe(onNextAction, onErrorAction, onCompletedAction);
```
***

## 创建Observable的操作符：

### create操作符
创建一个自定义的Observable
```java
Observable<String> myObservable = Observable.create(
    new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> sub) {
            sub.onNext("Hello, world!");
            sub.onCompleted();
        }
    }
);
```

### defer操作符
和其他创建Observable的操作符不同，Defer操作符在有observer订阅时才会创建Observable，并且为每一个observer创建一个全新的Observable。  
```java
public class SomeType {  
    private String value;

    public void setValue(String value) {
        this.value = value;
    }

    public Observable<String> valueObservable() {
        return Observable.just(value);
    }
}

// 执行以下代码，会打印出null，而不是"Some Value"
// 原因就是在用just创建Observable时，Observable已经将value的值保存下来了
// 因此在subscribe时value的值是null
SomeType instance = new SomeType();  
Observable<String> value = instance.valueObservable();  
instance.setValue("Some Value");  
value.subscribe(System.out::println);  

// 使用defer，可在每次被订阅时再创建Observable
Observable.defer(() -> Observable.just(value));
```
详细说明可查看：[Deferring Observable code until subscription in RxJava](http://blog.danlew.net/2015/07/23/deferring-observable-code-until-subscription-in-rxjava/)


### just操作符
just将传入的数据依次发出。  
```java
Observable observable = Observable.just("Hello", "Hi", "Aloha");
// 将会依次调用：
// onNext("Hello");
// onNext("Hi");
// onNext("Aloha");
// onCompleted();
```

### from操作符
from操作符接收一个集合作为输入，每次输出一个元素给subscriber
```java
String[] words = {"Hello", "Hi", "Aloha"};
Observable observable = Observable.from(words);
// 将会依次调用：
// onNext("Hello");
// onNext("Hi");
// onNext("Aloha");
// onCompleted();
```

### interval操作符
每隔一段时间发送一个从0开始递增的数字。  
```java
// 每隔500ms发送一个数字，依次是0，1，2，3，4...
// 这个操作会无限进行下去，所以需要手动取消掉这个subscription
Observable.interval(500, TimeUnit.MILLISECONDS);
```

### range操作符
range(n, m)会依次发出m个数据，数据从n开始递增，如n, n+1, n+2 ... n+m-1
```java
// 依次发出10到24
Observable.range(10, 15);
```

### repeat操作符
将数据重复发送几次
```java
// 发出的数据是：0, 1, 2, 0, 1, 2, 0, 1, 2
Observable.range(0, 3)
    .repeat(3);
```
***

### map操作符
map操作符用来把Observable传来的数据转换成另一个数据。
```java
Observable.just("Hello, world!")
	.map(new Func1<String, String>() {
	  @Override
	  public String call(String s) {
	      return s + " -Dan";
	  }
})
.subscribe(s -> System.out.println(s));

//使用lambda表达式简化后：
Observable.just("Hello, world!")
    .map(s -> s + " -Dan")
    .subscribe(s -> System.out.println(s));
```

### flatMap操作符
flatMap将Observable的数据转换成一个或多个Observable。  
```java
// 假设有个函数根据输入的字符串返回一个url列表：
Observable<List<String>> query(String text){
	// ...
}

// 现在需要查询"Hello, world!"字符串并且显示结果
query("Hello, world!")
    .flatMap(new Func1<List<String>, Observable<String>>() {
        @Override
        public Observable<String> call(List<String> urls) {
            return Observable.from(urls);
        }
    })
    .subscribe(url -> System.out.println(url));

//使用lambda表达式简化后：
query("Hello, world!")
    .flatMap(urls -> Observable.from(urls))
    .subscribe(url -> System.out.println(url));
```

### filter操作符
filter把输入的数据进行过滤，然后输出符合条件的数据。  
```java
//过滤title为null的数据
query("Hello, world!")
	.flatMap(urls -> Observable.from(urls))
	.flatMap(url -> getTitle(url))
	.filter(title -> title != null)
	.subscribe(title -> System.out.println(title));
```

### take操作符
take指定最多输出多少个结果。
```java
query("Hello, world!")
    .flatMap(urls -> Observable.from(urls))
    .flatMap(url -> getTitle(url))
    .filter(title -> title != null)
    .take(5)
    .subscribe(title -> System.out.println(title));
```

### doOnNext操作符
doOnNext允许我们在每次输出一个元素之前做一些额外的事情，比如这里的保存标题。
```java
query("Hello, world!")
    .flatMap(urls -> Observable.from(urls))
    .flatMap(url -> getTitle(url))
    .filter(title -> title != null)
    .take(5)
    .doOnNext(title -> saveTitle(title))
    .subscribe(title -> System.out.println(title));
```

### buffer操作符
周期性地把Observable的数据合并成列表，并在一定时间后将列表传给Observer
```java
// 每2秒更新一次在这期间view的点击次数
RxView.clickEvents(_tapBtn)
      .map(new Func1<ViewClickEvent, Integer>() {
          @Override
          public Integer call(ViewClickEvent onClickEvent) {
              _log("GOT A TAP");
              return 1;
          }
      })
      .buffer(2, TimeUnit.SECONDS)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Observer<List<Integer>>() {

          @Override
          public void onCompleted() {
              // fyi: you'll never reach here
          }

          @Override
          public void onError(Throwable e) {
              _log("Dang error! check your logs");
          }

          @Override
          public void onNext(List<Integer> integers) {
              Timber.d("--------- onNext");
              if (integers.size() > 0) {
                  _log(String.format("%d taps", integers.size()));
              }
          }
      });
```

### debounce操作符
在一次事件发生后的一段时间内没有其他的操作，则发出这次事件
```java
// 在textChangeEvents发生后的400ms内的没有收到其他的textChangeEvents事件，则发出这次事件
RxTextView.textChangeEvents(_inputSearchText)
          .debounce(400, TimeUnit.MILLISECONDS)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(_getSearchObserver());
```

### throttleFirst操作符
在每次事件触发后的一定时间间隔内丢弃新的事件。常用作去抖动过滤，例如按钮的点击监听器：  
```java
RxView.clickEvents(button)
    .throttleFirst(500, TimeUnit.MILLISECONDS) // 设置防抖间隔为 500ms
    .subscribe(subscriber);
```
***

### 错误处理
代码中的potentialException() 和 anotherPotentialException()有可能会抛出异常。每一个Observerable对象在终结的时候都会调用onCompleted()或者onError()方法，所以以下代码会打印出”Completed!”或者”Ouch!”。
```java
Observable.just("Hello, world!")
    .map(s -> potentialException(s))
    .map(s -> anotherPotentialException(s))
    .subscribe(new Subscriber<String>() {
        @Override
        public void onNext(String s) { System.out.println(s); }

        @Override
        public void onCompleted() { System.out.println("Completed!"); }

        @Override
        public void onError(Throwable e) { System.out.println("Ouch!"); }
    });
```
这种模式有以下几个优点：  
1.只要有异常发生onError()一定会被调用  
这极大的简化了错误处理。只需要在一个地方处理错误即可以。  
2.操作符不需要处理异常  
将异常处理交给订阅者来做，Observerable的操作符调用链中一旦有一个抛出了异常，就会直接执行onError()方法。  
3.你能够知道什么时候订阅者已经接收了全部的数据。  
知道什么时候任务结束能够帮助简化代码的流程。（虽然有可能Observable对象永远不会结束）

***

### 订阅关系Subscription
调用Observable.subscribe()会返回一个Subscription对象，这个对象代表了被观察者和订阅者之间的联系。  
```java
Subscription subscription = Observable.just("Hello, World!")
    .subscribe(s -> System.out.println(s));
```
你可以在后面使用这个Subscription对象来操作被观察者和订阅者之间的联系。
```java
subscription.unsubscribe();
System.out.println("Unsubscribed=" + subscription.isUnsubscribed());
// Outputs "Unsubscribed=true"
```
RxJava在处理unsubscribing的时候，会停止整个调用链。如果你使用了一串很复杂的操作符，调用unsubscribe将会在他当前执行的地方终止，不需要做任何额外的工作！  
一般在unsubscribe()调用前，可以使用isUnsubscribed()先判断一下状态。

### 取消所有的订阅
使用CompositeSubscription来持有所有的Subscriptions，然后在onDestroy()或者onDestroyView()里取消所有的订阅。
```java
private CompositeSubscription mCompositeSubscription
    = new CompositeSubscription();

private void doSomething() {
    mCompositeSubscription.add(
        AndroidObservable.bindActivity(this, Observable.just("Hello, World!"))
        .subscribe(s -> System.out.println(s)));
}

@Override
protected void onDestroy() {
    super.onDestroy();

    mCompositeSubscription.unsubscribe();
}
```
***

### 利用compose实现链式调用的复用
假设在程序中有多个 Observable ，并且他们都需要应用一组相同的变换：
```java
observable1
    .lift1()
    .lift2()
    .lift3()
    .lift4()
    .subscribe(subscriber1);
observable2
    .lift1()
    .lift2()
    .lift3()
    .lift4()
    .subscribe(subscriber2);
observable3
    .lift1()
    .lift2()
    .lift3()
    .lift4()
    .subscribe(subscriber3);
observable4
    .lift1()
    .lift2()
    .lift3()
    .lift4()
    .subscribe(subscriber1);
```
使用 compose() 方法，Observable 可以利用传入的 Transformer 对象的 call 方法直接对自身进行处理。
```java
public class LiftAllTransformer implements Observable.Transformer<Integer, String> {
    @Override
    public Observable<String> call(Observable<Integer> observable) {
        return observable
            .lift1()
            .lift2()
            .lift3()
            .lift4();
    }
}
...
Transformer liftAll = new LiftAllTransformer();
observable1.compose(liftAll).subscribe(subscriber1);
observable2.compose(liftAll).subscribe(subscriber2);
observable3.compose(liftAll).subscribe(subscriber3);
observable4.compose(liftAll).subscribe(subscriber4);
```
***

### Scheduler调度器
在不指定线程的情况下， RxJava 遵循的是线程不变的原则，即：在哪个线程调用 subscribe()，就在哪个线程生产事件；在哪个线程生产事件，就在哪个线程消费事件。如果需要切换线程，就需要用到 Scheduler （调度器）。  
RxJava通过Scheduler来指定每一段代码应该运行在什么样的线程。RxJava 已经内置了几个 Scheduler：  
```java
// 直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler。
Schedulers.immediate()

// 总是启用新线程，并在新线程执行操作。
Schedulers.newThread()

// I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。行为模式和 newThread() 差不多，区别在于 io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率。不要把计算工作放在 io() 中，可以避免创建不必要的线程。  
Schedulers.io()

// 计算所使用的 Scheduler。这个计算指的是 CPU 密集型计算，即不会被 I/O 等操作限制性能的操作，例如图形的计算。这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。不要把 I/O 操作放在 computation() 中，否则 I/O 操作的等待时间会浪费 CPU。  
Schedulers.computation()

// Android专用，指定操作在Android主线程运行。
AndroidSchedulers.mainThread()
```

有了这几个 Scheduler ，就可以使用 subscribeOn() 和 observeOn() 两个方法来对线程进行控制了。  

#### subscribeOn():
指定 subscribe() 所发生的线程，即 Observable.OnSubscribe 被激活时所处的线程。或者叫做事件产生的线程。  

#### observeOn():
指定 Subscriber 所运行在的线程。或者叫做事件消费的线程。  
代码示例：  
```java
Observable.just(1, 2, 3, 4)
    .subscribeOn(Schedulers.io()) // 指定subscribe()发生在IO线程
    .observeOn(AndroidSchedulers.mainThread()) // 指定Subscriber的回调发生在主线程
    .subscribe(new Action1<Integer>() {
        @Override
        public void call(Integer number) {
            Log.d(tag, "number:" + number);
        }
    });
```
observeOn() 指定的是它之后的操作所在的线程。因此如果有多次切换线程的需求，只要在每个想要切换线程的位置调用一次 observeOn() 即可。  
```java
Observable.just(1, 2, 3, 4) // IO 线程，由 subscribeOn() 指定
    .subscribeOn(Schedulers.io())
    .observeOn(Schedulers.newThread())
    .map(mapOperator) // 新线程，由 observeOn() 指定
    .observeOn(Schedulers.io())
    .map(mapOperator2) // IO 线程，由 observeOn() 指定
    .observeOn(AndroidSchedulers.mainThread)
    .subscribe(subscriber);  // Android 主线程，由 observeOn() 指定
```
不同于observeOn()，subscribeOn()的位置放在哪里都可以，但它是只能调用一次的。
