package com.ymm.ebatis.request;

import com.ymm.ebatis.meta.MethodMeta;
import org.elasticsearch.action.ActionRequest;

/**
 * # 请求工厂接口，根据请求的方法定义和实参，创建ES请求
 *
 * @author 章多亮
 * @since 2020/5/14 11:16
 */
public interface RequestFactory {
    /**
     * 创建ES请求，根据注解对应创建不同的ES请求
     *
     * @param method 接口方法
     * @param args   实参
     * @param <R>    请求类型
     * @return ES请求
     * @see org.elasticsearch.action.search.SearchRequest 搜索请求
     * @see org.elasticsearch.action.index.IndexRequest 索引请求
     * @see org.elasticsearch.action.update.UpdateRequest 更新请求
     * @see org.elasticsearch.action.delete.DeleteRequest 删除请求
     */
    <R extends ActionRequest> R create(MethodMeta method, Object... args);
}
