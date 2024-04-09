package com.cardapi.card.wrappers;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class GeneralResponseWrapper<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private int responseCode;
    private String message;
    private Date date=new Date();
    private T data;

}