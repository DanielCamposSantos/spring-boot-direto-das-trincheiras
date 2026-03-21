package io.github.danielcampossantos.userprofile;

import io.github.danielcampossantos.domain.User;
import io.github.danielcampossantos.domain.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserProfileMapper {

    List<UserProfileGetResponse> toUserProfileGetResponseList(List<UserProfile> userProfile);

    List<UserProfileUserGetRespose> toUserProfileUsersGetResponseList(List<User> users);
}
