package com.econovation.whichbook_user.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionMessage {
    private LocalDateTime timeStamp;
    private String message;
    private String details;
}
