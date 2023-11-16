package io.github.nestigogroup.jhttpclient.internal;

import io.github.nestigogroup.jhttpclient.helpers.RequestHelper;

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
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Blocking Http Client build around the {@link HttpClient}
 */
public class BlockingHttpClient {

    private final HttpClient httpClient;
    private final Map<String, String> headers;
    private final Charset charset;

    /**
     * Creates an instance of the {@link BlockingHttpClient} with HTTP version 1.1, preventing redirects from <i>Https</i> to <i>Http</i>, 30 seconds timeout and UTF-8 as Charset
     */
    public BlockingHttpClient() {
        httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).followRedirects(HttpClient.Redirect.NORMAL).connectTimeout(Duration.ofSeconds(30)).build();
        headers = new HashMap<>();
        addHeader("User-Agent", "RestHttpClient/1.0");
        charset = StandardCharsets.UTF_8;
    }

    /**
     * Creates an instance of the {@link BlockingHttpClient} with the specified parameters
     * @param version the HTTP version (refer: {@link java.net.http.HttpClient.Version})
     * @param redirectPolicy the redirect policy (refer: {@link java.net.http.HttpClient.Redirect})
     * @param timeout the timeout as {@link Duration}
     * @param sslContext the {@link SSLContext}
     * @param headers {@link Map} of header key/value pairs to be included in all requests
     * @param charset The specified {@link Charset}
     */
    public BlockingHttpClient(HttpClient.Version version, HttpClient.Redirect redirectPolicy, Duration timeout, SSLContext sslContext, Map<String, String> headers, Charset charset) {
        this(version, null, redirectPolicy, timeout, sslContext, headers, charset);
    }

    /**
     * Creates an instance of the {@link BlockingHttpClient} with the specified parameters
     * @param version the HTTP version (refer: {@link java.net.http.HttpClient.Version})
     * @param executor the underlining executor to use
     * @param redirectPolicy the redirect policy (refer: {@link java.net.http.HttpClient.Redirect})
     * @param timeout the timeout as {@link Duration}
     * @param sslContext the {@link SSLContext}
     * @param headers {@link Map} of header key/value pairs to be included in all requests
     * @param charset The specified {@link Charset}
     */
    public BlockingHttpClient(HttpClient.Version version, Executor executor, HttpClient.Redirect redirectPolicy, Duration timeout, SSLContext sslContext, Map<String, String> headers, Charset charset) {
        this.headers = new HashMap<>();
        var clientBuilder = HttpClient.newBuilder();
        clientBuilder.version(version);
        if(executor != null) {
            clientBuilder.executor(executor);
        }
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

    /**
     * Add a new header to be sent for every request after
     * @param name the header name
     * @param value tne header value
     */
    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    /**
     * Remove a header, so it is no longer send with every request.
     * Has no effect if the header doesn't exist
     * @param name the header name
     */
    public void removeHeader(String name) {
        headers.remove(name);
    }

    /**
     * Executes in a blocking manner a HEAD request toward the provided url
     * @param url The Request URL
     * @return {@link java.net.http.HttpResponse} object
     */
    public HttpResponse<Void> headBodyHandler(String url) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder().uri(URI.create(url)).method("HEAD", HttpRequest.BodyPublishers.noBody()).headers(RequestHelper.convertToHeadersArray(headers)).build();
        return httpClient.send(request, HttpResponse.BodyHandlers.discarding());
    }

    /**
     * Executes in a blocking manner a GET request toward the provided url
     * @param url The Request URL
     * @param respHandler The specific handler to process the response (refer: {@link java.net.http.HttpResponse.BodyHandler})
     * @return {@link java.net.http.HttpResponse} object
     */
    public <T> HttpResponse<T> getBodyHandler(String url, HttpResponse.BodyHandler<T> respHandler) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder().uri(URI.create(url)).GET().headers(RequestHelper.convertToHeadersArray(headers)).build();
        return httpClient.send(request, respHandler);
    }

    /**
     * Executes in a blocking manner a GET request toward the provided url and process the response as <b>byte[]</b>
     * @param url The Request URL
     * @return {@link java.net.http.HttpResponse} object
     */
    public HttpResponse<byte[]> getBinary(String url) throws IOException, InterruptedException {
        return getBodyHandler(url, HttpResponse.BodyHandlers.ofByteArray());
    }

    /**
     * Executes in a blocking manner a GET request toward the provided url and process the response as {@link String}
     * @param url The Request URL
     * @return {@link java.net.http.HttpResponse} object
     */
    public HttpResponse<String> getString(String url) throws IOException, InterruptedException {
        return getBodyHandler(url, HttpResponse.BodyHandlers.ofString(charset));
    }

    /**
     * Executes in a blocking manner a GET request toward the provided url and process the response by storing it in <b>File</b> define by the provided {@link Path}
     * @param url The Request URL
     * @param path The location where the file should be downloaded
     * @return {@link java.net.http.HttpResponse} object
     */
    public HttpResponse<Path> getFile(String url, Path path) throws IOException, InterruptedException {
        return getBodyHandler(url, HttpResponse.BodyHandlers.ofFileDownload(path));
    }

    /**
     * Executes in a blocking manner a POST request toward the provided url
     * @param url The Request URL
     * @param respHandler The specific handler to process the response (refer: {@link java.net.http.HttpResponse.BodyHandler})
     * @param body The pre-build {@link java.net.http.HttpRequest.BodyPublisher} with the request body
     * @return {@link java.net.http.HttpResponse} object
     */
    public <T> HttpResponse<T> postBodyHandler(String url, HttpResponse.BodyHandler<T> respHandler, HttpRequest.BodyPublisher body) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder().uri(URI.create(url)).POST(body).headers(RequestHelper.convertToHeadersArray(headers)).build();
        return httpClient.send(request, respHandler);
    }

    /**
     * Executes in a blocking manner a POST request toward the provided url and process the response as <b>byte[]</b>
     * @param url The Request URL
     * @param body The pre-build {@link java.net.http.HttpRequest.BodyPublisher} with the request body
     * @return {@link java.net.http.HttpResponse} object
     */
    public HttpResponse<byte[]> postBinary(String url, HttpRequest.BodyPublisher body) throws IOException, InterruptedException {
        return postBodyHandler(url, HttpResponse.BodyHandlers.ofByteArray(), body);
    }

    /**
     * Executes in a blocking manner a POST request toward the provided url and process the response as {@link String}
     * @param url The Request URL
     * @param body The pre-build {@link java.net.http.HttpRequest.BodyPublisher} with the request body
     * @return {@link java.net.http.HttpResponse} object
     */
    public HttpResponse<String> postString(String url, HttpRequest.BodyPublisher body) throws IOException, InterruptedException {
        return postBodyHandler(url, HttpResponse.BodyHandlers.ofString(charset), body);
    }

    /**
     * Executes in a blocking manner a POST request toward the provided url the response by storing it in <b>File</b> define by the provided {@link Path}
     * @param url The Request URL
     * @param path The location where the file should be downloaded
     * @param body The pre-build {@link java.net.http.HttpRequest.BodyPublisher} with the request body
     * @return {@link java.net.http.HttpResponse} object
     */
    public HttpResponse<Path> postFile(String url, Path path, HttpRequest.BodyPublisher body) throws IOException, InterruptedException {
        return postBodyHandler(url, HttpResponse.BodyHandlers.ofFileDownload(path), body);
    }

    /**
     * Executes in a blocking manner a PUT request toward the provided url
     * @param url The Request URL
     * @param respHandler The specific handler to process the response (refer: {@link java.net.http.HttpResponse.BodyHandler})
     * @param body The pre-build {@link java.net.http.HttpRequest.BodyPublisher} with the request body
     * @return {@link java.net.http.HttpResponse} object
     */
    public <T> HttpResponse<T> putBodyHandler(String url, HttpResponse.BodyHandler<T> respHandler, HttpRequest.BodyPublisher body) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder().uri(URI.create(url)).PUT(body).headers(RequestHelper.convertToHeadersArray(headers)).build();
        return httpClient.send(request, respHandler);
    }

    /**
     * Executes in a blocking manner a PUT request toward the provided url and process the response as <b>byte[]</b>
     * @param url The Request URL
     * @param body The pre-build {@link java.net.http.HttpRequest.BodyPublisher} with the request body
     * @return {@link java.net.http.HttpResponse} object
     */
    public HttpResponse<byte[]> putBinary(String url, HttpRequest.BodyPublisher body) throws IOException, InterruptedException {
        return putBodyHandler(url, HttpResponse.BodyHandlers.ofByteArray(), body);
    }

    /**
     * Executes in a blocking manner a PUT request toward the provided url and process the response as {@link String}
     * @param url The Request URL
     * @param body The pre-build {@link java.net.http.HttpRequest.BodyPublisher} with the request body
     * @return {@link java.net.http.HttpResponse} object
     */
    public HttpResponse<String> putString(String url, HttpRequest.BodyPublisher body) throws IOException, InterruptedException {
        return putBodyHandler(url, HttpResponse.BodyHandlers.ofString(charset), body);
    }

    /**
     * Executes in a blocking manner a PUT request toward the provided url the response by storing it in <b>File</b> define by the provided {@link Path}
     * @param url The Request URL
     * @param path The location where the file should be downloaded
     * @param body The pre-build {@link java.net.http.HttpRequest.BodyPublisher} with the request body
     * @return {@link java.net.http.HttpResponse} object
     */
    public HttpResponse<Path> putFile(String url, Path path, HttpRequest.BodyPublisher body) throws IOException, InterruptedException {
        return putBodyHandler(url, HttpResponse.BodyHandlers.ofFileDownload(path), body);
    }

    /**
     * Executes in a blocking manner a PATCH request toward the provided url
     * @param url The Request URL
     * @param respHandler The specific handler to process the response (refer: {@link java.net.http.HttpResponse.BodyHandler})
     * @param body The pre-build {@link java.net.http.HttpRequest.BodyPublisher} with the request body
     * @return {@link java.net.http.HttpResponse} object
     */
    public <T> HttpResponse<T> patchBodyHandler(String url, HttpResponse.BodyHandler<T> respHandler, HttpRequest.BodyPublisher body) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder().uri(URI.create(url)).method("PATCH", body).headers(RequestHelper.convertToHeadersArray(headers)).build();
        return httpClient.send(request, respHandler);
    }

    /**
     * Executes in a blocking manner a PATCH request toward the provided url and process the response as <b>byte[]</b>
     * @param url The Request URL
     * @param body The pre-build {@link java.net.http.HttpRequest.BodyPublisher} with the request body
     * @return {@link java.net.http.HttpResponse} object
     */
    public HttpResponse<byte[]> patchBinary(String url, HttpRequest.BodyPublisher body) throws IOException, InterruptedException {
        return patchBodyHandler(url, HttpResponse.BodyHandlers.ofByteArray(), body);
    }

    /**
     * Executes in a blocking manner a PATCH request toward the provided url and process the response as {@link String}
     * @param url The Request URL
     * @param body The pre-build {@link java.net.http.HttpRequest.BodyPublisher} with the request body
     * @return {@link java.net.http.HttpResponse} object
     */
    public HttpResponse<String> patchString(String url, HttpRequest.BodyPublisher body) throws IOException, InterruptedException {
        return patchBodyHandler(url, HttpResponse.BodyHandlers.ofString(charset), body);
    }

    /**
     * Executes in a blocking manner a PATCH request toward the provided url the response by storing it in <b>File</b> define by the provided {@link Path}
     * @param url The Request URL
     * @param path The location where the file should be downloaded
     * @param body The pre-build {@link java.net.http.HttpRequest.BodyPublisher} with the request body
     * @return {@link java.net.http.HttpResponse} object
     */
    public HttpResponse<Path> patchFile(String url, Path path, HttpRequest.BodyPublisher body) throws IOException, InterruptedException {
        return patchBodyHandler(url, HttpResponse.BodyHandlers.ofFileDownload(path), body);
    }

    /**
     * Executes in a blocking manner a DELETE request toward the provided url
     * @param url The Request URL
     * @param respHandler The specific handler to process the response (refer: {@link java.net.http.HttpResponse.BodyHandler})
     * @return {@link java.net.http.HttpResponse} object
     */
    public <T> HttpResponse<T> deleteBodyHandler(String url, HttpResponse.BodyHandler<T> respHandler) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder().uri(URI.create(url)).DELETE().headers(RequestHelper.convertToHeadersArray(headers)).build();
        return httpClient.send(request, respHandler);
    }

    /**
     * Executes in a blocking manner a DELETE request toward the provided url and process the response as <b>byte[]</b>
     * @param url The Request URL
     * @return {@link java.net.http.HttpResponse} object
     */
    public HttpResponse<byte[]> deleteBinary(String url) throws IOException, InterruptedException {
        return deleteBodyHandler(url, HttpResponse.BodyHandlers.ofByteArray());
    }

    /**
     * Executes in a blocking manner a DELETE request toward the provided url and process the response as {@link String}
     * @param url The Request URL
     * @return {@link java.net.http.HttpResponse} object
     */
    public HttpResponse<String> deleteString(String url) throws IOException, InterruptedException {
        return deleteBodyHandler(url, HttpResponse.BodyHandlers.ofString(charset));
    }

    /**
     * Executes in a blocking manner a DELETE request toward the provided url and process the response by storing it in <b>File</b> define by the provided {@link Path}
     * @param url The Request URL
     * @param path The location where the file should be downloaded
     * @return {@link java.net.http.HttpResponse} object
     */
    public HttpResponse<Path> deleteFile(String url, Path path) throws IOException, InterruptedException {
        return deleteBodyHandler(url, HttpResponse.BodyHandlers.ofFileDownload(path));
    }

}
