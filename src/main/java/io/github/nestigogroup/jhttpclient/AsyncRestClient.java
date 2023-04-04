package io.github.nestigogroup.jhttpclient;

import io.github.nestigogroup.jhttpclient.internal.AsyncHttpClient;
import io.github.nestigogroup.jhttpclient.responses.FileResponse;
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
import java.util.concurrent.Executor;

/**
 * Simplified Async Rest Http Client for working with Rest services
 * <b>Content-Type</b> is set to <i>application/json</i> by default (but can be overridden)
 */
public class AsyncRestClient extends AsyncHttpClient {

    /**
     * Creates an instance of the {@link AsyncRestClient} with HTTP version 1.1, preventing redirects from <i>Https</i> to <i>Http</i>, 30 seconds timeout and UTF-8 as Charset and <b>Content-Type</b> as <i>application/json</i>
     */
    public AsyncRestClient() {
        super();
        addHeader("Content-Type", "application/json");
    }

    /**
     * Creates an instance of the {@link AsyncRestClient} with the specified parameters and <b>Content-Type</b> as <i>application/json</i>
     * @param version the HTTP version (refer: {@link java.net.http.HttpClient.Version})
     * @param redirectPolicy the redirect policy (refer: {@link java.net.http.HttpClient.Redirect})
     * @param timeout the timeout as {@link Duration}
     * @param sslContext the {@link SSLContext}
     * @param headers {@link Map} of header key/value pairs to be included in all requests
     * @param charset The specified {@link Charset}
     */
    public AsyncRestClient(HttpClient.Version version, HttpClient.Redirect redirectPolicy, Duration timeout, SSLContext sslContext, Map<String, String> headers, Charset charset) {
        super(version, redirectPolicy, timeout, sslContext, headers, charset);
        addHeader("Content-Type", "application/json");
    }

    /**
     * Creates an instance of the {@link AsyncRestClient} with the specified parameters and <b>Content-Type</b> as <i>application/json</i>
     * @param version the HTTP version (refer: {@link java.net.http.HttpClient.Version})
     * @param executor the underlining executor to use
     * @param redirectPolicy the redirect policy (refer: {@link java.net.http.HttpClient.Redirect})
     * @param timeout the timeout as {@link Duration}
     * @param sslContext the {@link SSLContext}
     * @param headers {@link Map} of header key/value pairs to be included in all requests
     * @param charset The specified {@link Charset}
     */
    public AsyncRestClient(HttpClient.Version version, Executor executor, HttpClient.Redirect redirectPolicy, Duration timeout, SSLContext sslContext, Map<String, String> headers, Charset charset) {
        super(version, executor, redirectPolicy, timeout, sslContext, headers, charset);
        addHeader("Content-Type", "application/json");
    }

    /**
     * Performs an asynchronous HEAD request
     * @param url The Request URL
     * @return CompletableFuture resolving to {@link NoBodyResponse object} containing the response code and the response headers
     */
    public CompletableFuture<NoBodyResponse> head(String url) {
        return headBodyHandler(url).thenApplyAsync(resp -> new NoBodyResponse(resp.statusCode(), resp.headers().map()));
    }

    /**
     * Performs an asynchronous GET request
     * @param url The Request URL
     * @return CompletableFuture resolving to {@link StringResponse object} containing the response code, response headers and the response body as {@link String}
     */
    public CompletableFuture<StringResponse> get(String url) {
        return getString(url).thenApplyAsync(resp -> new StringResponse(resp.statusCode(), resp.headers().map(), resp.body()));
    }

    /**
     * Performs an asynchronous POST request
     * @param url The Request URL
     * @param body The request String body
     * @return CompletableFuture resolving to {@link StringResponse object} containing the response code, response headers and the response body as {@link String}
     */
    public CompletableFuture<StringResponse> post(String url, String body) {
        return postString(url, HttpRequest.BodyPublishers.ofString(body)).thenApplyAsync(resp -> new StringResponse(resp.statusCode(), resp.headers().map(), resp.body()));
    }

    /**
     * Performs an asynchronous PUT request
     * @param url The Request URL
     * @param body The request String body
     * @return CompletableFuture resolving to {@link StringResponse object} containing the response code, response headers and the response body as {@link String}
     */
    public CompletableFuture<StringResponse> put(String url, String body) {
        return putString(url, HttpRequest.BodyPublishers.ofString(body)).thenApplyAsync(resp -> new StringResponse(resp.statusCode(), resp.headers().map(), resp.body()));
    }

    /**
     * Performs an asynchronous PATCH request
     * @param url The Request URL
     * @param body The request String body
     * @return CompletableFuture resolving to {@link StringResponse object} containing the response code, response headers and the response body as {@link String}
     */
    public CompletableFuture<StringResponse> patch(String url, String body) {
        return patchString(url, HttpRequest.BodyPublishers.ofString(body)).thenApplyAsync(resp -> new StringResponse(resp.statusCode(), resp.headers().map(), resp.body()));
    }

    /**
     * Performs an asynchronous DELETE request
     * @param url The Request URL
     * @return CompletableFuture resolving to {@link StringResponse object} containing the response code, response headers and the response body as {@link String}
     */
    public CompletableFuture<StringResponse> delete(String url) {
        return deleteString(url).thenApplyAsync(resp -> new StringResponse(resp.statusCode(), resp.headers().map(), resp.body()));
    }

    /**
     * Downloads a file to specified location
     * @param url - The file URL
     * @param downloadPath - The location where the file to be stored
     * @return CompletableFuture resolving to {@link FileResponse object} containing the response code, response headers and the response body as {@link Path}
     */
    public CompletableFuture<FileResponse> downloadFile(String url, Path downloadPath) throws ExecutionException, InterruptedException {
        return getFile(url, downloadPath).thenApplyAsync(resp -> new FileResponse(resp.statusCode(), resp.headers().map(), resp.body()));
    }
}
