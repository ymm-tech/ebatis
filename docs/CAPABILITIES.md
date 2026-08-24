# Ebatis 7.15 能力矩阵

状态说明：✅ 已支持；🟡 部分支持或计划补全；➖ 通过原生客户端访问；❌ 不在 ORM 核心范围。

| 能力 | 状态 | 说明 |
|---|---:|---|
| Index/Get/Update/Delete | ✅ | 支持同步、异步 Mapper 和常用版本/路由参数 |
| Bulk/Multi Get/Multi Search | ✅ | Bulk 支持分批、受控并发、指数退避、可重试状态和逐项失败明细 |
| Search/Scroll/Search After/PIT | ✅ | PIT 生命周期、keep-alive、最新 PIT ID 和 search_after 已支持 |
| Delete By Query/Update By Query | ✅ | 7.15.0.3 修复响应提取 |
| Count/Exists/Get Source | ✅ | 原生 Count API、文档存在性和仅取 `_source` 均支持同步/异步 Mapper |
| Query DSL | ✅ | 常用查询已补齐，长尾查询可通过 `QueryBuilderProvider` 接入原生 builder |
| Search Source | ✅ | 支持分页、过滤、计分/诊断/字段选取；suggest/rescore 等可通过原生扩展口配置 |
| Aggregation DSL | ✅ | 常用桶、指标及 composite 已支持，其他聚合可通过 `AggregationBuilderProvider` 接入 |
| Reindex/Tasks | ✅ | 支持同步结果、异步任务 ID、任务查询、限速、切片和冲突策略 |
| 原生操作扩展 | ✅ | `@NativeOperation` 注册完整请求工厂、执行器和响应提取链，承载长尾 API |
| Index 管理 | 🟡 | 目前支持 create/delete/refresh，不作为本轮主范围 |
| Cluster/Snapshot/Ingest/ML | ➖ | 可通过底层客户端访问，不做 Mapper 注解全覆盖 |
| Spring Boot 连接配置 | ✅ | scheme、认证、SSL、节点、连接池、超时、请求头、sniff 和命名多集群 |
| ebatis-web | ❌ | 仅为二进制兼容保留的空壳模块，不承载 Web 功能 |
