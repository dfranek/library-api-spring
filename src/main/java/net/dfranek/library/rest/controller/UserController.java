package net.dfranek.library.rest.controller;

import net.dfranek.library.rest.config.FileConfig;
import net.dfranek.library.rest.dto.InformationalResponse;
import net.dfranek.library.rest.dto.UserDto;
import net.dfranek.library.rest.entity.User;
import net.dfranek.library.rest.repository.UserRepository;
import net.dfranek.library.rest.utils.SecurityHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping(path = "/user")
public class UserController {

    private Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileConfig fileConfig;

    @Autowired
    private SecurityHelper securityHelper;

    private final Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();

    @PostMapping
    public @ResponseBody
    ResponseEntity<InformationalResponse> addUser(@RequestBody UserDto userToCreate) {

        User existingUser = userRepository.findByEmail(userToCreate.getEmail());
        if(existingUser == null) {

            Optional.ofNullable(userToCreate.getAvatar())
                    .ifPresent(fileObject -> {
                        try {
                            fileObject.save(fileConfig.getAvatarStoragePath());
                        } catch (IOException e) {
                            LOG.warn("error writing file", e);
                        }
                    });

            User user = userToCreate.toEntity();
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            userRepository.save(user);
            return new ResponseEntity<>(new InformationalResponse(HttpStatus.CREATED.value(), "user created"), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new InformationalResponse(HttpStatus.CONFLICT.value(), "user already exists"), HttpStatus.CONFLICT);
        }
    }

    @GetMapping
    public @ResponseBody
    ResponseEntity<UserDto> getUser(@RequestHeader(SecurityHelper.TOKEN_HEADER) String authHeader) {
        User user = userRepository.findByEmail(securityHelper.getUserNameFromAuthHeader(authHeader));

        return new ResponseEntity<>(user.toDto(), HttpStatus.OK);
    }

    @PutMapping
    public @ResponseBody
    ResponseEntity<InformationalResponse> editUser(@RequestHeader(SecurityHelper.TOKEN_HEADER) String authHeader,
                                                   @RequestBody UserDto userData) {
        User user = userRepository.findByEmail(securityHelper.getUserNameFromAuthHeader(authHeader));
        if(user == null) {
            return new ResponseEntity<>(new InformationalResponse(HttpStatus.UNAUTHORIZED.value(), "no user found"), HttpStatus.UNAUTHORIZED);
        } else {
            Optional.ofNullable(userData.getAvatar())
                .ifPresent(fileObject -> {
                    try {
                        fileObject.save(fileConfig.getAvatarStoragePath());
                        user.setAvatar(fileObject.toEntity());
                    } catch (IOException e) {
                        LOG.warn("error writing file", e);
                    }
                });

            Optional.ofNullable(userData.getFirstName())
                    .ifPresent(user::setFirstName);
            Optional.ofNullable(userData.getLastName())
                    .ifPresent(user::setLastName);
            Optional.ofNullable(userData.getPassword())
                    .map(passwordEncoder::encode)
                    .ifPresent(user::setPassword);

            if(userData.getEmail() != null) {
                User dbUserForNewEmail = userRepository.findByEmail(userData.getEmail());
                if(dbUserForNewEmail == null || dbUserForNewEmail.getId().equals(user.getId())) {
                    user.setEmail(userData.getEmail());
                } else {
                    return new ResponseEntity<>(new InformationalResponse(HttpStatus.CONFLICT.value(), "User with this email already exists"), HttpStatus.CONFLICT);
                }
            }

            userRepository.save(user);
        }

        return new ResponseEntity<>(new InformationalResponse(HttpStatus.OK.value(), "User updated"), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestHeader(SecurityHelper.TOKEN_HEADER) String authHeader) {
        User user = userRepository.findByEmail(securityHelper.getUserNameFromAuthHeader(authHeader));

        userRepository.delete(user);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
