package com.ymm.ebatis.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ymm.ebatis.executor.Retries;
import lombok.Data;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.reindex.BulkByScrollTask;

/**
 * @author duoliang.zhang
 */
@Data
public class ByQueryResponse {
    private long took;
    @JsonProperty("timed_out")
    private boolean timeout;
    private long total;
    private long deleted;
    private long created;
    private long updated;
    private int batches;
    @JsonProperty("version_conflicts")
    private long versionConflicts;
    private long noops;
    private Retries retries;
    @JsonProperty("throttled_millis")
    private long throttledMillis;
    @JsonProperty("requests_per_second")
    private float requestsPerSecond;
    @JsonProperty("throttled_until_millis")
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
