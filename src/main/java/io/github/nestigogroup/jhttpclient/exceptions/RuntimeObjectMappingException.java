package io.github.nestigogroup.jhttpclient.exceptions;

/**
 * Runtime Exception to be thrown when the {@link io.github.nestigogroup.jhttpclient.interfaces.IObjectMapper ObjectMapper} fails to serialize/deserialize in Async context
 */
public class RuntimeObjectMappingException extends RuntimeException {

    public RuntimeObjectMappingException(Exception e) {
        super(e);
    }
}
