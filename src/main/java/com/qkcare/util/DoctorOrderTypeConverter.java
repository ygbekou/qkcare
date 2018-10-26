package com.qkcare.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.qkcare.model.enums.DoctorOrderStatusEnum;
import com.qkcare.model.enums.DoctorOrderTypeEnum;

@Converter(autoApply = true)
public class DoctorOrderTypeConverter implements AttributeConverter<DoctorOrderTypeEnum, Long> {

	@Override
	public Long convertToDatabaseColumn(DoctorOrderTypeEnum enumValue) {
        switch (enumValue) {
            case PHARMACY:
                return 1L;
            case LABORATORY:
                return 2L;
            default:
                throw new IllegalArgumentException("Unknown" + enumValue);
        }
    }

	@Override
	public DoctorOrderTypeEnum convertToEntityAttribute(Long dbData) {
        switch (dbData.intValue()) {
            case 1:
                return DoctorOrderTypeEnum.PHARMACY;
            case 2:
                return DoctorOrderTypeEnum.LABORATORY;
            default:
                throw new IllegalArgumentException("Unknown" + dbData);
        }
    }

}