package com.scm.services.impl;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import com.scm.helpers.AppConstants;
import com.scm.services.ImageService;

@Service
public class ImageServiceImpl implements ImageService{

    private Cloudinary cloudinary;

    // Constructor Injection...
    public ImageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImage(MultipartFile contactImage, String fileName) {
     // This func upload the image to the server and return the URL of that image...

        try {
            byte[] data = new byte[contactImage.getInputStream().available()];
            
            contactImage.getInputStream().read(data);
            cloudinary.uploader().upload(data, ObjectUtils.asMap("public_id", fileName));

            return this.getUrlFomPublicId(fileName);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getUrlFomPublicId(String publicId) {
        
        return cloudinary.url()
                        .transformation(new Transformation<>().width(AppConstants.CONTACT_IMAGE_WIDTH)
                                                            .height(AppConstants.CONTACT_IMAGE_HEIGHT)
                                                            .crop(AppConstants.CONATCT_IMAGE_CROP))
                        .generate(publicId);
    }

    @Override
    public void deleteImage(String cloudinaryImagePublicId) throws Exception {
        try {
            ApiResponse apiResponse = cloudinary.api().deleteResources(Arrays.asList(cloudinaryImagePublicId),
                                                ObjectUtils.asMap("type", "upload", "resource_type", "image"));
            System.out.println(apiResponse);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }    
    }

}
