package com.ymm.ebatis.domain;

import com.ymm.ebatis.annotation.Ignore;
import com.ymm.ebatis.provider.SourceProvider;

/**
 * @author 章多亮
 */
public class MetaCondition extends PageRequest implements Pageable, SourceProvider, Sortable {
    private static final String[] DEFAULT_INCLUDE_FIELDS = new String[0];
    private static final String[] DEFAULT_EXCLUDE_FIELDS = new String[0];
    @Ignore
    private String[] includeFields = DEFAULT_INCLUDE_FIELDS;
    @Ignore
    private String[] excludesFields = DEFAULT_EXCLUDE_FIELDS;


    protected MetaCondition() {
        this(0, 20);
    }

    protected MetaCondition(int page, int size) {
        super(page, size);
    }

    @Override
    public String[] getIncludeFields() {
        return includeFields;
    }

    public MetaCondition setIncludeFields(String... fields) {
        includeFields = fields == null ? DEFAULT_INCLUDE_FIELDS : fields;
        return this;
    }

    @Override
    public String[] getExcludeFields() {
        return excludesFields;
    }

    public MetaCondition setExcludeFields(String... fields) {
        excludesFields = fields == null ? DEFAULT_EXCLUDE_FIELDS : fields;
        return this;
    }

    @Override
    public Sort[] getSorts() {
        return new Sort[0];
    }
}
