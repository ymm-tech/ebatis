# Changelog

## 7.15.0.3

- 修复 DeleteByQuery、UpdateByQuery 响应类型与实际 `BulkByScrollResponse` 不一致的问题。
- 修复 Index pipeline/final pipeline 配置混用问题。
- 支持直接参数构建 Wildcard 查询，并对未实现 QueryType 提前报错。
- 默认测试与需要 Elasticsearch 的集成测试分离。
- 增加 Elasticsearch 服务端版本探测和兼容性日志。
- 升级 Lombok，使 Java 8 目标代码可在 JDK 17 构建。
