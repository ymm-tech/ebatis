package io.manbang.ebatis.sample.model;

import io.manbang.ebatis.core.annotation.Filter;
import io.manbang.ebatis.core.annotation.QueryType;
import io.manbang.ebatis.core.domain.Aggregation;
import io.manbang.ebatis.core.domain.Script;
import io.manbang.ebatis.core.provider.AggProvider;
import lombok.Data;
import lombok.val;

import java.util.HashMap;

@Data
public class AggGroupByStatusCondition implements AggProvider {
    public static final String GROUP_BY_STATUS = "GroupByStatus";
    public static final String GROUP_BY_CREATE_TIME = "GroupByCreateTime";
    private static final String GROUP_BY_CREATE_TIME_SCRIPT = "ZonedDateTime now = ZonedDateTime.ofInstant(Instant.ofEpochMilli(params['now']), ZoneId.of('Z')); \n" +
            "          ZonedDateTime createTime = doc['createTime'].value; \n" +
            "          long days = createTime.until(now, ChronoUnit.DAYS);\n" +
            "          long lastDays = params['lastDays'];\n" +
            "          \n" +
            "          if (days <= lastDays) {\n" +
            "            return '最近 ' + lastDays + ' 天';\n" +
            "          } else {\n" +
            "            return '其他';\n" +
            "          }";
    @Filter(queryType = QueryType.BOOL, nested = true)
    private ManufacturerCondition manufacturer;
    @Filter(queryType = QueryType.TERM)
    private Long manufacturerId;

    @Override
    public Aggregation[] getAggregations() {
        val params = new HashMap<String, Object>();
        params.put("lastDays", 7);
        params.put("now", System.currentTimeMillis());
        return new Aggregation[]{Aggregation.terms(GROUP_BY_STATUS)
                .fieldName("status"),
                Aggregation.terms(GROUP_BY_CREATE_TIME)
                        .script(Script.inline(GROUP_BY_CREATE_TIME_SCRIPT, params))
        };
    }
}
