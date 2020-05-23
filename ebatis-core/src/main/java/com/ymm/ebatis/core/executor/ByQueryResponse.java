package com.ymm.ebatis.core.executor;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.reindex.BulkByScrollTask;

/**
 * @author duoliang.zhang
 */
@Data
public class ByQueryResponse {
    private long took;
    @JSONField(name = "timed_out")
    private boolean timeout;
    private long total;
    private long deleted;
    private long created;
    private long updated;
    private int batches;
    @JSONField(name = "version_conflicts")
    private long versionConflicts;
    private long noops;
    private Retries retries;
    @JSONField(name = "throttled_millis")
    private long throttledMillis;
    @JSONField(name = "requests_per_second")
    private float requestsPerSecond;
    @JSONField(name = "throttled_until_millis")
    private long throttledUntilMillis;

    public long getBulkRetries() {
        return retries.getBulk();
    }

    public long getSearchRetries() {
        return retries.getSearch();
    }

    public TimeValue getThrottled() {
        return TimeValue.timeValueMillis(throttledMillis);
    }

    public TimeValue getThrottledUntil() {
        return TimeValue.timeValueMillis(throttledUntilMillis);
    }

    public BulkByScrollTask.Status getStatus() {
        return new BulkByScrollTask.Status(null, total, updated, created, deleted, batches, versionConflicts, noops,
                getBulkRetries(), this.getSearchRetries(), this.getThrottled(), this.getRequestsPerSecond(), null, this.getThrottledUntil());
    }
}
