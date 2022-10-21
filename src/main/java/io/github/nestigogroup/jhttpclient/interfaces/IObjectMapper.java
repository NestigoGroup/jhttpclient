package io.github.nestigogroup.jhttpclient.interfaces;

import io.github.nestigogroup.jhttpclient.exceptions.ObjectMappingException;

/**
 * Interface to be implemented to match the used Json serializer library
 */
public interface IObjectMapper {

    /**
     * Method that convert POJO/Record to Json String
     * @param obj the input POJO or {@link Record}
     * @return Valid Json {@link String}
     * @throws ObjectMappingException when POJO/{@link Record} fails to be converted to String
     */
    String convertToJson(Object obj) throws ObjectMappingException;

    /**
     * Method that maps valid Json String to POJO/Record
     * @param json Valid Json {@link String}
     * @param outClass Target {@link Class} of the desired POJO or {@link Record}
     * @return POJO or {@link Record}
     * @throws ObjectMappingException when input String is not valid or can't be mapped to the provided {@link Class}
     */
    Object convertFromJson(String json, Class<?> outClass) throws ObjectMappingException;
}
