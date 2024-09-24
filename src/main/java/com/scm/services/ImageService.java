package com.scm.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String uploadImage(MultipartFile contactImage, String fileName);

    void deleteImage(String cloudinaryImagePublicId) throws Exception;

    String getUrlFomPublicId(String publicId);

}
