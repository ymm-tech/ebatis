package io.manbang.ebatis.core.domain;

import org.elasticsearch.search.sort.SortMode;

import java.util.function.Consumer;

/**
 * @author 章多亮
 * @since 2020/1/6 19:06
 */
abstract class AbstractSort implements Sort {
    private final String name;
    private final SortDirection direction;
    private SortMode sortMode;

    protected AbstractSort() {
        this(null, null);
    }

    AbstractSort(String name, SortDirection direction) {
        this.name = name;
        this.direction = direction;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public SortDirection direction() {
        return direction;
    }

    @Override
    public SortMode sortMode() {
        return sortMode;
    }

    @Override
    public Sort sortMode(SortMode sortMode) {
        this.sortMode = sortMode;
        return this;
    }

    protected void setSortMode(Consumer<SortMode> consumer) {
        if (sortMode != null) {
            consumer.accept(sortMode);
        }
    }
}
