# Ebatis 7.15 能力矩阵

状态说明：✅ 已支持；🟡 部分支持或计划补全；➖ 通过原生客户端访问；❌ 不在 ORM 核心范围。

| 能力 | 状态 | 说明 |
|---|---:|---|
| Index/Get/Update/Delete | ✅ | 支持同步、异步 Mapper 和常用版本/路由参数 |
| Bulk/Multi Get/Multi Search | ✅ | Bulk 高级批处理能力将在 7.15.0.5 补充 |
| Search/Scroll/Search After | ✅ | PIT 生命周期将在 7.15.0.4 补齐 |
| Delete By Query/Update By Query | ✅ | 7.15.0.3 修复响应提取 |
| Count API | 🟡 | 7.15.0.4 实现；当前仅有 Search countOnly 和 CAT count |
| Query DSL | 🟡 | 已支持 Bool、Term(s)、Match、Wildcard、Range、Nested、Geo 等常用查询 |
| Aggregation DSL | 🟡 | 已支持部分指标和 Terms/Filters；常用聚合在 7.15.0.4 补充 |
| Reindex | 🟡 | 7.15.0.5 实现 |
| Index 管理 | 🟡 | 目前支持 create/delete/refresh，不作为本轮主范围 |
| Cluster/Snapshot/Ingest/ML | ➖ | 可通过底层客户端访问，不做 Mapper 注解全覆盖 |
| ebatis-web | ❌ | 兼容保留的空壳模块 |
