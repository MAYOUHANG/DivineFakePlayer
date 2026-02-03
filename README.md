# DivineFakePlayer（神圣假人 - 智能气氛组）

> 一个轻量级、基于数据包的假人插件，集成 DeepSeek AI，用最小开销营造真实活跃的服务器氛围。

![Java 17](https://img.shields.io/badge/Java-17-007396?logo=java&logoColor=white)
![Spigot 1.20.1](https://img.shields.io/badge/Spigot-1.20.1-ED8106)
![ProtocolLib](https://img.shields.io/badge/ProtocolLib-5.0%2B-5C2D91)
![Build Status](https://img.shields.io/badge/Build-Status-success)

## ✨ 核心特性 (Features)

- **👻 零实体占用 (Packet-Based)**：使用 ProtocolLib 注入数据包生成“幽灵”玩家，无真实实体，几乎 0 TPS 占用。
- **🤖 DeepSeek AI 驱动**：集成 SiliconFlow API，关键词触发智能回复。
- **🎭 群体智能 (Ensemble Mode)**：一名假人回答，其他假人附和（如“确实”“6”），模拟热闹群聊。
- **👋 智能欢迎**：新玩家加入时按节奏分批欢迎（默认 60% 概率触发）。
- **💬 氛围聊天**：定时发送闲聊内容，支持自定义短句库。
- **💀 死亡广播**：可选模拟死亡信息，支持 CMI 格式。
- **🔌 上下线模拟**：可选模拟假人加入/退出，支持 CMI 加入/退出格式。
- **📊 完美伪装**：支持 Vault 前缀与聊天格式，延迟随机模拟（10-100ms）。

## 📦 前置要求 (Requirements)

- Java 17+
- Vault（必需，用于聊天前缀/格式）
- ProtocolLib 5.0+（可选但强烈推荐，用于 Tab 列表假人）
- **重要**：需要 SiliconFlow 的 API Key（用于 DeepSeek AI）。

## ⚙️ 快速上手 (Getting Started)

### 安装步骤

1. 下载插件 JAR 并放入 `plugins/` 目录。
2. 确保已安装 Vault（可选安装 ProtocolLib）。
3. 启动服务器生成配置文件。
4. 填写 SiliconFlow API Key 并按需调整配置。
5. 重启服务器或执行 `/dfp reload` 生效。

### 配置指南（必读）

以下是 `config.yml` 中常用配置块（示例）：

```yaml
ghost-count: 20
chat-interval: 15
ai-settings:
  provider: siliconflow
  api-url: "https://api.siliconflow.cn/v1/chat/completions"
  api-key: "YOUR_SILICONFLOW_API_KEY"
  model: "deepseek-ai/DeepSeek-V3"
  system-prompt: |
    你是一个Minecraft模组生存服务器的普通玩家。你的回答必须简短（15字以内）、口语化、包含中文网络俚语。
events:
  welcome-chance: 0.6
  reply-chance: 0.2
death-settings:
  enabled: true
  chance: 0.05
connection-settings:
  enabled: true
  interval-seconds: 30
  quit-chance: 0.02
  rejoin-chance: 0.1
```

- **ghost-count**：假人数量。
- **chat-interval**：闲聊间隔（秒）。
- **api-url / api-key / model**：DeepSeek 接口与模型配置。
- **system-prompt**：AI 人设（Persona），建议简短口语化。
- **events.welcome-chance**：新玩家欢迎概率。
- **events.reply-chance**：群体附和概率。
- **death-settings**：死亡广播开关与触发概率。
- **connection-settings**：上下线模拟开关与频率。

## 💬 指令与权限

| 指令 | 权限 | 描述 |
| --- | --- | --- |
| `/dfp reload` | `divinefakeplayer.reload` | 重新加载配置 | 

## ❓ 常见问题 (FAQ)

**Q: 我能在游戏里看到这些假人吗？**  
A: 不能。它们是“幽灵”玩家，只会出现在 Tab 列表与聊天中，不会生成实体。

**Q: 会不会导致服务器卡顿？**  
A: 不会。核心逻辑异步执行，且不生成实体，TPS 影响几乎为 0。

**Q: 为什么没有看到假人？**  
A: 请确认安装了 ProtocolLib（用于 Tab 假人），并检查 `ghost-count` 是否大于 0。
