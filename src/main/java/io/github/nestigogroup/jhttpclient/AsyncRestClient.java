package io.github.nestigogroup.jhttpclient;

import io.github.nestigogroup.jhttpclient.internal.AsyncHttpClient;
import io.github.nestigogroup.jhttpclient.responses.NoBodyResponse;
import io.github.nestigogroup.jhttpclient.responses.StringResponse;

import javax.net.ssl.SSLContext;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AsyncRestClient extends AsyncHttpClient {

    public AsyncRestClient() {
        super();
        addHeader("Content-Type", "application/json");
    }

    public AsyncRestClient(HttpClient.Version version, HttpClient.Redirect redirectPolicy, Duration timeout, SSLContext sslContext, Map<String, String> headers, Charset charset) {
        super(version, redirectPolicy, timeout, sslContext, headers, charset);
        addHeader("Content-Type", "application/json");
    }

    public CompletableFuture<NoBodyResponse> head(String url) {
        return headBodyHandler(url).thenApplyAsync(resp -> new NoBodyResponse(resp.statusCode(), resp.headers().map()));
    }
    public CompletableFuture<StringResponse> get(String url) {
        return getString(url).thenApplyAsync(resp -> new StringResponse(resp.statusCode(), resp.headers().map(), resp.body()));
    }

    public CompletableFuture<StringResponse> post(String url, String body) {
        return postString(url, HttpRequest.BodyPublishers.ofString(body)).thenApplyAsync(resp -> new StringResponse(resp.statusCode(), resp.headers().map(), resp.body()));
    }

    public CompletableFuture<StringResponse> put(String url, String body) {
        return putString(url, HttpRequest.BodyPublishers.ofString(body)).thenApplyAsync(resp -> new StringResponse(resp.statusCode(), resp.headers().map(), resp.body()));
    }

    public CompletableFuture<StringResponse> patch(String url, String body) {
        return patchString(url, HttpRequest.BodyPublishers.ofString(body)).thenApplyAsync(resp -> new StringResponse(resp.statusCode(), resp.headers().map(), resp.body()));
    }

    public CompletableFuture<StringResponse> delete(String url) {
        return deleteString(url).thenApplyAsync(resp -> new StringResponse(resp.statusCode(), resp.headers().map(), resp.body()));
    }

    public void downloadFile(String url, Path downloadPath) throws ExecutionException, InterruptedException {
        getFile(url, downloadPath).get();
    }
}
