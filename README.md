# KeenLib

> A shared library for Huziyang520's Minecraft 26.3 mods · Fabric + NeoForge

KeenLib provides functions on **both the client and the server**, and may be **installed on the client only**. It extracts the boilerplate Huziyang520's mods would otherwise repeat: loader platform abstraction, JSON config handling, a business-mod config registry with an **optional Cloth Config screen**, GUI helpers (including the "ESC means save" abstraction), localization utilities with fallbacks, and a **join-notice library** for chat messages.

---

## English

### Features

| Capability | Entry point | Description |
|---|---|---|
| Platform abstraction | `platform.Services.PLATFORM` / `platform.services.IPlatformHelper` | Unified Fabric / NeoForge differences via `ServiceLoader`: platform name, mod-loaded check, dev-environment check, `config` directory |
| JSON config | `config.KeenConfig` | One instance per mod, reads/writes `config/<modId>.json`; typed `get*` / `set` with fault-tolerant loading |
| Business config registry | `config.KeenConfigApi` / `KeenBusinessConfig` | Business mods register their own options in a few lines; KeenLib renders them. **No Cloth Config types are used by business mods** |
| Config screen (optional) | `client.KeenConfigScreenHook` | Renders everything with **Cloth Config** when installed. Two categories: **Business mod notice settings** (per-mod join-notice switches) and **Client-side business mod settings** (each mod's own options). Without Cloth Config there is simply no config screen |
| Notice settings | `notice.KeenNoticePreferences` | Per-mod on/off for join notices, edited from the config screen, stored in `config/keenlib.json` |
| Languages | `assets/keenlib/lang` | The config screen is fully localized (`en_us`, `zh_cn`); business mods supply their own keys |
| GUI helpers | `gui.AutoSaveScreen` / `gui.KeenScreenHelper` | Shared abstraction for "ESC saves and exits", reused by feature mods via Mixin |
| Join notices | `notice.KeenNoticeApi` | A mod registers a chat message; KeenLib sends it locally when the player enters a world (`EVERY_JOIN` or `ONCE_PER_WORLD`) |
| Localization | `text.KeenText` | Translatable component factory with `fallback` text |
| Logging | `Constants.LOG` | Single SLF4J logger entry |

### Optional integrations (never required)

| Mod | Version | What it adds | Behaviour when absent |
|---|---|---|---|
| **Cloth Config API** | `26.3.159` | The config screen itself | No config screen; all other features keep working |
| **Mod Menu** (Fabric only) | `21.0.0` | A "Config" button in the Fabric mod list | On Fabric, no in-game entry to the config screen |

Neither is declared as a dependency (`depends`); both are `compileOnly` at build time and checked at runtime.

### Compatibility

| Item | Value |
|---|---|
| Minecraft | 26.3 (`[26.3, 26.4)`) |
| Loaders | Fabric Loader `0.19.5` / NeoForge `26.3.0.1-beta` |
| Fabric API | `0.160.5+26.3` |
| Java | 25 |
| Side | **Client & server** (may be installed client-side only) |
| Dependencies | None |
| Optional | Cloth Config `26.3.159`, Mod Menu `21.0.0` (Fabric) |

### Installation

1. Install a Minecraft 26.3 instance with the matching loader.
2. Put the jar for your loader into the instance `mods` folder:
   - Fabric: `keenlib-0.2.1-fabric-26.3.jar`
   - NeoForge: `keenlib-0.2.1-neoforge-26.3.jar`
3. Optional: install **Cloth Config** to get a config screen, plus **Mod Menu** on Fabric for a config button.

### For Developers — config

Business mods never touch Cloth Config; they only describe their options:

```java
// In your client initialiser (safe even when Cloth Config is not installed)
KeenConfigApi.business("mymod", "My Mod")
        .booleanToggle("enabled", true,
                Component.literal("Enable feature"),
                Component.literal("Tooltip line"))
        .submit();   // writes defaults into config/mymod.json
```

The option then appears under **KeenLib → 纯客户端业务模组 → My Mod** in the config screen.

### For Developers — join notices

```java
KeenNoticeApi.register(
        Identifier.fromNamespaceAndPath("mymod", "welcome"),
        KeenNoticeMode.ONCE_PER_WORLD,
        Component.literal("My Mod is active"));
```

Notices are sent **locally on the client**, so client-only mods can use them too. `ONCE_PER_WORLD` remembers each world save / server address in `config/keenlib/notices.json`.

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

KeenLib 是 Huziyang520 系列模组共用的**通用共享库**，在**客户端与服务端均有功能**，且允许**仅客户端安装**。它把各功能模组反复重写的样板代码抽成公共能力：加载器平台抽象、JSON 配置读写、**业务模组配置注册（可选 Cloth Config 界面）**、GUI 辅助（含「ESC 即保存」抽象）、本地化兜底工具，以及**进入世界的聊天框提示库**。

### 功能特性

| 能力 | 入口类 | 说明 |
|---|---|---|
| 平台抽象 | `platform.Services.PLATFORM` / `platform.services.IPlatformHelper` | 用 ServiceLoader 统一 Fabric / NeoForge 差异：平台名、模组加载判定、开发环境判定、`config` 目录 |
| JSON 配置 | `config.KeenConfig` | 每个模组一个实例，读写 `config/<modId>.json`；带类型化 `get*` / `set` 与容错加载 |
| 业务配置注册 | `config.KeenConfigApi` / `KeenBusinessConfig` | 业务模组几行代码即可登记自己的选项，由 KeenLib 负责渲染；**业务模组不使用任何 Cloth Config 类型** |
| 配置界面（可选） | `client.KeenConfigScreenHook` | 装了 **Cloth Config** 时渲染界面。左侧两个分类：**业务模组通知设置**（各模组的进入世界提示开关）与**纯客户端业务模组启用设置**（各模组自己的选项）；不装则没有配置界面 |
| 通知开关 | `notice.KeenNoticePreferences` | 每个模组的进入世界提示开关，在配置界面里改，存 `config/keenlib.json` |
| 多语言 | `assets/keenlib/lang` | 配置界面文案全部走语言键（内置 `en_us` / `zh_cn`）；业务模组提供自己的键 |
| GUI 辅助 | `gui.AutoSaveScreen` / `gui.KeenScreenHelper` | 「ESC 即保存并退出」的统一抽象，供功能模组以 Mixin 复用 |
| 进入世界提示 | `notice.KeenNoticeApi` | 注册一条聊天框提示，玩家进入世界时由客户端本地发送（`EVERY_JOIN` / `ONCE_PER_WORLD`） |
| 本地化 | `text.KeenText` | 可翻译组件创建，支持 `fallback` 兜底文案 |
| 日志 | `Constants.LOG` | 统一 SLF4J 日志入口 |

### 可选联动（都不是依赖）

| 模组 | 版本 | 作用 | 未安装时 |
|---|---|---|---|
| **Cloth Config API** | `26.3.159` | 配置界面本体 | 没有配置界面，其余功能照常 |
| **Mod Menu**（仅 Fabric） | `21.0.0` | Fabric 模组列表里的「配置」按钮 | Fabric 侧没有打开配置界面的入口 |

两者都不写进 `depends`：构建期 `compileOnly`，运行期检测。

### 兼容性

| 项 | 值 |
|---|---|
| Minecraft | 26.3（`[26.3, 26.4)`） |
| 加载器 | Fabric Loader `0.19.5` / NeoForge `26.3.0.1-beta` |
| Fabric API | `0.160.5+26.3` |
| Java | 25 |
| 运行环境 | **客户端与服务端**（允许仅客户端安装） |
| 前置依赖 | 无 |
| 可选联动 | Cloth Config `26.3.159`、Mod Menu `21.0.0`（Fabric） |

### 安装

1. 安装对应加载器版本的 Minecraft 26.3 实例。
2. 把对应加载器端的 jar 放入实例 `mods` 目录：
   - Fabric：`keenlib-0.2.1-fabric-26.3.jar`
   - NeoForge：`keenlib-0.2.1-neoforge-26.3.jar`
3. 可选：装 **Cloth Config** 以获得配置界面；Fabric 端可再装 **Mod Menu** 获得配置入口。

### 面向开发者 —— 配置

业务模组不需要接触 Cloth Config，只描述自己的选项：

```java
// 在客户端初始化阶段调用（未安装 Cloth Config 时同样安全）
KeenConfigApi.business("mymod", "My Mod")
        .booleanToggle("enabled", true,
                Component.literal("启用功能"),
                Component.literal("提示文字"))
        .submit();   // 把默认值写入 config/mymod.json
```

之后该选项会出现在配置界面的 **KeenLib → 纯客户端业务模组 → My Mod** 二级菜单中。

### 面向开发者 —— 进入世界提示

```java
KeenNoticeApi.register(
        Identifier.fromNamespaceAndPath("mymod", "welcome"),
        KeenNoticeMode.ONCE_PER_WORLD,
        Component.literal("My Mod 已启用"));
```

提示由**客户端本地发送**，因此纯客户端模组也能使用；`ONCE_PER_WORLD` 会按世界（存档名 / 服务器地址）记录在 `config/keenlib/notices.json`。

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
