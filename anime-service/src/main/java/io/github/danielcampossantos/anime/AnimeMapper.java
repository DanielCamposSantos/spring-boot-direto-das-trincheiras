package io.github.danielcampossantos.anime;

import io.github.danielcampossantos.domain.Anime;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimeMapper {
    Anime toAnime(AnimePostRequest postRequest);

    Anime toAnime(AnimePutRequest request);

    List<AnimeGetResponse> toAnimeGetResponseList(List<Anime> anime);

    AnimeGetResponse toAnimeGetResponse(Anime anime);


    AnimePostResponse toAnimePostResponse(Anime anime);


}
