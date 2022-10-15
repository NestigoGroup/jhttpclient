package io.github.nestigogroup.jhttpclient;

import io.github.nestigogroup.jhttpclient.interfaces.IObjectMapper;
import io.github.nestigogroup.jhttpclient.internal.BlockingHttpClient;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class RestJsonClient extends BlockingHttpClient {

    private IObjectMapper externalMapper;

    private RestJsonClient() {
        super();
    }

    public RestJsonClient(HttpClient.Version version, HttpClient.Redirect redirectPolicy, Duration timeout, SSLContext sslContext, Map<String, String> headers, Charset charset, IObjectMapper objectMapper) {
        super(version, redirectPolicy, timeout, sslContext, headers, charset);
        externalMapper = objectMapper;
        addHeader("Content-Type", "application/json");
    }

    public RestJsonClient(IObjectMapper objectMapper) {
        super();
        externalMapper = objectMapper;
        addHeader("Content-Type", "application/json");
    }

    public Map<String, List<String>> head(String url) throws IOException, InterruptedException {
        return headBodyHandler(url).headers().map();
    }
    public Object get(String url, Class<?> outClass) throws IOException, InterruptedException {
        return externalMapper.convertFromJson(getString(url).body(), outClass);
    }

    public Object post(String url, Class<?> outClass, Object body) throws IOException, InterruptedException {
        return externalMapper.convertFromJson(postString(url, HttpRequest.BodyPublishers.ofString(externalMapper.convertToJson(body))).body(), outClass);
    }

    public Object put(String url, Class<?> outClass, Object body) throws IOException, InterruptedException {
        return externalMapper.convertFromJson(putString(url, HttpRequest.BodyPublishers.ofString(externalMapper.convertToJson(body))).body(), outClass);
    }

    public Object patch(String url, Class<?> outClass, Object body) throws IOException, InterruptedException {
        return externalMapper.convertFromJson(patchString(url, HttpRequest.BodyPublishers.ofString(externalMapper.convertToJson(body))).body(), outClass);
    }

    public Object delete(String url, Class<?> outClass) throws IOException, InterruptedException {
        return externalMapper.convertFromJson(deleteString(url).body(), outClass);
    }

    public void downloadFile(String url, Path downloadPath) throws IOException, InterruptedException {
        getFile(url, downloadPath);
    }

}
