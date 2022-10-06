package org.storevm.framework.remote.core;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.GzipCompressingEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.core.annotation.AnnotationUtils;
import org.storevm.framework.remote.annotation.*;
import org.storevm.framework.remote.enums.CallMethod;
import org.storevm.framework.remote.enums.PartType;
import org.storevm.framework.remote.handler.HttpInvokeHandler;
import org.storevm.framework.remote.handler.HttpInvokeHandlerFactory;
import org.storevm.framework.remote.httpclient.HttpClientConfigurator;
import org.storevm.framework.remote.utils.JsonUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jack
 */
@Slf4j
@Data
public class RemoteInvoker {
    private Class targetClass;
    private Method method;
    private URIBuilder uri;
    private Object[] values;
    private List<org.apache.http.Header> headers = new ArrayList<>(5);
    private HttpEntity entity;
    private CallMethod callMethod;
    private boolean multipart;

    public RemoteInvoker(Class target, Method method, Object[] values) {
        this.targetClass = target;
        this.method = method;
        this.values = values;
        this.uri = new URIBuilder();
    }

    public void resolve(SpringBeanExpressionResolver resolver) throws URISyntaxException {
        resolveInterfaceAnnotation(resolver);
        resolveMethodAnnotation();
        resolveParametersAnnotation(resolver);
    }

    public CallResult invoke(HttpClientConfigurator template) throws URISyntaxException {
        HttpInvokeHandler handler = HttpInvokeHandlerFactory.getHandler(template, this.callMethod);
        CallResult result = handler.invoke(this.uri.build(), this.headers, this.entity);
        return result;
    }

    private void resolveInterfaceAnnotation(SpringBeanExpressionResolver resolver) {
        Remote annotation = AnnotationUtils.findAnnotation(this.targetClass, Remote.class);
        this.uri.setScheme(String.valueOf(resolver.resolveExpression(annotation.scheme())));
        this.uri.setHost(String.valueOf(resolver.resolveExpression(annotation.host())));
    }

    private void resolveMethodAnnotation() {
        Headers annotation = AnnotationUtils.findAnnotation(this.method, Headers.class);
        if (annotation != null) {
            for (Header header : annotation.value()) {
                headers.add(new BasicHeader(header.name(), header.value()));
            }
        }
    }

    private void resolveParametersAnnotation(SpringBeanExpressionResolver resolver) throws URISyntaxException {
        Call annotation = AnnotationUtils.findAnnotation(method, Call.class);
        if (annotation != null) {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            this.callMethod = annotation.method();
            String path = String.valueOf(resolver.resolveExpression(annotation.path()));
            List<NameValuePair> pairs = new ArrayList<>(5);
            Parameter[] parameters = method.getParameters();
            for (int i = 0, n = parameters.length; i < n; i++) {
                Path p = parameters[i].getAnnotation(Path.class);
                if (p != null) {
                    String value = p.value() != null ? p.value() : p.name();
                    path = StringUtils.replace(path, "{" + value + "}", String.valueOf(this.values[i]));
                }
                Param param = parameters[i].getAnnotation(Param.class);
                if (param != null) {
                    String value = param.value() != null ? param.value() : param.name();
                    pairs.add(new BasicNameValuePair(value, String.valueOf(this.values[i])));
                }
                Header header = parameters[i].getAnnotation(Header.class);
                if (header != null) {
                    String value = header.value() != null ? header.value() : header.name();
                    headers.add(new BasicHeader(value, String.valueOf(this.values[i])));
                }
                Body body = parameters[i].getAnnotation(Body.class);
                if (body != null) {
                    entity = new StringEntity(JsonUtils.toJSONString(this.values[i]), ContentType.APPLICATION_JSON);
                    if (body.compress()) {
                        entity = new GzipCompressingEntity(entity);
                        headers.add(entity.getContentEncoding());
                    }
                }
                Part part = parameters[i].getAnnotation(Part.class);
                if (part != null) {
                    this.multipart = true;
                    String value = part.value() != null ? part.value() : part.name();
                    if (part.type() == PartType.File && this.values[i] instanceof File) {
                        builder.addBinaryBody(value, (File) this.values[i]);
                    } else if (part.type() == PartType.File && this.values[i] instanceof File[]) {
                        for (File file : (File[]) this.values[i]) {
                            builder.addBinaryBody(value, file);
                        }
                    } else if (part.type() == PartType.File && this.values[i] instanceof List) {
                        for (Object file : (List) this.values[i]) {
                            if (file instanceof File) {
                                builder.addBinaryBody(value, (File) file);
                            }
                        }
                    }
                    if (part.type() == PartType.Text) {
                        String text = JsonUtils.toJSONString(this.values[i]);
                        if (text != null) {
                            builder.addTextBody(value, text, ContentType.APPLICATION_JSON);
                        } else {
                            text = String.valueOf(this.values[i]);
                            builder.addTextBody(value, text);
                        }

                    }
                }
            }
            if (this.multipart) {
                builder.setCharset(Consts.UTF_8);
                builder.setContentType(ContentType.MULTIPART_FORM_DATA);
                this.entity = builder.build();
            }
            this.uri.setPath(path);
            this.uri.addParameters(pairs);
        }
    }
}
