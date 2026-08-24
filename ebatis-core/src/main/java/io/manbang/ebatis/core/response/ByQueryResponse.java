package io.manbang.ebatis.core.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.BulkByScrollTask;
import org.elasticsearch.index.reindex.ScrollableHitSource;

import java.util.Collections;
import java.util.List;

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
    private String reasonCancelled;
    private List<BulkItemResponse.Failure> bulkFailures = Collections.emptyList();
    private List<ScrollableHitSource.SearchFailure> searchFailures = Collections.emptyList();

    public static ByQueryResponse from(BulkByScrollResponse response) {
        BulkByScrollTask.Status status = response.getStatus();
        ByQueryResponse result = new ByQueryResponse();
        result.setTook(response.getTook().millis());
        result.setTimeout(response.isTimedOut());
        result.setTotal(response.getTotal());
        result.setDeleted(response.getDeleted());
        result.setCreated(response.getCreated());
        result.setUpdated(response.getUpdated());
        result.setBatches(response.getBatches());
        result.setVersionConflicts(response.getVersionConflicts());
        result.setNoops(response.getNoops());

        Retries responseRetries = new Retries();
        responseRetries.setBulk(response.getBulkRetries());
        responseRetries.setSearch(response.getSearchRetries());
        result.setRetries(responseRetries);

        if (status != null) {
            result.setThrottledMillis(status.getThrottled().millis());
            result.setRequestsPerSecond(status.getRequestsPerSecond());
            result.setThrottledUntilMillis(status.getThrottledUntil().millis());
            result.setReasonCancelled(status.getReasonCancelled());
        }
        result.setBulkFailures(response.getBulkFailures());
        result.setSearchFailures(response.getSearchFailures());
        return result;
    }

    public long getBulkRetries() {
        return retries == null ? 0 : retries.getBulk();
    }

    public long getSearchRetries() {
        return retries == null ? 0 : retries.getSearch();
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
