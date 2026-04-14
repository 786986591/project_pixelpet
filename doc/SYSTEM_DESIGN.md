# project_pixelpet 系统设计

## 设计目标
系统设计需要保证三件事：
- 宠物状态在离线情况下可计算、可恢复、可持久化
- Widget 与 App 内详情页基于同一套状态来源展示
- 首版模块清晰，方便后续接入提醒、背包、成长、乃至未来云同步能力

## 领域模型
### `PetState`
建议作为核心领域状态对象，至少包含：
- `stage`
- `hunger`
- `mood`
- `cleanliness`
- `energy`
- `lastUpdatedAt`
- `lastSleptAt`
- `bondExp`

职责说明：
- 表达宠物当前可被推导和展示的真实状态
- 为成长判定、提醒规则、UI 映射提供统一输入

### `PetAction`
V1 核心动作枚举：
- `FEED`
- `PLAY`
- `CLEAN`
- `SLEEP`

职责说明：
- 作为交互结算的标准输入
- 避免 Widget、App、提醒系统各自定义动作语义

### `WidgetUiModel`
建议作为 Widget 展示层模型，至少包含：
- `petFrame`
- `statusSummary`
- `cta`
- `sizeVariant`

职责说明：
- 承接领域状态到桌面展示状态的映射
- 隔离 UI 呈现与底层状态计算逻辑

## 模块划分
### `PetRepository`
职责：
- 读写宠物状态
- 管理快照持久化
- 提供行为日志与成长记录读取能力

### `PetStateCalculator`
职责：
- 根据最后一次可信状态和时间差推导真实状态
- 统一计算衰减、恢复与边界值裁剪
- 保证 App 与 Widget 使用同一套状态逻辑

### `PetActionUseCase`
职责：
- 接收 `PetAction`
- 调用状态计算与仓储更新
- 处理交互反馈、奖励、冷却、日志记录
- 在需要时触发成长判定与后续更新

### `WidgetUpdater`
职责：
- 刷新所有已存在的 Widget 实例
- 在动作结算、提醒触发、系统回调后更新桌面视图

### `ReminderScheduler`
职责：
- 根据当前状态判断是否需要提醒
- 与 `WorkManager` 协作安排下一次检查或提醒

### `AssetMapper`
职责：
- 将宠物状态映射到像素帧、表情、气泡文案和 CTA
- 让视觉资源切换与逻辑状态切换解耦

## 数据流转
### 用户交互流
`用户点击 Widget / App 操作 -> PetActionUseCase -> PetStateCalculator -> PetRepository 持久化 -> WidgetUpdater 刷新展示`

### 被动刷新流
`系统重绘 Widget / App 打开 / Worker 触发 -> PetRepository 读取快照 -> PetStateCalculator 推导当前状态 -> AssetMapper 生成 UI 模型 -> WidgetUpdater 或页面 UI 展示`

## 状态计算逻辑
V1 默认采用以下思路：
- 存储层保存最近一次可信状态快照和关键时间字段
- 每次需要展示或结算时，先根据当前时间推导衰减后的状态
- 再应用用户动作或系统事件带来的增量变化
- 最终结果写回存储，并作为新的可信快照

这样做的原因：
- 能覆盖长时间未打开 App 的情况
- 能避免不同入口各自维护状态
- 能把“真实状态”和“展示刷新”解耦

## Widget 与 App 的协作方式
- Widget 与 App 详情页都从同一份持久化快照和同一套计算器读取状态
- Widget 负责高频、轻量的操作入口与状态反馈
- App 详情页负责展示更完整的信息，如背包、成长记录、设置
- 两者禁止维护彼此独立的状态副本，以防出现展示不一致

## 扩展位预留
为未来社交或云同步保留轻量字段即可：
- `userId`
- `petId`
- `cloudSyncToken?`

V1 不做：
- 账号登录
- 服务端同步层
- 实时共养或跨端同步

若未来要扩展：
- 在现有领域状态和仓储层之上新增同步层
- 不直接破坏当前离线单机模型

## 当前设计结论
- V1 的系统核心不在复杂业务数量，而在 `状态单一可信来源`
- 只要 `PetStateCalculator`、`PetActionUseCase`、`WidgetUpdater` 三者边界清楚，后续迭代会比较稳定

