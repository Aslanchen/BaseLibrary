# BaseLibrary
[ ![Download](https://api.bintray.com/packages/aslanchen/AndroidLibrary/BaseLibrary/images/download.svg) ](https://bintray.com/aslanchen/AndroidLibrary/BaseLibrary/_latestVersion)
平常开发的基础框架

## 使用
```
implementation 'com.github.aslanchen:baselibrary:x.x.x'
```

## 上传
local.properties中填写
```
bintray.user=
bintray.apikey=
```

```
gradlew install
gradlew bintrayUpload
```

### 混淆(原始文件在工程下面)
1. proguard-flexible-adapter.pro
2. proguard-rules.pro
3. proguard-rules-eventbus.pro
4. proguard-rules-gson.pro
5. proguard-rules-okhttp.pro
6. proguard-rules-ormlite.pro
7. proguard-rules-retrofit.pro