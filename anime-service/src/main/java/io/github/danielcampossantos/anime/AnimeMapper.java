package io.github.danielcampossantos.anime;

import io.github.danielcampossantos.domain.Anime;
import io.github.danielcampossantos.dto.AnimeGetResponse;
import io.github.danielcampossantos.dto.AnimePostRequest;
import io.github.danielcampossantos.dto.AnimePostResponse;
import io.github.danielcampossantos.dto.AnimePutRequest;
import io.github.danielcampossantos.dto.PageAnimeGetResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimeMapper {

  Anime toAnime(AnimePostRequest postRequest);

  Anime toAnime(AnimePutRequest request);

  List<AnimeGetResponse> toAnimeGetResponseList(List<Anime> anime);

  AnimeGetResponse toAnimeGetResponse(Anime anime);


  AnimePostResponse toAnimePostResponse(Anime anime);


  PageAnimeGetResponse toPageAnimeGetResponse(Page<Anime> page);
}
