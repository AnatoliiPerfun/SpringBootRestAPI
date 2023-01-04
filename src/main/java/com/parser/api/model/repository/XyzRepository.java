package com.parser.api.model.repository;

import com.parser.api.model.XyzEntity;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author tolik
 * @project xyz
 * @created 04.01.2023 - 11:58
 */

public interface XyzRepository extends JpaRepository<XyzEntity, Long> {

}


