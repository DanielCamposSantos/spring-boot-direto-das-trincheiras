package io.github.danielcampossantos.producer;

import io.github.danielcampossantos.domain.Producer;
import io.github.danielcampossantos.dto.ProducerGetResponse;
import io.github.danielcampossantos.dto.ProducerPostRequest;
import io.github.danielcampossantos.dto.ProducerPostResponse;
import io.github.danielcampossantos.dto.ProducerPutRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProducerMapper {
    Producer toProducer(ProducerPostRequest postRequest);

    Producer toProducer(ProducerPutRequest request);

    ProducerGetResponse toProducerGetResponse(Producer producer);

    List<ProducerGetResponse> toProducerGetResponseList(List<Producer> producer);

    ProducerPostResponse toProducerPostResponse(Producer producer);
}
