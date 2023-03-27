package io.github.nestigogroup.jhttpclient;

import io.github.nestigogroup.jhttpclient.internal.BlockingHttpClient;
import io.github.nestigogroup.jhttpclient.responses.NoBodyResponse;
import io.github.nestigogroup.jhttpclient.responses.StringResponse;

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
 * Simplified Rest Http Client for working with Rest services
 * <b>Content-Type</b> is set to <i>application/json</i> by default (but can be overridden)
 */
public class RestClient extends BlockingHttpClient {

    /**
     * Creates an instance of the {@link RestClient} with HTTP version 1.1, preventing redirects from <i>Https</i> to <i>Http</i>, 30 seconds timeout and UTF-8 as Charset and <b>Content-Type</b> as <i>application/json</i>
     */
    public RestClient() {
        super();
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
     */
    public RestClient(HttpClient.Version version, HttpClient.Redirect redirectPolicy, Duration timeout, SSLContext sslContext, Map<String, String> headers, Charset charset) {
        super(version, redirectPolicy, timeout, sslContext, headers, charset);
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
     */
    public RestClient(HttpClient.Version version, Executor executor, HttpClient.Redirect redirectPolicy, Duration timeout, SSLContext sslContext, Map<String, String> headers, Charset charset) {
        super(version, executor, redirectPolicy, timeout, sslContext, headers, charset);
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
     * @return StringResponse object containing the response code, response headers and the response body as {@link String}
     */
    public StringResponse get(String url) throws IOException, InterruptedException {
        var resp = getString(url);
        return new StringResponse(resp.statusCode(), resp.headers().map(), resp.body());
    }

    /**
     * Executes a POST request
     * @param url The Request URL
     * @param body The request String body
     * @return StringResponse object containing the response code, response headers and the response body as {@link String}
     */
    public StringResponse post(String url, String body) throws IOException, InterruptedException {
        var resp = postString(url, HttpRequest.BodyPublishers.ofString(body));
        return new StringResponse(resp.statusCode(), resp.headers().map(), resp.body());
    }

    /**
     * Executes a PUT request
     * @param url The Request URL
     * @param body The request String body
     * @return StringResponse object containing the response code, response headers and the response body as {@link String}
     */
    public StringResponse put(String url, String body) throws IOException, InterruptedException {
        var resp = putString(url, HttpRequest.BodyPublishers.ofString(body));
        return new StringResponse(resp.statusCode(), resp.headers().map(), resp.body());
    }

    /**
     * Executes a PATCH request
     * @param url The Request URL
     * @param body The request String body
     * @return StringResponse object containing the response code, response headers and the response body as {@link String}
     */
    public StringResponse patch(String url, String body) throws IOException, InterruptedException {
        var resp = patchString(url, HttpRequest.BodyPublishers.ofString(body));
        return new StringResponse(resp.statusCode(), resp.headers().map(), resp.body());
    }


    /**
     * Executes a DELETE request
     * @param url The Request URL
     * @return StringResponse object containing the response code, response headers and the response body as {@link String}
     */
    public StringResponse delete(String url) throws IOException, InterruptedException {
        var resp = deleteString(url);
        return new StringResponse(resp.statusCode(), resp.headers().map(), resp.body());
    }

    /**
     * Downloads a file to specified location
     * @param url - The file URL
     * @param downloadPath - The location where the file to be stored
     */
    public void downloadFile(String url, Path downloadPath) throws IOException, InterruptedException {
        getFile(url, downloadPath);
    }

}
