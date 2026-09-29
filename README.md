# KeenLib

> A lightweight shared library for Huziyang520's Minecraft 26.3 mods · Fabric + NeoForge

KeenLib is a **shared library** used by Huziyang520's mods. It provides functions on **both the client and the server**, and may be installed on the client only. It extracts the boilerplate they would otherwise repeat: loader platform abstraction, JSON config handling, GUI helpers (including the "ESC means save" abstraction) and localization utilities with fallbacks. KeenLib does not change any vanilla behaviour and does nothing on its own.

---

## English

### Overview

| Capability | Entry point | Description |
|---|---|---|
| Platform abstraction | `platform.Services.PLATFORM` / `platform.services.IPlatformHelper` | Unified Fabric / NeoForge differences via `ServiceLoader`: platform name, mod-loaded check, dev-environment check, `config` directory |
| JSON config | `config.KeenConfig` | One instance per mod, reads/writes `config/<modId>.json`; typed `get*` / `set` with fault-tolerant loading (corrupt file never throws) |
| GUI helpers | `gui.AutoSaveScreen` / `gui.KeenScreenHelper` | Shared abstraction and close-path entry for "ESC saves and exits", to be reused by feature mods via Mixin |
| Localization | `text.KeenText` | Translatable component factory with `fallback` text, so the UI never shows a raw key |
| Logging | `Constants.LOG` | Single SLF4J logger entry |

### Compatibility

| Item | Value |
|---|---|
| Minecraft | 26.3 (`[26.3, 26.4)`) |
| Loaders | Fabric Loader `0.19.5` / NeoForge `26.3.0.1-beta` |
| Fabric API | `0.160.5+26.3` |
| Java | 25 |
| Side | **Client & server** (may be installed client-side only) |
| Dependencies | None |

### Installation

1. Install a Minecraft 26.3 instance with the matching loader.
2. Put the jar for your loader into the instance `mods` folder:
   - Fabric: `keenlib-0.1.2-fabric-26.3.jar`
   - NeoForge: `keenlib-0.1.2-neoforge-26.3.jar`
3. As a library it needs no configuration. Any mod that depends on it must be installed alongside it.

### For Developers

Put the KeenLib jar into your project's root `libs/` and declare a compile-time dependency (under Loom 1.17 `modImplementation(files(...))` does not work, use `compileOnly`):

```groovy
dependencies {
    // common / fabric / neoforge module, pick the jar for the loader
    compileOnly(rootProject.files("libs/keenlib-0.1.2-neoforge-26.3.jar"))
}
```

```java
KeenConfig config = KeenConfig.create("mymod");
boolean enabled = config.getBoolean("enabled", true);
config.set("enabled", false);
config.save();

MutableComponent text = KeenText.trans("gui.mymod.title", "My Mod");
boolean onFabric = "Fabric".equals(Services.PLATFORM.getPlatformName());
```

### Building

Requires JDK 25.

```bash
./gradlew :fabric:jar :neoforge:jar
```

Output goes to `<module>/build/libs/` as `<modId>-<version>-<loader>-<minecraft_version>.jar`.

### Links

- GitHub: <https://github.com/Huziyang520/KeenLib>
- Issues: <https://github.com/Huziyang520/KeenLib/issues>
- Backup feedback: <https://issue.mengcai.online/>

### License

Released under the [MIT License](LICENSE), Copyright (c) 2026 Huziyang520.

---

## 中文

### 简介

KeenLib 是 Huziyang520 系列模组共用的**通用共享库**，在**客户端与服务端均有功能**，且允许**仅客户端安装**。它把各功能模组反复重写的样板代码抽成公共能力：加载器平台抽象、JSON 配置读写、GUI 辅助（含「ESC 即保存」抽象）、本地化兜底工具。KeenLib 本身不改变任何原版行为，单独安装不会产生可观察效果。

### 功能特性

| 能力 | 入口类 | 说明 |
|---|---|---|
| 平台抽象 | `platform.Services.PLATFORM` / `platform.services.IPlatformHelper` | 用 ServiceLoader 统一 Fabric / NeoForge 差异：平台名、模组加载判定、开发环境判定、`config` 目录 |
| JSON 配置 | `config.KeenConfig` | 每个模组一个实例，读写实例 `config/<modId>.json`；带类型化 `get*` / `set` 与容错加载（文件损坏不抛异常） |
| GUI 辅助 | `gui.AutoSaveScreen` / `gui.KeenScreenHelper` | 「ESC 即保存并退出」的统一抽象与关闭路径入口，供功能模组以 Mixin 实现复用 |
| 本地化 | `text.KeenText` | 可翻译组件创建，支持 `fallback` 兜底文案，避免切语言后仍显示写死文本 |
| 日志 | `Constants.LOG` | 统一 SLF4J 日志入口 |

### 兼容性

| 项 | 值 |
|---|---|
| Minecraft | 26.3（`[26.3, 26.4)`） |
| 加载器 | Fabric Loader `0.19.5` / NeoForge `26.3.0.1-beta` |
| Fabric API | `0.160.5+26.3` |
| Java | 25 |
| 运行环境 | **客户端与服务端**（允许仅客户端安装） |
| 前置依赖 | 无 |

### 安装

1. 安装对应加载器版本的 Minecraft 26.3 实例。
2. 把对应加载器端的 jar 放入实例 `mods` 目录：
   - Fabric：`keenlib-0.1.2-fabric-26.3.jar`
   - NeoForge：`keenlib-0.1.2-neoforge-26.3.jar`
3. 作为前置库，本身无需任何配置；依赖它的功能模组需与本库**同时安装**。

### 面向开发者

把 KeenLib 的 jar 放进项目根 `libs/`，按加载器声明编译期依赖（Loom 1.17 下 `modImplementation(files(...))` 不可用，改用 `compileOnly`）：

```groovy
dependencies {
    // common / fabric / neoforge 对应模块，按加载器选 jar
    compileOnly(rootProject.files("libs/keenlib-0.1.2-neoforge-26.3.jar"))
}
```

```java
KeenConfig config = KeenConfig.create("mymod");
boolean enabled = config.getBoolean("enabled", true);
config.set("enabled", false);
config.save();

MutableComponent text = KeenText.trans("gui.mymod.title", "My Mod");
boolean onFabric = "Fabric".equals(Services.PLATFORM.getPlatformName());
```

### 构建

前置：JDK 25。

```bash
./gradlew :fabric:jar :neoforge:jar
```

产物输出在 `<module>/build/libs/`，命名规则：`<modId>-<version>-<loader>-<minecraft_version>.jar`。

### 链接

- GitHub：<https://github.com/Huziyang520/KeenLib>
- 问题反馈：<https://github.com/Huziyang520/KeenLib/issues>
- 备用反馈站：<https://issue.mengcai.online/>

### 许可证

本项目基于 [MIT License](LICENSE) 发布，Copyright (c) 2026 Huziyang520。
