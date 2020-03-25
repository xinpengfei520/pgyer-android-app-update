# pgyer-android-app-update

A light-weighted library which can update android app by pgyer App Store.

[Download Demo](https://www.pgyer.com/android_app_update)

## 1.Usage

### step 1:

add below code in your app *AndroidManifest.xml* file.

```
    <!-- 蒲公英 apiKey -->
    <meta-data
        android:name="PGYER_API_KEY"
        android:value="your app pgyer apiKey" />
    <!-- 蒲公英 appKey -->
    <meta-data
        android:name="PGYER_APP_KEY"
        android:value="your app pgyer apiKey" />
```

### step 2:

```
implementation 'com.xsir:PgyerAndroidAppUpdate:<latest-version>'
```

Lastest version click to [here](https://github.com/xinpengfei520/pgyer-android-app-update/releases).

### step 3:

In MainActivity onCreate() method:

```
PgyerApi.checkUpdate(this);
```

## 2.Update Log

### v0.9.4

- 自定义 FileProvider 防止和集成 app 的 FileProvider 冲突；
- 保证 FileProvider authorities 的唯一性；
- 抽取几个常量类和工具类；

### v0.9.6

- fix bug；
- 不能使用固定的 authority，否则通一个手机上无法安装 2 个使用了本库的 APP；

### v1.0.0

- 使用 okhttp3 作为网络请求；
- 修复请求动态权限有时会出现状态栏白屏的 bug；
- 其他优化；

### v1.0.1

- 修复bug，不判断响应信息判断 message；

## FAQ

okhttp3 依赖库冲突；

内部依赖了 **okhttp:4.1.0**，如果你们的项目也依赖了 okhttp 可能会导致依赖冲突，可以将你们项目中 okhttp 的版本一致或者
使用 exclude 将其剔除即可：

```
implementation('com.xsir:PgyerAndroidAppUpdate:1.0.1', {
    exclude group: 'com.squareup.okhttp3'
})
```

## TODO

// TODO: 2019/3/20 使用构建者模式增加更新参数配置

## LICENSE

[Apache2.0](https://github.com/xinpengfei520/pgyer-android-app-update/blob/master/LICENSE)