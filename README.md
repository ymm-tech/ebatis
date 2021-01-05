# ebatis是什么

`ebatis`采用和`MyBatis`类似思想，只需要定义接口，便可访问`elasticsearch`，隔离业务对`elasticserach`底层接口的直接访问。如此以来，数据访问的时候，不需要自己手动去构建DSL语句，同时，当升级`elastisearch
`版本的时候，业务可以完全不用关心底层接口的变动，平滑升级（目前支持elastisearch 6.5.1与7.5.1版本）。

# ebatis现状

目前`ebatis`已经在满帮业务系统上稳定运行近一年，承载着每日上亿次搜索服务。

# quick start
POM依赖（目前也支持6.5.1.2.RELEASE）
```xml
<dependency>
     <groupId>io.manbang</groupId>
     <artifactId>ebatis-core</artifactId>
     <version>7.5.1.4.RELEASE</version>
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
ebatis版本使用xx.xx.xx.xx.RELEASE表示，前三位代表Elasticsearch适配集群的驱动版本，后一位代表ebatis在此版本上的迭代。例如7.5.1.3.RELEASE表示ebatis在Elasticsearch 7.5.1版本上迭代的第三次版本。

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