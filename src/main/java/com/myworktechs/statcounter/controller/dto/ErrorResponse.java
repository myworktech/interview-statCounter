package com.myworktechs.statcounter.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ErrorResponse {
    String message;
}
