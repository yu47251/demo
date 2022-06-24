package com.wlb.demo.resource.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.IOUtils;
import org.springframework.lang.NonNull;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON;
import static org.springframework.util.MimeTypeUtils.parseMimeType;

/**
 * 请求参数过滤前后空格
 *
 * @author Eric Wang
 * @since 2021/4/1
 */
public class TrimHttpServletRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 暂存RequestBody内容
     */
    private String content;

    /**
     * 暂存request
     */
    private final HttpServletRequest request;

    /**
     * json数据类型
     */
    private static final String JSON_CONTENT = "application/json";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 构造方法
     *
     * @param request request
     */
    public TrimHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.request = request;
        // 仅处理application/json
        if (request.getContentType() != null && APPLICATION_JSON.equals(parseMimeType(request.getContentType()))) {
            // 获取文本数据;
            String content = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8.toString());
            this.content = jsonTrim(content);
        }
    }

    private String jsonTrim(String content) throws IOException {
        JsonNode node = OBJECT_MAPPER.readTree(content);
        if (node != null) {
            trimNode(node);
            return node.toString();
        }
        return null;
    }

    private static void trimNode(JsonNode node) {
        if (node == null) {
            return;
        }
        if (node.isObject() && node instanceof ObjectNode) {
            ObjectNode objectNode = (ObjectNode) node;
            Iterator<String> it = node.fieldNames();
            while (it.hasNext()) {
                String name = it.next();
                JsonNode subNode = node.get(name);
                if (subNode != null) {
                    if (subNode.isTextual()) {
                        // 字符串Trim
                        objectNode.put(name, subNode.asText().trim());
                    } else {
                        trimNode(subNode);
                    }
                }
            }
        } else if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {
                trimNode(node.get(i));
            }
        }
    }

    /**
     * 重写
     *
     * @param name 参数名
     * @return trim后的值
     */
    @Override
    public String getParameter(String name) {
        String parameter = super.getParameter(name);
        return parameter == null ? null : parameter.trim();
    }

    /**
     * 重写
     *
     * @param name 参数名
     * @return trim后的值
     */
    @Override
    public String[] getParameterValues(String name) {
        String[] parameterValues = super.getParameterValues(name);
        if (parameterValues == null || parameterValues.length == 0) {
            return parameterValues;
        } else {
            String[] values = new String[parameterValues.length];
            for (int i = 0; i < parameterValues.length; i++) {
                values[i] = parameterValues[i].trim();
            }
            return values;
        }
    }


    /**
     * 重写
     *
     * @return ServletInputStream
     * @throws IOException IOException
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (!JSON_CONTENT.equalsIgnoreCase(request.getContentType())) {
            //非application/json数据不处理
            return super.getInputStream();
        } else {
            ByteArrayInputStream in = new ByteArrayInputStream(this.content.getBytes(StandardCharsets.UTF_8));
            return new ServletInputStream() {
                @Override
                public int read() {
                    return in.read();
                }

                @Override
                public int read(@NonNull byte[] b, int off, int len) {
                    return in.read(b, off, len);
                }

                @Override
                public int read(@NonNull byte[] b) throws IOException {
                    return in.read(b);
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener listener) {

                }

                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public long skip(long n) {
                    return in.skip(n);
                }

                @Override
                public void close() throws IOException {
                    in.close();
                }

                @Override
                public synchronized void mark(int readLimit) {
                    in.mark(readLimit);
                }

                @Override
                public synchronized void reset() {
                    in.reset();
                }
            };
        }
    }

}
