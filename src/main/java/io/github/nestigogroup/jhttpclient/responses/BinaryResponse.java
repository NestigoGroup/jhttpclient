package io.github.nestigogroup.jhttpclient.responses;

import java.util.List;
import java.util.Map;

public record BinaryResponse(int code, Map<String, List<String>> headers, byte[] body) {
}
