# project_pixelpet 数据模型

## 文档目标
本页用于把 V1 基础功能落到可持久化的数据层，帮助后续实现 `Room + DataStore` 时提前统一字段与对象边界。

本页是建议设计，不等同于最终数据库代码。

## 设计原则
- 以单机离线为前提
- 状态快照应小而稳定
- 日志与设置分开存储
- 为未来扩展预留字段，但不引入当前不用的复杂关系

## 核心对象
### 1. `pet`
用途：
- 存储唯一主宠物的当前可信状态

建议字段：
- `pet_id`
- `stage`
- `hunger`
- `mood`
- `cleanliness`
- `energy`
- `bond_exp`
- `last_updated_at`
- `last_slept_at`
- `created_at`
- `updated_at`

说明：
- V1 默认只有 1 只主宠物，但仍保留 `pet_id`，避免以后改模型时重构过大
- `last_updated_at` 是状态推导的重要基准字段

### 2. `pet_action_log`
用途：
- 存储关键互动记录，用于成长记录和调试回放

建议字段：
- `id`
- `pet_id`
- `action_type`
- `action_source`
- `before_state_snapshot`
- `after_state_snapshot`
- `created_at`

说明：
- `action_source` 可区分 `widget`、`app`、`reminder`
- 快照字段可以先用轻量 JSON 或序列化结构保存，不必一开始拆得很细

### 3. `inventory_item`
用途：
- 存储背包中的基础道具数量

建议字段：
- `id`
- `pet_id`
- `item_type`
- `quantity`
- `updated_at`

V1 说明：
- 首版只需要支持少量基础道具
- 不做复杂稀有度、有效期、装备位

### 4. `growth_record`
用途：
- 存储宠物成长阶段和关键节点记录

建议字段：
- `id`
- `pet_id`
- `stage`
- `record_type`
- `summary`
- `created_at`

说明：
- `record_type` 可区分 `stage_up`、`milestone`、`special_event`
- V1 只需要覆盖成长和关键互动摘要

### 5. `user_settings`
用途：
- 存储轻配置与用户偏好

建议字段：
- `reminder_enabled`
- `sound_enabled`
- `theme_mode`
- `onboarding_completed`
- `updated_at`

说明：
- 这类字段更适合落在 `DataStore`
- 若后续字段变多，再评估是否迁移为独立持久化对象

## 状态快照建议
V1 不建议把每一次衰减结果都直接写回数据库，而是采用：
- 存一份最近可信快照
- 存关键时间字段
- 在读取时推导当前状态

这样可以减少：
- 高频写入
- 后台任务依赖
- Widget 与 App 状态不一致的风险

## 字段边界建议
### 状态字段
- `hunger`
- `mood`
- `cleanliness`
- `energy`

建议使用统一量纲，例如 `0-100`，便于：
- 状态裁剪
- UI 映射
- 提醒阈值判断

### 成长字段
- `stage`
- `bond_exp`

说明：
- `stage` 表示当前形态阶段
- `bond_exp` 用于累计照顾关系和成长推进

### 时间字段
- `created_at`
- `updated_at`
- `last_updated_at`
- `last_slept_at`

说明：
- 所有时间字段都应使用统一时区与格式存储
- 状态系统优先依赖机器可计算时间，而不是展示型字符串

## 数据关系建议
- `pet` 是核心主表
- `pet_action_log`、`inventory_item`、`growth_record` 都通过 `pet_id` 关联到主宠物
- `user_settings` 独立存在，不和宠物状态强耦合

## V1 不需要的数据复杂度
- 多宠物关系表
- 用户账号表
- 云同步冲突表
- 任务系统表
- 商业化订单表

## 当前结论
- V1 的数据层重点是“一个主宠物 + 一套状态快照 + 少量日志与设置”
- 只要先把这层做好，后续加提醒、背包、成长记录都会比较顺

