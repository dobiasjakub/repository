package com.luckybusiness.luckyapp.exceptions;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExceptionResponseMessage {
    private LocalDateTime timestamp = LocalDateTime.now();
    private int status;
    private String error;
    private String path;
    private String message;
}