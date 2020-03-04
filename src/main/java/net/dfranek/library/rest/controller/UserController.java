package net.dfranek.library.rest.controller;

import net.dfranek.library.rest.config.FileConfig;
import net.dfranek.library.rest.dto.InformationalResponse;
import net.dfranek.library.rest.dto.UserDto;
import net.dfranek.library.rest.entity.User;
import net.dfranek.library.rest.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Optional;

@Controller // This means that this class is a Controller
@RequestMapping(path = "/user")
public class UserController {

    private Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileConfig fileConfig;

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

}
