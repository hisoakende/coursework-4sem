package ru.hisoakende.coursework.requests;

import lombok.Data;

@Data
public class OrderRequest {

    private String username;

    private Long ticketId;
}
