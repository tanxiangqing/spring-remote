package org.storevm.framework.remote.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.protocol.HTTP;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

@Slf4j
public class CompressHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private static final String GZIP_CODEC = "gzip";
    private static final String ZIP_CODEC = "compress";

    public CompressHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final HttpServletRequest request = (HttpServletRequest) super.getRequest();
        String encoding = request.getHeader(HTTP.CONTENT_ENCODING);
        if (StringUtils.indexOf(encoding, GZIP_CODEC) >= 0 || StringUtils.indexOf(encoding, ZIP_CODEC) >= 0) {
            InputStream in = null;
            // 如果存在，则表示需要解压
            if (StringUtils.equalsIgnoreCase(encoding, GZIP_CODEC)) {
                in = new GZIPInputStream(request.getInputStream());
            } else if (StringUtils.equalsIgnoreCase(encoding, ZIP_CODEC)) {
                in = new ZipInputStream(request.getInputStream());
            }
            if (in != null) {
                return new CompressServletInputStream(in);
            }
        }
        return super.getInputStream();
    }
}
