package io.github.danielcampossantos.userservice.mapper;

import io.github.danielcampossantos.userservice.domain.User;
import io.github.danielcampossantos.userservice.request.UserPostRequest;
import io.github.danielcampossantos.userservice.request.UserPutRequest;
import io.github.danielcampossantos.userservice.response.UserGetResponse;
import io.github.danielcampossantos.userservice.response.UserPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(target = "id", expression= "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(0, 100_00))")
    User toUser(UserPostRequest userPostRequest);

    User toUser(UserPutRequest userPutRequest);


    List<UserGetResponse> toUserGetResponseList(List<User> user);

    UserGetResponse toUserGetResponse(User user);



    UserPostResponse toUserPostResponse(User user);
}

