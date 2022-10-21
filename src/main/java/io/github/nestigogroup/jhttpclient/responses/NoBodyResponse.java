package io.github.nestigogroup.jhttpclient.responses;

import java.util.List;
import java.util.Map;

/**
 * Response without body wrapper
 * @param code The response status {@link Integer code}
 * @param headers The response {@link Map headers}
 */
public record NoBodyResponse(int code, Map<String, List<String>> headers) {
}
