# Ebatis 7.15 能力矩阵

状态说明：✅ 已支持；🟡 部分支持或计划补全；➖ 通过原生客户端访问；❌ 不在 ORM 核心范围。

| 能力 | 状态 | 说明 |
|---|---:|---|
| Index/Get/Update/Delete | ✅ | 支持同步、异步 Mapper 和常用版本/路由参数 |
| Bulk/Multi Get/Multi Search | ✅ | Bulk 高级批处理能力将在 7.15.0.5 补充 |
| Search/Scroll/Search After/PIT | ✅ | PIT 生命周期、keep-alive、最新 PIT ID 和 search_after 已支持 |
| Delete By Query/Update By Query | ✅ | 7.15.0.3 修复响应提取 |
| Count/Exists/Get Source | ✅ | 原生 Count API、文档存在性和仅取 `_source` 均支持同步/异步 Mapper |
| Query DSL | ✅ | 常用查询已补齐，长尾查询可通过 `QueryBuilderProvider` 接入原生 builder |
| Search Source | ✅ | 支持分页、过滤、计分/诊断/字段选取；suggest/rescore 等可通过原生扩展口配置 |
| Aggregation DSL | ✅ | 常用桶、指标及 composite 已支持，其他聚合可通过 `AggregationBuilderProvider` 接入 |
| Reindex | 🟡 | 7.15.0.5 实现 |
| Index 管理 | 🟡 | 目前支持 create/delete/refresh，不作为本轮主范围 |
| Cluster/Snapshot/Ingest/ML | ➖ | 可通过底层客户端访问，不做 Mapper 注解全覆盖 |
| ebatis-web | ❌ | 兼容保留的空壳模块 |
