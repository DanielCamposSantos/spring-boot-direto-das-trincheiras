package io.github.danielcampossantos.profile;

import io.github.danielcampossantos.domain.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProfileMapper {
    ProfileGetResponse toProfileGetResponse(Profile profile);

    List<ProfileGetResponse> toProfileGetResponseList(List<Profile> profiles);

    Profile toProfile(ProfilePostRequest profilePostRequest);

    ProfilePostResponse toProfilePostResponse(Profile profile);
}
