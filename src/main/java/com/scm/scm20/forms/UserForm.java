package com.scm.scm20.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserForm {

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 3, message = "Min 3 characters required for name")
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Min 6 characters required for password")
    private String password;

    @NotBlank(message = "About section cannot be blank")
    private String about;

    @Size(min = 10, max = 15, message = "Phone number must be between 10 to 15 characters")
    private String phoneNumber;
}
