package io.github.danielcampossantos.anime.mapper;

import io.github.danielcampossantos.anime.response.AnimeGetResponse;
import io.github.danielcampossantos.anime.request.AnimePostRequest;
import io.github.danielcampossantos.anime.response.AnimePostResponse;
import io.github.danielcampossantos.anime.request.AnimePutRequest;
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
