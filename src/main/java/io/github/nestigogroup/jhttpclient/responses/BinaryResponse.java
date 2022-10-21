package io.github.nestigogroup.jhttpclient.responses;

import java.util.List;
import java.util.Map;

/**
 * Binary Response wrapper
 * @param code the response status {@link Integer code}
 * @param headers {@link Map response headers}
 * @param body the response as <b>byte[]</b>
 */
public record BinaryResponse(int code, Map<String, List<String>> headers, byte[] body) {
}
