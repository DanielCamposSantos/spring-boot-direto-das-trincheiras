package io.github.danielcampossantos.mapper;

import io.github.danielcampossantos.domain.Anime;
import io.github.danielcampossantos.requests.AnimePostRequest;
import io.github.danielcampossantos.requests.AnimePutRequest;
import io.github.danielcampossantos.response.AnimeGetResponse;
import io.github.danielcampossantos.response.AnimePostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AnimeMapper {
    AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);

    List<AnimeGetResponse> toAnimeGetResponseList(List<Anime> anime);

    AnimeGetResponse toAnimeGetResponse(Anime anime);

    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(1,100_000))")
    AnimePostResponse toAnimePostResponse(AnimePostRequest postRequest);

    Anime toAnime(AnimePostRequest postRequest);


    Anime toAnime(AnimePutRequest request);
}
