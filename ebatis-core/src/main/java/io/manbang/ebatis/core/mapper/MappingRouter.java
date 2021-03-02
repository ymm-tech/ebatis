package io.manbang.ebatis.core.mapper;

/**
 * 此接口用来路由到对应的 Mapping，有时候，我们需要在执行ES请求的时候，才能确认要到那个Index执行操作
 * 比如，按月归档的ES，月份不同，索引就不同；别名方式虽然能解决名称不同的问题，但是，如果仍然要对就的索引操作
 * 可能会有问题，此接口让开发人员可以更灵活的控制执行索引
 *
 * @author weilong.hu
 * @since 2021/2/3 14:39
 */
public interface MappingRouter {
    String[] _TYPES = new String[0];

    String[] indices();

    default String[] types() {
        return _TYPES;
    }
}
