package io.manbang.ebatis.core.domain;

import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ScoreSortTest {
    @Test
    public void shouldCreateNativeScoreSortInBothDirections() {
        Sort ascending = Sort.scoreAsc();
        Sort descending = Sort.scoreDesc();

        assertEquals("_score", ascending.name());
        assertEquals(SortDirection.ASC, ascending.direction());
        assertTrue(ascending.toSortBuilder() instanceof ScoreSortBuilder);
        assertEquals(SortOrder.ASC, ascending.toSortBuilder().order());

        assertEquals("_score", descending.name());
        assertEquals(SortDirection.DESC, descending.direction());
        assertTrue(descending.toSortBuilder() instanceof ScoreSortBuilder);
        assertEquals(SortOrder.DESC, descending.toSortBuilder().order());
    }
}
