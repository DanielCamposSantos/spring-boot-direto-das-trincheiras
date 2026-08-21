package io.github.danielcampossantos.profile;

import io.github.danielcampossantos.domain.Profile;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProfileMapper {

  ProfileGetResponse toProfileGetResponse(Profile profile);

  List<ProfileGetResponse> toProfileGetResponseList(List<Profile> profiles);

  Profile toProfile(ProfilePostRequest profilePostRequest);

  ProfilePostResponse toProfilePostResponse(Profile profile);
}
