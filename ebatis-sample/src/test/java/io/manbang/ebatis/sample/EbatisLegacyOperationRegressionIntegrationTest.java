package io.manbang.ebatis.sample;

import io.manbang.ebatis.core.annotation.Agg;
import io.manbang.ebatis.core.annotation.Bulk;
import io.manbang.ebatis.core.annotation.BulkType;
import io.manbang.ebatis.core.annotation.Cat;
import io.manbang.ebatis.core.annotation.CatType;
import io.manbang.ebatis.core.annotation.Count;
import io.manbang.ebatis.core.annotation.Delete;
import io.manbang.ebatis.core.annotation.DeleteByQuery;
import io.manbang.ebatis.core.annotation.Get;
import io.manbang.ebatis.core.annotation.Index;
import io.manbang.ebatis.core.annotation.MultiGet;
import io.manbang.ebatis.core.annotation.MultiSearch;
import io.manbang.ebatis.core.annotation.Must;
import io.manbang.ebatis.core.annotation.QueryType;
import io.manbang.ebatis.core.annotation.RefreshPolicy;
import io.manbang.ebatis.core.annotation.Search;
import io.manbang.ebatis.core.annotation.SearchScroll;
import io.manbang.ebatis.core.annotation.Update;
import io.manbang.ebatis.core.annotation.UpdateByQuery;
import io.manbang.ebatis.core.cluster.Cluster;
import io.manbang.ebatis.core.domain.Aggregation;
import io.manbang.ebatis.core.domain.Page;
import io.manbang.ebatis.core.domain.Pageable;
import io.manbang.ebatis.core.domain.ScrollResponse;
import io.manbang.ebatis.core.domain.Script;
import io.manbang.ebatis.core.provider.AggProvider;
import io.manbang.ebatis.core.provider.DefaultScrollProvider;
import io.manbang.ebatis.core.provider.IdProvider;
import io.manbang.ebatis.core.provider.ScriptProvider;
import io.manbang.ebatis.core.proxy.MapperProxyFactory;
import io.manbang.ebatis.core.response.ResponseExtractor;
import io.manbang.ebatis.spring.annotation.EasyMapper;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.BulkByScrollTask;
import org.elasticsearch.index.get.GetResult;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.aggregations.Aggregations;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * 使用 7.15.0.2 已有注解和返回类型回归传统 Mapper 执行链路。
 */
public class EbatisLegacyOperationRegressionIntegrationTest {
    private static final String INDEX_PREFIX = "ebatis_legacy_regression_it_";
    private static final String ALIAS = "ebatis_legacy_regression_it";
    private static final String PIPELINE = "ebatis_legacy_regression_pipeline";
    private static final String FINAL_PIPELINE = "ebatis_legacy_regression_final_pipeline";

    private Cluster cluster;
    private String physicalIndex;
    private LegacyMapper mapper;

    @Before
    public void setUp() throws IOException {
        String host = System.getProperty("ebatis.it.host", "");
        String username = System.getProperty("ebatis.it.username", "");
        String password = System.getProperty("ebatis.it.password", "");
        Assume.assumeTrue("通过系统属性显式启用本地 ES 集成测试",
                !host.isEmpty() && !username.isEmpty() && !password.isEmpty());
        cluster = Cluster.simple(host, Integer.getInteger("ebatis.it.port", 9200), username, password);
        assertFalse("测试别名已被占用，拒绝覆盖: " + ALIAS,
                cluster.highLevelClient().indices().exists(new GetIndexRequest(ALIAS), RequestOptions.DEFAULT));
        physicalIndex = INDEX_PREFIX + UUID.randomUUID().toString().replace("-", "");
        String mapping = "{\"mappings\":{\"dynamic\":\"strict\",\"properties\":{" +
                "\"id\":{\"type\":\"keyword\"},\"category\":{\"type\":\"keyword\"}," +
                "\"name\":{\"type\":\"keyword\"},\"amount\":{\"type\":\"integer\"}," +
                "\"marker\":{\"type\":\"keyword\"}}}}";
        CreateIndexRequest request = new CreateIndexRequest(physicalIndex).source(mapping, XContentType.JSON)
                .alias(new Alias(ALIAS).writeIndex(true));
        assertTrue(cluster.highLevelClient().indices().create(request, RequestOptions.DEFAULT).isAcknowledged());
        mapper = MapperProxyFactory.getMapperProxy(LegacyMapper.class, "chip");
    }

    @After
    public void tearDown() throws IOException {
        if (cluster == null) {
            return;
        }
        try {
            if (physicalIndex != null && physicalIndex.startsWith(INDEX_PREFIX)
                    && cluster.highLevelClient().indices()
                    .exists(new GetIndexRequest(physicalIndex), RequestOptions.DEFAULT)) {
                cluster.highLevelClient().indices().delete(
                        new DeleteIndexRequest(physicalIndex), RequestOptions.DEFAULT);
            }
        } finally {
            cluster.close();
        }
    }

    @Test
    public void shouldRegressLegacyCrudSearchBulkScrollAggregationAndByQuery() throws Exception {
        assertLegacyMapperInterfacesRegister();
        assertTrue(mapper.indexBoolean(doc("index-bool", "index", 1)));
        assertEquals("index-id", mapper.indexId(doc("index-id", "index", 2)));
        assertEquals(RestStatus.CREATED, mapper.indexAsync(doc("index-async", "index", 3)).get(10, TimeUnit.SECONDS));
        assertEquals(RestStatus.CREATED, mapper.indexRaw(doc("index-raw", "index", 4)).status());
        mapper.indexVoid(doc("index-void", "index", 5));
        mapper.indexFutureVoid(doc("index-future-void", "index", 6)).get(10, TimeUnit.SECONDS);

        BulkResponse seed = mapper.bulkRaw(Arrays.asList(
                doc("search-1", "search", 10), doc("search-2", "search", 20), doc("search-3", "search", 30),
                doc("ubq-raw", "ubq-raw", 1), doc("ubq-status", "ubq-status", 1), doc("ubq-async", "ubq-async", 1),
                doc("dbq-raw", "dbq-raw", 1), doc("dbq-status", "dbq-status", 1), doc("dbq-async", "dbq-async", 1),
                doc("update-get", "update", 1), doc("update-status", "update", 1),
                doc("update-boolean", "update", 1), doc("update-bool", "update", 1),
                doc("update-result", "update", 1), doc("update-void", "update", 1),
                doc("update-future-void", "update", 1),
                doc("delete-response", "delete", 1), doc("delete-status", "delete", 1),
                doc("delete-boolean", "delete", 1), doc("delete-bool", "delete", 1),
                doc("delete-void", "delete", 1), doc("delete-async", "delete", 1),
                doc("delete-future-void", "delete", 1)));
        assertFalse(seed.buildFailureMessage(), seed.hasFailures());

        LegacyDoc found = mapper.get("search-1");
        assertEquals("search-1", found.getId());
        assertTrue(mapper.getOptional("search-2").isPresent());
        assertEquals("search-3", mapper.getAsync("search-3").get(10, TimeUnit.SECONDS).getId());
        assertEquals("search-1", mapper.getRaw("search-1").getId());

        LegacyDoc changed = doc("search-1", "search", 11);
        changed.setName("changed");
        UpdateResponse update = mapper.update(changed);
        assertEquals(DocWriteResponse.Result.UPDATED, update.getResult());
        LegacyDoc changedAgain = doc("search-1", "search", 12);
        changedAgain.setName("changed-again");
        assertEquals(DocWriteResponse.Result.UPDATED,
                mapper.updateAsync(changedAgain).get(10, TimeUnit.SECONDS));
        assertEquals("changed-again", mapper.get("search-1").getName());
        GetResult getResult = mapper.updateGetResult(changed("update-get", 2));
        assertNotNull(getResult);
        assertEquals(RestStatus.OK, mapper.updateStatus(changed("update-status", 2)));
        assertTrue(mapper.updateBoolean(changed("update-boolean", 2)));
        assertTrue(mapper.updateBool(changed("update-bool", 2)));
        assertEquals(DocWriteResponse.Result.UPDATED, mapper.updateResult(changed("update-result", 2)));
        mapper.updateVoid(changed("update-void", 2));
        mapper.updateFutureVoid(changed("update-future-void", 2)).get(10, TimeUnit.SECONDS);

        LegacyCondition search = new LegacyCondition("search");
        assertEquals(3, mapper.searchList(search).size());
        assertEquals(3, mapper.searchArrayAsync(search).get(10, TimeUnit.SECONDS).length);
        Page<LegacyDoc> page = mapper.searchPage(Pageable.first(2), search);
        assertEquals(3L, page.getTotal());
        assertEquals(2, page.getContent().size());
        assertEquals(3L, mapper.searchRaw(search).getHits().getTotalHits().value);
        assertEquals(3L, mapper.searchCount(search).longValue());
        assertTrue(mapper.searchExists(search));
        assertNotNull(mapper.searchSingle(search));

        MultiGetResponse multiGet = mapper.multiGetRaw("search-1", "search-2");
        assertEquals(2, multiGet.getResponses().length);
        assertEquals(2, mapper.multiGetList("search-1", "search-2").size());
        assertEquals(2, mapper.multiGetItems("search-1", "search-2").length);
        assertEquals(2, mapper.multiGetItemList(Arrays.asList(
                doc("search-1", "search", 0), doc("search-2", "search", 0))).size());
        assertTrue(mapper.multiGetOptional("search-1", "missing").get(0).isPresent());
        assertFalse(mapper.multiGetOptional("search-1", "missing").get(1).isPresent());
        assertEquals(2, mapper.multiGetAsync(Arrays.asList(
                doc("search-1", "search", 0), doc("search-2", "search", 0)))
                .get(10, TimeUnit.SECONDS).length);

        LegacyCondition[] searches = {new LegacyCondition("search"), new LegacyCondition("index")};
        MultiSearchResponse multiSearch = mapper.multiSearchRaw(searches);
        assertEquals(2, multiSearch.getResponses().length);
        List<List<LegacyDoc>> multiSearchLists = mapper.multiSearchLists(searches);
        assertEquals(3, multiSearchLists.get(0).size());
        assertEquals(6, multiSearchLists.get(1).size());
        assertEquals(2, mapper.multiSearchArrays(searches).length);
        assertEquals(2, mapper.multiSearchListArrays(searches).size());
        assertEquals(2, mapper.multiSearchCounts(searches).length);
        assertTrue(mapper.multiSearchExists(searches)[0]);
        Page<LegacyDoc>[] multiPages = mapper.multiSearchPages(searches,
                new Pageable[]{Pageable.first(2), Pageable.first(2)});
        assertEquals(3L, multiPages[0].getTotal());

        Aggregations aggregations = mapper.aggregate(new LegacyAggCondition());
        assertNotNull(aggregations.get("by_category"));
        assertNotNull(mapper.aggregateRaw(new LegacyAggCondition()).getAggregations().get("by_category"));
        assertEquals(1, mapper.aggregateList(new LegacyAggCondition()).size());
        assertTrue(mapper.aggregateMap(new LegacyAggCondition()).containsKey("by_category"));

        LegacyScrollCondition scrollCondition = new LegacyScrollCondition("search");
        ScrollResponse<LegacyDoc> firstScroll = mapper.scroll(scrollCondition, Pageable.first(1));
        assertFalse(firstScroll.isEmpty());
        assertNotNull(firstScroll.getScrollId());
        scrollCondition.setScrollId(firstScroll.getScrollId());
        ScrollResponse<LegacyDoc> secondScroll = mapper.scroll(scrollCondition, Pageable.first(1));
        assertFalse(secondScroll.isEmpty());
        assertTrue(mapper.clearScroll(secondScroll.getScrollId(), response -> true));

        List<BulkItemResponse> bulkList = mapper.bulkList(
                doc("bulk-list-1", "bulk", 1), doc("bulk-list-2", "bulk", 2));
        assertEquals(2, bulkList.size());
        assertFalse(bulkList.get(0).isFailed());
        BulkItemResponse[] bulkArray = mapper.bulkArray(doc("bulk-array", "bulk", 3));
        assertEquals(1, bulkArray.length);
        LegacyDoc bulkUpdated = doc("bulk-array", "bulk", 4);
        bulkUpdated.setName("bulk-updated");
        assertFalse(mapper.bulkUpdateAsync(Collections.singletonList(bulkUpdated))
                .get(10, TimeUnit.SECONDS).get(0).isFailed());
        assertFalse(mapper.bulkDelete("bulk-list-1").get(0).isFailed());

        assertEquals(1L, mapper.updateByQuery(new LegacyScriptCondition("ubq-raw")).getUpdated());
        assertEquals(1L, mapper.updateByQueryStatus(new LegacyScriptCondition("ubq-status")).getUpdated());
        assertEquals(1L, mapper.updateByQueryAsync(new LegacyScriptCondition("ubq-async"))
                .get(10, TimeUnit.SECONDS).getUpdated());

        assertEquals(1L, mapper.deleteByQuery(new LegacyCondition("dbq-raw")).getDeleted());
        assertEquals(1L, mapper.deleteByQueryStatus(new LegacyCondition("dbq-status")).getDeleted());
        assertEquals(1L, mapper.deleteByQueryAsync(new LegacyCondition("dbq-async"))
                .get(10, TimeUnit.SECONDS).getDeleted());

        DeleteResponse deleted = mapper.delete("delete-response");
        assertEquals(DocWriteResponse.Result.DELETED, deleted.getResult());
        assertEquals(RestStatus.OK, mapper.deleteStatus("delete-status"));
        assertTrue(mapper.deleteBoolean("delete-boolean"));
        assertTrue(mapper.deleteBool("delete-bool"));
        mapper.deleteVoid("delete-void");
        assertTrue(mapper.deleteAsync("delete-async").get(10, TimeUnit.SECONDS));
        mapper.deleteFutureVoid("delete-future-void").get(10, TimeUnit.SECONDS);

        mapper.catCount(response -> null);
    }

    @Test
    public void shouldApplyPipelineAndFinalPipelineSeparately() throws Exception {
        assertPipelineUnused(PIPELINE);
        assertPipelineUnused(FINAL_PIPELINE);
        putPipeline(PIPELINE, "pipeline");
        putPipeline(FINAL_PIPELINE, "final-pipeline");
        try {
            setIndexFinalPipeline(FINAL_PIPELINE);
            IndexResponse response = mapper.indexWithPipelines(doc("pipeline-doc", "pipeline", 1));
            assertEquals(RestStatus.CREATED, response.status());
            assertEquals("final-pipeline", mapper.get("pipeline-doc").getMarker());
        } finally {
            setIndexFinalPipeline("_none");
            deletePipeline(PIPELINE);
            deletePipeline(FINAL_PIPELINE);
        }
    }

    private void assertPipelineUnused(String name) throws IOException {
        try {
            cluster.lowLevelClient().performRequest(new Request("GET", "/_ingest/pipeline/" + name));
            throw new AssertionError("测试 pipeline 已被占用，拒绝覆盖: " + name);
        } catch (ResponseException exception) {
            assertEquals(404, exception.getResponse().getStatusLine().getStatusCode());
        }
    }

    private void putPipeline(String name, String marker) throws IOException {
        Request request = new Request("PUT", "/_ingest/pipeline/" + name);
        request.setJsonEntity("{\"processors\":[{\"set\":{\"field\":\"marker\",\"value\":\"" + marker + "\"}}]}");
        assertEquals(200, cluster.lowLevelClient().performRequest(request).getStatusLine().getStatusCode());
    }

    private void deletePipeline(String name) throws IOException {
        try {
            cluster.lowLevelClient().performRequest(new Request("DELETE", "/_ingest/pipeline/" + name));
        } catch (ResponseException exception) {
            if (exception.getResponse().getStatusLine().getStatusCode() != 404) {
                throw exception;
            }
        }
    }

    private void setIndexFinalPipeline(String name) throws IOException {
        Request request = new Request("PUT", "/" + physicalIndex + "/_settings");
        request.setJsonEntity("{\"index\":{\"final_pipeline\":\"" + name + "\"}}");
        assertEquals(200, cluster.lowLevelClient().performRequest(request).getStatusLine().getStatusCode());
    }

    private LegacyDoc doc(String id, String category, int amount) {
        return new LegacyDoc(id, category, id, amount, "original");
    }

    private LegacyDoc changed(String id, int amount) {
        return new LegacyDoc(id, "update", id + "-changed", amount, "changed");
    }

    private void assertLegacyMapperInterfacesRegister() {
        Class<?>[] mapperTypes = {
                io.manbang.ebatis.sample.mapper.RecentOrderIndexMapper.class,
                io.manbang.ebatis.sample.mapper.RecentOrderGetMapper.class,
                io.manbang.ebatis.sample.mapper.RecentOrderUpdateMapper.class,
                io.manbang.ebatis.sample.mapper.RecentOrderDeleteMapper.class,
                io.manbang.ebatis.sample.mapper.RecentOrderBulkMapper.class,
                io.manbang.ebatis.sample.mapper.RecentOrderMultiGetMapper.class,
                io.manbang.ebatis.sample.mapper.RecentOrderSearchMapper.class,
                io.manbang.ebatis.sample.mapper.RecentOrderMultiSearchMapper.class,
                io.manbang.ebatis.sample.mapper.RecentOrderAggMapper.class,
                io.manbang.ebatis.sample.mapper.RecentOrderScrollMapper.class,
                io.manbang.ebatis.sample.mapper.RecentOrderUpdateByQueryMapper.class,
                io.manbang.ebatis.sample.mapper.RecentOrderDeleteByQueryMapper.class,
                io.manbang.ebatis.sample.mapper.SummaryMapper.class
        };
        for (Class<?> mapperType : mapperTypes) {
            assertNotNull(MapperProxyFactory.getMapperProxy(mapperType, "chip"));
        }
    }

    @EasyMapper(indices = ALIAS, clusterRouter = "chip")
    public interface LegacyMapper {
        @Index(refreshPolicy = RefreshPolicy.IMMEDIATE) boolean indexBoolean(LegacyDoc document);
        @Index(refreshPolicy = RefreshPolicy.IMMEDIATE) String indexId(LegacyDoc document);
        @Index(refreshPolicy = RefreshPolicy.IMMEDIATE) CompletableFuture<RestStatus> indexAsync(LegacyDoc document);
        @Index(refreshPolicy = RefreshPolicy.IMMEDIATE) IndexResponse indexRaw(LegacyDoc document);
        @Index(refreshPolicy = RefreshPolicy.IMMEDIATE) void indexVoid(LegacyDoc document);
        @Index(refreshPolicy = RefreshPolicy.IMMEDIATE) CompletableFuture<Void> indexFutureVoid(LegacyDoc document);
        @Index(pipeline = PIPELINE, finalPipeline = FINAL_PIPELINE, refreshPolicy = RefreshPolicy.IMMEDIATE)
        IndexResponse indexWithPipelines(LegacyDoc document);

        @Get LegacyDoc get(String id);
        @Get Optional<LegacyDoc> getOptional(String id);
        @Get CompletableFuture<LegacyDoc> getAsync(String id);
        @Get GetResponse getRaw(String id);

        @Update(refreshPolicy = RefreshPolicy.IMMEDIATE) UpdateResponse update(LegacyDoc document);
        @Update(refreshPolicy = RefreshPolicy.IMMEDIATE) CompletableFuture<DocWriteResponse.Result> updateAsync(LegacyDoc document);
        @Update(fetchSource = true, refreshPolicy = RefreshPolicy.IMMEDIATE) GetResult updateGetResult(LegacyDoc document);
        @Update(refreshPolicy = RefreshPolicy.IMMEDIATE) RestStatus updateStatus(LegacyDoc document);
        @Update(refreshPolicy = RefreshPolicy.IMMEDIATE) Boolean updateBoolean(LegacyDoc document);
        @Update(refreshPolicy = RefreshPolicy.IMMEDIATE) boolean updateBool(LegacyDoc document);
        @Update(refreshPolicy = RefreshPolicy.IMMEDIATE) DocWriteResponse.Result updateResult(LegacyDoc document);
        @Update(refreshPolicy = RefreshPolicy.IMMEDIATE) void updateVoid(LegacyDoc document);
        @Update(refreshPolicy = RefreshPolicy.IMMEDIATE) CompletableFuture<Void> updateFutureVoid(LegacyDoc document);

        @Delete(refreshPolicy = RefreshPolicy.IMMEDIATE) DeleteResponse delete(String id);
        @Delete(refreshPolicy = RefreshPolicy.IMMEDIATE) RestStatus deleteStatus(String id);
        @Delete(refreshPolicy = RefreshPolicy.IMMEDIATE) Boolean deleteBoolean(String id);
        @Delete(refreshPolicy = RefreshPolicy.IMMEDIATE) boolean deleteBool(String id);
        @Delete(refreshPolicy = RefreshPolicy.IMMEDIATE) void deleteVoid(String id);
        @Delete(refreshPolicy = RefreshPolicy.IMMEDIATE) CompletableFuture<Boolean> deleteAsync(String id);
        @Delete(refreshPolicy = RefreshPolicy.IMMEDIATE) CompletableFuture<Void> deleteFutureVoid(String id);

        @Bulk(bulkType = BulkType.INDEX, refreshPolicy = "true") BulkResponse bulkRaw(List<LegacyDoc> documents);
        @Bulk(bulkType = BulkType.INDEX, refreshPolicy = "true") List<BulkItemResponse> bulkList(LegacyDoc... documents);
        @Bulk(bulkType = BulkType.INDEX, refreshPolicy = "true") BulkItemResponse[] bulkArray(LegacyDoc... documents);
        @Bulk(bulkType = BulkType.UPDATE, refreshPolicy = "true")
        CompletableFuture<List<BulkItemResponse>> bulkUpdateAsync(List<LegacyDoc> documents);
        @Bulk(bulkType = BulkType.DELETE, refreshPolicy = "true") List<BulkItemResponse> bulkDelete(String... ids);

        @Search(queryType = QueryType.BOOL, trackTotalHits = true) List<LegacyDoc> searchList(LegacyCondition condition);
        @Search(queryType = QueryType.BOOL, trackTotalHits = true) CompletableFuture<LegacyDoc[]> searchArrayAsync(LegacyCondition condition);
        @Search(queryType = QueryType.BOOL, trackTotalHits = true) Page<LegacyDoc> searchPage(Pageable pageable, LegacyCondition condition);
        @Search(queryType = QueryType.BOOL, trackTotalHits = true) SearchResponse searchRaw(LegacyCondition condition);
        @Search(queryType = QueryType.BOOL, trackTotalHits = true) Long searchCount(LegacyCondition condition);
        @Search(queryType = QueryType.BOOL) boolean searchExists(LegacyCondition condition);
        @Search(queryType = QueryType.BOOL) LegacyDoc searchSingle(LegacyCondition condition);

        @MultiGet MultiGetResponse multiGetRaw(String... ids);
        @MultiGet List<LegacyDoc> multiGetList(String... ids);
        @MultiGet MultiGetItemResponse[] multiGetItems(String... ids);
        @MultiGet List<MultiGetItemResponse> multiGetItemList(List<LegacyDoc> documents);
        @MultiGet List<Optional<LegacyDoc>> multiGetOptional(String... ids);
        @MultiGet CompletableFuture<Optional<LegacyDoc>[]> multiGetAsync(List<LegacyDoc> documents);

        @MultiSearch(queryType = QueryType.BOOL) MultiSearchResponse multiSearchRaw(LegacyCondition[] conditions);
        @MultiSearch(queryType = QueryType.BOOL) List<List<LegacyDoc>> multiSearchLists(LegacyCondition[] conditions);
        @MultiSearch(queryType = QueryType.BOOL) LegacyDoc[][] multiSearchArrays(LegacyCondition[] conditions);
        @MultiSearch(queryType = QueryType.BOOL) List<LegacyDoc[]> multiSearchListArrays(LegacyCondition[] conditions);
        @MultiSearch(queryType = QueryType.BOOL) long[] multiSearchCounts(LegacyCondition[] conditions);
        @MultiSearch(queryType = QueryType.BOOL) boolean[] multiSearchExists(LegacyCondition[] conditions);
        @MultiSearch(queryType = QueryType.BOOL) Page<LegacyDoc>[] multiSearchPages(
                LegacyCondition[] conditions, Pageable[] pageables);

        @Agg(queryType = QueryType.MATCH_ALL, aggOnly = true) Aggregations aggregate(LegacyAggCondition condition);
        @Agg(queryType = QueryType.MATCH_ALL) SearchResponse aggregateRaw(LegacyAggCondition condition);
        @Agg(queryType = QueryType.MATCH_ALL) List<org.elasticsearch.search.aggregations.Aggregation> aggregateList(
                LegacyAggCondition condition);
        @Agg(queryType = QueryType.MATCH_ALL) Map<String, org.elasticsearch.search.aggregations.Aggregation> aggregateMap(
                LegacyAggCondition condition);

        @SearchScroll(queryType = QueryType.BOOL, initialKeepAlive = "1m", keepAlive = "1m")
        ScrollResponse<LegacyDoc> scroll(LegacyScrollCondition condition, Pageable pageable);
        @SearchScroll(clearScroll = true) boolean clearScroll(String scrollId, ResponseExtractor<Boolean> extractor);

        @UpdateByQuery(refresh = true) BulkByScrollResponse updateByQuery(LegacyScriptCondition condition);
        @UpdateByQuery(refresh = true) BulkByScrollTask.Status updateByQueryStatus(LegacyScriptCondition condition);
        @UpdateByQuery(refresh = true) CompletableFuture<BulkByScrollResponse> updateByQueryAsync(LegacyScriptCondition condition);

        @DeleteByQuery(refresh = true) BulkByScrollResponse deleteByQuery(LegacyCondition condition);
        @DeleteByQuery(refresh = true) BulkByScrollTask.Status deleteByQueryStatus(LegacyCondition condition);
        @DeleteByQuery(refresh = true) CompletableFuture<BulkByScrollResponse> deleteByQueryAsync(LegacyCondition condition);

        @Cat(catType = CatType.COUNT, count = @Count(index = ALIAS))
        void catCount(ResponseExtractor<Void> extractor);
    }

    public static class LegacyDoc implements IdProvider {
        private String id;
        private String category;
        private String name;
        private int amount;
        private String marker;

        public LegacyDoc() { }

        public LegacyDoc(String id, String category, String name, int amount, String marker) {
            this.id = id;
            this.category = category;
            this.name = name;
            this.amount = amount;
            this.marker = marker;
        }

        @Override public String id() { return id; }
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getAmount() { return amount; }
        public void setAmount(int amount) { this.amount = amount; }
        public String getMarker() { return marker; }
        public void setMarker(String marker) { this.marker = marker; }
    }

    public static class LegacyCondition {
        @Must
        private String category;

        public LegacyCondition(String category) { this.category = category; }
        public String getCategory() { return category; }
    }

    public static class LegacyScriptCondition extends LegacyCondition implements ScriptProvider {
        public LegacyScriptCondition(String category) { super(category); }

        @Override
        public Script getScript() {
            return Script.inline("ctx._source.marker = params.marker",
                    Collections.singletonMap("marker", "updated-by-query"));
        }
    }

    public static class LegacyScrollCondition extends DefaultScrollProvider {
        @Must
        private String category;

        public LegacyScrollCondition(String category) { this.category = category; }
        public String getCategory() { return category; }
    }

    public static class LegacyAggCondition implements AggProvider {
        @Override
        public Aggregation[] getAggregations() {
            return new Aggregation[]{Aggregation.terms("by_category").fieldName("category")};
        }
    }
}
