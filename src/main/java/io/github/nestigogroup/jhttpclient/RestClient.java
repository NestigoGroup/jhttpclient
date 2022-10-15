package io.github.nestigogroup.jhttpclient;

import io.github.nestigogroup.jhttpclient.internal.BlockingHttpClient;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
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

    public Map<String, List<String>> head(String url) throws IOException, InterruptedException {
        return headBodyHandler(url).headers().map();
    }
    public String get(String url) throws IOException, InterruptedException {
        return getString(url).body();
    }

    public String post(String url, String body) throws IOException, InterruptedException {
        return postString(url, HttpRequest.BodyPublishers.ofString(body)).body();
    }

    public String put(String url, String body) throws IOException, InterruptedException {
        return putString(url, HttpRequest.BodyPublishers.ofString(body)).body();
    }

    public String patch(String url, String body) throws IOException, InterruptedException {
        return patchString(url, HttpRequest.BodyPublishers.ofString(body)).body();
    }

    public String delete(String url) throws IOException, InterruptedException {
        return deleteString(url).body();
    }

    public void downloadFile(String url, Path downloadPath) throws IOException, InterruptedException {
        getFile(url, downloadPath);
    }

}
