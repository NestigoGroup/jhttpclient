package io.github.nestigogroup.jhttpclient.responses;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * File response wrapper
 * @param code The response status {@link Integer code}
 * @param headers The response {@link Map headers}
 * @param body {@link Path location} of the file
 */
public record FileResponse(int code, Map<String, List<String>> headers, Path body) {
}
