package org.storevm.framework.remote.core;

import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.storevm.framework.remote.utils.JsonUtils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Jack
 */
@Slf4j
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class CallResult implements Closeable {
    private static final String FORMAT_T = "yyyy-MM-dd'T'HH:mm:ss";
    int statusCode;
    boolean success;
    Header[] headers;
    ContentType contentType;
    String encoding;
    HttpEntity entity;
    long contentLength;
    CloseableHttpResponse response;

    public CallResult(CloseableHttpResponse response) {
        this.response = response;
        this.setStatusCode(this.response.getStatusLine().getStatusCode());
        this.setHeaders(this.response.getAllHeaders());
        this.setEntity(this.response.getEntity());
        if (this.entity != null) {
            this.contentLength = this.entity.getContentLength();
            if (this.entity.getContentType() != null) {
                this.contentType = ContentType.get(this.entity);
            }
            if (this.entity.getContentEncoding() != null) {
                this.encoding = this.entity.getContentEncoding().getValue();
            }
        }
    }

    public boolean isSuccess() {
        this.success = statusCode == HttpStatus.SC_OK;
        return this.success;
    }

    public Object getReturnObject(Type type) throws IOException {
        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            if (ClassUtils.isAssignable(List.class, (Class) pt.getRawType())) {
                Class cls = (Class) pt.getActualTypeArguments()[0];
                return JsonUtils.parseToList(EntityUtils.toString(this.entity, StandardCharsets.UTF_8), cls);
            }
        }
        if (ClassUtils.isAssignable(File.class, (Class<File>) type)) {
            File file = new File(FileUtils.getTempDirectory(), UUID.randomUUID().toString());
            FileUtils.writeByteArrayToFile(file, IOUtils.toByteArray(this.entity.getContent()), 0, (int) this.contentLength);
            return file;
        } else {
            if (ClassUtils.isAssignable(Date.class, (Class<Date>) type)) {
                String value = EntityUtils.toString(this.entity, StandardCharsets.UTF_8);
                value = StringUtils.remove(value, '"');
                try {
                    DateFormat df = new SimpleDateFormat(FORMAT_T);
                    Date date = df.parse(value);
                    return date;
                } catch (ParseException ex) {
                    log.error("parse date error", ex);
                }
            } else if (ClassUtils.isAssignable(Boolean.class, (Class<Boolean>) type)) {
                return Boolean.valueOf(EntityUtils.toString(this.entity, StandardCharsets.UTF_8));
            } else if (ClassUtils.isAssignable(String.class, (Class<String>) type)) {
                return EntityUtils.toString(this.entity, StandardCharsets.UTF_8);
            } else if (ClassUtils.isAssignable(Integer.class, (Class<Integer>) type)) {
                return Integer.valueOf(EntityUtils.toString(this.entity, StandardCharsets.UTF_8));
            } else if (ClassUtils.isAssignable(Long.class, (Class<Long>) type)) {
                return Long.valueOf(EntityUtils.toString(this.entity, StandardCharsets.UTF_8));
            } else if (ClassUtils.isAssignable(Short.class, (Class<Short>) type)) {
                return Short.valueOf(EntityUtils.toString(this.entity, StandardCharsets.UTF_8));
            } else if (ClassUtils.isAssignable(Float.class, (Class<Float>) type)) {
                return Float.valueOf(EntityUtils.toString(this.entity, StandardCharsets.UTF_8));
            } else if (ClassUtils.isAssignable(Double.class, (Class<Double>) type)) {
                return Double.valueOf(EntityUtils.toString(this.entity, StandardCharsets.UTF_8));
            } else if (ClassUtils.isAssignable(BigDecimal.class, (Class<BigDecimal>) type)) {
                return new BigDecimal(EntityUtils.toString(this.entity, StandardCharsets.UTF_8));
            } else {
                return JsonUtils.parse(EntityUtils.toString(this.entity, StandardCharsets.UTF_8), (Class) type);
            }
        }
        return null;
    }

    @Override
    public void close() throws IOException {
        if (this.entity != null) {
            EntityUtils.consumeQuietly(this.entity);
        }
        if (this.response != null) {
            this.response.close();
        }
    }
}
