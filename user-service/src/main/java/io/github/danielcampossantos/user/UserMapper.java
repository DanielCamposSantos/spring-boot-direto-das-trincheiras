package io.github.danielcampossantos.user;

import io.github.danielcampossantos.annotation.EncodedMapping;
import io.github.danielcampossantos.domain.User;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
    uses = PasswordEncoderMapper.class)

public interface UserMapper {

  @Mapping(target = "roles", constant = "USER")
  @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
  User toUser(UserPostRequest userPostRequest);

  @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
  User toUser(UserPutRequest userPutRequest);


  List<UserGetResponse> toUserGetResponseList(List<User> user);

  UserGetResponse toUserGetResponse(User user);


  UserPostResponse toUserPostResponse(User user);

  @Mapping(target = "password", source = "userToUpdate.password", qualifiedBy = EncodedMapping.class)
  @Mapping(target = "roles", source = "savedUser.roles")
  @Mapping(target = "id", source = "userToUpdate.id")
  @Mapping(target = "firstName", source = "userToUpdate.firstName")
  @Mapping(target = "lastName", source = "userToUpdate.lastName")
  @Mapping(target = "email", source = "userToUpdate.email")
  User userToUserWithPasswordAndRoles(User userToUpdate, String rawPassword, User savedUser);

  @AfterMapping
  default void setPasswordIfNull(@MappingTarget User user, String rawPassword, User savedUser) {
    if (rawPassword == null) {
      user.setPassword(savedUser.getPassword());
    }
  }

}

