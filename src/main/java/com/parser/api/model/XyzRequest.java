package com.parser.api.model;


import lombok.*;

import java.sql.Date;


/**
 * @author tolik
 * @project xyz
 * @created 04.01.2023 - 12:05
 */

@Getter
@NoArgsConstructor
public class XyzRequest {

    DeviceInfo deviceInfo;
    GetPages getPages;

    @NoArgsConstructor
    @Getter
    public static class DeviceInfo {
        String name; String id; ScreenInfo screenInfo; OsInfo osInfo; AppInfo appInfo;
    }

    @NoArgsConstructor
    @Getter
    public static class GetPages {
        Integer editionDefId; Date publicationDate;
    }

    @NoArgsConstructor
    @Getter
    public static class ScreenInfo {
        Integer width; Integer height; Integer dpi;
    }

    @NoArgsConstructor
    @Getter
    public static class OsInfo {
        String name; String version;
    }

    @NoArgsConstructor
    @Getter
    public static class AppInfo {
        String newspaperName; String version;
    }
}
