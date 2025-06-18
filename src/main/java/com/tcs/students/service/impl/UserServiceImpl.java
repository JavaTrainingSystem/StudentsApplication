package com.tcs.students.service.impl;

import com.tcs.students.constants.CommonConstants;
import com.tcs.students.dao.UserRepo;
import com.tcs.students.dto.APIResponse;
import com.tcs.students.dto.UserDTO;
import com.tcs.students.entity.UserEntity;
import com.tcs.students.service.FileService;
import com.tcs.students.service.UserService;
import com.tcs.students.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    private final CommonUtils commonUtils;

    private final FileService fileService;

    @Value("${local.file.path}")
    private String localFilePath;


    @Override
    public UserDTO saveUser(UserDTO user) {

        UserEntity userEntity = new UserEntity();

        BeanUtils.copyProperties(user, userEntity);

        userRepo.save(userEntity);

        user.setUserId(userEntity.getUserId());

        return user;
    }

    @Override
    public List<UserDTO> getUsers() {

        List<UserDTO> userResponse = new ArrayList<>();

        List<UserEntity> users = userRepo.findAll();

        users.forEach(user -> {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            userResponse.add(userDTO);
        });


        return userResponse;
    }

    @Override
    public void deleteUser(Integer userId) {

        Optional<UserEntity> userEntity = userRepo.findById(userId);

        if (userEntity.isPresent()) {

            userRepo.delete(userEntity.get());

        }

    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {

        Optional<UserEntity> userEntity = userRepo.findById(userDTO.getUserId());

        if (userEntity.isPresent()) {

            BeanUtils.copyProperties(userDTO, userEntity.get());

            userRepo.save(userEntity.get());

        }

        return userDTO;
    }

    @Override
    public void uploadProfilePic(MultipartFile file) {

        String currentLoggedInUserName = commonUtils.getCurrentLoggedInUserName();

        fileService.uploadFile("profilepics", currentLoggedInUserName + ".png", file);

    }

    @Override
    public InputStream downloadFile() throws FileNotFoundException {
        return fileService.downloadFile(localFilePath + "profilepics" + File.separator + commonUtils.getCurrentLoggedInUserName() + ".png");
    }

    @Override
    public String getProfilePhoto(String userName) {
        String file = "";
        try {
            InputStream inputStream = fileService.downloadFile(localFilePath + "profilepics" + File.separator +
                    userName + ".png");

            return convertToBase64(inputStream);
        } catch (IOException e) {
            try {
                ClassPathResource fallbackImage = new ClassPathResource("static/images/default-profile.jpg");
                InputStream fallbackStream = fallbackImage.getInputStream();
                return convertToBase64(fallbackStream);
            } catch (IOException ex) {
                e.printStackTrace();
            }
        }
        return file;
    }

    @Override
    public APIResponse changePassword(String oldPassword, String newPassword) {

        String currentLoggedInUserName = commonUtils.getCurrentLoggedInUserName();

        UserEntity userEntity = userRepo.findByUserName(currentLoggedInUserName);

        if (oldPassword.equals(userEntity.getPassword())) {

            userEntity.setPassword(newPassword);
            userRepo.save(userEntity);

        } else {

            return new APIResponse(CommonConstants.FAILED, 500, "Invalid Old Password");

        }


        return new APIResponse(CommonConstants.SUCCESS, 200, "Password Updated successfully");
    }

    public static String convertToBase64(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        byte[] bytes = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(bytes);
    }


}
