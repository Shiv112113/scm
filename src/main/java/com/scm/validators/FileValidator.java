package com.scm.validators;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileValidator implements ConstraintValidator<ValidateFile, MultipartFile> {

    private static final long MAX_FILE_SIZE = 1024 * 1024 * 2;  // (i.e 2MB)

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        
        // Checking if File Exist or not...
        if(file == null || file.isEmpty()) {

            // context.disableDefaultConstraintViolation();
            // context.buildConstraintViolationWithTemplate("* File Cannot be Empty!").addConstraintViolation();

            return true;
        }

        // Checking File Size... 
        if(file.getSize() > MAX_FILE_SIZE) {

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("* File Size Should be less then 2MB.").addConstraintViolation();

            return false;
        }


     /*
        // Chacking Resolution...
        try {
            BufferedImage BufferedImage = ImageIO.read(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
          
     */

        return true;
        

    }

}
