[TOC]

# 设计说明

`ebatis`采用和`MyBatis`类似思想，只需要定义接口，便可访问`elasticsearch`，隔离业务对`elasticserach`底层接口的直接访问。如此以来，数据访问的时候，不需要自己手动去构建DSL语句，同时，当升级`elastisearch`版本的时候，业务可以完全不用关心底层接口的变动，平滑升级。
# 快速入门
> 创建索引

```json
PUT /recent_order_index
{
  "settings": {
    "number_of_replicas": 0,
    "number_of_shards": 1
  },
  "mappings": {
    "properties": {
      "cargoId": {
        "type": "long"
      },
      "driverUserName": {
        "type": "keyword"
      },
      "loadAddress": {
        "type": "text"
      },
      "searchable": {
        "type": "boolean"
      },
      "companyId": {
        "type": "long"
      }
    }
  }
}
```

> 增加测试数据

```json
POST /recent_order_index/_bulk
{"index":{}}
{"cargoId": 1, "driverUserName":"张三", "loadAddress": "南京市玄武区", "searchable": true,"companyId": 666}
{"index":{}}
{"cargoId": 2, "driverUserName":"李四", "loadAddress": "南京市秦淮区", "searchable": false,"companyId": 667}
{"index":{}}
{"cargoId": 3, "driverUserName":"王五", "loadAddress": "南京市六合区", "searchable": true,"companyId": 668}
{"index":{}}
{"cargoId": 4, "driverUserName":"赵六", "loadAddress": "南京市建邺区", "searchable": true,"companyId": 669}
{"index":{}}
{"cargoId": 5, "driverUserName":"钱七", "loadAddress": "南京市鼓楼区", "searchable": true,"companyId": 665}
```

> POM依赖

```xml
<dependency>
  <groupId>com.ymm.ebatis</groupId>
  <artifactId>ebatis-core</artifactId>
  <version>7.5.1.1-SNAPSHOT</version>
</dependency>
```

> 创建集群连接

```java
Cluster cluster = Cluster.simple("127.0.0.1", 9200);
ClusterRouter router = ClusterRouter.single(cluster);
```

> 定义POJO对象

```java
@Data
public class RecentOrder {
    private Long cargoId
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

> 定义Mapper接口

```java
@EasyMapper(indices = "recent_order_index")
public interface RecentOrderRepository {
    @Search
    List<RecentOrder> search(RecentOrderCondition condition);
}
```

> 测试接口

```java
@AutoService(ClusterRouterProvider.class)
public class SampleClusterRouterProvider implements ClusterRouterProvider {
    public static final String SAMPLE_CLUSTER_NAME = "sampleCluster";

    @Override
    public ClusterRouter getClusterRouter(String name) {
        if (SAMPLE_CLUSTER_NAME.equalsIgnoreCase(name)) {
            Cluster cluster = Cluster.simple("127.0.0.1", 9200, Credentials.basic("admin", "123456"));
            ClusterRouter clusterRouter = ClusterRouter.single(cluster);
            return clusterRouter;
        } else {
            return null;
        }
    }
}


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

# 连接ES集群
为了保证高ES集群的高可用，同时支持对集群的负载均衡，`ebatis`没有直接使用`elasticsearch`提供的`RestClient`和`RestHighLevelClient`接口来访问集群，而是抽象出一个`Cluster`。一个`Cluster`代表一个ES集群，如果系统需要连接多个集群，则通过`ClusterRouter`和`ClusterLoadBalancer`来实现，多集群的路由和负载均衡。
## Cluster
`Cluster`代表一个ES集群实例，`ebatis`内建了两个实现：`SimpleCluster`，`FixWeightedCluster`和`SimpleFederalCluster`。
`SimpleCluster`和`FixedWeightedCluster`的区别在于，后者是带固定权值的值，在对集群做负载均衡的时候，可以通过权值来控制负载的比例。`SimpleFederalCluster`的特殊地方在于，在一批集群上做批量操作，同步一批集群，一般用于一批集群数据的增删改，不适用于查。

> 创建`Cluster`实例

```java
Cluster cluster = Cluster.simple("127.0.0.1", 9200);

int weight = 10
WeightedCluster cluster = Cluster.weighted(weight, "127.0.0.1", 9200);

Cluster cluster = FederalCluster.of(Cluster.simple("127.0.0.1", 9200),Cluster.simple("127.0.0.1", 9300));
```

如果需要自己实现集群的定义，可以通过继承`AbstractCuster`来实现，也可以你直接实现`Cluster`接口。
## ClusterRouter
`ClusterRouter`用于路由出一个可以访问`Cluster`，内部是通过负载均衡器`ClusterLoadBalancer`，来同一组集群中，选中一个集群的。根据不同的负载均衡器，`ebatis`内建了多个对应的路由器：

|序号|路由器|负载均衡器|备注|
|----|-----|---------|----|
|1|`RandomClusterRouter`|`RandomClusterLoadBalancer`|随机负载均衡|
|2|`RoundRobinClusterRouter`|`RoundRoubinClusterLoadBalancer`|轮询负载均衡|
|3|`SingleClusterRouter`|`SingleClusterLoaderBalancer`|只有一个集群|
|4|`WeightedClusterRouter`|`WeightedClusterLoadBalancer`|权重负载均衡|

> 创建 ClusterRouter实例

```java
Cluster[] clusters = {Cluster.simple("127.0.0.1", 9200), Cluster.simple("127.0.0.2", 9200)};
// 随机路由器
ClusterRouter router = ClusterRouter.random(clusters);

// 轮询路由器
ClusterRouter router = ClusterRouter.roundRobbin(cluster);

// 带权重的路由器，带权重的集群必须实现Weighted接口
int weight 10;
FixedWeightedCluster[] clusters = {Cluster.weighted(weight, "127.0.0.1", 9200)}
ClusterRouter router = ClusterRouter.weighted(clusters);

// 单集群路由
Cluster cluster = Cluster.simple("127.0.0.1", 9200);
ClusterRouter router = ClusterRouter.single(cluster);
```

# 创建Mapper对象
创建`Mapper`对象，需要先定义`Mapper`接口，所有的`Mapper`都需要加上`@Mapper`或`@EasyMapper`注解，然后通过`MapperProxyFactory`来创建接口的代理。
## @Mapper
此注解用于标记接口定义类是Mapper对象，属性说明如下：

|序号|属性名|默认值|说明|
|---|---|---|---|
|1|`indices`|必填|`ebatis`只支持多个索引操作|
|2|`types`|可选，默认为空|最新版的ES已经不支持`type`定义|
|3|`clusterRouter`|可选，默认为空|为每个Mapper对象绑定一个路由器，当不配置时默认取ebatis.properties中ebatis.clusterRouter配置的集群名|
## Mapper接口定义
```java
@EasyMapper(indices = "recent_order_index", types = "order" , clusterRouter = "sampleCluster")
public interface OrderRepository {
    //通过注解定义搜索的类型，不同搜索类型支持的返回结果类型不一样
    @Search
    List<Order> search(OrderCondition condition);
}
```

## 创建cluster对象

集群的加载通过SPI方式。

```java
@AutoService(ClusterRouterProvider.class)
public class SampleClusterRouterProvider implements ClusterRouterProvider {
    public static final String SAMPLE_CLUSTER_NAME = "sampleCluster";

    @Override
    public ClusterRouter getClusterRouter(String name) {
        if (SAMPLE_CLUSTER_NAME.equalsIgnoreCase(name)) {
            Cluster cluster = Cluster.simple("127.0.0.1", 9200, Credentials.basic("admin", "123456"));
            ClusterRouter clusterRouter = ClusterRouter.single(cluster);
            return clusterRouter;
        } else {
            return null;
        }
    }
}
```

## 创建Mapper对象

```java
OrderRepository repository = MapperProxyFactory.getMapperProxy((OrderRepository.class);
```

## Mapper搜索方法返回值类型
| 请求类型                          | 接口声明返回值                                  | 备注                      |
| --------------------------------- | ----------------------------------------------- | ------------------------- |
| @Search | Page\<Entity>                                   |                           |
|                                   | List\<Entity>                                   |                           |
|                                   | Entity[]                                        |                           |
|                                   | SearchResponse                                  |                           |
|                                   | Entity                                          |                           |
|                                   | Long                                            |                           |
|                                   | long                                            |                           |
|                                   | Boolean                                         |                           |
|                                   | boolean                                         |                           |
| @MultiSearch                      | List\<Page\<Entity>>                            |                           |
|                                   | Page\<Entity>\[]                                |                           |
|                                   | List\<List\<Entity>>                            |                           |
|                                   | Entity\[]\[]                                    |                           |
|                                   | List\<Entity\[]>                                |                           |
|                                   | List\<Entity>[]                                  |                       |
|                                   | MultiSearchResponse                             |                           |
|                                   | List\<Long>                                      |                           |
|                                   | Long[]                                          |                           |
|                                   | long[]                                          |                           |
|                                   | List\<Boolean>                                   |                           |
|                                   | Boolean[]                                       |                           |
|                                   | boolean[]                                       |                           |
| @Index                            | IndexResponse                                   |                           |
|                                   | RestStatus                                      |                   |
|                                   | boolean                                         |                           |
|                                   | Boolean                                         |               |
|                                   | String                                          |                     |
|                                   | void                                            |                           |
| @Get                                 | GetResponse                                     |                           |
|                                   | Entity                                          |                           |
|                                   | Optional\<Entity>                               |                           |
| @Delete                                 | RestStatus                                      |                           |
|                                   | DeleteResponse                                  |                           |
|                                   | boolean                                         |                           |
|                                   | Boolean                                         |                           |
|                                   | void                                            |                           |
| @Update                                 | UpdateResponse                                  |                           |
|                                   | GetResult                                       |  |
|                                   | RestStatus                                      |                           |
|                                   | boolean                                         |                           |
|                                   | Boolean                                         |                           |
|                                   | Result                                          |                           |
|                                   | void                                            |                           |
| @Bulk                                 | List\<BulkItemResponse>                         |                           |
|                                   | BulkResponse                                    |                           |
|                                   | BulkItemResponse\[]                             |                           |
| @MultiGet                                 | MultiGetResponse                                |                           |
|                                   | MultiGetItemResponse\[]                         |                           |
|                                   | List\<MultiGetItemResponse>                     |                           |
|                                   | List\<Entity>                                   |                       |
|                                   | Entity[]                                        |                       |
|                                   | List<Optional\<Entity>>                          |                           |
|                                   | Optional\<Entity>[]                              |                           |
| @UpdateByQuery                                 | BulkByScrollResponse                            |                           |
|                                   | BulkByScrollTask.Status                         |                           |
|@DeleteByQuery                                  | BulkByScrollResponse                            |                           |
|                                   | BulkByScrollTask.Status                         |                           |
| @SearchScroll                                 | SearchResponse                                  |                           |
|                                   | ScrollResponse                                  |                           |
| @ClearScroll                                 | ClearScrollResponse                             |                           |
|                                   | boolean                                         |                           |
|                                   | Boolean                                         |                           |
| @Agg | SearchResponse                                  | （暂时只支持桶聚合 terms查询） |
|                                   | Aggregations                                    |                           |
|                                   | List\<Aggregation>                               |                           |
|                                   | Map<String, Aggregation>                        |                           |

## 返回类型异步支持

Mapper搜索方法支持异步操作，只需要将Mapper接口返回结果定义为CompletableFuture<Page\<Entity>>，这样异步的调用不会阻塞并且立刻返回，业务方可以继续处理自己的业务逻辑，在需要获取结果时，提取结果。

# 文档接口
## 单文档接口
### Index API
#### 接口定义
```java
/**
 * 创建一笔订单
 * @param order order 订单
 * @return 创建成功，返回<code>true</code>
 */
@Index
Boolean indexRecentOrderBoolean(RecentOrderModel order);

/**
 * 创建一笔订单
 * @param order order 订单
 * @return 创建成功，返回<code>true</code>
 */
@Index
boolean indexRecentOrderBool(RecentOrderModel order);

/**
 * 创建一笔订单
 * @param order order 订单
 * @return 创建成功，返回文档id
 */
@Index
String indexRecentOrderString(RecentOrderModel order);

/**
 * 创建一笔订单
 * @param order order 订单
 */
@Index
void indexRecentOrderVoid(RecentOrderModel order);

/**
 * 创建一笔订单
 * @param order order 订单
 * @return 创建成功，返回文档response
 */
@Index
IndexResponse indexRecentOrderIndexResponse(RecentOrderModel order);

/**
 * 创建一笔订单
 * @param order order 订单
 * @return 创建成功，返回RestStatus状态码
 */
@Index
RestStatus indexRecentOrderRestStatus(RecentOrderModel order);

/**
 * 创建一笔订单
 * @param order order 订单
 * @return 创建成功，返回异步RestStatus状态码
 */
@Index
CompletableFuture<RestStatus> indexRecentOrderCompletableFuture(RecentOrderModel order);

/**
 * 创建一笔订单
 * @param order order 订单
 * @return 创建成功，返回异步结果
 */
@Index
CompletableFuture<Void> indexRecentOrderFutureVoid(RecentOrderModel order);
```

> 执行index操作时，如果想自定义文档 _id，入参实体需实现IdProvider接口。
>
> 如果想自定义文档的路由值，入参实体需实现RoutingProvider接口。

#### @Index属性说明

|序号|属性名|默认值|说明|
|---|---|---|---|
|1|`timeout`|`1m`|数值+时间单位(ms/s/m/h/M/y)|
|2|`refreshPolicy`|`RefreshPolicy.NONE`|默认不刷新|
|4|`pipeline`|可选||
|5|`versionType`|`VersionType.INTERNAL`||
|6|`waitForActiveShards`|`-2`|ActiveShardCount.DEFAULT|
### Get API
#### 接口定义

```java
/**
 * 查询订单
 *
 * @param id 文档id
 * @return 订单 可能为null
 */
@Get
RecentOrder getRecentOrder(Long id);
/**
 * 查询订单
 *
 * @param id 文档id
 * @return 订单 可能为null
 */
@Get
RecentOrder getRecentOrder(String id);
/**
 * 查询订单
 *
 * @param order 订单，需实现IdProvider
 * @return 订单 可能为null
 */
@Get
RecentOrder getRecentOrder(RecentOrderModel order);
/**
 * 查询订单
 *
 * @param id 订单id
 * @return Option
 */
@Get
Optional<RecentOrder> getRecentOrderOptional(Long id);
/**
 * 查询订单
 *
 * @param id 订单id
 * @return GetResponse
 */
@Get
GetResponse getRecentOrderGetResponse(Long id);
/**
 * 查询订单
 *
 * @param id 订单id
 * @return 异步Optional
 */
@Get
CompletableFuture<Optional<RecentOrder>> getRecentOrderCompletableFuture(Long id);
/**
 * 查询订单
 *
 * @param id 订单id
 * @return 异步订单结果
 */
@Get
CompletableFuture<RecentOrder> getRecentOrderCompletableFuture(String id);
```

> 执行get操作时，如果入参非基本类型，入参实体需实现IdProvider接口。
>
> 如果想自定义文档的路由值，入参实体需实现RoutingProvider接口。

#### @Get属性说明

| 序号 | 属性名       | 默认值  | 说明                             |
| ---- | ------------ | ------- | -------------------------------- |
| 1    | `preference` | ""      | 设置查询偏好，影响查询的分片策略 |
| 2    | `refresh`    | `false` | 设置是否刷新，默认不刷新         |
| 3    | `realtime`   | `true`  | 设置是否实时查询，默认实时       |

### Delete API

#### 接口定义
```java
/**
 * 删除订单
 *
 * @param id 订单id
 * @return RestStatus
 */
@Delete
RestStatus deleteRecentOrder(Long id);
/**
 * 删除订单
 *
 * @param model model
 * @return RestStatus
 */
@Delete
RestStatus deleteRecentOrder(RecentOrderModel model);
/**
 * 删除订单
 *
 * @param id 订单id
 * @return DeleteResponse
 */
@Delete
DeleteResponse deleteRecentOrderDeleteResponse(Long id);
/**
 * 删除订单
 *
 * @param id 订单id
 * @return 删除成功，返回<code>true</code>
 */
@Delete
Boolean deleteRecentOrderBoolean(Long id);
/**
 * 删除订单
 *
 * @param id 订单id
 * @return 删除成功，返回<code>true</code>
 */
@Delete
boolean deleteRecentOrderBool(Long id);
/**
 * 删除订单
 *
 * @param id 订单id
 */
@Delete
void deleteRecentOrderVoid(Long id);
/**
 * 异步删除订单
 *
 * @param id 订单id
 * @return 异步结果
 */
@Delete
CompletableFuture<Boolean> deleteRecentOrderBooleanFuture(Long id);
/**
 * 异步删除订单
 *
 * @param id 订单id
 * @return 异步结果
 */
@Delete
CompletableFuture<Void> deleteRecentOrderVoidFuture(Long id);
```
> 执行delete操作时，如果入参非基本类型，入参实体需实现IdProvider接口。
>
> 如果想自定义文档的路由值，入参实体需实现RoutingProvider接口。

#### @Delete属性说明
|序号|属性名|默认值|说明|
|---|---|---|---|
|1|`waitForActiveShards`|活动分片数量|默认-2，不指定分片数量，-1或all指定全部分片|
|2|`timeout`|1m|数值+时间单位(ms/s/m/h/M/y)|
|3|`refreshPolicy`|`RefreshPolicy.NONE`|默认不刷新|
### Update API
#### 接口定义
```java
/**
 * 使用脚本更新订单，支持部分更新
 *
 * @param orderScript 订单
 * @return UpdateResponse
 */
@Update
UpdateResponse updateRecentOrder(RecentOrderModelScript orderScript);
/**
 * 更新订单，支持部分更新，如果文档不存在，则将部分更新文档建立索引
 *
 * @param order 订单
 * @return UpdateResponse
 */
@Update(docAsUpsert = true)
UpdateResponse updateRecentOrder(RecentOrderModel order);
/**
 * 更新订单，支持部分更新
 *
 * @param order 订单
 * @return GetResult
 */
@Update
GetResult updateRecentOrderGetResult(RecentOrderModel order);
/**
 * 更新订单，支持部分更新
 *
 * @param order 订单
 * @return RestStatus状态码
 */
@Update
RestStatus updateRecentOrderRestStatus(RecentOrderModel order);
/**
 * 更新订单，支持部分更新
 *
 * @param order 订单
 * @return 更新成功，返回<code>true</code>
 */
@Update
Boolean updateRecentOrderBoolean(RecentOrderModel order);
/**
 * 更新订单，支持部分更新
 *
 * @param order 订单
 * @return 更新成功，返回<code>true</code>
 */
@Update
boolean updateRecentOrderBool(RecentOrderModel order);
/**
 * 更新订单，支持部分更新
 *
 * @param order 订单
 * @return Result
 */
@Update
Result updateRecentOrderResult(RecentOrderModel order);
/**
 * 更新订单，支持部分更新
 *
 * @param order 订单
 */
@Update
void updateRecentOrderVoid(RecentOrderModel order);
/**
 * 异步更新订单，支持部分更新
 *
 * @param order 订单
 * @return 异步结果
 */
@Update
CompletableFuture<Result> updateRecentOrderFuture(RecentOrderModel order);
/**
 * 异步更新订单，支持部分更新
 *
 * @param order 订单
 * @return 异步结果
 */
@Update
CompletableFuture<Void> updateRecentOrderFutureVoid(RecentOrderModel order);
```
> `@Update` 注解标明，此方法是更新方法。
>
> 设置docAsUpsert=true当文档不存在时，做插入操作。
>
> 如果想自定义文档 _id，入参实体需实现IdProvider接口。

#### @Update属性说明

| 序号 | 属性名                | 默认值               | 说明                                        |
| ---- | --------------------- | -------------------- | ------------------------------------------- |
| 1    | `docAsUpsert`         | `false`              | 设置true，当文档不存在时，做插入操作        |
| 2    | `retryOnConflict`     | `0`                  | 设置版本冲突时，重试的次数                  |
| 3    | `refreshPolicy`       | `RefreshPolicy.NONE` | 默认不刷新                                  |
| 4    | `waitForActiveShards` | 活动分片数量         | 默认-2，不指定分片数量，-1或all指定全部分片 |
| 5    | `scriptedUpsert`      | `false`              | 运行脚本，无论文档是否存在                  |



## 多文档接口

### Multi Get API

#### 接口定义

```java
/**
 * 多订单查询
 *
 * @param ids 文档id
 * @return MultiGetResponse
 */
@MultiGet
MultiGetResponse getRecentOrdersResponse(Long... ids);
/**
 * 单订单查询
 *
 * @param id 文档id
 * @return MultiGetResponse
 */
@MultiGet
MultiGetResponse getRecentOrdersResponse(Long id);
/**
 * 单订单查询
 *
 * @param order 订单
 * @return MultiGetResponse
 */
@MultiGet
MultiGetResponse getRecentOrdersResponse(RecentOrderModel order);
/**
 * 多订单查询
 *
 * @param ids 订单id
 * @return 订单集合
 */
@MultiGet
List<RecentOrder> getRecentOrders(Long... ids);
/**
 * 多订单查询
 *
 * @param orders 订单
 * @return 订单数组
 */
@MultiGet
RecentOrder[] getRecentOrders(List<RecentOrderModel> orders);
/**
 * 多订单查询
 *
 * @param ids 订单id
 * @return MultiGetItemResponse[]
 */
@MultiGet
MultiGetItemResponse[] getRecentOrdersItemResponse(Long... ids);
/**
 * 多订单查询
 *
 * @param orders 订单id
 * @return List<MultiGetItemResponse>
 */
@MultiGet
List<MultiGetItemResponse> getRecentOrdersItemResponse(List<RecentOrderModel> orders);
/**
 * 多订单查询
 *
 * @param ids 订单id
 * @return List<Optional < RecentOrder>>
 */
@MultiGet
List<Optional<RecentOrder>> getRecentOrdersOptional(Long... ids);
/**
 * 多订单查询
 *
 * @param orders 订单
 * @return Optional<RecentOrder>[]
 */
@MultiGet
Optional<RecentOrder>[] getRecentOrdersOptional(List<RecentOrderModel> orders);
/**
 * 多订单异步查询
 *
 * @param orders 订单
 * @return 异步订单查询结果
 */
@MultiGet
CompletableFuture<Optional<RecentOrder>[]> getRecentOrdersOptionalFuture(List<RecentOrderModel> orders);
```



### Bulk API
Bulk接口目前只支持但类型批量操作，也即是要全全部是索引操作，要不全部是删除操作等等。
#### 接口定义
```java
/**
 * 批量创建商品文档
 *
 * @param products 商品
 * @return 响应
 */
@Bulk(bulkType = BulkType.INDEX, index = @Index(id = "id"))
CompletableFuture<BulkResponse> bulkIndexProducts(Product[] products);
```
> `@Bulk` 注解标明，此方法是批量方法，必须要有此注解，同时必须要指定`BulkType`，标明是何种类型的批量操作

#### @Bulk属性说明
|序号|属性名|默认值|说明|
|---|---|---|---|
|1|`bulkType`|必填|可选类型：`INDEX`/`DELETE`/`UPDATE`|
|2|`timeout`|1m|数值+时间单位(ms/s/m/h/M/y)|
|3|`waitForActiveShards`|-2|ActiveShardCount.DEFAULT|
|4|`refreshPolicy`|`RefreshPolicy.NONE`||
|5|`index`|可选|`bulkType` = `INDEX` 有效|
|6|`delete`|可选|`bulkType` = `DELETE` 有效|
|7|`update`|可选|`bulkType` = `UPDATE` 有效|
### Delete By Query API
#### 接口定义
```java
/**
 * 根据指定条件删除文档
 *
 * @param condition 查询条件
 * @return 查询删除响应
 */
@DeleteByQuery
BulkByScrollResponse deleteByQuery(ProductCondition condition);
```
> `@DeleteByQuery` 注解标明，此方法是查询删除方法，必须要有此注解

#### @DeleteByQuery属性说明
|序号|属性名|默认值|说明|
|---|---|---|---|
|1|`routing`|可选|默认无路由|
|2|`timeout`|1m|数值+时间单位(ms/s/m/h/M/y)|
|3|`waitForActiveShards`|-2|ActiveShardCount.DEFAULT|
|4|`refresh`|`false`||
|5|`maxDocs`|-1|最大处理文档数，超过此文档数量，就不在处理，默认-1，是全部文档|
|6|`batchSize`|1000|批量大小|
|7|`conflicts`|`abort`|冲突策略：`abort`/`procced`|
|8|`slices`|1|分片|
|9|`maxRetries`|11|冲突后，最大重试次数|
|10|`shouldStoreResult`|`false`||
|11|`scrollKeepAlive`|0||
### Update By Query API
#### 接口定义
```java
@UpdateByQuery
CompletableFuture<BulkByScrollResponse> updateByQuery(Product document);
```
> `@UpdateByQuery` 注解标明，此方法是查询更新方法，必须要有此注解

#### @UpdateByQuery属性说明
|序号|属性名|默认值|说明|
|---|---|---|---|
|1|`routing`|可选|默认无路由|
|2|`timeout`|1m|数值+时间单位(ms/s/m/h/M/y)|
|3|`waitForActiveShards`|-2|ActiveShardCount.DEFAULT|
|4|`refresh`|`false`||
|5|`maxDocs`|-1|最大处理文档数，超过此文档数量，就不在处理，默认-1，是全部文档|
|6|`batchSize`|1000|批量大小|
|7|`conflicts`|`abort`|冲突策略：`abort`/`procced`|
|8|`slices`|1|分片|
|9|`maxRetries`|11|冲突后，最大重试次数|
|10|`shouldStoreResult`|`false`||
|11|`scrollKeepAlive`|0||
### Reindex API
> **暂不支持**

# 查询DSL
为了便宜统一设计，所有的查询语句最后都被封装成`BoolQueryBuilder`，查询条件最终会被封装成为一个有层级关系的对象，来处理。查询操作统一由`@Search`注解来标记，支持分页查询。
## 查询条件定义
### 基本条件定义
查询条件统一定义一个POJO对象，对象的属性名即为Mapping字段名称，如果属性名称和Mapping字段名称不一致，通过`@Field`注解来映射；属性分为基本类型和对象类型，对象类型会再次递归定义查询条件。
### 查询语句类型（Query Clause)
语句类型通过属性注解来表示，详细如下面:

|序号|注解|说明|
|---|---|---|
|1|`@Must`|必须满足的条件|
|2|`@MustNot`|必须排除的条件|
|3|`@Should`|可选条件|
|4|`@Filter`|过滤条件|
|5|`@Ignore`|忽略的条件，不参与语句拼装|
|6|`@Exists`|字段是否存在|

> 如果属性不加注解，默认就是`@Must`条件，语句支持嵌套属性

## @Query属性说明
|序号|属性名|默认值|说明|
|---|---|---|---|
|1|`queryType`|`QueryType.BOOL`|默认Bool查询，可选类型：`BOOL`/`FUNCTION_SCORE`/`CONSTANT_SCORE`|
|2|`searchType`|`SearchType.QUERY_THEN_FETCH`|默认就好|
|3|`routing`|可选|默认无路由|
## Bool查询
### 接口定义
#### 条件定义
```java
/**
* @author 章多亮
* @since 2019/12/25 14:12
*/
@Data
public class DefaultStrategy {
    private Integer expireTime;
    private Integer refreshCycle;
    private Integer startTime;
}

/**
 * @author 章多亮
 * @since 2019/12/25 14:11
 */
@Data
public class Display {
    @Must(nested = true)
    private DefaultStrategy defaultStrategy;
}

/**
 * @author 章多亮
 * @since 2019/12/25 14:16
 */
@Data
public class Coordinate {
    private Double lat;
    private Double lon;
}

/**
* @author 章多亮
* @since 2019/12/25 14:14
*/
@Data
public class Location {
    private String address;
    private String[] h3Locations;
    @Field("location")
    @MustNot(nested = true)
    private Coordinate coordinate;
}

/**
 * @author 章多亮
 * @since 2019/12/19 18:39
 */
@Data
public class CargoCondition implements ScriptFieldProvider {
    @Field("cargoChannel")
    private Integer[] cargoChannels;
    @Exists
    private Long cargoId;
    private String[] cargoLines;
    @Should
    private Long createTime;
    @Must(nested = true)
    private Display display;
    @MustNot(nested = true)
    private Location endLocation;
    @Must(nested = true)
    private Location startLocation;

    @Override
    public ScriptField[] getScriptFields() {
        return new ScriptField[0];
    }
}

/**
 * @author 章多亮
 * @since 2019/12/19 18:36
 */
@Data
public class Cargo {
    private Long cargoId;
    private Long cargoUserId;
    private Date createTime;
    private Integer cargoType;
}
```

#### 接口定义
```java
/**
 * 搜索货源
 *
 * @param searchable 是否可以搜索
 * @param condition  搜索条件
 * @param pageable   分页信息
 * @return 货源分页
 */
@Search(queryType = QueryType.BOOL)
CompletableFuture<Page<Cargo>> search(Boolean searchable, CargoCondition condition, Pageable pageable);
```

### 接口使用示例
```java
DefaultStrategy strategy = new DefaultStrategy();
strategy.setExpireTime(-1);
strategy.setRefreshCycle(1740000);
strategy.setStartTime(0);

Display display = new Display();
display.setDefaultStrategy(strategy);

Coordinate coordinate = new Coordinate();
coordinate.setLng(115.03885);
coordinate.setLat(36.769641);

Location startLocation = new Location();
startLocation.setH3Locations(new String[]{"833198fffffffff", "84319e3ffffffff", "85319e27fffffff", "86319e26fffffff"});
startLocation.setCoordinate(coordinate);

CargoCondition condition = new CargoCondition();
condition.setCargoChannels(new Integer[]{20});
condition.setCargoLines(new String[]{"220000_130000", "220000_130400"});
condition.setCreateTime(1576636761249L);
condition.setStartLocation(startLocation);
condition.setDisplay(display);

AtomicBoolean finished = new AtomicBoolean();
CargoRepository repository = createEsMapper(CargoRepository.class);
repository.search(true, condition, Pageable.of(0, 10)).whenComplete(((cargoes, throwable) -> {
   if (throwable == null) {
       cargoes.forEach(cargo -> log.info("{}", cargo));
   } else {
       log.error("查询失败", throwable);
   }
   finished.set(true);
}));
Awaitility.await().forever().untilTrue(finished);
```

上面的POJO条件最终会被映射为如下DSL：

```json
{
 "from": 0,
 "size": 10,
 "query": {
   "bool": {
     "must": [
       {
         "term": {
           "searchable": {
             "value": true,
             "boost": 1
           }
         }
       },
       {
         "bool": {
           "must": [
             {
               "term": {
                 "cargoChannel": {
                   "value": 20,
                   "boost": 1
                 }
               }
             },
             {
               "terms": {
                 "cargoLines": [
                   "220000_130000",
                   "220000_130400"
                 ],
                 "boost": 1
               }
             },
             {
               "term": {
                 "createTime": {
                   "value": 1576636761249,
                   "boost": 1
                 }
               }
             },
             {
               "bool": {
                 "must": [
                   {
                     "bool": {
                       "must": [
                         {
                           "term": {
                             "display.defaultStrategy.expireTime": {
                               "value": -1,
                               "boost": 1
                             }
                           }
                         },
                         {
                           "term": {
                             "display.defaultStrategy.refreshCycle": {
                               "value": 1740000,
                               "boost": 1
                             }
                           }
                         },
                         {
                           "term": {
                             "display.defaultStrategy.startTime": {
                               "value": 0,
                               "boost": 1
                             }
                           }
                         }
                       ],
                       "adjust_pure_negative": true,
                       "boost": 1
                     }
                   }
                 ],
                 "adjust_pure_negative": true,
                 "boost": 1
               }
             },
             {
               "bool": {
                 "must": [
                   {
                     "terms": {
                       "startLocation.h3Locations": [
                         "833198fffffffff",
                         "84319e3ffffffff",
                         "85319e27fffffff",
                         "86319e26fffffff"
                       ],
                       "boost": 1
                     }
                   }
                 ],
                 "must_not": [
                   {
                     "bool": {
                       "must": [
                         {
                           "term": {
                             "startLocation.location.lat": {
                               "value": 36.769641,
                               "boost": 1
                             }
                           }
                         },
                         {
                           "term": {
                             "startLocation.location.lng": {
                               "value": 115.03885,
                               "boost": 1
                             }
                           }
                         }
                       ],
                       "adjust_pure_negative": true,
                       "boost": 1
                     }
                   }
                 ],
                 "adjust_pure_negative": true,
                 "boost": 1
               }
             }
           ],
           "adjust_pure_negative": true,
           "boost": 1
         }
       }
     ],
     "adjust_pure_negative": true,
     "boost": 1
   }
 },
 "_source": {
   "includes": [
     "cargoId",
     "cargoUserId",
     "createTime",
     "cargoType"
   ],
   "excludes": []
 }
}
```

## Constant Score查询
### 接口定义
```java
@Search(queryType = QueryType.CONSTANT_SCORE)
CompletableFuture<List<Cargo>> constantSearch(CargoCondition condition);
```
## Function Score查询
函数打分查询，允许我们修改文档的相关度分值，通过提供一个或多个函数来计算出查询出来的文档新分值。因此，Function Score查询条件必须要提供打分函数，`ebatis`要求，条件必须实现`ScoreFunctionProvider`接口。此接口，有两个接口方法，默认实现`getFunction`方法即可。
### 查询条件POJO定义
```java

/**
 * @author 章多亮
 * @since 2019/12/25 17:37
 */
@Data
public class FunctionCargoCondition implements ScoreFunctionProvider, ScriptFieldProvider {
    @Field("cargoChannel")
    private Integer[] channels;
    private Long createTime;
    @Must(nested = true)
    private Display display;
    @MustNot(nested = true)
    private Location endLocation;
    @Must(nested = true)
    private Location startLocation;

    @Ignore
    private DefaultStrategy strategy;
    @Ignore
    private Coordinate position;

    @Override
    public ScoreFunction getFunction() {
        return ScoreFunction.storedScript("score-script-id", strategy);
    }

    @Override
    public ScriptField[] getScriptFields() {
        Objects.requireNonNull(position);
        return new ScriptField[]{
                ScriptField.of("endPoint", Script.stored("searchCargoIdList-endLocation-script")),
                ScriptField.of("startPoint", Script.inline("Hello World.", position))};
    }

}
```

> `ScoreFunctionProvider` 即为打分函数提供者接口，函数打分查询，必须继承此接口；`ScriptFieldProvider` 此接口为脚本字段提供者接口。

### 接口定义
```java
/**
 * 函数搜索货源
 *
 * @param condition 查询条件
 * @return 货源分页
 */
@Search(queryType = QueryType.FUNCTION_SCORE, )
CompletableFuture<List<Cargo>> functionSearch(FunctionCargoCondition condition);
```

## Boosting查询
> **暂不支持**

## Dis Max查询
> **暂不支持**

## 分页查询
分页查询需要增加一个新的入参`Pageable`，此入参，必须是方法最后一个参数。
### Pagable创建

```java
int page = 0; // 页码，从0开始
int size = 20; // 分页大小
Pageable pageale = Pageable.of(page, size);
```

### 分页查询接口定义

```java
/**
 * 函数分页搜索货源
 *
 * @param condition 查询条件
 * @param pageable 分页信息
 * @return 货源分页
 */
@Search(queryType = QueryType.FUNCTION_SCORE, functionScore = @FunctionScore(scoreType = ScoreType.SCRIPT_SCORE))
CompletableFuture<Page<Cargo>> functionSearch(FunctionCargoCondition condition, Pageable pageable);

/**
 * 分页搜索货源
 *
 * @param condition 查询条件
 * @param pageable 分页信息
 * @return 货源分页
 */
@Search(queryType = QueryType.BOOL)
Page<Cargo> boolSearch(CargoCondition condition, Pageable pageable);
```

> 分页查询，返回值必须是`Page`类型

## 各种Provider

* `MissingProvider`
* `ScoreFunctionProvider`
* `ScriptFieldProvider`
* `ScriptProvider`
* `SortProvider`
* `SortScriptProvider`
* `VersionProvider`
* `SourceProvider`

## 排序

### 普通排序
普通排序需要实现`SortProvider`接口。

```java
public class CargoCondition implements SortProvider {
    private static final Sort[] SORTS = {Sort.asc("createTime", "cargoChannel")};

    @Override
    public Sort[] getSorts() {
        return SORTS;
    }
}
```

### 脚本排序
脚本排序需要实现`SortScriptProvider`接口。

```java
@Data
public class CargoCondition implements SortScriptProvider {
    @Field(name = "cargoChannel")
    private Integer[] channels;
    private Boolean searchable;

    @Ignore
    private String cargoLabel;

    @Override
    public SortScript[] getSortScripts() {
        Script script = Script.inline("ctx._source.");
        return new SortScript[]{SortScript.string(script)};
    }

}
```

## Script Field 脚本字段
脚本字段需要实现`ScriptFieldProvider`接口。

```java
/**
 * @author 章多亮
 * @since 2019/12/19 18:39
 */
@Data
public class CargoCondition implements ScriptFieldProvider {
    @Field("cargoChannel")
    private Integer[] cargoChannels;
    private Long cargoId;
    private String[] cargoLines;
    private Long createTime;
    @Ignore
    private Display display;

    @Override
    public ScriptField[] getScriptFields() {
        Script script = Script.stored("script-id", display);
        return new ScriptField[]{ScriptField.of("display", script)};
    }
}
```

## 指定返回字段
自定返回字段，可以通过两种方式实现：

> 实现`SourceProvider`接口

```java
public class ProductCondition implements SourceProvider {
    private static final String[] INCLUDE_FIELDS = {"id", "name"};
    private static final String[] EXCLUDE_FIELDS = {"label"};
    /**
     * 获取包含的字段列表
     *
     * @return 字段列表
     */
     @Override
    public String[] getIncludeFields() {
      return INCLUDE_FIELDS;
    }

    /**
     * 获取排除的字段列表
     *
     * @return 字段列表
     */
     @Override
    public String[] getExcludeFields() {
        return EXCLUDE_FIELDS;
    }
}
```

> 返回值实体类型定义

```java
@Data
public class Product {
    private Long id;
    private String name;
    @Ignore
    private String label;
}
```

> 此返回值类型，说明返回的字段为：`["id", "name"]`

## 查询接口返回类型

定义Mapper接口，对返回值类型有一定的要求，可以在一下指定的类型中，选择可以满足业务需求的类型返回，`T` 是业务对象类型。

|序号|返回类型|异步已否|说明|
|---|---|---|---|
|1|`Optional<T>`|同步|可选返回值类型，单文档返回，此返回值说明，查询会返回最多一个文档|
|2|`Optional<List<T>>`|同步| 可选返回列表，多文档返回，此返回值说明，查询会返回零个或多个文档|
|3|`T`|同步|单文档返回类型|
|4|`List<T>`|同步|多文档返回类型|
|5|`Page<T>`|同步|分页多文档返回类型|
|6|`CompletableFuture<T>`|异步|异步返回值类型，单文档返回，此返回值说明，异步返回最多一个文档|
|7|`CompletableFuture<List<T>>`|异步|多文档返回类型|
|7|`CompletableFuture<Page<T>>`|异步|分页多文档返回类型|
|8|`void`|同步|无返回值|


