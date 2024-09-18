package io.manbang.ebatis.sample.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.manbang.ebatis.core.annotation.Ignore;
import io.manbang.ebatis.core.provider.IdProvider;
import io.manbang.ebatis.core.provider.VersionProvider;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Summary implements VersionProvider, IdProvider {
    @Ignore
    private long version;
    private Long id;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Scene scene;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Type type;
    private Long value;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @Override
    public long version() {
        return version;
    }

    @Override
    public String id() {
        return String.valueOf(id);
    }
}
