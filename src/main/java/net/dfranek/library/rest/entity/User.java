package net.dfranek.library.rest.entity;

import net.dfranek.library.rest.dto.UserDto;

import javax.persistence.*;

@Entity // This tells Hibernate to make a table out of this class
public class User implements EntityInterface<UserDto> {
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Integer id;

  private String email;

  private String firstName;

  private String lastName;

  private String password;

  private final String roles = "[]";

  @OneToOne(
      fetch = FetchType.EAGER,
      cascade = CascadeType.ALL
  )
  @JoinColumn(name = "avatar_id")
  private DatabaseFile avatar;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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

  public String getRoles() {
    return roles;
  }

  public DatabaseFile getAvatar() {
    return avatar;
  }

  public void setAvatar(DatabaseFile avatar) {
    this.avatar = avatar;
  }

  @Override
  public UserDto toDto() {
    UserDto userDto = new UserDto();
    userDto.setId(getId());
    userDto.setEmail(getEmail());
    userDto.setFirstName(getFirstName());
    userDto.setLastName(getLastName());
    if(getAvatar() != null) {
      userDto.setAvatar(getAvatar().toDto());
    }

    return userDto;
  }
}