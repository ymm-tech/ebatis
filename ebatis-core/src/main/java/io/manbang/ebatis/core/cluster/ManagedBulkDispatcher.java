package io.manbang.ebatis.core.cluster;

import io.manbang.ebatis.core.request.ManagedBulkRequest;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

final class ManagedBulkDispatcher {
    private static final ScheduledExecutorService RETRY_EXECUTOR = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, "ebatis-bulk-retry");
            thread.setDaemon(true);
            return thread;
        }
    });

    private ManagedBulkDispatcher() {
    }

    static void execute(RestHighLevelClient client, ManagedBulkRequest request, ActionListener<BulkResponse> listener) {
        List<DocWriteRequest<?>> actions = request.requests();
        if (actions.isEmpty()) {
            listener.onResponse(new BulkResponse(new BulkItemResponse[0], 0L));
            return;
        }

        int batchSize = request.getBatchSize();
        int batchCount = (actions.size() + batchSize - 1) / batchSize;
        AtomicInteger nextBatch = new AtomicInteger();
        AtomicInteger completed = new AtomicInteger();
        AtomicLong tookMillis = new AtomicLong();
        AtomicBoolean terminal = new AtomicBoolean();
        BulkItemResponse[] items = new BulkItemResponse[actions.size()];

        Runnable[] worker = new Runnable[1];
        worker[0] = () -> {
            if (terminal.get()) {
                return;
            }
            int batch = nextBatch.getAndIncrement();
            if (batch >= batchCount) {
                return;
            }
            int from = batch * batchSize;
            int to = Math.min(actions.size(), from + batchSize);
            BulkRequest chunk = copyOptions(request);
            chunk.add(actions.subList(from, to));
            executeWithRetry(client, request, chunk, 0, ActionListener.wrap(response -> {
                tookMillis.addAndGet(response.getTook().millis());
                BulkItemResponse[] chunkItems = response.getItems();
                for (int i = 0; i < chunkItems.length; i++) {
                    BulkItemResponse item = chunkItems[i];
                    items[from + i] = item.isFailed()
                            ? BulkItemResponse.failure(from + i, item.getOpType(), item.getFailure())
                            : BulkItemResponse.success(from + i, item.getOpType(), item.getResponse());
                }
                if (completed.incrementAndGet() == batchCount && terminal.compareAndSet(false, true)) {
                    listener.onResponse(new BulkResponse(items, tookMillis.get()));
                } else {
                    worker[0].run();
                }
            }, failure -> {
                if (terminal.compareAndSet(false, true)) {
                    listener.onFailure(failure);
                }
            }));
        };

        int concurrency = Math.min(request.getConcurrentRequests(), batchCount);
        for (int i = 0; i < concurrency; i++) {
            worker[0].run();
        }
    }

    private static void executeWithRetry(RestHighLevelClient client, ManagedBulkRequest managed, BulkRequest request,
                                         int attempt, ActionListener<BulkResponse> listener) {
        client.bulkAsync(request, RequestOptions.DEFAULT, ActionListener.wrap(response -> {
            if (!response.hasFailures() || attempt >= managed.getMaxRetries()) {
                listener.onResponse(response);
                return;
            }
            BulkRequest retry = copyOptions(request);
            java.util.List<Integer> positions = new java.util.ArrayList<>();
            BulkItemResponse[] currentItems = response.getItems();
            for (int i = 0; i < currentItems.length; i++) {
                if (currentItems[i].isFailed() && isRetryable(currentItems[i].status().getStatus())) {
                    retry.add(request.requests().get(i));
                    positions.add(i);
                }
            }
            if (positions.isEmpty()) {
                listener.onResponse(response);
                return;
            }
            long delay = retryDelay(managed, attempt);
            RETRY_EXECUTOR.schedule(() -> executeWithRetry(client, managed, retry, attempt + 1,
                    ActionListener.wrap(retryResponse -> {
                        BulkItemResponse[] merged = currentItems.clone();
                        for (int i = 0; i < retryResponse.getItems().length; i++) {
                            int position = positions.get(i);
                            BulkItemResponse item = retryResponse.getItems()[i];
                            merged[position] = item.isFailed()
                                    ? BulkItemResponse.failure(position, item.getOpType(), item.getFailure())
                                    : BulkItemResponse.success(position, item.getOpType(), item.getResponse());
                        }
                        listener.onResponse(new BulkResponse(merged,
                                response.getTook().millis() + retryResponse.getTook().millis()));
                    }, listener::onFailure)), delay, TimeUnit.MILLISECONDS);
        }, failure -> {
            if (attempt >= managed.getMaxRetries()) {
                listener.onFailure(failure);
                return;
            }
            long delay = retryDelay(managed, attempt);
            RETRY_EXECUTOR.schedule(
                    () -> executeWithRetry(client, managed, request, attempt + 1, listener),
                    delay, TimeUnit.MILLISECONDS);
        }));
    }

    private static long retryDelay(ManagedBulkRequest request, int attempt) {
        return request.getRetryBackoff().millis() * (1L << Math.min(attempt, 20));
    }

    private static boolean isRetryable(int status) {
        return status == 429 || status == 502 || status == 503 || status == 504;
    }

    private static BulkRequest copyOptions(BulkRequest source) {
        BulkRequest target = new BulkRequest()
                .setRefreshPolicy(source.getRefreshPolicy())
                .timeout(source.timeout())
                .waitForActiveShards(source.waitForActiveShards());
        if (source.pipeline() != null) {
            target.pipeline(source.pipeline());
        }
        if (source.routing() != null) {
            target.routing(source.routing());
        }
        if (source.requireAlias() != null) {
            target.requireAlias(source.requireAlias());
        }
        return target;
    }
}
