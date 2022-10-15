package io.github.nestigogroup.jhttpclient.interfaces;

public interface IObjectMapper {

    String convertToJson(Object obj);

    Object convertFromJson(String response, Class<?> outClass);
}
