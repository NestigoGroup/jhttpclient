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

public class RestClient extends BlockingHttpClient {

    public RestClient() {
        super();
        addHeader("Content-Type", "application/json");
    }

    public RestClient(HttpClient.Version version, HttpClient.Redirect redirectPolicy, Duration timeout, SSLContext sslContext, Map<String, String> headers, Charset charset) {
        super(version, redirectPolicy, timeout, sslContext, headers, charset);
        addHeader("Content-Type", "application/json");
    }

    public NoBodyResponse head(String url) throws IOException, InterruptedException {
        var resp = headBodyHandler(url);
        return new NoBodyResponse(resp.statusCode(), resp.headers().map());
    }
    public StringResponse get(String url) throws IOException, InterruptedException {
        var resp = getString(url);
        return new StringResponse(resp.statusCode(), resp.headers().map(), resp.body());
    }

    public StringResponse post(String url, String body) throws IOException, InterruptedException {
        var resp = postString(url, HttpRequest.BodyPublishers.ofString(body));
        return new StringResponse(resp.statusCode(), resp.headers().map(), resp.body());
    }

    public StringResponse put(String url, String body) throws IOException, InterruptedException {
        var resp = putString(url, HttpRequest.BodyPublishers.ofString(body));
        return new StringResponse(resp.statusCode(), resp.headers().map(), resp.body());
    }

    public StringResponse patch(String url, String body) throws IOException, InterruptedException {
        var resp = patchString(url, HttpRequest.BodyPublishers.ofString(body));
        return new StringResponse(resp.statusCode(), resp.headers().map(), resp.body());
    }

    public StringResponse delete(String url) throws IOException, InterruptedException {
        var resp = deleteString(url);
        return new StringResponse(resp.statusCode(), resp.headers().map(), resp.body());
    }

    public void downloadFile(String url, Path downloadPath) throws IOException, InterruptedException {
        getFile(url, downloadPath);
    }

}
