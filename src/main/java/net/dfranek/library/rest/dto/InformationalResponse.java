package net.dfranek.library.rest.dto;

public class InformationalResponse {

    private int code;
    private String message;

    public InformationalResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
