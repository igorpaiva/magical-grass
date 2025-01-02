package com.test.magical_grass.mapper;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

import java.util.ArrayList;
import java.util.List;

public class DozerMapper {
    private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();
    public static <S, D> D parseObject(S sourceClass, Class<D> destinationClass) {
        return mapper.map(sourceClass, destinationClass);
    }
    public static <S, D> List<D> parseListObject(List<S> sourceClass, Class<D> destinationClass) {
        List<D> destinationList = new ArrayList<>();
        for (S s : sourceClass) {
            destinationList.add(parseObject(s, destinationClass));
        }
        return destinationList;
    }
}
