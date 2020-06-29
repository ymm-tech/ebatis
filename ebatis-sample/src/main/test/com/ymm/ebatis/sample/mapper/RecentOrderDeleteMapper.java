package com.ymm.ebatis.sample.mapper;

import com.ymm.ebatis.core.annotation.Delete;
import com.ymm.ebatis.spring.annotation.EasyMapper;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.rest.RestStatus;

/**
 * @author weilong.hu
 */
@EasyMapper(indices = "recent_order_index")
public interface RecentOrderDeleteMapper {
    //delete RestStatus Long
    @Delete
    RestStatus deleteRecentOrder(Long id);

    //delete DeleteResponse Long
    @Delete
    DeleteResponse deleteRecentOrderDeleteResponse(Long id);

    //delete Boolean Long
    @Delete
    Boolean deleteRecentOrderBoolean(Long id);

    //delete boolean Long
    @Delete
    boolean deleteRecentOrderBool(Long id);

    //delete void Long
    @Delete
    void deleteRecentOrderVoid(Long id);
}
