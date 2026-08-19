package io.github.danielcampossantos.anime;

import io.github.danielcampossantos.domain.Anime;
import io.github.danielcampossantos.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimeMapper {
    Anime toAnime(AnimePostRequest postRequest);

    Anime toAnime(AnimePutRequest request);

    List<AnimeGetResponse> toAnimeGetResponseList(List<Anime> anime);

    AnimeGetResponse toAnimeGetResponse(Anime anime);


    AnimePostResponse toAnimePostResponse(Anime anime);


    PageAnimeGetResponse toPageAnimeGetResponse(Page<Anime> page);
}
