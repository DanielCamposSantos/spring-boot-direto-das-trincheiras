package io.github.danielcampossantos.user;

import io.github.danielcampossantos.annotation.EncodedMapping;
import io.github.danielcampossantos.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = PasswordEncoderMapper.class)
public interface UserMapper {
    @Mapping(target = "roles", constant = "USER")
    @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
    User toUser(UserPostRequest userPostRequest);

    @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
    @Mapping(target = "roles", constant = "USER")
    User toUser(UserPutRequest userPutRequest);


    List<UserGetResponse> toUserGetResponseList(List<User> user);

    UserGetResponse toUserGetResponse(User user);


    UserPostResponse toUserPostResponse(User user);
}

