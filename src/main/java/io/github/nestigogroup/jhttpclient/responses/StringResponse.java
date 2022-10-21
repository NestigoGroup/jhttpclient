package io.github.nestigogroup.jhttpclient.responses;

import java.util.List;
import java.util.Map;

/**
 * String Response wrapper
 * @param code The response status {@link Integer code}
 * @param headers The response {@link Map headers}
 * @param body the response as {@link String}
 */
public record StringResponse(int code, Map<String, List<String>> headers, String body) {
}
