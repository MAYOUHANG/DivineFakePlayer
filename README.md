# DivineFakePlayer（神圣假人 - 智能气氛组）

> 一个轻量级、基于数据包的假人插件，集成 DeepSeek AI，用最小开销营造真实活跃的服务器氛围。

![Java 17](https://img.shields.io/badge/Java-17-007396?logo=java&logoColor=white)
![Spigot 1.20.1](https://img.shields.io/badge/Spigot-1.20.1-ED8106)
![ProtocolLib](https://img.shields.io/badge/ProtocolLib-5.0%2B-5C2D91)
![Build Status](https://img.shields.io/badge/Build-Status-success)

## ✨ 核心特性 (Features)

- **👻 零实体占用 (Packet-Based)**：使用 ProtocolLib 注入数据包生成“幽灵”玩家，无真实实体，几乎 0 TPS 占用。
- **🤖 DeepSeek AI 驱动**：集成 SiliconFlow API，假人可像真实玩家一样回答问题。
- **🎭 群体智能 (Ensemble Mode)**：一名假人回答，其他假人附和（如“确实”“6”），模拟热闹群聊。
- **👋 智能欢迎**：新玩家加入时按节奏分批欢迎（约 60% 概率触发）。
- **📊 完美伪装**：支持 LuckPerms 前缀、CMI 聊天格式；延迟随机模拟（10-100ms）。

## 📦 前置要求 (Requirements)

- Java 17+
- ProtocolLib 5.0+
- Vault（可选，用于前缀显示）
- **重要**：需要 SiliconFlow 的 API Key（用于 DeepSeek AI）。

## ⚙️ 快速上手 (Getting Started)

### 安装步骤

1. 下载插件 JAR 并放入 `plugins/` 目录。
2. 确保已安装 ProtocolLib（以及可选的 Vault）。
3. 启动服务器生成配置文件。
4. 填写 SiliconFlow API Key 并按需调整配置。
5. 重启服务器或执行 `/dfp reload` 生效。

### 配置指南（必读）

以下是 `config.yml` 中与 AI 相关的关键配置块（示例）：

```yaml
ai-settings:
  provider: siliconflow
  api-key: "YOUR_SILICONFLOW_API_KEY"
  system-prompt: |
    你是服务器中的一名活跃玩家，语气友善且简短。
```

- **api-key**：将你的 SiliconFlow API Key 填入此处，否则 AI 无法回复。
- **system-prompt**：用于设定 AI 人设（Persona），可描述性格、语气、行为规范等。

## 💬 指令与权限

| 指令 | 权限 | 描述 |
| --- | --- | --- |
| `/dfp reload` | `dfp.admin` | 重新加载配置 | 

## ❓ 常见问题 (FAQ)

**Q: 我能在游戏里看到这些假人吗？**  
A: 不能。它们是“幽灵”玩家，只会出现在 Tab 列表与聊天中，不会生成实体。

**Q: 会不会导致服务器卡顿？**  
A: 不会。核心逻辑异步执行，且不生成实体，TPS 影响几乎为 0。
