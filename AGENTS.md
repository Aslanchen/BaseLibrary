# AGENTS.md

本文件用于指导 AI 编码代理在本仓库中工作。回答用户时使用中文。

## 项目概览

- 项目名：`BaseLibrary`
- 类型：Android Gradle 多模块项目
- 模块：
  - `baselibrary`：核心 Android 基础库，包名 `com.aslan.baselibrary`
  - `app`：示例/验证应用，包名 `com.aslan.app`
- 主要语言：Kotlin 与 Java 混合
- 构建系统：Gradle Wrapper
- Android Gradle Plugin：`8.13.2`
- Kotlin：`2.3.0`
- Gradle Wrapper：`8.14.4`
- Java/Kotlin JVM target：`21`
- SDK：
  - `compileSdk 35`
  - `targetSdk 33`
  - `baselibrary minSdk 19`
  - `app minSdk 21`

## 常用命令

在 Windows PowerShell 下优先使用：

```powershell
.\gradlew.bat build
.\gradlew.bat test
.\gradlew.bat :baselibrary:assembleRelease
.\gradlew.bat :app:assembleDebug
.\gradlew.bat publish
```

在 macOS/Linux 下使用：

```sh
./gradlew build
./gradlew test
./gradlew :baselibrary:assembleRelease
./gradlew :app:assembleDebug
./gradlew publish
```

发布相关配置在 `baselibrary/build.gradle` 和 `.github/workflows/android-publish.yml` 中。GitHub Actions 会根据分支或 tag 传入 Maven 发布版本。

## 目录说明

- `baselibrary/src/main/java/com/aslan/baselibrary/base`：Activity、Fragment、Dialog、列表页面、MVP、ViewBinding 基类
- `baselibrary/src/main/java/com/aslan/baselibrary/http`：Retrofit、OkHttp、RxJava2 相关网络封装
- `baselibrary/src/main/java/com/aslan/baselibrary/permissions`：权限请求封装
- `baselibrary/src/main/java/com/aslan/baselibrary/utils`：通用工具类
- `baselibrary/src/main/java/com/aslan/baselibrary/view`：自定义 View
- `baselibrary/src/main/java/com/aslan/baselibrary/widget`：通用控件与辅助组件
- `baselibrary/src/main/res`：库资源、布局、样式、权限相关 XML、FileProvider 路径等
- `app/src/main/java/com/aslan/app`：示例应用代码，演示库内基类、权限、输入控件、网络数据封装等用法

## 编码约定

- 保持现有 Kotlin/Java 混合风格，不要为了统一语言进行无关重写。
- 新增 Android 页面时优先使用现有 ViewBinding 基类，例如 `VBBaseActivity`、`VBBaseFragment` 及其列表派生类。
- 保持现有命名习惯：
  - 成员变量常见前缀为 `m`，例如 `mViewBinding`
  - 初始化方法沿用现有拼写，例如 `iniBundle`
  - 包名维持 `com.aslan.baselibrary` 或 `com.aslan.app`
- 公共库 API 修改要谨慎，优先保持二进制/源码兼容。
- 不要随意调整 `minSdk`、`targetSdk`、`compileSdk`、AGP、Kotlin、Gradle 版本，除非任务明确要求。
- 不要移除或替换现有依赖体系；本库对 AndroidX、Material、Retrofit、OkHttp、RxJava2、Rxlifecycle3、Glide、Gson、EventBus、MMKV、FlexibleAdapter、ORMLite、XLog、WorkManager 等有公开依赖。
- 修改资源时同步检查 `values`、`values-zh`、`values-night` 中是否需要配套变更。
- 修改 `AndroidManifest.xml`、`network_security_config.xml`、`file_provider_paths.xml` 时注意会影响下游接入方。

## 测试与验证

- 对通用库逻辑优先运行：

```powershell
.\gradlew.bat :baselibrary:test
```

- 对示例应用或跨模块改动运行：

```powershell
.\gradlew.bat build
```

- 对 UI、权限、文件安装、通知、下载等 Android 运行时行为，仅单元测试通常不足；应说明是否已在设备/模拟器上验证。
- 如果改动发布配置或混淆规则，至少执行对应模块的 release 构建：

```powershell
.\gradlew.bat :baselibrary:assembleRelease
.\gradlew.bat :app:assembleRelease
```

## 发布与凭据

- `baselibrary` 使用 `maven-publish`，包含 `maven` 与 `aliyun` 两个 publication。
- 阿里云 Maven 仓库凭据通过 `aliyun_maven_username`、`aliyun_maven_password` 读取，通常来自本地 Gradle 配置；不要把凭据写入仓库。
- `app` 中 `uploadBugly` 任务依赖本地 `JAVA_HOME` 和 Bugly 上传 jar；不要在无明确要求时修改 Bugly appid、appKey 或包名。
- `local.properties`、IDE 文件、构建产物不应作为功能改动提交。

## 变更边界

- 修改 `baselibrary` 时要考虑下游项目通过 JitPack 或 Maven 直接依赖该库。
- `app` 模块主要用于演示和手动验证；除非任务要求，不要把业务逻辑放入 `app` 后宣称库功能已完成。
- 遇到旧式 API 或过时依赖时，除非任务是升级维护，否则只做完成当前目标所需的最小调整。
- 保留现有 ProGuard consumer rules；新增依赖或反射/序列化能力时补充对应混淆规则。
- 不要批量格式化全仓库；只格式化或调整本次修改相关文件。

## 代理工作流程

1. 先阅读相关模块的 `build.gradle`、源码和资源，确认影响范围。
2. 优先使用 `rg` / `rg --files` 搜索文件和引用。
3. 改动前检查 `git status --short`，避免覆盖用户已有修改。
4. 实现时保持改动聚焦，避免无关重构。
5. 修改后运行与改动范围匹配的 Gradle 命令；如果无法运行，明确说明原因。
6. 最终回复中说明改动文件、验证命令与结果。
