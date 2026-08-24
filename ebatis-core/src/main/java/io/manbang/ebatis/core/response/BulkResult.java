package io.manbang.ebatis.core.response;

import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class BulkResult {
    private final long tookMillis;
    private final int total;
    private final int succeeded;
    private final List<Failure> failures;

    private BulkResult(long tookMillis, int total, int succeeded, List<Failure> failures) {
        this.tookMillis = tookMillis;
        this.total = total;
        this.succeeded = succeeded;
        this.failures = Collections.unmodifiableList(failures);
    }

    public static BulkResult from(BulkResponse response) {
        List<Failure> failures = new ArrayList<>();
        for (BulkItemResponse item : response) {
            if (item.isFailed()) {
                failures.add(new Failure(item.getItemId(), item.getIndex(), item.getId(),
                        item.status().getStatus(), item.getFailureMessage()));
            }
        }
        return new BulkResult(response.getTook().millis(), response.getItems().length,
                response.getItems().length - failures.size(), failures);
    }

    public long getTookMillis() { return tookMillis; }
    public int getTotal() { return total; }
    public int getSucceeded() { return succeeded; }
    public List<Failure> getFailures() { return failures; }
    public boolean isSuccessful() { return failures.isEmpty(); }

    public static final class Failure {
        private final int itemId;
        private final String index;
        private final String id;
        private final int status;
        private final String message;

        private Failure(int itemId, String index, String id, int status, String message) {
            this.itemId = itemId;
            this.index = index;
            this.id = id;
            this.status = status;
            this.message = message;
        }

        public int getItemId() { return itemId; }
        public String getIndex() { return index; }
        public String getId() { return id; }
        public int getStatus() { return status; }
        public String getMessage() { return message; }
    }
}
