package io.github.nestigogroup.jhttpclient.helpers;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URLEncoder;
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

/**
 * Helper class for building complex request bodies and headers
 */
public class RequestHelper {

    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String DOUBLE_DASH = "--";

    private RequestHelper() {}

    /**
     * Converts a {@link Map} of header name/value pairs to a String Array as expected by the underline {@link java.net.http.HttpClient}
     * @param headers {@link Map} of name/value String pairs
     * @return String[] representation of the Map
     */
    public static String[] convertToHeadersArray(Map<String, String> headers) {
        var headerAsList = new LinkedList<String>();
        for(var header: headers.entrySet()) {
            headerAsList.add(header.getKey());
            headerAsList.add(header.getValue());
        }
        return headerAsList.toArray(new String[0]);
    }

    /**
     * Converts a {@link Map} of key/value pairs to FormData request body using UTF-8 for the URL encoding
     * @param data {@link Map} of key/value pairs
     * @return BodyPublisher construct
     */
    public static HttpRequest.BodyPublisher ofFormData(Map<Object, Object> data) {
        return ofFormData(data, StandardCharsets.UTF_8);
    }

    /**
     * Converts a {@link Map} of key/value pairs to FormData request body using the specified {@link Charset} for the URL encoding
     * @param data {@link Map} of key/value pairs
     * @param charset the URL encoding charset
     * @return BodyPublisher construct
     */
    public static HttpRequest.BodyPublisher ofFormData(Map<Object, Object> data, Charset charset) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (!builder.isEmpty()) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), charset));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), charset));
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }

    /**
     * Converts a {@link Map} of key/value pairs to MultipartData request body using the specified Boundary and UTF-8 for the byte array transformations
     * @param data {@link Map} of key/value pairs (including Files Paths)
     * @param boundary the String used for separating the different parts of the request
     * @return BodyPublisher construct
     */
    public static HttpRequest.BodyPublisher ofMultipartData(Map<Object, Object> data, String boundary) throws IOException {
        return ofMultipartData(data, boundary, StandardCharsets.UTF_8);
    }

    /**
     * Converts a {@link Map} of key/value pairs to MultipartData request body using the specified Boundary and {@link Charset} for the byte array transformations
     * @param data {@link Map} of key/value pairs (including Files Paths)
     * @param boundary the String used for separating the different parts of the request
     * @param charset the Byte Array transformation encoding charset
     * @return BodyPublisher construct
     */
    public static HttpRequest.BodyPublisher ofMultipartData(Map<Object, Object> data, String boundary, Charset charset) throws IOException {
        List<byte[]> byteArrays = new ArrayList<>();
        byte[] separator = (DOUBLE_DASH + boundary + LINE_SEPARATOR +"Content-Disposition: form-data; name=").getBytes(charset);

        for(var entry: data.entrySet()) {
            byteArrays.add(separator);

            if(entry.getValue() instanceof Path path) {
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

    /**
     * Build Basic Authorization header using the provided Username and Password
     * @param username {@link String} value
     * @param password {@link String} value
     * @return The Authorization header value
     */
    public static String buildBasicAuthHeader(String username, String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Build Bearer token header using the provided token value
     * @param token {@link String} value
     * @return The Bearer token header value
     */
    public static String buildBearerHeader(String token) {
        return "Bearer " + token;
    }

    /**
     * Method to generate random valid boundary values
     * @return {@link String} value
     */
    public static String generateBoundary() {
        return new BigInteger(256, new Random()).toString();
    }

}
