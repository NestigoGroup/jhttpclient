package io.github.nestigogroup.jhttpclient.responses;

import java.util.List;
import java.util.Map;

public record StringResponse(int code, Map<String, List<String>> headers, String body) {
}
