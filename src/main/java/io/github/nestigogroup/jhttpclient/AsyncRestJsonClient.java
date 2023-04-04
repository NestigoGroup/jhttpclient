package io.github.nestigogroup.jhttpclient;

import io.github.nestigogroup.jhttpclient.exceptions.ObjectMappingException;
import io.github.nestigogroup.jhttpclient.exceptions.RuntimeObjectMappingException;
import io.github.nestigogroup.jhttpclient.interfaces.IObjectMapper;
import io.github.nestigogroup.jhttpclient.internal.AsyncHttpClient;
import io.github.nestigogroup.jhttpclient.responses.FileResponse;
import io.github.nestigogroup.jhttpclient.responses.NoBodyResponse;
import io.github.nestigogroup.jhttpclient.responses.MappedResponse;
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
 * Simplified Async Rest Http Client for working with Rest services that handles serialization/deserialization of request/responses
 * <b>Content-Type</b> is set to <i>application/json</i> by default (but can be overridden)
 */
public class AsyncRestJsonClient extends AsyncHttpClient {

    private IObjectMapper externalMapper;

    /**
     * The default constructor is made private as the Client doesn't work without provided {@link IObjectMapper} implementation
     */
    private AsyncRestJsonClient() {
        super();
    }

    /**
     * Creates an instance of the {@link AsyncRestJsonClient} with HTTP version 1.1, preventing redirects from <i>Https</i> to <i>Http</i>, 30 seconds timeout and UTF-8 as Charset and <b>Content-Type</b> as <i>application/json</i>
     * @param objectMapper {@link IObjectMapper} implementation
     */
    public AsyncRestJsonClient(IObjectMapper objectMapper) {
        super();
        externalMapper = objectMapper;
        addHeader("Content-Type", "application/json");
    }

    /**
     * Creates an instance of the {@link AsyncRestJsonClient} with the specified parameters and <b>Content-Type</b> as <i>application/json</i>
     * @param version the HTTP version (refer: {@link java.net.http.HttpClient.Version})
     * @param redirectPolicy the redirect policy (refer: {@link java.net.http.HttpClient.Redirect})
     * @param timeout the timeout as {@link Duration}
     * @param sslContext the {@link SSLContext}
     * @param headers {@link Map} of header key/value pairs to be included in all requests
     * @param charset The specified {@link Charset}
     * @param objectMapper {@link IObjectMapper} implementation
     */
    public AsyncRestJsonClient(HttpClient.Version version, HttpClient.Redirect redirectPolicy, Duration timeout, SSLContext sslContext, Map<String, String> headers, Charset charset, IObjectMapper objectMapper) {
        super(version, redirectPolicy, timeout, sslContext, headers, charset);
        externalMapper = objectMapper;
        addHeader("Content-Type", "application/json");
    }

    /**
     * Creates an instance of the {@link AsyncRestJsonClient} with the specified parameters and <b>Content-Type</b> as <i>application/json</i>
     * @param version the HTTP version (refer: {@link java.net.http.HttpClient.Version})
     * @param executor the underlining executor to use
     * @param redirectPolicy the redirect policy (refer: {@link java.net.http.HttpClient.Redirect})
     * @param timeout the timeout as {@link Duration}
     * @param sslContext the {@link SSLContext}
     * @param headers {@link Map} of header key/value pairs to be included in all requests
     * @param charset The specified {@link Charset}
     * @param objectMapper {@link IObjectMapper} implementation
     */
    public AsyncRestJsonClient(HttpClient.Version version, Executor executor, HttpClient.Redirect redirectPolicy, Duration timeout, SSLContext sslContext, Map<String, String> headers, Charset charset, IObjectMapper objectMapper) {
        super(version, executor, redirectPolicy, timeout, sslContext, headers, charset);
        externalMapper = objectMapper;
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
     * @return CompletableFuture resolving to {@link MappedResponse object} containing the response code, response headers and the response body as deserialized POJO/Record
     * @throws RuntimeObjectMappingException if the deserialization fails
     */
    public CompletableFuture<MappedResponse> get(String url, Class<?> outClass) {
        return getString(url).thenApplyAsync(resp -> {
            try {
                return new MappedResponse(resp.statusCode(), resp.headers().map(), externalMapper.convertFromJson(resp.body(), outClass));
            } catch (ObjectMappingException e) {
                throw new RuntimeObjectMappingException(e);
            }
        });
    }

    /**
     * Performs an asynchronous POST request
     * @param url The Request URL
     * @param body The request POJO/Record body
     * @return CompletableFuture resolving to {@link StringResponse object} containing the response code, response headers and the response body as deserialized POJO/Record
     * @throws RuntimeObjectMappingException if the serialization/deserialization fails
     */
    public CompletableFuture<MappedResponse> post(String url, Class<?> outClass, Object body) throws ObjectMappingException {
        return postString(url, HttpRequest.BodyPublishers.ofString(externalMapper.convertToJson(body))).thenApplyAsync(resp -> {
            try {
                return new MappedResponse(resp.statusCode(), resp.headers().map(), externalMapper.convertFromJson(resp.body(), outClass));
            } catch (ObjectMappingException e) {
                throw new RuntimeObjectMappingException(e);
            }
        });
    }

    /**
     * Performs an asynchronous PUT request
     * @param url The Request URL
     * @param body The request POJO/Record body
     * @return CompletableFuture resolving to {@link StringResponse object} containing the response code, response headers and the response body as deserialized POJO/Record
     * @throws RuntimeObjectMappingException if the serialization/deserialization fails
     */
    public CompletableFuture<MappedResponse> put(String url, Class<?> outClass, Object body) throws ObjectMappingException {
        return putString(url, HttpRequest.BodyPublishers.ofString(externalMapper.convertToJson(body))).thenApplyAsync(resp -> {
            try {
                return new MappedResponse(resp.statusCode(), resp.headers().map(), externalMapper.convertFromJson(resp.body(), outClass));
            } catch (ObjectMappingException e) {
                throw new RuntimeObjectMappingException(e);
            }
        });
    }

    /**
     * Performs an asynchronous PATCH request
     * @param url The Request URL
     * @param body The request POJO/Record body
     * @return CompletableFuture resolving to {@link StringResponse object} containing the response code, response headers and the response body as deserialized POJO/Record
     * @throws RuntimeObjectMappingException if the serialization/deserialization fails
     */
    public CompletableFuture<MappedResponse> patch(String url, Class<?> outClass, Object body) throws ObjectMappingException {
        return patchString(url, HttpRequest.BodyPublishers.ofString(externalMapper.convertToJson(body))).thenApplyAsync(resp -> {
            try {
                return new MappedResponse(resp.statusCode(), resp.headers().map(), externalMapper.convertFromJson(resp.body(), outClass));
            } catch (ObjectMappingException e) {
                throw new RuntimeObjectMappingException(e);
            }
        });
    }

    /**
     * Performs an asynchronous DELETE request
     * @param url The Request URL
     * @return CompletableFuture resolving to {@link MappedResponse object} containing the response code, response headers and the response body as deserialized POJO/Record
     * @throws RuntimeObjectMappingException if the deserialization fails
     */
    public CompletableFuture<MappedResponse> delete(String url, Class<?> outClass) {
        return deleteString(url).thenApplyAsync(resp -> {
            try {
                return new MappedResponse(resp.statusCode(), resp.headers().map(), externalMapper.convertFromJson(resp.body(), outClass));
            } catch (ObjectMappingException e) {
                throw new RuntimeObjectMappingException(e);
            }
        });
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
