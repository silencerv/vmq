package com.v.inf.mq.common.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.time.FastDateFormat;

import java.io.IOException;
import java.util.Date;

/**
 * @anthor v
 * Create on 2019/1/14
 */
public class DateJsonSerializer extends JsonSerializer<Date> {
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance(DATE_PATTERN);


    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeString(date != null ? DATE_FORMAT.format(date) : "null");
    }
}
