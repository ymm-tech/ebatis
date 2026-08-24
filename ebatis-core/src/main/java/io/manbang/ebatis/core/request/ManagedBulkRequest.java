package io.manbang.ebatis.core.request;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.core.TimeValue;

/** Bulk request carrying Ebatis client-side execution options. */
public final class ManagedBulkRequest extends BulkRequest {
    private int batchSize = 1000;
    private int concurrentRequests = 1;
    private int maxRetries = 3;
    private TimeValue retryBackoff = TimeValue.timeValueMillis(100);

    public int getBatchSize() {
        return batchSize;
    }

    public ManagedBulkRequest setBatchSize(int batchSize) {
        if (batchSize <= 0) {
            throw new IllegalArgumentException("Bulk.batchSize must be positive");
        }
        this.batchSize = batchSize;
        return this;
    }

    public int getConcurrentRequests() {
        return concurrentRequests;
    }

    public ManagedBulkRequest setConcurrentRequests(int concurrentRequests) {
        if (concurrentRequests <= 0) {
            throw new IllegalArgumentException("Bulk.concurrentRequests must be positive");
        }
        this.concurrentRequests = concurrentRequests;
        return this;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public ManagedBulkRequest setMaxRetries(int maxRetries) {
        if (maxRetries < 0) {
            throw new IllegalArgumentException("Bulk.maxRetries must not be negative");
        }
        this.maxRetries = maxRetries;
        return this;
    }

    public TimeValue getRetryBackoff() {
        return retryBackoff;
    }

    public ManagedBulkRequest setRetryBackoff(TimeValue retryBackoff) {
        this.retryBackoff = retryBackoff;
        return this;
    }
}
