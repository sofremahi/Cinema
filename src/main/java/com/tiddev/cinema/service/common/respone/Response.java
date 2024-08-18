package com.tiddev.cinema.service.common.respone;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@AllArgsConstructor
@Getter
@Setter
public class Response<T>{

    private T data ;
    private String message;
    private HttpStatus status;

    public Response(T data) {
        this.data = data;
    }

    public Response(HttpStatus status) {
        this.status = status;
    }

    public Response(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
