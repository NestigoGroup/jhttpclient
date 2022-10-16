package io.github.nestigogroup.jhttpclient.helpers;

import java.io.IOException;
import java.math.BigInteger;
import java.net.http.HttpRequest;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RequestsHelper {

    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String DOUBLE_DASH = "--";

    private RequestsHelper() {}

    public static String[] convertToHeadersArray(Map<String, String> headers) {
        var headerAsList = new LinkedList<String>();
        for(var header: headers.entrySet()) {
            headerAsList.add(header.getKey());
            headerAsList.add(header.getValue());
        }
        return headerAsList.toArray(new String[0]);
    }

    public static HttpRequest.BodyPublisher ofMultipartData(Map<Object, Object> data, String boundary, Charset charset) throws IOException {
        List<byte[]> byteArrays = new ArrayList<>();
        byte[] separator = (DOUBLE_DASH + boundary + LINE_SEPARATOR +"Content-Disposition: form-data; name=").getBytes(charset);

        for(var entry: data.entrySet()) {
            byteArrays.add(separator);

            if(entry.getValue() instanceof Path) {
                var path = (Path) entry.getValue();
                var mimeType = Files.probeContentType(path);
                byteArrays.add(("\"" + entry.getKey() + "\"; filename=\"" + path.getFileName() + "\"" + LINE_SEPARATOR +"Content-Type: " + mimeType + LINE_SEPARATOR + LINE_SEPARATOR).getBytes(charset));
                byteArrays.add(Files.readAllBytes(path));
                byteArrays.add(LINE_SEPARATOR.getBytes(charset));
            } else {
                byteArrays.add(("\"" + entry.getKey() + "\"" + LINE_SEPARATOR + LINE_SEPARATOR + entry.getValue() + LINE_SEPARATOR).getBytes(charset));
            }
        }
        byteArrays.add((DOUBLE_DASH + boundary + DOUBLE_DASH).getBytes(charset));
        return  HttpRequest.BodyPublishers.ofByteArrays(byteArrays);
    }

    public static String basicAuth(String username, String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
    }

    public static String buildBoundary() {
        return new BigInteger(256, new Random()).toString();
    }

}
