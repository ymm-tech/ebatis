package io.manbang.ebatis.sample;

import io.manbang.ebatis.core.annotation.Bulk;
import io.manbang.ebatis.core.annotation.BulkType;
import io.manbang.ebatis.core.annotation.Delete;
import io.manbang.ebatis.core.annotation.Field;
import io.manbang.ebatis.core.annotation.Index;
import io.manbang.ebatis.core.annotation.Prefix;
import io.manbang.ebatis.core.annotation.QueryType;
import io.manbang.ebatis.core.annotation.RefreshPolicy;
import io.manbang.ebatis.core.annotation.Search;
import io.manbang.ebatis.core.annotation.Should;
import io.manbang.ebatis.core.annotation.Term;
import io.manbang.ebatis.core.annotation.VersionType;
import io.manbang.ebatis.core.annotation.Wildcard;
import io.manbang.ebatis.core.cluster.Cluster;
import io.manbang.ebatis.core.domain.Page;
import io.manbang.ebatis.core.domain.Pageable;
import io.manbang.ebatis.core.domain.Sort;
import io.manbang.ebatis.core.provider.IdProvider;
import io.manbang.ebatis.core.provider.SortProvider;
import io.manbang.ebatis.core.provider.VersionProvider;
import io.manbang.ebatis.core.proxy.MapperProxyFactory;
import io.manbang.ebatis.spring.annotation.EasyMapper;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class EBatisModelCapabilityIntegrationTest {
    private static final String INDEX_PREFIX = "ebatis_model_capability_it_";
    private static final String ALIAS = "ebatis_model_capability_it";

    private Cluster cluster;
    private String physicalIndex;
    private CapabilityMapper mapper;

    @Before
    public void setUp() throws IOException {
        String host = System.getProperty("ebatis.it.host", "");
        String username = System.getProperty("ebatis.it.username", "");
        String password = System.getProperty("ebatis.it.password", "");
        Assume.assumeTrue("通过系统属性显式启用本地 ES 集成测试",
                !host.isEmpty() && !username.isEmpty() && !password.isEmpty());
        int port = Integer.getInteger("ebatis.it.port", 9200);
        cluster = Cluster.simple(host, port, username, password);
        assertFalse("测试别名已被占用，拒绝覆盖", cluster.highLevelClient().indices()
                .exists(new GetIndexRequest(ALIAS), RequestOptions.DEFAULT));
        physicalIndex = INDEX_PREFIX + UUID.randomUUID().toString().replace("-", "");
        CreateIndexRequest request = new CreateIndexRequest(physicalIndex)
                .source(indexSource(), XContentType.JSON)
                .alias(new Alias(ALIAS).writeIndex(true));
        assertTrue(cluster.highLevelClient().indices().create(request, RequestOptions.DEFAULT).isAcknowledged());
        mapper = MapperProxyFactory.getMapperProxy(CapabilityMapper.class, "chip");
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
    public void shouldExecuteAliasSearchExternalVersionBulkAndVersionedDelete() throws IOException {
        BulkResponse initial = mapper.indexAll(Arrays.asList(
                new CapabilityDoc("model", 2L, "HC32L196KCTA"),
                new CapabilityDoc("variant", 1L, "HC32L196KCTA-LQ64")));
        assertFalse(initial.buildFailureMessage(), initial.hasFailures());

        Page<CapabilityDoc> exact = mapper.search(
                new CapabilityCondition("hc32l196kcta"), Pageable.of(0, 10));
        assertEquals("model", exact.getContent().get(0).getId());
        Page<CapabilityDoc> contains = mapper.search(
                new CapabilityCondition("196kc"), Pageable.of(0, 10));
        assertEquals(2L, contains.getTotal());
        assertEquals("model", contains.getContent().get(0).getId());

        BulkResponse staleBulk = mapper.indexAll(Arrays.asList(
                new CapabilityDoc("model", 1L, "STALE")));
        assertTrue(staleBulk.hasFailures());
        GetResponse afterStaleBulk = get("model");
        assertEquals(2L, afterStaleBulk.getVersion());
        assertEquals("HC32L196KCTA", afterStaleBulk.getSourceAsMap().get("name"));

        try {
            mapper.delete(new CapabilityIdentity("model", 1L));
            fail("低版本删除必须被 Elasticsearch 拒绝");
        } catch (RuntimeException expected) {
            assertTrue(get("model").isExists());
        }

        DeleteResponse deleted = mapper.delete(new CapabilityIdentity("model", 3L));
        assertEquals(DocWriteResponse.Result.DELETED, deleted.getResult());
        assertFalse(get("model").isExists());
    }

    private GetResponse get(String id) throws IOException {
        return cluster.highLevelClient().get(new GetRequest(ALIAS, id), RequestOptions.DEFAULT);
    }

    private String indexSource() {
        return "{\"mappings\":{\"dynamic\":\"strict\",\"properties\":{" +
                "\"id\":{\"type\":\"keyword\"}," +
                "\"version\":{\"type\":\"long\"}," +
                "\"name\":{\"type\":\"wildcard\"}," +
                "\"nameLength\":{\"type\":\"integer\"}" +
                "}}}";
    }

    @EasyMapper(indices = ALIAS, clusterRouter = "chip")
    public interface CapabilityMapper {
        @Bulk(bulkType = BulkType.INDEX, refreshPolicy = "true",
                index = @Index(versionType = VersionType.EXTERNAL_GTE))
        BulkResponse indexAll(List<CapabilityDoc> docs);

        @Search(trackTotalHits = true)
        Page<CapabilityDoc> search(CapabilityCondition condition, Pageable pageable);

        @Delete(versionType = VersionType.EXTERNAL_GTE, refreshPolicy = RefreshPolicy.IMMEDIATE)
        DeleteResponse delete(CapabilityIdentity identity);
    }

    public static class CapabilityCondition implements SortProvider {
        @Field("name")
        @Should(queryType = QueryType.TERM, minimumShouldMatch = "1",
                term = @Term(caseInsensitive = true, boost = 1000.0f))
        private final String exact;
        @Field("name")
        @Should(queryType = QueryType.PREFIX,
                prefix = @Prefix(caseInsensitive = true, boost = 500.0f))
        private final String prefix;
        @Field("name")
        @Should(queryType = QueryType.WILDCARD,
                wildcard = @Wildcard(caseInsensitive = true, boost = 100.0f))
        private final String contains;

        public CapabilityCondition(String name) {
            exact = name;
            prefix = name;
            contains = name;
        }

        public String getExact() {
            return exact;
        }

        public String getPrefix() {
            return prefix;
        }

        public String getContains() {
            return contains;
        }

        @Override
        public Sort[] getSorts() {
            return new Sort[]{Sort.scoreDesc(), Sort.fieldAsc("nameLength"), Sort.fieldAsc("id")};
        }
    }

    public static class CapabilityDoc implements IdProvider, VersionProvider {
        private String id;
        private long version;
        private String name;
        private int nameLength;

        public CapabilityDoc() {
        }

        public CapabilityDoc(String id, long version, String name) {
            this.id = id;
            this.version = version;
            this.name = name;
            this.nameLength = name.length();
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public long getVersion() {
            return version;
        }

        public void setVersion(long version) {
            this.version = version;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getNameLength() {
            return nameLength;
        }

        public void setNameLength(int nameLength) {
            this.nameLength = nameLength;
        }

        @Override
        public String id() {
            return id;
        }

        @Override
        public long version() {
            return version;
        }
    }

    public static class CapabilityIdentity implements IdProvider, VersionProvider {
        private final String id;
        private final long version;

        public CapabilityIdentity(String id, long version) {
            this.id = id;
            this.version = version;
        }

        @Override
        public String id() {
            return id;
        }

        @Override
        public long version() {
            return version;
        }
    }
}
