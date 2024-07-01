package me.yeop.global.exception;

public class ServerException extends RuntimeException {

    int status;

    public ServerException(int status, String message) {
        super(message);
        this.status = status;
    }
}
