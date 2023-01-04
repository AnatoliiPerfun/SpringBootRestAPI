package com.parser.api.controller;


import com.parser.api.handlers.CustomException;
import com.parser.api.handlers.EntityValidationException;
import com.parser.api.model.XyzEntity;
import com.parser.api.service.XyzService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * @author tolik
 * @project xyz
 * @created 04.01.2023 - 11:49
 */

@AllArgsConstructor
@RestController
@RequestMapping("/api")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class XyzController {

    XyzService xyzService;

    /**
     * Post request
     * @param file required to parse to db
     * @return
     */
    @PostMapping(value = "/xyz", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> uploadFile(@RequestParam("file") MultipartFile file) {
        XyzEntity xyzEntity = xyzService.save(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(xyzEntity.getId());
    }

    /**
     * Get response with entities stored in db
     * @param xyzFilter
     * @param page
     * @param size
     * @param sort
     * @return
     */
    @GetMapping(value = "/xyz", produces = MediaType.APPLICATION_JSON_VALUE)
      public ResponseEntity<Map<String, Object>> findAllPaged(
               @RequestBody(required = false) XyzEntity xyzFilter,
               @RequestParam(defaultValue = "0") int page,
               @RequestParam(defaultValue = "5") int size,
               @RequestParam(defaultValue = "id,asc") String... sort) {
         validateSortParameter(sort);
         try {
               Pageable pageable = PageRequest.of(page, size, Sort.by(new Sort.Order(findDirection(sort[1]), sort[0])));
               Page<XyzEntity> pagedXyz = xyzService.findAllMatch(xyzFilter, pageable);
               List<XyzEntity> xyzList = pagedXyz.getContent();
             if (xyzList.isEmpty()) {
                 return new ResponseEntity<>(HttpStatus.NO_CONTENT);
               } else {
                 return ResponseEntity.ok(Map.of(
                         "xyzList", xyzList,
                         "xyz", pagedXyz.getContent(),
                         "currentPage", pagedXyz.getNumber(),
                         "totalItems", pagedXyz.getTotalElements(),
                         "totalPages", pagedXyz.getTotalPages()

                 ));
             }

         } catch (Exception e) {
               throw new CustomException("Error while getting all xyz");
         }
    }

    /**
     *  Custom exception handler
     * @param exception
     * @return
     */
    @ExceptionHandler({MultipartException.class, HttpMessageConversionException.class, CustomException.class})
    ResponseEntity<EntityValidationException> handleException(Exception exception) {
        if(exception instanceof CustomException) {
            return ResponseEntity.status(((CustomException) exception).getHttpStatus())
                    .body(EntityValidationException
                            .from(((CustomException) exception)));
        }
         return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                 .body(EntityValidationException
                         .from(new CustomException(exception.getMessage())));
    }
    static List<String> VALID_SORT_PARAMS = List.of(
            "asc",
            "desc",
            "id",
            "newspaperName",
            "width",
            "height",
            "dpi",
            "uploadTime",
            "fileName");

    public static void validateSortParameter(String[] sort) {
        Set<String> sortFromRequest = new HashSet<>(Arrays.asList(sort));
        VALID_SORT_PARAMS.forEach(sortFromRequest::remove);
        if (!sortFromRequest.isEmpty()) {
            throw new CustomException("Invalid sort parameter: " + sortFromRequest, HttpStatus.BAD_REQUEST);
        }
    }
    public static Sort.Direction findDirection(String direction) {
        return "desc".equals(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
    }
}
