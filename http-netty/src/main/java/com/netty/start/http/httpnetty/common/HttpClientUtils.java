package com.netty.start.http.httpnetty.common;


import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStreamReader;

import static com.netty.start.http.httpnetty.common.GatewayOptions.GATEWAY_OPTION_SERVICE_ACCESS_ERROR;

public class HttpClientUtils {
    public static StringBuilder post(String serverUrl, String xml) {
        StringBuilder result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(serverUrl);
        CloseableHttpResponse response = null;
        try {
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new ByteArrayEntity(xml.getBytes("UTF-8")));
            response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();
            result = entityToString(entity);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new StringBuilder(String.valueOf(GATEWAY_OPTION_SERVICE_ACCESS_ERROR));
    }

    private static StringBuilder entityToString(HttpEntity entity) throws IOException {
        StringBuilder result = null;
        if (entity != null) {
            long lenth = entity.getContentLength();
            if (lenth != -1 && lenth < 2048) {
                result = new StringBuilder(EntityUtils.toString(entity, "UTF-8"));
            } else {
                InputStreamReader reader1 = new InputStreamReader(entity.getContent(), "UTF-8");
                CharArrayBuffer buffer = new CharArrayBuffer(2048);
                char[] tmp = new char[1024];
                int l;
                while ((l = reader1.read(tmp)) != -1) {
                    buffer.append(tmp, 0, l);
                }
                result = new StringBuilder(buffer.toString());
            }
        }
        return result;
    }
}
