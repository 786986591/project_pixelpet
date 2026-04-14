# project_pixelpet 文档索引

## 项目一句话定义
`project_pixelpet` 是一个以 `Android 原生 + 桌面 Widget 优先 + 单人宠物养成 + 像素复古美术` 为核心方向的轻量虚拟宠物产品。

## 当前版本目标
V1 聚焦验证三件事：
- 用户是否愿意把宠物长期放在桌面上，形成 `桌面常驻率`
- 用户是否会围绕喂食、玩耍、照顾形成 `日常回访频次`
- 基础养成玩法是否足以支撑 `早期留存`

当前版本默认前提：
- 平台只做 `Android`
- 体验核心是 `桌面 Widget 养成`，不是全屏宠物游戏
- 以 `18-35 岁、喜欢复古像素与轻陪伴感` 的个人用户为主
- 先不深入设计商业化，仅预留未来 `皮肤包 / 道具包 / 去广告` 空间

## 文档导航
### 1. 产品定义
- [PRD](./PRD.md)
  - 说明产品定位、目标用户、核心价值、V1 范围与关键用户流程

### 2. 技术方案
- [TECH_ARCHITECTURE](./TECH_ARCHITECTURE.md)
  - 说明 Android 技术选型、Widget 约束、状态更新原则与技术风险

### 3. 系统设计
- [SYSTEM_DESIGN](./SYSTEM_DESIGN.md)
  - 说明领域模型、模块职责、状态流转、Widget 与 App 的协作方式

### 4. 美术规范
- [ART_DIRECTION](./ART_DIRECTION.md)
  - 说明美术风格、动画策略、资源清单、导出规范与视觉边界，当前同时保留像素宠物方案与 `4x2` 鱼缸方案

### 5. 验收与测试
- [QA_ACCEPTANCE](./QA_ACCEPTANCE.md)
  - 说明验收标准、核心测试场景、边界条件与发布前检查项

### 6. 参考资料
- [REFERENCES](./REFERENCES.md)
  - 汇总官方文档、平台政策、竞品链接与后续补充入口

### 7. 页面结构
- [PAGE_STRUCTURE](./PAGE_STRUCTURE.md)
  - 说明 V1 App 内页面组成、页面职责、页面之间的进入与返回关系

### 8. 功能清单
- [FEATURE_SPEC](./FEATURE_SPEC.md)
  - 说明 V1 基础功能的目标、规则、输入输出与实现边界

### 9. 数据模型
- [DATA_MODEL](./DATA_MODEL.md)
  - 说明本地数据对象、建议数据表、字段含义与状态持久化方式

## 建议阅读顺序
1. 先读 [PRD](./PRD.md)，统一产品边界与目标
2. 再读 [TECH_ARCHITECTURE](./TECH_ARCHITECTURE.md)，确认技术路线是否支撑产品目标
3. 接着读 [SYSTEM_DESIGN](./SYSTEM_DESIGN.md)，把抽象方案落到模块和接口职责
4. 再读 [PAGE_STRUCTURE](./PAGE_STRUCTURE.md) 与 [FEATURE_SPEC](./FEATURE_SPEC.md)，把产品目标落到页面和功能
5. 然后读 [DATA_MODEL](./DATA_MODEL.md)，确认基础数据与状态存储方式
6. 再读 [ART_DIRECTION](./ART_DIRECTION.md)，统一首版视觉与资源产出方式
7. 最后读 [QA_ACCEPTANCE](./QA_ACCEPTANCE.md)，用验收标准反推开发完成度

## 文档维护约定
- 每份文档只回答一类问题，避免产品、技术、美术、测试内容混写
- 同一条规则只在一份文档中完整展开，其他文档只引用或摘要
- 若后续需要补充交互稿、排期、埋点、商业化方案，应继续新增独立文档，而不是重新堆回总览页
- 第二层文档默认承接 V1 基础功能，不直接扩写到多人社交、联网同步或复杂经济系统

