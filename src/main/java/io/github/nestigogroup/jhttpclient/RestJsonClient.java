package io.github.nestigogroup.jhttpclient;

import io.github.nestigogroup.jhttpclient.interfaces.IObjectMapper;
import io.github.nestigogroup.jhttpclient.internal.BlockingHttpClient;
import io.github.nestigogroup.jhttpclient.responses.NoBodyResponse;
import io.github.nestigogroup.jhttpclient.responses.MappedResponse;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.time.Duration;
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

    public NoBodyResponse head(String url) throws IOException, InterruptedException {
        var resp = headBodyHandler(url);
        return new NoBodyResponse(resp.statusCode(), resp.headers().map());
    }
    public MappedResponse get(String url, Class<?> outClass) throws IOException, InterruptedException {
        var resp = getString(url);
        return new MappedResponse(resp.statusCode(), resp.headers().map(), externalMapper.convertFromJson(resp.body(), outClass));
    }

    public Object post(String url, Class<?> outClass, Object body) throws IOException, InterruptedException {
        var resp = postString(url, HttpRequest.BodyPublishers.ofString(externalMapper.convertToJson(body)));
        return new MappedResponse(resp.statusCode(), resp.headers().map(), externalMapper.convertFromJson(resp.body(), outClass));
    }

    public Object put(String url, Class<?> outClass, Object body) throws IOException, InterruptedException {
        var resp = putString(url, HttpRequest.BodyPublishers.ofString(externalMapper.convertToJson(body)));
        return new MappedResponse(resp.statusCode(), resp.headers().map(), externalMapper.convertFromJson(resp.body(), outClass));
    }

    public Object patch(String url, Class<?> outClass, Object body) throws IOException, InterruptedException {
        var resp = patchString(url, HttpRequest.BodyPublishers.ofString(externalMapper.convertToJson(body)));
        return new MappedResponse(resp.statusCode(), resp.headers().map(), externalMapper.convertFromJson(resp.body(), outClass));
    }

    public Object delete(String url, Class<?> outClass) throws IOException, InterruptedException {
        var resp = deleteString(url);
        return new MappedResponse(resp.statusCode(), resp.headers().map(), externalMapper.convertFromJson(resp.body(), outClass));
    }

    public void downloadFile(String url, Path downloadPath) throws IOException, InterruptedException {
        getFile(url, downloadPath);
    }

}
