package com.ushan.lady_shoe_mart.admin.domain.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
public class PaginateShoeMartResponse<T> {
    private int status;
    private String message;
    private List<T> list;
    private Long count = 0L;

    public PaginateShoeMartResponse(List<T> list, Long count) {
        this.status = HttpStatus.OK.value();
        this.message = HttpStatus.OK.getReasonPhrase();
        this.list = list;
        this.count = count;
    }

    public PaginateShoeMartResponse(List<T> list) {
        this.status = HttpStatus.OK.value();
        this.message = HttpStatus.OK.getReasonPhrase();
        this.list = list;
    }
}
