package io.github.nestigogroup.jhttpclient.responses;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public record FileResponse(int code, Map<String, List<String>> headers, Path body) {
}
