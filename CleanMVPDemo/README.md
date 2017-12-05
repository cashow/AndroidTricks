# CleanMVPDemo

Clean 架构学习笔记

相关链接：

<https://github.com/android10/Android-CleanArchitecture>  
<https://fernandocejas.com/2014/09/03/architecting-android-the-clean-way/>  
<https://fernandocejas.com/2015/07/18/architecting-android-the-evolution/>  
<https://fernandocejas.com/2015/04/11/tasting-dagger-2-on-android/>  
<https://fernandocejas.com/2016/12/24/clean-architecture-dynamic-parameters-in-use-cases/>

-------

### 简介

Clean 架构的目的是通过项目分层，实现：

1. Frameworks独立；
2. 方便测试；
3. UI独立；
4. 数据库独立；
5. 所有的外部代理独立。

![clean_architecture1](https://fernandocejas.com/assets/migrated/clean_architecture1.png)

这里的依赖关系是：  
每一层的代码只能依赖于本层以及内部层的代码，不能对当前层之上的代码有依赖关系。

以下是图中各个名词的解释：

#### Entities: business objects of the application  
应用中用到的各种数据对象。  

#### Use Cases: orchestrate the flow of data to and from the entities  
对基础数据进行处理的实体，像是从网络或者数据库里获取数据之类的操作，都放在 Use Cases 里。Use Cases 也被称作 Interactors.  

#### Interface Adapters: This set of adapters convert data from the format most convenient for the use cases and entities. Presenters and Controllers belong here.
这里的 Adapter 将 Entities 或者 Use Cases 里的数据，转换成适合外部服务的数据，例如将 User 实例转换成适合数据库存储的结构、或者适合网络传输的结构。

#### Frameworks and Drivers: This is where all the details go: UI, tools, frameworks, etc.
最外层一般是数据库、网络框架、UI 框架等等，一般你不需要对这部分做任何改动，只需要将他们接入到应用内即可。

这一层是所有数据最终到达的地方，例如将数据传给服务器、或者保存到数据库、或者在 UI 上显示等等。之所以把这些放到最外层，是为了避免这一层实现的代码影响到内部层，将这一层的代码影响降到最低。

-------

### Presentation 层（实现层）

在这里存放所有和 views 以及 animations 相关的代码。你可以使用 MVP、 MVC 或者 MVVM 实现这一层。在这里，fragments 和 activities 都被当做是 views，在 views 里有且只有 UI 的逻辑, 所有的绘制工作都在这里实现。

这一层里的 Presenters (指的是 MVP 中的 Presenters) 都是通过运行于主线程之外的 interactors 去获取在 view 绘制过程中所需要的数据。

### Domain 层（接口适配层）

应用内数据处理的逻辑都在这里实现。所有的 interactors 都存放在这一层。

这一层应该是纯 java 的模块，不应该有任何 android 相关的代码。外层通过 interactors 获取数据时，只能通过定义好的 interfaces 去获取，这样的话外层代码就与 interactors 的具体实现解耦，即设计模式里的依赖倒置原则。

### Data 层（业务逻辑层）

所有应用内需要用到的数据都应该通过这一层的 UserRepository 去获取。这一层的 UserRepository 需要实现在 Domain 层定义的那些接口。UserRepository 会在不同的条件下从不同地方的数据源（网络、本地缓存等等）获取数据。这是设计模式中的仓库模式（Repository Pattern）。

例如，当通过 id 去获取用户信息时，如果有本地缓存，会直接从缓存中读取用户信息，否则需要通过网络接口去获取用户的信息，并将获取到的信息记录到缓存里。

----------

### 文件夹及各个文件的说明

```
data    // Clean 架构中的 Data 层
    cache  // 存放缓存相关的接口及实现
    entity  // 存放 Data 层的 Entities，以及用来将 Data 层数据转换成 Domain 层数据的转换器（Mapper）
    exception  // 存放自定义的 Exception
    executor  // 存放 ThreadPoolExecutor 的装饰器
    net  // 存放网络请求相关的类
    repository  // 存放 UserRepository 相关的类。这里需要实现 Domain 层定义的获取数据的接口。在这里，获取数据的方式可能是从本地缓存中获取，或者是通过网络请求去获取


domain    // Clean 架构中的 Domain 层
    exception  // 存放 Exception 的装饰器
    executor
        PostExecutionThread.java  // 用来切换 thread 的接口。如果要从后台线程切换到 UI 线程，需要实现这个接口
        ThreadExecutor.java  // 用来执行 UseCase 的执行器，在这里运行的代码是脱离于 UI 线程的
    interactor  // 存放 Use Cases，即 Interactors
    repository  // 在这里定义获取数据的接口，这个接口的实现是在 Data 层的 repository 文件夹里


presentation    // Clean 架构中的 Presentation 层
    common  // 公用的代码
    userdetail  // 用户详情页相关的代码
    userlist  // 用户列表页相关的代码
    UIThread.java  // PostExecutionThread 的实现，在这里返回的是 android 的主线程，即 AndroidSchedulers.mainThread()
```
