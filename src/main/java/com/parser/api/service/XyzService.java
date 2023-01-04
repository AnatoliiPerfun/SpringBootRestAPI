package com.parser.api.service;

import com.parser.api.model.XyzEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author tolik
 * @project xyz
 * @created 04.01.2023 - 11:57
 */

public interface XyzService {
    XyzEntity save(MultipartFile file);
    Page<XyzEntity> findAllMatch(XyzEntity filter, Pageable pageable);
}
