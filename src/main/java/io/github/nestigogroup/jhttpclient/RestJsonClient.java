package io.github.nestigogroup.jhttpclient;

import io.github.nestigogroup.jhttpclient.exceptions.ObjectMappingException;
import io.github.nestigogroup.jhttpclient.interfaces.IObjectMapper;
import io.github.nestigogroup.jhttpclient.internal.BlockingHttpClient;
import io.github.nestigogroup.jhttpclient.responses.FileResponse;
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
import java.util.concurrent.Executor;

/**
 * Simplified Rest Http Client for working with Rest services that handles serialization/deserialization of request/responses
 * <b>Content-Type</b> is set to <i>application/json</i> by default (but can be overridden)
 */
public class RestJsonClient extends BlockingHttpClient {

    private IObjectMapper externalMapper;

    /**
     * The default constructor is made private as the Client doesn't work without provided {@link IObjectMapper} implementation
     */
    private RestJsonClient() {
        super();
    }

    /**
     * Creates an instance of the {@link RestJsonClient} with HTTP version 1.1, preventing redirects from <i>Https</i> to <i>Http</i>, 30 seconds timeout and UTF-8 as Charset and <b>Content-Type</b> as <i>application/json</i>
     * @param objectMapper {@link IObjectMapper} implementation
     */
    public RestJsonClient(IObjectMapper objectMapper) {
        super();
        externalMapper = objectMapper;
        addHeader("Content-Type", "application/json");
    }

    /**
     * Creates an instance of the {@link RestClient} with the specified parameters and <b>Content-Type</b> as <i>application/json</i>
     * @param version the HTTP version (refer: {@link java.net.http.HttpClient.Version})
     * @param redirectPolicy the redirect policy (refer: {@link java.net.http.HttpClient.Redirect})
     * @param timeout the timeout as {@link Duration}
     * @param sslContext the {@link SSLContext}
     * @param headers {@link Map} of header key/value pairs to be included in all requests
     * @param charset The specified {@link Charset}
     * @param objectMapper {@link IObjectMapper} implementation
     */
    public RestJsonClient(HttpClient.Version version, HttpClient.Redirect redirectPolicy, Duration timeout, SSLContext sslContext, Map<String, String> headers, Charset charset, IObjectMapper objectMapper) {
        super(version, redirectPolicy, timeout, sslContext, headers, charset);
        externalMapper = objectMapper;
        addHeader("Content-Type", "application/json");
    }

    /**
     * Creates an instance of the {@link RestClient} with the specified parameters and <b>Content-Type</b> as <i>application/json</i>
     * @param version the HTTP version (refer: {@link java.net.http.HttpClient.Version})
     * @param executor the underlining executor to use
     * @param redirectPolicy the redirect policy (refer: {@link java.net.http.HttpClient.Redirect})
     * @param timeout the timeout as {@link Duration}
     * @param sslContext the {@link SSLContext}
     * @param headers {@link Map} of header key/value pairs to be included in all requests
     * @param charset The specified {@link Charset}
     * @param objectMapper {@link IObjectMapper} implementation
     */
    public RestJsonClient(HttpClient.Version version, Executor executor, HttpClient.Redirect redirectPolicy, Duration timeout, SSLContext sslContext, Map<String, String> headers, Charset charset, IObjectMapper objectMapper) {
        super(version, executor, redirectPolicy, timeout, sslContext, headers, charset);
        externalMapper = objectMapper;
        addHeader("Content-Type", "application/json");
    }

    /**
     * Executes a HEAD request
     * @param url The Request URL
     * @return NoBodyResponse object containing the response code and the response headers
     */
    public NoBodyResponse head(String url) throws IOException, InterruptedException {
        var resp = headBodyHandler(url);
        return new NoBodyResponse(resp.statusCode(), resp.headers().map());
    }

    /**
     * Executes a GET request
     * @param url The Request URL
     * @return MappedResponse object containing the response code, response headers and the response body as deserialized POJO/Record
     * @throws ObjectMappingException if the deserialization fails
     */
    public MappedResponse get(String url, Class<?> outClass) throws IOException, InterruptedException, ObjectMappingException {
        var resp = getString(url);
        return new MappedResponse(resp.statusCode(), resp.headers().map(), externalMapper.convertFromJson(resp.body(), outClass));
    }

    /**
     * Executes a POST request
     * @param url The Request URL
     * @param body The request POJO/Record body
     * @return MappedResponse object containing the response code, response headers and the response body as deserialized POJO/Record
     * @throws ObjectMappingException if the serialization/deserialization fails
     */
    public MappedResponse post(String url, Class<?> outClass, Object body) throws IOException, InterruptedException, ObjectMappingException {
        var resp = postString(url, HttpRequest.BodyPublishers.ofString(externalMapper.convertToJson(body)));
        return new MappedResponse(resp.statusCode(), resp.headers().map(), externalMapper.convertFromJson(resp.body(), outClass));
    }

    /**
     * Executes a PUT request
     * @param url The Request URL
     * @param body The request POJO/Record body
     * @return MappedResponse object containing the response code, response headers and the response body as deserialized POJO/Record
     * @throws ObjectMappingException if the serialization/deserialization fails
     */
    public MappedResponse put(String url, Class<?> outClass, Object body) throws IOException, InterruptedException, ObjectMappingException {
        var resp = putString(url, HttpRequest.BodyPublishers.ofString(externalMapper.convertToJson(body)));
        return new MappedResponse(resp.statusCode(), resp.headers().map(), externalMapper.convertFromJson(resp.body(), outClass));
    }

    /**
     * Executes a PATCH request
     * @param url The Request URL
     * @param body The request POJO/Record body
     * @return MappedResponse object containing the response code, response headers and the response body as deserialized POJO/Record
     * @throws ObjectMappingException if the serialization/deserialization fails
     */
    public MappedResponse patch(String url, Class<?> outClass, Object body) throws IOException, InterruptedException, ObjectMappingException {
        var resp = patchString(url, HttpRequest.BodyPublishers.ofString(externalMapper.convertToJson(body)));
        return new MappedResponse(resp.statusCode(), resp.headers().map(), externalMapper.convertFromJson(resp.body(), outClass));
    }

    /**
     * Executes a DELETE request
     * @param url The Request URL
     * @return MappedResponse object containing the response code, response headers and the response body as deserialized POJO/Record
     * @throws ObjectMappingException if the deserialization fails
     */
    public MappedResponse delete(String url, Class<?> outClass) throws IOException, InterruptedException, ObjectMappingException {
        var resp = deleteString(url);
        return new MappedResponse(resp.statusCode(), resp.headers().map(), externalMapper.convertFromJson(resp.body(), outClass));
    }

    /**
     * Downloads a file to specified location
     * @param url - The file URL
     * @param downloadPath - The location where the file to be stored
     * @return FileResponse object containing the response code, response headers and the response body as {@link Path}
     */
    public FileResponse downloadFile(String url, Path downloadPath) throws IOException, InterruptedException {
        var resp = getFile(url, downloadPath);
        return new FileResponse(resp.statusCode(), resp.headers().map(), resp.body());
    }

}
