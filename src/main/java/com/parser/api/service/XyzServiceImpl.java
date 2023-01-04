package com.parser.api.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.parser.api.handlers.CustomException;
import com.parser.api.model.XyzEntity;
import com.parser.api.model.XyzRequest;
import com.parser.api.model.repository.XyzRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * @author tolik
 * @project xyz
 * @created 04.01.2023 - 12:13
 */

@Getter
@AllArgsConstructor
@Service
@FieldDefaults(makeFinal = true)
public class XyzServiceImpl implements XyzService {

    private final XyzRepository xyzRepository;
    private final XmlMapper xmlMapper;
    File XSD_SCHEMA_FILE = readXsdSchemaFile();

    /**
     * @param file - multipart file
     * @return - XyzEntity
     */
    public XyzRequest parseXmlFile(MultipartFile file) {
        XyzRequest xyzRequest;
        try {
            String xmlString = new String((file.getInputStream()
                    .readAllBytes()), StandardCharsets.UTF_8);
            xyzRequest = xmlMapper.readValue(xmlString, XyzRequest.class);
        } catch (IOException e) {
            throw new CustomException("Error while parsing xml file");
        }
        return xyzRequest;
    }

    /**
     * save xml file to database
     * @param file - multipart file
     */
    @Override
    public XyzEntity save(MultipartFile file) {
        validate(file);
        XyzRequest xyzRequest = parseXmlFile(file);
        XyzRequest.ScreenInfo screenInfo = xyzRequest.getDeviceInfo().getScreenInfo();
        return xyzRepository.saveAndFlush(XyzEntity.builder()
                .newspaperName(xyzRequest.getDeviceInfo()
                        .getAppInfo()
                        .getNewspaperName())
                .width(screenInfo.getWidth())
                .height(screenInfo.getHeight())
                .dpi(screenInfo.getDpi())
                .fileName(file.getResource().getFilename())
                .uploadTime(Timestamp.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)))
                .build()
        );
    }

    /**
     * @param - file to validate
     * @return - true if file is valid
     */
    @Override
    public Page<XyzEntity> findAllMatch(XyzEntity filter, Pageable pageable) {
        Page<XyzEntity> xyzEntities;
        if (Objects.isNull(filter)) {
            xyzEntities = xyzRepository.findAll(pageable);
        } else {
            XyzEntity xyzEntity = XyzEntity.builder()
                    .id(filter.getId())
                    .newspaperName(filter.getNewspaperName())
                    .dpi(filter.getDpi())
                    .width(filter.getWidth())
                    .height(filter.getHeight())
                    .fileName(filter.getFileName())
                    .uploadTime(filter.getUploadTime())
                    .build();
            xyzEntities = xyzRepository.findAll(Example.of(xyzEntity), pageable);
        }
        return xyzEntities;
    }

    /**
     * Validate xml file with xsd schema
     * @param file xml file
     */
    private void validate(MultipartFile file) {
       if(file.isEmpty()) {
           throw new CustomException("File is empty", BAD_REQUEST);
       }
        validateXsd(file);
    }

    /**
     * Validate xml file with xsd schema
     * @param file xml file
     */
    private void validateXsd(MultipartFile file) {
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Validator validator = schemaFactory.newSchema(XSD_SCHEMA_FILE).newValidator();
            Source xmlFile = new StreamSource(file.getInputStream());
            validator.validate(xmlFile);
        } catch (SAXException | IOException e) {
            throw new CustomException("Error while validating xml file");
        }
    }

    /**
     * Read xsd schema file from resources
     * @return xsd schema file
     */
    private File readXsdSchemaFile() {
        ClassPathResource classPathResource = new ClassPathResource("file.xsd");
        try (InputStream inputStream = classPathResource.getInputStream()) {
            File schemaFile = File.createTempFile("file", ".xsd");
            FileUtils.copyInputStreamToFile(inputStream, schemaFile);
            return schemaFile;
        } catch (IOException e) {
            throw new CustomException("Error while reading xsd schema file");
        }
    }
}
