package com.tcs.students.service;

import com.tcs.students.dto.APIResponse;
import com.tcs.students.dto.UserDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public interface UserService {


    UserDTO saveUser(UserDTO user);


    List<UserDTO> getUsers();

    void deleteUser(Integer userId);

    UserDTO updateUser(UserDTO userDTO);


    void uploadProfilePic(MultipartFile file);

    InputStream downloadFile() throws FileNotFoundException;

    APIResponse changePassword(String oldPassword, String newPassword);

    String getProfilePhoto(String userName);
}
