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

## TODO

// TODO: 2019/3/20 使用构建者模式增加更新参数配置，使用线程池

## LICENSE

[Apache2.0](https://github.com/xinpengfei520/pgyer-android-app-update/blob/master/LICENSE)