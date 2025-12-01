package com.example.caching.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DownstreamResponse {
    private String status;
    private String data;
    private String message;
    private String guid;
}
