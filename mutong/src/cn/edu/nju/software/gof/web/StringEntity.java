package cn.edu.nju.software.gof.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.protocol.HTTP;

public class StringEntity extends AbstractHttpEntity implements Cloneable {

    protected final byte[] content;

    /**
     * Creates a StringEntity with the specified content, mimetype and charset
     *
     * @param string content to be used. Not {@code null}.
     * @param mimeType mime type to be used. May be {@code null}, in which case the default is {@link HTTP#PLAIN_TEXT_TYPE} i.e. "text/plain"
     * @param charset character set to be used. May be {@code null}, in which case the default is {@link HTTP#DEFAULT_CONTENT_CHARSET} i.e. "ISO-8859-1"
     *
     * @since 4.1
     * @throws IllegalArgumentException if the string parameter is null
     */
    public StringEntity(final String string, String mimeType, String charset)
            throws UnsupportedEncodingException {
        super();
        if (string == null) {
            throw new IllegalArgumentException("Source string may not be null");
        }
        if (mimeType == null) {
            mimeType = HTTP.PLAIN_TEXT_TYPE;
        }
        if (charset == null) {
            charset = HTTP.DEFAULT_CONTENT_CHARSET;
        }
        this.content = string.getBytes(charset);
        setContentType(mimeType + HTTP.CHARSET_PARAM + charset);
    }

    /**
     * Creates a StringEntity with the specified content and charset.
     * <br/>
     * The mime type defaults to {@link HTTP#PLAIN_TEXT_TYPE} i.e. "text/plain".
     *
     * @param string content to be used. Not {@code null}.
     * @param charset character set to be used. May be {@code null}, in which case the default is {@link HTTP#DEFAULT_CONTENT_CHARSET} i.e. "ISO-8859-1"
     *
     * @throws IllegalArgumentException if the string parameter is null
     */
    public StringEntity(final String string, String charset)
            throws UnsupportedEncodingException {
        this(string, null, charset);
    }

    /**
     * Creates a StringEntity with the specified content and charset.
     * <br/>
     * The charset defaults to {@link HTTP#DEFAULT_CONTENT_CHARSET} i.e. "ISO-8859-1".
     * <br/>
     * The mime type defaults to {@link HTTP#PLAIN_TEXT_TYPE} i.e. "text/plain".
     *
     * @param string content to be used. Not {@code null}.
     *
     * @throws IllegalArgumentException if the string parameter is null
     */
    public StringEntity(final String string)
            throws UnsupportedEncodingException {
        this(string, null);
    }

    public boolean isRepeatable() {
        return true;
    }

    public long getContentLength() {
        return this.content.length;
    }

    public InputStream getContent() throws IOException {
        return new ByteArrayInputStream(this.content);
    }

    public void writeTo(final OutputStream outstream) throws IOException {
        if (outstream == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        outstream.write(this.content);
        outstream.flush();
    }

    /**
     * Tells that this entity is not streaming.
     *
     * @return <code>false</code>
     */
    public boolean isStreaming() {
        return false;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

} // class StringEntity
