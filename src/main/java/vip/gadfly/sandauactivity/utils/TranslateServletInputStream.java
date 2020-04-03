package vip.gadfly.sandauactivity.utils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class TranslateServletInputStream extends ServletInputStream {
    private InputStream inputStream;
    /**
     * 解析json之后的文本
     */
    private String body;

    public TranslateServletInputStream(String body) throws IOException {
        this.body = body;
        inputStream = null;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setReadListener(ReadListener readListener) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }

    private InputStream acquireInputStream() throws IOException {
        if (inputStream == null) {
            inputStream = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
            //通过解析之后传入的文本生成inputStream以便后面controller调用
        }

        return inputStream;
    }

    @Override
    public void close() throws IOException {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            throw e;
        } finally {
            inputStream = null;
        }
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public synchronized void mark(int i) {
        throw new UnsupportedOperationException("mark not supported");
    }

    @Override
    public synchronized void reset() throws IOException {
        throw new IOException(new UnsupportedOperationException("reset not supported"));
    }

    @Override
    public int read() throws IOException {
        return acquireInputStream().read();

    }
}
