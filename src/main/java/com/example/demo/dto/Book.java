package com.example.demo.dto;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Book {
    private int bid;
    private String btitle;
    private String bpublisher;
    private int bprice;
    private String bauthor;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private MultipartFile battach;
    private String battachoname;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String battachsname;
    private String battachtype;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private byte[] battachdata;
}
