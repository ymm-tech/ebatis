package io.manbang.ebatis.core.request;

import io.manbang.ebatis.core.annotation.ClosePointInTime;
import io.manbang.ebatis.core.annotation.Count;
import io.manbang.ebatis.core.annotation.DocumentExists;
import io.manbang.ebatis.core.annotation.GetSource;
import io.manbang.ebatis.core.annotation.Mapper;
import io.manbang.ebatis.core.annotation.OpenPointInTime;
import io.manbang.ebatis.core.annotation.QueryType;
import io.manbang.ebatis.core.annotation.Search;
import io.manbang.ebatis.core.meta.MapperInterface;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.provider.PointTimeProvider;
import io.manbang.ebatis.core.provider.QueryBuilderProvider;
import io.manbang.ebatis.core.provider.SearchAfterProvider;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.search.ClosePointInTimeRequest;
import org.elasticsearch.action.search.OpenPointInTimeRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class HighFrequencyRequestFactoryTest {
    private final MapperInterface mapper = MapperInterface.of(HighFrequencyMapper.class);

    @Test
    public void shouldCreateNativeCountRequest() throws Exception {
        MethodMeta meta = meta("count");
        CountRequest request = RequestFactory.count().create(meta);

        assertArrayEquals(new String[]{"ebatis-test"}, request.indices());
        assertTrue(request.query() instanceof MatchAllQueryBuilder);
    }

    @Test
    public void shouldCreateExistsAndGetSourceRequests() throws Exception {
        MethodMeta existsMeta = meta("exists", String.class);
        GetRequest exists = RequestFactory.documentExists().create(existsMeta, "42");
        assertEquals("42", exists.id());
        assertEquals("ebatis-test", exists.index());

        MethodMeta sourceMeta = meta("source", String.class);
        GetSourceActionRequest source = RequestFactory.getSource().create(sourceMeta, "42");
        assertEquals("42", source.getRequest().id());
        assertEquals("ebatis-test", source.getRequest().index());
    }

    @Test
    public void shouldBuildPitSearchWithoutIndicesPreferenceOrRouting() throws Exception {
        PitCondition condition = new PitCondition();
        SearchRequest request = RequestFactory.search().create(meta("pitSearch", PitCondition.class), condition);

        assertEquals(0, request.indices().length);
        assertNull(request.preference());
        assertNull(request.routing());
        assertEquals("pit-1", request.source().pointInTimeBuilder().getEncodedId());
        assertEquals("2m", request.source().pointInTimeBuilder().getKeepAlive().toString());
        assertArrayEquals(new Object[]{"sort-1"}, request.source().searchAfter());
    }

    @Test
    public void shouldCreateOpenAndClosePitRequests() throws Exception {
        OpenPointInTimeRequest open = RequestFactory.openPointInTime().create(meta("openPit"));
        assertArrayEquals(new String[]{"ebatis-test"}, open.indices());
        assertEquals("2m", open.keepAlive().toString());

        ClosePointInTimeRequest close = RequestFactory.closePointInTime()
                .create(meta("closePit", String.class), "pit-1");
        assertEquals("pit-1", close.getId());
    }

    private MethodMeta meta(String method, Class<?>... parameterTypes) throws Exception {
        return mapper.getMapperMethod(HighFrequencyMapper.class.getMethod(method, parameterTypes));
    }

    @Mapper(indices = "ebatis-test")
    private interface HighFrequencyMapper {
        @Count(queryType = QueryType.MATCH_ALL)
        long count();

        @DocumentExists
        boolean exists(String id);

        @GetSource
        java.util.Map<String, Object> source(String id);

        @OpenPointInTime(keepAlive = "2m")
        String openPit();

        @ClosePointInTime
        boolean closePit(String pitId);

        @Search(preference = "ignored-for-pit")
        org.elasticsearch.action.search.SearchResponse pitSearch(PitCondition condition);
    }

    private static class PitCondition implements PointTimeProvider, QueryBuilderProvider, SearchAfterProvider {
        @Override
        public String pitId() {
            return "pit-1";
        }

        @Override
        public String keepAlive() {
            return "2m";
        }

        @Override
        public QueryBuilder queryBuilder() {
            return QueryBuilders.matchAllQuery();
        }

        @Override
        public Object[] sortValues() {
            return new Object[]{"sort-1"};
        }
    }
}
