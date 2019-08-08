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
            case Pharmacie:
                return 1L;
            case Laboratoire:
                return 2L;
            case Medical:
                return 3L;
            case Lit:
                return 4L;
            default:
                throw new IllegalArgumentException("Unknown" + enumValue);
        }
    }

	@Override
	public DoctorOrderTypeEnum convertToEntityAttribute(Long dbData) {
        switch (dbData.intValue()) {
            case 1:
                return DoctorOrderTypeEnum.Pharmacie;
            case 2:
                return DoctorOrderTypeEnum.Laboratoire;
            case 3:
                return DoctorOrderTypeEnum.Medical;
            case 4:
                return DoctorOrderTypeEnum.Lit;
            default:
                throw new IllegalArgumentException("Unknown" + dbData);
        }
    }

}