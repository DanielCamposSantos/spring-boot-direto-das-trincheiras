package io.github.danielcampossantos.mapper;

import io.github.danielcampossantos.domain.User;
import io.github.danielcampossantos.request.UserPostRequest;
import io.github.danielcampossantos.request.UserPutRequest;
import io.github.danielcampossantos.response.UserGetResponse;
import io.github.danielcampossantos.response.UserPostResponse;
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

