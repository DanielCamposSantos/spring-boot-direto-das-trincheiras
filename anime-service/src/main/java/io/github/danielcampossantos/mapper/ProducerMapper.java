package io.github.danielcampossantos.mapper;

import io.github.danielcampossantos.domain.Producer;
import io.github.danielcampossantos.requests.ProducerPostRequest;
import io.github.danielcampossantos.requests.ProducerPutRequest;
import io.github.danielcampossantos.response.ProducerGetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProducerMapper {
    ProducerMapper INSTANCE = Mappers.getMapper(ProducerMapper.class);

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(1,100_000))")
    Producer toProducer(ProducerPostRequest postRequest);

    Producer toProducer(ProducerPutRequest request);

    ProducerGetResponse toProducerGetResponse(Producer producer);

    List<ProducerGetResponse> toProducerGetResponseList(List<Producer> producer);

}
