package io.github.danielcampossantos.userservice.mapper;

import io.github.danielcampossantos.userservice.domain.User;
import io.github.danielcampossantos.userservice.response.UserGetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    List<UserGetResponse> toUserGetResponseList(List<User> user);

}
