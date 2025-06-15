package com.tcs.students.controllers;


import com.tcs.students.dto.APIResponse;
import com.tcs.students.dto.UserDTO;
import com.tcs.students.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDTO saveUser(@RequestBody UserDTO user) {
        return userService.saveUser(user);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }


    @DeleteMapping
    public ResponseEntity<Map> deleteUser(@RequestParam Integer userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(Map.of("status", "SUCCESS"), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.updateUser(userDTO), HttpStatus.OK);
    }

    @PostMapping("/profile-pic")
    public ResponseEntity<?> uploadProfilePic(@RequestParam("file") MultipartFile file) {
        try {

            userService.uploadProfilePic(file);

            return ResponseEntity.ok(Map.of("status", "uploaded"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
        }
    }

    @GetMapping("/profile-pic")
    public ResponseEntity<InputStreamResource> getProfilePic() {
        try {

            InputStream inputStream = userService.downloadFile();

            InputStreamResource resource = new InputStreamResource(inputStream);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } catch (FileNotFoundException e) {
            try {
                ClassPathResource fallbackImage = new ClassPathResource("static/images/default-profile.jpg");
                InputStream fallbackStream = fallbackImage.getInputStream();
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new InputStreamResource(fallbackStream));
            } catch (IOException ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("change-password")
    public ResponseEntity<APIResponse> changePassword(@RequestBody Map<String, String> payload) {

        String oldPassword = payload.get("oldPassword");
        String newPassword = payload.get("newPassword");

        APIResponse response = userService.changePassword(oldPassword, newPassword);

        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));

    }

}
