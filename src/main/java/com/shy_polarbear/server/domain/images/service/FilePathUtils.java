package com.shy_polarbear.server.domain.images.service;

import com.shy_polarbear.server.domain.images.exception.ImageException;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.UUID;

import static java.text.MessageFormat.format;

public class FilePathUtils {

    private static final String FILE_EXTENSION_SEPARATOR = ".";
    private static final String FILE_SEPARATOR = "/";

    private FilePathUtils() {
    }

    // 파일경로  = {파일타입}/{UUID}_{유저파일이름}.{확장자}
    static String createS3UploadFilePath(MultipartFile multipartFile, String fileType) {
        String originalFilename = Normalizer.normalize(multipartFile.getOriginalFilename(), Normalizer.Form.NFC);
        String fileDir = getFileDir(fileType);
        String fileName = createS3UploadFileName(originalFilename);
        return fileDir + FILE_SEPARATOR + fileName;
    }

    static String getFileDir(String fileType) {
        if (fileType.equals("profile")) {
            return S3FileDir.PROFILE.path;
        } else if (fileType.equals("feed")) {
            return S3FileDir.FEED.path;
        } else {
            throw new ImageException(ExceptionStatus.INVALID_IMAGE_TYPE);
        }
    }

    static String createS3UploadFileName(String originalFilename) {
        int pos = originalFilename.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        String userFileName = originalFilename.substring(0, pos);
        String fileExtension = originalFilename.substring(pos + 1);
        return format("{0}_{1}.{2}", userFileName, UUID.randomUUID(), fileExtension);
    }

    static String parseFilePathFromUrl(String imageUrl) {
        String[] parsedUrl = imageUrl.split("/");
        String fileDir = parsedUrl[parsedUrl.length - 2];
        String fileName = parsedUrl[parsedUrl.length - 1];
        return fileDir + FILE_SEPARATOR + URLDecoder.decode(fileName, StandardCharsets.UTF_8);
    }
}
