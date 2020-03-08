package net.dfranek.library.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.dfranek.library.rest.entity.User;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto implements DtoInterface<User> {

    private Integer id;

    private String email;

    private String password;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private FileObject avatar;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public FileObject getAvatar() {
        return avatar;
    }

    public void setAvatar(FileObject avatar) {
        this.avatar = avatar;
    }

    public User toEntity() {
        User user = new User();
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setEmail(email);
        user.setPassword(password);

        if(avatar != null) {
            user.setAvatar(avatar.toEntity());
        }

        return user;
    }
}
