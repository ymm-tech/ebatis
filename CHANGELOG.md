# Changelog

## 7.15.0.4-SNAPSHOT

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
