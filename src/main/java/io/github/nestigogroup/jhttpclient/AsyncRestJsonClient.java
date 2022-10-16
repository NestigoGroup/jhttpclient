package io.github.nestigogroup.jhttpclient;

import io.github.nestigogroup.jhttpclient.interfaces.IObjectMapper;
import io.github.nestigogroup.jhttpclient.internal.AsyncHttpClient;
import io.github.nestigogroup.jhttpclient.responses.NoBodyResponse;
import io.github.nestigogroup.jhttpclient.responses.MappedResponse;

import javax.net.ssl.SSLContext;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AsyncRestJsonClient extends AsyncHttpClient {

    private IObjectMapper externalMapper;

    public AsyncRestJsonClient(IObjectMapper objectMapper) {
        super();
        externalMapper = objectMapper;
        addHeader("Content-Type", "application/json");
    }

    public AsyncRestJsonClient(HttpClient.Version version, HttpClient.Redirect redirectPolicy, Duration timeout, SSLContext sslContext, Map<String, String> headers, Charset charset, IObjectMapper objectMapper) {
        super(version, redirectPolicy, timeout, sslContext, headers, charset);
        externalMapper = objectMapper;
        addHeader("Content-Type", "application/json");
    }

    public CompletableFuture<NoBodyResponse> head(String url) {
        return headBodyHandler(url).thenApplyAsync(resp -> new NoBodyResponse(resp.statusCode(), resp.headers().map()));
    }
    public CompletableFuture<MappedResponse> get(String url, Class<?> outClass) {
        return getString(url).thenApplyAsync(resp -> new MappedResponse(resp.statusCode(), resp.headers().map(), externalMapper.convertFromJson(resp.body(), outClass)));
    }

    public CompletableFuture<MappedResponse> post(String url, Class<?> outClass, Object body) {
        return postString(url, HttpRequest.BodyPublishers.ofString(externalMapper.convertToJson(body))).thenApplyAsync(resp -> new MappedResponse(resp.statusCode(), resp.headers().map(), externalMapper.convertFromJson(resp.body(), outClass)));
    }

    public CompletableFuture<MappedResponse> put(String url, Class<?> outClass, Object body) {
        return putString(url, HttpRequest.BodyPublishers.ofString(externalMapper.convertToJson(body))).thenApplyAsync(resp -> new MappedResponse(resp.statusCode(), resp.headers().map(), externalMapper.convertFromJson(resp.body(), outClass)));
    }

    public CompletableFuture<MappedResponse> patch(String url, Class<?> outClass, Object body) {
        return patchString(url, HttpRequest.BodyPublishers.ofString(externalMapper.convertToJson(body))).thenApplyAsync(resp -> new MappedResponse(resp.statusCode(), resp.headers().map(), externalMapper.convertFromJson(resp.body(), outClass)));
    }

    public CompletableFuture<MappedResponse> delete(String url, Class<?> outClass) {
        return deleteString(url).thenApplyAsync(resp -> new MappedResponse(resp.statusCode(), resp.headers().map(), externalMapper.convertFromJson(resp.body(), outClass)));
    }

    public void downloadFile(String url, Path downloadPath) throws ExecutionException, InterruptedException {
        getFile(url, downloadPath).get();
    }
}
