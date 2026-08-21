package io.github.danielcampossantos.brasilapi;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "v1/brasil-api/cep")
@Log4j2
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class BraisilApiController {

  private final BrasilApiService brasilApiService;

  @GetMapping("/{cep}")
  public ResponseEntity<CepGetResponse> findCep(@PathVariable String cep) {
    return ResponseEntity.ok(brasilApiService.findCep(cep));
  }
}
