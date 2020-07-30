package io.manbang.ebatis.core.domain;

import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoValidationMethod;

import java.util.List;

/**
 * @author 章多亮
 * @since 2020/1/6 19:20
 */
public interface GeoDistanceSort extends Sort {

    /**
     * 获取Geo距离计算类型
     *
     * @return Geo距离类型
     */
    GeoDistance geoDistance();

    /**
     * 设置Geo距离计算类型
     *
     * @param distance Geo距离类型
     * @return 距离排序
     */
    GeoDistanceSort geoDistance(GeoDistance distance);

    /**
     * 获取距离单位
     *
     * @return 单位
     */
    DistanceUnit unit();

    /**
     * 设置距离单位
     *
     * @param unit 单位
     * @return 距离排序
     */
    GeoDistanceSort unit(DistanceUnit unit);

    /**
     * 获取验证方
     *
     * @return 验证方法
     */
    GeoValidationMethod validation();

    /**
     * 设置验证方法
     *
     * @param validationMethod 验证方法
     * @return 距离排序
     */
    GeoDistanceSort validation(GeoValidationMethod validationMethod);


    /**
     * 判断是否忽略为映射字段
     *
     * @return 忽略未映射字段，返回<code>true</code>
     */
    boolean ignoreUnmapped();

    /**
     * 设置是否忽略为映射字段
     *
     * @param ignoreUnmapped 是否忽略未映射
     * @return 距离排序
     */
    GeoDistanceSort ignoreUnmapped(boolean ignoreUnmapped);

    /**
     * 获取地理位置坐标
     *
     * @return 地理坐标列表
     */
    List<GeoPoint> points();

    /**
     * 增加位置坐标
     *
     * @param point 地理坐标列表
     * @return 距离排序
     */
    GeoDistanceSort addPoint(GeoPoint point);

    /**
     * 增加位置坐标
     *
     * @param lat 经度
     * @param lon 纬度
     * @return 距离排序
     */
    GeoDistanceSort addPoint(double lat, double lon);

    /**
     * 增加位置坐标
     *
     * @param value GeoHash 或者 geo tuple
     * @return 距离排序
     */
    GeoDistanceSort addPoint(String value);
}
