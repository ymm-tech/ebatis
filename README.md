# ebatis是什么
[![GitHub Stars](https://img.shields.io/badge/stars-100%2B-blue)](https://github.com/ymm-tech/ebatis/stargazers) 
[![JDK support](https://img.shields.io/badge/JDK-8+-green.svg)](https://github.com/ymm-tech/ebatis/releases)
[![Maven Central](https://img.shields.io/badge/maven-7.15.0.5-green)](https://search.maven.org/search?q=ebatis)
[![License](https://img.shields.io/badge/License-MIT-yellowgreen)](https://choosealicense.com/licenses/mit/)  
`ebatis`是一个声明式`Elasticsearch ORM`框架。只需要定义接口，便可轻松访问`Elasticsearch`。`ebatis`优雅地帮你隔离业务对`Elasticserach`底层驱动接口的直接调用，你不再需要自己手动去构建繁琐`DSL`
语句。同时，当升级`Elastisearch`版本的时候，业务可以完全不用关心底层接口的变动，平滑升级。  
当前维护线面向 `Elasticsearch 7.15.0`，使用 Java 8 兼容字节码。

# ebatis现状

`ebatis`已经在满帮业务系统上稳定运行近一年，承载着每日近十亿次搜索服务。

# QUICK START
`POM`依赖
```xml
<dependency>
     <groupId>io.manbang</groupId>
     <artifactId>ebatis-core</artifactId>
     <version>7.15.0.5</version>
</dependency>
```
创建集群连接如下:
```java
@AutoService(ClusterRouterProvider.class)
public class SampleClusterRouterProvider implements ClusterRouterProvider {
    public static final String SAMPLE_CLUSTER_NAME = "sampleCluster";
 
    @Override
    public ClusterRouter getClusterRouter(String name) {
        if (SAMPLE_CLUSTER_NAME.equalsIgnoreCase(name)) {
            Cluster cluster = Cluster.simple("127.0.0.1", 9200, Credentials.basic("admin", "123456"));
            return ClusterRouter.single(cluster);
        } else {
            return null;
        }
    }
}
```
定义`POJO`对象如下:
```java
@Data
public class RecentOrder {
    private Long cargoId;
    private String driverUserName;
    private String loadAddress;
    private Boolean searchable;
    private Integer companyId;
}

@Data
public class RecentOrderCondition {
    private Boolean searchable;

    private String driverUserName;
}
```
定义`Mapper`接口
```java
@Mapper(indices = "recent_order_index")
public interface RecentOrderRepository {
    @Search
    List<RecentOrder> search(RecentOrderCondition condition);
}
```
测试接口如下:
```java
@Slf4j
public class OrderRepositoryTest {
 
    @Test
    public void search() {
        // 组装查询条件
        RecentOrderCondition condition = new RecentOrderCondition();
        condition.setSearchable(Boolean.TRUE);
        condition.setDriverUserName("张三");
 
        // 映射接口
        RecentOrderRepository repository = MapperProxyFactory.getMapperProxy(RecentOrderRepository.class, SampleClusterRouterProvider.SAMPLE_CLUSTER_NAME);
 
        // 搜索货源
        List<RecentOrder> orders = repository.search(condition);
 
        // 断言
        Assert.assertEquals(3, orders.size());
 
        // 打印输出
        orders.forEach(order -> log.info("{}", order));
    }
}
```
搜索得`DSL`语句如下:
```json
{
  "query" : {
    "bool" : {
      "must" : [ {
        "term" : {
          "searchable" : {
            "value" : true,
            "boost" : 1.0
          }
        }
      }, {
        "term" : {
          "driverUserName" : {
            "value" : "张三",
            "boost" : 1.0
          }
        }
      } ],
      "adjust_pure_negative" : true,
      "boost" : 1.0
    }
  },
  "_source" : {
    "includes" : [ "cargoId", "driverUserName", "loadAddress", "searchable", "companyId" ],
    "excludes" : [ ]
  }
}
```
`ebatis` 版本前三位表示 Elasticsearch 客户端基线，最后一位表示 Ebatis 迭代版本；开发版本使用 `-SNAPSHOT` 后缀，例如 `7.15.0.3-SNAPSHOT`。

# quick start
POM依赖
```xml
<dependency>
     <groupId>io.manbang</groupId>
     <artifactId>ebatis-core</artifactId>
     <version>7.15.0.5</version>
</dependency>
```
创建集群连接
```java
@AutoService(ClusterRouterProvider.class)
public class SampleClusterRouterProvider implements ClusterRouterProvider {
    public static final String SAMPLE_CLUSTER_NAME = "sampleCluster";
 
    @Override
    public ClusterRouter getClusterRouter(String name) {
        if (SAMPLE_CLUSTER_NAME.equalsIgnoreCase(name)) {
            Cluster cluster = Cluster.simple("127.0.0.1", 9200, Credentials.basic("admin", "123456"));
            return ClusterRouter.single(cluster);
        } else {
            return null;
        }
    }
}
```
定义POJO对象
```java
@Data
public class RecentOrder {
    private Long cargoId;
    private String driverUserName;
    private String loadAddress;
    private Boolean searchable;
    private Integer companyId;
}

@Data
public class RecentOrderCondition {
    private Boolean searchable;

    private String driverUserName;
}
```
定义Mapper接口
```java
@Mapper(indices = "recent_order_index")
public interface RecentOrderRepository {
    @Search
    List<RecentOrder> search(RecentOrderCondition condition);
}
```
测试接口
```java
@Slf4j
public class OrderRepositoryTest {
 
    @Test
    public void search() {
        // 组装查询条件
        RecentOrderCondition condition = new RecentOrderCondition();
        condition.setSearchable(Boolean.TRUE);
        condition.setDriverUserName("张三");
 
        // 映射接口
        RecentOrderRepository repository = MapperProxyFactory.getMapperProxy(RecentOrderRepository.class, SampleClusterRouterProvider.SAMPLE_CLUSTER_NAME);
 
        // 搜索货源
        List<RecentOrder> orders = repository.search(condition);
 
        // 断言
        Assert.assertEquals(3, orders.size());
 
        // 打印输出
        orders.forEach(order -> log.info("{}", order));
    }
}
```
搜索得DSL语句
```json
{
  "query" : {
    "bool" : {
      "must" : [ {
        "term" : {
          "searchable" : {
            "value" : true,
            "boost" : 1.0
          }
        }
      }, {
        "term" : {
          "driverUserName" : {
            "value" : "张三",
            "boost" : 1.0
          }
        }
      } ],
      "adjust_pure_negative" : true,
      "boost" : 1.0
    }
  },
  "_source" : {
    "includes" : [ "cargoId", "driverUserName", "loadAddress", "searchable", "companyId" ],
    "excludes" : [ ]
  }
}
```
默认测试不依赖本地 Elasticsearch：

```bash
mvn clean verify
```

连接 Elasticsearch 7.15 后可运行集成测试：

```bash
mvn clean verify -Pintegration
```

完整能力状态见 [能力矩阵](docs/CAPABILITIES.md)，版本变化见 [CHANGELOG](CHANGELOG.md)。

### 原生扩展口

当注解 DSL 不足以描述 Elasticsearch 请求时，条件对象可以实现以下接口，继续复用 Mapper 路由、拦截器、同步/异步适配和响应提取链路：

- `QueryBuilderProvider`：提供原生 `QueryBuilder`。
- `AggregationBuilderProvider`：提供一个或多个原生 `AggregationBuilder`。
- `SearchSourceBuilderProvider`：配置 post filter、suggest、rescore 等 `SearchSourceBuilder` 能力。

PIT 深分页条件同时实现 `PointTimeProvider` 和 `SearchAfterProvider`；每次响应返回的新 PIT ID 应传入下一页请求，`keepAlive()` 用于逐页续期。

Span、Intervals、Rank Eval、Term Vectors、Search Template 等长尾 API 可通过 `@NativeOperation` 和 `OperationExtensionRegistry` 注册完整的请求创建、执行与响应提取链，不需要修改核心枚举。

### Spring Boot 连接配置

`ebatis.cluster` 保持原有单集群/随机/轮询/权重配置兼容，并支持 `scheme`、`username/password`、`ssl`、连接池、三类超时、默认请求头和 sniff。`ebatis.clusters.<name>` 可声明命名多集群，Mapper 使用 `clusterRouter = "name"` 路由。

```yaml
ebatis:
  cluster:
    single: 127.0.0.1:9200
    scheme: https
    username: elastic
    password: ${ES_PASSWORD}
    connect-timeout: 1000
    socket-timeout: 30000
    max-conn-total: 100
    sniff: false
    ssl:
      enabled: true
      trust-store: /path/to/truststore.p12
      trust-store-password: ${ES_TRUSTSTORE_PASSWORD}
  clusters:
    archive:
      single: archive-1:9200,archive-2:9200
      scheme: https
```

# ebatis入门及相关文章

使用手册:https://github.com/ymm-tech/ebatis/wiki  
相关文章:https://www.infoq.cn/article/u4Xhw5Q3jfLE1brGhtbR  
相关文章:https://mp.weixin.qq.com/s/GFRiiQEk-JLpPnCi_WrRqw  

## 交流群

> 钉钉 
 
<img src="https://github.com/codingPao/ymm-tech/blob/main/ebatisDingDing.JPG?raw=true" width="350px">


## 支持我们

### 你可以打赏我们 :coffee: 来杯咖啡 :coffee:

<img src="https://github.com/codingPao/ymm-tech/blob/main/coffee.JPG?raw=true" width="360px">

### 也可以点个 Star
开源项目需要的是持续地坚持，而我们坚持的动力当然也来自于你们的支持，希望你 :point_right: `来都来了，加个关注再走吧` :point_left:
