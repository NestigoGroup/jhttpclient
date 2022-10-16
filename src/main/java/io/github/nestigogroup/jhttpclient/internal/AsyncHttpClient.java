package io.github.nestigogroup.jhttpclient.internal;

import io.github.nestigogroup.jhttpclient.helpers.RequestsHelper;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class AsyncHttpClient {

    private HttpClient httpClient;
    private Map<String, String> headers;
    private Charset charset;

    public AsyncHttpClient() {
        httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).followRedirects(HttpClient.Redirect.NORMAL).connectTimeout(Duration.ofSeconds(30)).build();
        headers = new HashMap<>();
        addHeader("User-Agent", "RestHttpClient/1.0");
        charset = StandardCharsets.UTF_8;
    }

    public AsyncHttpClient(HttpClient.Version version, HttpClient.Redirect redirectPolicy, Duration timeout, SSLContext sslContext, Map<String, String> headers, Charset charset) {
        this.headers = new HashMap<>();
        var clientBuilder = HttpClient.newBuilder();
        clientBuilder.version(version);
        clientBuilder.followRedirects(redirectPolicy);
        clientBuilder.connectTimeout(timeout);
        clientBuilder.sslContext(sslContext);
        if (headers != null) {
            this.headers.putAll(headers);
        }
        addHeader("User-Agent", "RestHttpClient/1.0");
        this.charset = charset != null ? charset : StandardCharsets.UTF_8;
        httpClient = clientBuilder.build();
    }

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    public void removeHeader(String name) {
        headers.remove(name);
    }

    public CompletableFuture<HttpResponse<Void>> headBodyHandler(String url) {
        var request = HttpRequest.newBuilder().uri(URI.create(url)).method("HEAD", HttpRequest.BodyPublishers.noBody()).headers(RequestsHelper.convertToHeadersArray(headers)).build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.discarding());
    }

    public CompletableFuture getBodyHandler(String url, HttpResponse.BodyHandler<?> respHandler) {
        var request = HttpRequest.newBuilder().uri(URI.create(url)).GET().headers(RequestsHelper.convertToHeadersArray(headers)).build();
        return httpClient.sendAsync(request, respHandler);
    }

    @SuppressWarnings("unchecked")
    public CompletableFuture<HttpResponse<byte[]>> getBinary(String url) {
        return getBodyHandler(url, HttpResponse.BodyHandlers.ofByteArray());
    }

    @SuppressWarnings("unchecked")
    public CompletableFuture<HttpResponse<String>> getString(String url) {
        return getBodyHandler(url, HttpResponse.BodyHandlers.ofString(charset));
    }

    @SuppressWarnings("unchecked")
    public CompletableFuture<HttpResponse<Path>> getFile(String url, Path path) {
        return getBodyHandler(url, HttpResponse.BodyHandlers.ofFileDownload(path));
    }

    public CompletableFuture postBodyHandler(String url, HttpResponse.BodyHandler<?> respHandler, HttpRequest.BodyPublisher body) {
        var request = HttpRequest.newBuilder().uri(URI.create(url)).POST(body).headers(RequestsHelper.convertToHeadersArray(headers)).build();
        return httpClient.sendAsync(request, respHandler);
    }

    @SuppressWarnings("unchecked")
    public CompletableFuture<HttpResponse<byte[]>> postBinary(String url, HttpRequest.BodyPublisher body) {
        return postBodyHandler(url, HttpResponse.BodyHandlers.ofByteArray(), body);
    }

    @SuppressWarnings("unchecked")
    public CompletableFuture<HttpResponse<String> >postString(String url, HttpRequest.BodyPublisher body) {
        return postBodyHandler(url, HttpResponse.BodyHandlers.ofString(charset), body);
    }

    @SuppressWarnings("unchecked")
    public CompletableFuture<HttpResponse<Path>> postFile(String url, Path path, HttpRequest.BodyPublisher body) {
        return postBodyHandler(url, HttpResponse.BodyHandlers.ofFileDownload(path), body);
    }

    public CompletableFuture putBodyHandler(String url, HttpResponse.BodyHandler<?> respHandler, HttpRequest.BodyPublisher body) {
        var request = HttpRequest.newBuilder().uri(URI.create(url)).PUT(body).headers(RequestsHelper.convertToHeadersArray(headers)).build();
        return httpClient.sendAsync(request, respHandler);
    }

    @SuppressWarnings("unchecked")
    public CompletableFuture<HttpResponse<byte[]>> putBinary(String url, HttpRequest.BodyPublisher body) {
        return putBodyHandler(url, HttpResponse.BodyHandlers.ofByteArray(), body);
    }

    @SuppressWarnings("unchecked")
    public CompletableFuture<HttpResponse<String>> putString(String url, HttpRequest.BodyPublisher body) {
        return putBodyHandler(url, HttpResponse.BodyHandlers.ofString(charset), body);
    }

    @SuppressWarnings("unchecked")
    public CompletableFuture<HttpResponse<Path>> putFile(String url, Path path, HttpRequest.BodyPublisher body) {
        return putBodyHandler(url, HttpResponse.BodyHandlers.ofFileDownload(path), body);
    }

    public CompletableFuture patchBodyHandler(String url, HttpResponse.BodyHandler<?> respHandler, HttpRequest.BodyPublisher body) {
        var request = HttpRequest.newBuilder().uri(URI.create(url)).method("PATCH", body).headers(RequestsHelper.convertToHeadersArray(headers)).build();
        return httpClient.sendAsync(request, respHandler);
    }

    @SuppressWarnings("unchecked")
    public CompletableFuture<HttpResponse<byte[]>> patchBinary(String url, HttpRequest.BodyPublisher body) {
        return patchBodyHandler(url, HttpResponse.BodyHandlers.ofByteArray(), body);
    }

    @SuppressWarnings("unchecked")
    public CompletableFuture<HttpResponse<String>> patchString(String url, HttpRequest.BodyPublisher body) {
        return patchBodyHandler(url, HttpResponse.BodyHandlers.ofString(charset), body);
    }

    @SuppressWarnings("unchecked")
    public CompletableFuture<HttpResponse<Path>> patchFile(String url, Path path, HttpRequest.BodyPublisher body) {
        return patchBodyHandler(url, HttpResponse.BodyHandlers.ofFileDownload(path), body);
    }

    public CompletableFuture deleteBodyHandler(String url, HttpResponse.BodyHandler<?> respHandler) {
        var request = HttpRequest.newBuilder().uri(URI.create(url)).DELETE().headers(RequestsHelper.convertToHeadersArray(headers)).build();
        return httpClient.sendAsync(request, respHandler);
    }

    @SuppressWarnings("unchecked")
    public CompletableFuture<HttpResponse<byte[]>> deleteBinary(String url) {
        return deleteBodyHandler(url, HttpResponse.BodyHandlers.ofByteArray());
    }

    @SuppressWarnings("unchecked")
    public CompletableFuture<HttpResponse<String>> deleteString(String url) {
        return deleteBodyHandler(url, HttpResponse.BodyHandlers.ofString(charset));
    }

    @SuppressWarnings("unchecked")
    public CompletableFuture<HttpResponse<Path>> deleteFile(String url, Path path) {
        return deleteBodyHandler(url, HttpResponse.BodyHandlers.ofFileDownload(path));
    }
}
