package org.storevm.framework.remote.filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 带压缩的请求
 * 
 * @author Jack
 * @date 2019/09/09
 * @version 1.0.0
 */
public class CompressServletInputStream extends ServletInputStream {
    /**
     * 输入流
     */
    private InputStream input;

    /**
     * constructor
     * 
     * @param input
     */
    public CompressServletInputStream(InputStream input) {
        this.input = input;
    }

    /**
     * @see ServletInputStream#isFinished()
     */
    @Override
    public boolean isFinished() {
        return false;
    }

    /**
     * @see ServletInputStream#isReady()
     */
    @Override
    public boolean isReady() {
        return false;
    }

    /**
     * @see ServletInputStream#setReadListener(ReadListener)
     */
    @Override
    public void setReadListener(ReadListener listener) {}

    /**
     * @see InputStream#read()
     */
    @Override
    public int read() throws IOException {
        return this.input.read();
    }

}
