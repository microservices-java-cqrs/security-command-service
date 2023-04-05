package com.freetech.sample.securitycommandservice.application.enums;

import lombok.Getter;

@Getter
public enum ExceptionEnum {
    ERROR_CREATE_USER("50001", "Error creating user"),
    ERROR_UPDATE_USER("50002", "Error updating user"),
    ERROR_DELETE_USER("50003", "Error deleting user"),
    ERROR_SEND_USER("50005", "Error sending user"),
    ERROR_NOT_FOUND_USER("50006", "User not found"),
    ERROR_CHANGE_PASSWORD_USER("50007", "Error changing password"),
    ERROR_PASSWORD_INCORRECT_USER("50008", "Password incorrect"),
    ERROR_NOT_CONFIRM_PASSWORD("50009", "Password and ConfirmPassword not same"),
    ERROR_NOT_CONFIRM_NEW_PASSWORD("50010", "NewPassword and ConfirmNewPassword not same"),
    ERROR_EXIST_USERNAME_USER("50011", "Username is taken"),
    ERROR_EXIST_NUMBER_DOCUMENT_ENTITY("50012", "Number document is taken"),

    ERROR_NOT_FOUND_ENTITY("50020", "Entity not found"),
    ERROR_EXITS_BY_ENTITY_TYPE_ENTITY("50021", "Entities exits by Type"),

    ERROR_CREATE_ENTITY_TYPE("50030", "Error creating entity type"),
    ERROR_UPDATE_ENTITY_TYPE("50031", "Error updating entity type"),
    ERROR_DELETE_ENTITY_TYPE("50032", "Error deleting entity type"),
    ERROR_SEND_ENTITY_TYPE("50033", "Error sending enitty type"),
    ERROR_NOT_FOUND_ENTITY_TYPE("50034", "TypeEntity not found"),
    ERROR_EXIST_NAME_ENTITY_TYPE("50035", "Name is taken"),

    ERROR_CREATE_ENTITY("50050", "Error creating entity");

    private String code;
    private String message;

    ExceptionEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
