# BaseLibrary

平常开发的基础框架

[![](https://jitpack.io/v/Aslanchen/BaseLibrary.svg)](https://jitpack.io/#Aslanchen/BaseLibrary)

## GitHub Actions

仓库已配置 `.github/workflows/android-publish.yml` 自动发布 Maven：

- 推送 `master` 分支时发布正式版本，不追加 `-SNAPSHOT`。
- 推送 `feature/**` 分支时发布 SNAPSHOT 版本，例如 `feature/v1.7.5` 发布为 `1.7.5-SNAPSHOT`。
- `master` 分支必须在当前提交上存在版本 tag，例如 `v1.7.5` 发布为 `1.7.5`。
- 本地构建默认使用模块 `build.gradle` 中写死的版本号，GitHub Actions 发布时会通过 `VERSION_NAME` Gradle property 覆盖 Maven 发布版本。
- GitHub Actions 仍通过 `maven-publish` 生成并推送 Maven 包，发布配置保留在 `baselibrary/build.gradle`。
- 其他分支和 tag 不触发 workflow。

发布到阿里云 Maven 前，需要在 GitHub 仓库 `Settings -> Secrets and variables -> Actions` 中配置：

```text
ALIYUN_MAVEN_USERNAME
ALIYUN_MAVEN_PASSWORD
```

## 使用

```
maven {
    url 'https://jitpack.io'
    credentials {
        username authToken
    }
}
        
implementation 'com.github.Aslanchen:BaseLibrary:x.x.x'
```
