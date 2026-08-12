package io.manbang.ebatis.core.provider;

/**
 * _seq_no和 _primary_term是两个非常重要的元数据字段，它们共同协作，在分布式环境下为文档提供乐观并发控制（Optimistic Concurrency Control, OCC），确保数据更新的一致性和安全性。
 */
public interface ReplicaVersionProvider extends Provider {
    /**
     * 主分片分配的、全局递增的序列号。它主要用于确保操作在主分片和副本分片间的顺序一致性
     *
     * @return 序列号
     */
    long seqNo();

    /**
     * 当前主分片的任期
     *
     * @return 主分片任期
     */
    long primaryTerm();
}
