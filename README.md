# KeenLib

> A shared library for Huziyang520's Minecraft mods · Fabric + NeoForge

KeenLib provides functions on **both the client and the server**, and may be **installed on the client only**. It extracts the boilerplate Huziyang520's mods would otherwise repeat: loader platform abstraction, JSON config handling, a business-mod config registry rendered on the loader's **native configuration screen**, GUI helpers (including the "ESC means save" abstraction), localization utilities with fallbacks, and a **join-notice library** for chat messages.

---

## English

### Features

| Capability | Entry point | Description |
|---|---|---|
| Platform abstraction | `platform.Services.PLATFORM` / `platform.services.IPlatformHelper` | Unified Fabric / NeoForge differences via `ServiceLoader`: platform name, mod-loaded check, dev-environment check, `config` directory |
| JSON config | `config.KeenConfig` | One instance per mod, reads/writes `config/<modId>.json`; typed `get*` / `set` with fault-tolerant loading |
| Business config registry | `config.KeenConfigApi` / `KeenBusinessConfig` | Business mods register their own options in a few lines; KeenLib turns them into a config spec and attaches the native screen. **No UI types leak into business mods** |
| Config screens | `client.KeenConfigScreenHook` | Every screen is the loader's **native** one (NeoForge built-in; on Fabric through Forge Config API Port; on 1.20.1 through Neo Configuration Screen Port). Each business mod's entry opens its own options, and its join-notice switch lives there too |
| Notice settings | `notice.KeenNoticePreferences` | Per-mod on/off for join notices, edited from the config screen, stored in `config/keenlib.json` |
| Languages | `assets/keenlib/lang` | The config screen is fully localized (`en_us`, `zh_cn`); business mods supply their own keys |
| GUI helpers | `gui.AutoSaveScreen` / `gui.KeenScreenHelper` | Shared abstraction for "ESC saves and exits", reused by feature mods via Mixin |
| Join notices | `notice.KeenNoticeApi` | A mod registers a chat message; KeenLib sends it locally when the player enters a world (`EVERY_JOIN` or `ONCE_PER_WORLD`) |
| Content registration | `registry.KeenRegistry` / `platform.services.IRegistryHelper` | Items, sound events, data component types and creative tabs registered the same way on Fabric and NeoForge |
| Localization | `text.KeenText` | Translatable component factory with `fallback` text |
| Logging | `Constants.LOG` | Single SLF4J logger entry |

### Optional integrations (never required)

| Mod | What it adds | Behaviour when absent |
|---|---|---|
| **Mod Menu** (Fabric only) | A "Config" button in the Fabric mod list | On Fabric, no in-game entry to the config screen |
| **Forge Config API Port** (Fabric only) | The native configuration screen and the config backend behind it | No config screen (options stay at their defaults) |

Neither is declared as a dependency (`depends`); both are `compileOnly` at build time and checked at runtime.

### Compatibility

| Item | Value |
|---|---|
| Side | **Client & server** (may be installed client-side only) |
| Dependencies | None |
| Optional | Mod Menu (Fabric), Forge Config API Port (Fabric), Neo Configuration Screen Port (Forge) |

### Installation

1. Install a Minecraft instance with the loader this build targets (see the file name).
2. Put the jar for your loader into the instance `mods` folder.
3. Optional: on Fabric install **Forge Config API Port** and on 1.20.1 **Neo Configuration Screen Port** for the config screens (NeoForge needs nothing extra); **Mod Menu** (Fabric) adds the buttons in the mod list.

### For Developers — config

Business mods never touch a screen class; they only describe their options:

```java
// In your mod constructor (the screen backend needs the spec registered there)
KeenConfigApi.business("mymod", "My Mod")
        .booleanToggle("enabled", true,
                Component.literal("Enable feature"),
                Component.literal("Tooltip line"))
        .submit();   // registers the options; values live in config/mymod-client.toml
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

### Bundled library

The jar also ships **Camelot**, a shared runtime library maintained by the same author (MIT). See `NOTICE`.

### Links

- GitHub: <https://github.com/Huziyang520/KeenLib>
- Issues: <https://github.com/Huziyang520/KeenLib/issues>
- Backup feedback: <https://issue.mengcai.online/>

### License

Released under [CC0 1.0 Universal](https://github.com/Huziyang520/KeenLib/blob/26.X/LICENSE).

---

## 中文

### 简介

KeenLib 是 Huziyang520 系列模组共用的**通用共享库**，在**客户端与服务端均有功能**，且允许**仅客户端安装**。它把各功能模组反复重写的样板代码抽成公共能力：加载器平台抽象、JSON 配置读写、**业务模组配置注册（渲染到加载器原生配置界面）**、GUI 辅助（含「ESC 即保存」抽象）、本地化兜底工具，以及**进入世界的聊天框提示库**。

### 功能特性

| 能力 | 入口类 | 说明 |
|---|---|---|
| 平台抽象 | `platform.Services.PLATFORM` / `platform.services.IPlatformHelper` | 用 ServiceLoader 统一 Fabric / NeoForge 差异：平台名、模组加载判定、开发环境判定、`config` 目录 |
| JSON 配置 | `config.KeenConfig` | 每个模组一个实例，读写 `config/<modId>.json`；带类型化 `get*` / `set` 与容错加载 |
| 业务配置注册 | `config.KeenConfigApi` / `KeenBusinessConfig` | 业务模组几行代码即可登记自己的选项，由 KeenLib 生成 spec 并挂上原生界面；**业务模组不接触任何界面类型** |
| 配置界面 | `client.KeenConfigScreenHook` | 全部使用加载器**原生**界面（NeoForge 自带；Fabric 经 Forge Config API Port；1.20.1 经 Neo Configuration Screen Port）。每个业务模组的条目打开它自己的选项，进入世界提示开关也在这里 |
| 通知开关 | `notice.KeenNoticePreferences` | 每个模组的进入世界提示开关，在配置界面里改，存 `config/keenlib.json` |
| 多语言 | `assets/keenlib/lang` | 配置界面文案全部走语言键（内置 `en_us` / `zh_cn`）；业务模组提供自己的键 |
| GUI 辅助 | `gui.AutoSaveScreen` / `gui.KeenScreenHelper` | 「ESC 即保存并退出」的统一抽象，供功能模组以 Mixin 复用 |
| 进入世界提示 | `notice.KeenNoticeApi` | 注册一条聊天框提示，玩家进入世界时由客户端本地发送（`EVERY_JOIN` / `ONCE_PER_WORLD`） |
| 内容注册 | `registry.KeenRegistry` / `platform.services.IRegistryHelper` | 物品、声音事件、数据组件与创造模式标签页在 Fabric / NeoForge 上用同一套调用注册 |
| 本地化 | `text.KeenText` | 可翻译组件创建，支持 `fallback` 兜底文案 |
| 日志 | `Constants.LOG` | 统一 SLF4J 日志入口 |

### 可选联动（都不是依赖）

| 模组 | 作用 | 未安装时 |
|---|---|---|
| **Mod Menu**（仅 Fabric） | Fabric 模组列表里的「配置」按钮 | Fabric 侧没有打开配置界面的入口 |
| **Forge Config API Port**（仅 Fabric） | 原生配置界面与其配置后端 | 没有界面（选项保持默认值） |

两者都不写进 `depends`：构建期 `compileOnly`，运行期检测。

### 兼容性

| 项 | 值 |
|---|---|
| 运行环境 | **客户端与服务端**（允许仅客户端安装） |
| 前置依赖 | 无 |
| 可选联动 | Mod Menu（Fabric）、Forge Config API Port（Fabric）、Neo Configuration Screen Port（Forge） |

### 安装

1. 安装与本构建对应的 Minecraft 实例（版本见文件名）。
2. 把对应加载器端的 jar 放入实例 `mods` 目录。
3. 可选：Fabric 端装 **Forge Config API Port**、1.20.1 装 **Neo Configuration Screen Port** 以获得配置界面（NeoForge 端无需额外前置）；**Mod Menu**（Fabric）提供模组列表里的配置按钮。

### 面向开发者 —— 配置

业务模组不需要接触任何界面类，只描述自己的选项：

```java
// 在模组构造期调用（spec 必须在这个时机注册）
KeenConfigApi.business("mymod", "My Mod")
        .booleanToggle("enabled", true,
                Component.literal("启用功能"),
                Component.literal("提示文字"))
        .submit();   // 登记选项；值落在 config/mymod-client.toml
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

### 内置库

本 jar 同时内置 **Camelot**（同作者维护的共享运行库，MIT 许可），详见 `NOTICE`。

### 链接

- GitHub：<https://github.com/Huziyang520/KeenLib>
- 问题反馈：<https://github.com/Huziyang520/KeenLib/issues>
- 备用反馈站：<https://issue.mengcai.online/>

### 许可证

本项目基于 [CC0 1.0 Universal](https://github.com/Huziyang520/KeenLib/blob/26.X/LICENSE) 发布。
