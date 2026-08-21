package io.github.danielcampossantos.userprofile;

import io.github.danielcampossantos.domain.User;
import io.github.danielcampossantos.domain.UserProfile;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserProfileMapper {

  List<UserProfileGetResponse> toUserProfileGetResponseList(List<UserProfile> userProfile);

  List<UserProfileUserGetResponse> toUserProfileUsersGetResponseList(List<User> users);
}
