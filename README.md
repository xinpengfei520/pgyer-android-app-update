# pgyer-android-app-update

A light-weighted library which can update android app by pgyer app store.

## step 1:

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

## step 2:

```
implementation 'com.xsir:PgyerAndroidAppUpdate:<latest-version>'
```

Lastest version click to [here](https://github.com/xinpengfei520/pgyer-android-app-update/releases).

### step 3:

In MainActivity onCreate() method:

```
PgyerApi.update(this);
```
