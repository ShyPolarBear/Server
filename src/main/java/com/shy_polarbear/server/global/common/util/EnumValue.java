package com.shy_polarbear.server.global.common.util;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class EnumValue <V>{
    private final String key;
    private final V value;

    public EnumValue(EnumModel<V> enumModel) {
        this.key = enumModel.getKey();
        this.value = enumModel.getValue();
    }

    // enum -> DTO
    public List<EnumValue<V>> toEnumValues(Class<? extends EnumModel<V>> enumModelClass) {
        return Arrays.stream(enumModelClass.getEnumConstants())
                .map(EnumValue<V>::new)
                .collect(Collectors.toList());
    }
}
