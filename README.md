## ExPermission

一个权限申请库，简单易用
## 依赖
首先对该库进行依赖<br>
在项目的build.gradle文件中相应位置加入下列代码。
```groovy
    allprojects {
            repositories {
                //code ...
                maven { url 'https://jitpack.io' }
            }
    }
```
在项目app文件夹下的build.gradle文件中相应位置加入下列代码。
```groovy
    dependencies {
         implementation 'com.github.ErolC:ExPermission:1.0.1'
    }
```
## 使用
在kotlin语言中，有两种方式<br>
#### 第一种方式：拓展方式
你可以在有fragment或者FragmentActivity实例的地方通过：
```
fragment.pageUsePermissions()
activity.pageUserPermissions()
```
这两个方法设置当前需要的权限。<br>
然后通过【下面会省略调用对象】：
```
requestPermission()
```
方法请求权限<br>
最后在：
```
 onGranted {
    //已同意权限
    } onDenied {
    //未同意权限
    }

```

#### 第二种方式：链式
```
PermissionHandle build = PermissionHandle.Build()
            .with(this)
            .pageUserPermissions {
               
            }
            .setPermissionResult {//权限申请的结果反馈
                onDenied {//当拒绝权限时被调用
                }
                onGranted {//当权限通过时被调用
                }
            }
            .build()
```
然后就可以通过：
```      
    build.request()//请求权限
```

然而这两者是可以混合一起使用的，比如：
```
pageUsePermissions()//权限设置
PermissionHandle build = PermissionHandle.Build()
            .with(this)
            .setPermissionResult {//权限申请的结果反馈
                onDenied {//当拒绝权限时被调用
                }
                onGranted {//当权限通过时被调用
                }
            }
            .build()

build.request()//请求权限            

```
只要保证扩展的和链式的作用的是同一个activity或者fragment就好了。

### 最后
这里有例子，[传送门](https://github.com/ErolC/ExPermission/blob/master/app/src/main/java/com/erolc/expermission/MainActivity.kt)