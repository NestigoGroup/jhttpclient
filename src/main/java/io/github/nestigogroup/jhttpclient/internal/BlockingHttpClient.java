package io.github.nestigogroup.jhttpclient.internal;

import io.github.nestigogroup.jhttpclient.helpers.RequestsHelper;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class BlockingHttpClient {

    private HttpClient httpClient;
    private Map<String, String> headers;
    private Charset charset;

    public BlockingHttpClient() {
        httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).followRedirects(HttpClient.Redirect.NORMAL).connectTimeout(Duration.ofSeconds(30)).build();
        headers = new HashMap<>();
        addHeader("User-Agent", "RestHttpClient/1.0");
        charset = StandardCharsets.UTF_8;
    }

    public BlockingHttpClient(HttpClient.Version version, HttpClient.Redirect redirectPolicy, Duration timeout, SSLContext sslContext, Map<String, String> headers, Charset charset) {
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

    public HttpResponse<Void> headBodyHandler(String url) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder().uri(URI.create(url)).method("HEAD", HttpRequest.BodyPublishers.noBody()).headers(RequestsHelper.convertToHeadersArray(headers)).build();
        return httpClient.send(request, HttpResponse.BodyHandlers.discarding());
    }

    public HttpResponse<?> getBodyHandler(String url, HttpResponse.BodyHandler<?> respHandler) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder().uri(URI.create(url)).GET().headers(RequestsHelper.convertToHeadersArray(headers)).build();
        return httpClient.send(request, respHandler);
    }

    @SuppressWarnings("unchecked")
    public HttpResponse<byte[]> getBinary(String url) throws IOException, InterruptedException {
        return (HttpResponse<byte[]>) getBodyHandler(url, HttpResponse.BodyHandlers.ofByteArray());
    }

    @SuppressWarnings("unchecked")
    public HttpResponse<String> getString(String url) throws IOException, InterruptedException {
        return (HttpResponse<String>) getBodyHandler(url, HttpResponse.BodyHandlers.ofString(charset));
    }

    @SuppressWarnings("unchecked")
    public HttpResponse<Path> getFile(String url, Path path) throws IOException, InterruptedException {
        return (HttpResponse<Path>) getBodyHandler(url, HttpResponse.BodyHandlers.ofFileDownload(path));
    }

    public HttpResponse<?> postBodyHandler(String url, HttpResponse.BodyHandler<?> respHandler, HttpRequest.BodyPublisher body) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder().uri(URI.create(url)).POST(body).headers(RequestsHelper.convertToHeadersArray(headers)).build();
        return httpClient.send(request, respHandler);
    }

    @SuppressWarnings("unchecked")
    public HttpResponse<byte[]> postBinary(String url, HttpRequest.BodyPublisher body) throws IOException, InterruptedException {
        return (HttpResponse<byte[]>) postBodyHandler(url, HttpResponse.BodyHandlers.ofByteArray(), body);
    }

    @SuppressWarnings("unchecked")
    public HttpResponse<String> postString(String url, HttpRequest.BodyPublisher body) throws IOException, InterruptedException {
        return (HttpResponse<String>) postBodyHandler(url, HttpResponse.BodyHandlers.ofString(charset), body);
    }

    @SuppressWarnings("unchecked")
    public HttpResponse<Path> postFile(String url, Path path, HttpRequest.BodyPublisher body) throws IOException, InterruptedException {
        return (HttpResponse<Path>) postBodyHandler(url, HttpResponse.BodyHandlers.ofFileDownload(path), body);
    }

    public HttpResponse<?> putBodyHandler(String url, HttpResponse.BodyHandler<?> respHandler, HttpRequest.BodyPublisher body) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder().uri(URI.create(url)).PUT(body).headers(RequestsHelper.convertToHeadersArray(headers)).build();
        return httpClient.send(request, respHandler);
    }

    @SuppressWarnings("unchecked")
    public HttpResponse<byte[]> putBinary(String url, HttpRequest.BodyPublisher body) throws IOException, InterruptedException {
        return (HttpResponse<byte[]>) putBodyHandler(url, HttpResponse.BodyHandlers.ofByteArray(), body);
    }

    @SuppressWarnings("unchecked")
    public HttpResponse<String> putString(String url, HttpRequest.BodyPublisher body) throws IOException, InterruptedException {
        return (HttpResponse<String>) putBodyHandler(url, HttpResponse.BodyHandlers.ofString(charset), body);
    }

    @SuppressWarnings("unchecked")
    public HttpResponse<Path> putFile(String url, Path path, HttpRequest.BodyPublisher body) throws IOException, InterruptedException {
        return (HttpResponse<Path>) putBodyHandler(url, HttpResponse.BodyHandlers.ofFileDownload(path), body);
    }

    public HttpResponse<?> patchBodyHandler(String url, HttpResponse.BodyHandler<?> respHandler, HttpRequest.BodyPublisher body) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder().uri(URI.create(url)).method("PATCH", body).headers(RequestsHelper.convertToHeadersArray(headers)).build();
        return httpClient.send(request, respHandler);
    }

    @SuppressWarnings("unchecked")
    public HttpResponse<byte[]> patchBinary(String url, HttpRequest.BodyPublisher body) throws IOException, InterruptedException {
        return (HttpResponse<byte[]>) patchBodyHandler(url, HttpResponse.BodyHandlers.ofByteArray(), body);
    }

    @SuppressWarnings("unchecked")
    public HttpResponse<String> patchString(String url, HttpRequest.BodyPublisher body) throws IOException, InterruptedException {
        return (HttpResponse<String>) patchBodyHandler(url, HttpResponse.BodyHandlers.ofString(charset), body);
    }

    @SuppressWarnings("unchecked")
    public HttpResponse<Path> patchFile(String url, Path path, HttpRequest.BodyPublisher body) throws IOException, InterruptedException {
        return (HttpResponse<Path>) patchBodyHandler(url, HttpResponse.BodyHandlers.ofFileDownload(path), body);
    }

    public HttpResponse<?> deleteBodyHandler(String url, HttpResponse.BodyHandler<?> respHandler) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder().uri(URI.create(url)).DELETE().headers(RequestsHelper.convertToHeadersArray(headers)).build();
        return httpClient.send(request, respHandler);
    }

    @SuppressWarnings("unchecked")
    public HttpResponse<byte[]> deleteBinary(String url) throws IOException, InterruptedException {
        return (HttpResponse<byte[]>) deleteBodyHandler(url, HttpResponse.BodyHandlers.ofByteArray());
    }

    @SuppressWarnings("unchecked")
    public HttpResponse<String> deleteString(String url) throws IOException, InterruptedException {
        return (HttpResponse<String>) deleteBodyHandler(url, HttpResponse.BodyHandlers.ofString(charset));
    }

    @SuppressWarnings("unchecked")
    public HttpResponse<Path> deleteFile(String url, Path path) throws IOException, InterruptedException {
        return (HttpResponse<Path>) deleteBodyHandler(url, HttpResponse.BodyHandlers.ofFileDownload(path));
    }

}
