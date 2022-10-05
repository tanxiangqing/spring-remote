package org.storevm.framework.remote.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 解压缩请求体的过滤器
 *
 * @author Jack
 * @version 1.0.0
 * @date 2019/09/09
 */
public class HttpServletCompressFilter implements Filter {

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse,
     * FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(new CompressHttpServletRequestWrapper((HttpServletRequest) request), response);
    }

}
