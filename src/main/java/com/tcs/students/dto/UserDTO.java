package com.tcs.students.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {

    private Integer userId;

    private String userName;

    private String password;

    private String mfaEnabled;

    private String email;

}
