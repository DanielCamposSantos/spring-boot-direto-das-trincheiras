package io.github.danielcampossantos.producer.mapper;

import io.github.danielcampossantos.domain.Producer;
import io.github.danielcampossantos.producer.response.ProducerGetResponse;
import io.github.danielcampossantos.producer.request.ProducerPostRequest;
import io.github.danielcampossantos.producer.response.ProducerPostResponse;
import io.github.danielcampossantos.producer.request.ProducerPutRequest;
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
