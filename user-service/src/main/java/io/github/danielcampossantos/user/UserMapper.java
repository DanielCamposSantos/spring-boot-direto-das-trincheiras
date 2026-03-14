package io.github.danielcampossantos.user;

import io.github.danielcampossantos.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User toUser(UserPostRequest userPostRequest);

    User toUser(UserPutRequest userPutRequest);


    List<UserGetResponse> toUserGetResponseList(List<User> user);

    UserGetResponse toUserGetResponse(User user);


    UserPostResponse toUserPostResponse(User user);
}

