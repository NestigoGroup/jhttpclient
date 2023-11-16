package io.github.nestigogroup.jhttpclient.responses;

import java.util.List;
import java.util.Map;

/**
 * POJO/Record response wrapper
 * @param code The response status {@link Integer code}
 * @param headers The response {@link Map headers}
 * @param body Response as POJO/Record
 */
public record MappedResponse<T>(int code, Map<String, List<String>> headers, T body) {
}
