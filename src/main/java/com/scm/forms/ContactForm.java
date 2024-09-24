package com.scm.forms;

import org.springframework.web.multipart.MultipartFile;

import com.scm.validators.ValidateFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContactForm {

    @NotBlank(message = "* Name is Required")
    private String name;

    @NotBlank(message = "* Email is Required")
    @Email(message = "* Invalid Email Address [exapmle@gmail.com]")
    private String email;

    @NotBlank(message = "* Phone Number is Required")
    @Pattern(regexp = "^[0-9]{10}$", message = "* Invalid Phone Number [Must be 10 Digits]")
    private String phoneNumber;

    @NotBlank(message = "* Address is Required")
    private String address;

    private String socialLink1;

    private String socialLink2;

    private boolean favourite;

    @ValidateFile  // (Custom Validator)...
    private MultipartFile contactImage;

    // Holds link of saved Image to show in update Page...
    private String contactPic;

}
