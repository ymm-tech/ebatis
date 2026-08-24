# Changelog

## 7.15.0.5

- 新增 Reindex Mapper，支持限速、切片、冲突策略、批大小、最大文档数、目标 pipeline/version/op type/alias 校验。
- Reindex 支持等待完成或返回异步任务 ID，并新增 `@TaskResult` 查询任务状态与结果。
- Bulk 支持客户端分批、受控并发、指数退避以及 429/502/503/504 逐项重试；新增 `BulkResult` 逐项失败明细。
- 修复 Bulk 子请求携带 refresh policy 被 ES 拒绝的问题。
- 新增 `@NativeOperation`、`OperationExtension` 和注册中心，统一开放注解、RequestFactory、Executor、ResponseExtractor 扩展链路。
- Spring Boot 配置新增 scheme、认证、SSL trust store、主机名校验、连接池、三类超时、默认请求头、sniff 参数和命名多集群。
- `ebatis-web` 继续保留二进制兼容，明确标记为空壳模块。
- 新增隔离的 ES 7.15 发布集成测试，覆盖 CRUD、Count、PIT、聚合、by-query、Bulk、同步/异步 Reindex 和任务结果。

## 7.15.0.4

- 新增真正的 Count API，同时保留 `@Cat(count = ...)` 兼容入口。
- 新增 Document Exists、Get Source、Open PIT、Close PIT Mapper 操作及同步/异步响应提取。
- PIT 搜索支持 keep-alive、search_after 和最新 PIT ID 传递，并自动规避 PIT 不允许的 index/preference/routing 参数。
- 新增 query string、simple query string、script、more like this、父子、geo bounding box、geo polygon、regexp 和 wrapper 查询。
- 新增 `QueryBuilderProvider`、`AggregationBuilderProvider` 和 `SearchSourceBuilderProvider` 原生扩展口。
- Search 补充 min score、track scores、explain/profile、stored fields 和 doc-value fields；post filter、suggest、rescore 可由 SearchSource 原生扩展口配置。
- 聚合补充 histogram、date histogram、range、filter、nested、cardinality、stats、top hits 和 composite 构建入口。

## 7.15.0.3

- 修复 DeleteByQuery、UpdateByQuery 响应类型与实际 `BulkByScrollResponse` 不一致的问题。
- 修复 Index pipeline/final pipeline 配置混用问题。
- 支持直接参数构建 Wildcard 查询，并对未实现 QueryType 提前报错。
- 默认测试与需要 Elasticsearch 的集成测试分离。
- 增加 Elasticsearch 服务端版本探测和兼容性日志。
- 升级 Lombok，使 Java 8 目标代码可在 JDK 17 构建。
