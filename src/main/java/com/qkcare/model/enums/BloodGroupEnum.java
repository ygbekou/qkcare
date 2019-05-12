package com.qkcare.model.enums;

public enum BloodGroupEnum {

	EMPTY(""),
	A_PLUS("A+"),
    A_MINUS("A-"),
    B_PLUS("B+"),
    B_MINUS("B-"),
    O_PLUS("O+"),
    O_MINUS("O-"),
    AB_PLUS("AB+"),
    AB_MINUS("AB-");

    private String bloodGroup;

    BloodGroupEnum(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }
}
