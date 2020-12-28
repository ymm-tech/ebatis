# ebatis是什么

`ebatis`采用和`MyBatis`类似思想，只需要定义接口，便可访问`elasticsearch`，隔离业务对`elasticserach`底层接口的直接访问。如此以来，数据访问的时候，不需要自己手动去构建DSL语句，同时，当升级`elastisearch
`版本的时候，业务可以完全不用关心底层接口的变动，平滑升级（目前支持elastisearch 6.5.1与7.5.1版本）。

# ebatis现状

目前`ebatis`已经在满帮业务系统上稳定运行近一年，承载着每日上亿次搜索服务。

# ebatis入门及相关文章

https://github.com/ymm-tech/ebatis/wiki
https://www.infoq.cn/article/u4Xhw5Q3jfLE1brGhtbR
https://mp.weixin.qq.com/s/GFRiiQEk-JLpPnCi_WrRqw

## 交流群

> 钉钉 
 
<img src="https://github.com/codingPao/ymm-tech/blob/main/ebatisDingDing.JPG?raw=true" width="350px">


## 支持我们

### 你可以打赏我们 :coffee: 来杯咖啡 :coffee:

<img src="https://github.com/codingPao/ymm-tech/blob/main/coffee.JPG?raw=true" width="360px">

### 也可以点个 Star
开源项目需要的是持续地坚持，而我们坚持的动力当然也来自于你们的支持，希望你 :point_right: `来都来了，加个关注再走吧` :point_left: