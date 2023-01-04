package com.parser.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.sql.Timestamp;

/**
 * @author tolik
 * @project xyz
 * @created 04.01.2023 - 15:16
 */

@Getter
public class XyzDto {
    Long id;
    String newspaperName;
    Integer width;
    Integer height;
    Integer dpi;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Timestamp uploadTime;
    String fileName;
}

