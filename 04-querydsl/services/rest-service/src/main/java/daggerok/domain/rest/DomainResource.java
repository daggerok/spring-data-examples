package daggerok.domain.rest;

import daggerok.domain.rest.model.DomainRequest;
import daggerok.domain.rest.model.DomainResponse;
import daggerok.domain.service.DomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DomainResource {

  final DomainService domainService;

  /**
   * usage: `http :8080/api/v1/domains\?username=1\&firstName=3\&test=t3`
   */
  @GetMapping("/api/v1/domains")
  @Transactional(readOnly = true)
  public ResponseEntity<List<DomainResponse>> getDomains(
      @RequestParam MultiValueMap<String, String> parameters,
      final DomainRequest domainRequest) {

    log.info("request: {}", domainRequest);

    if (parameters.size() < 1) {
      return ResponseEntity.ok(domainService.getAllDomains());
    }

    val body = domainService.filterDomains(domainRequest);

    log.info("body: {}", body);
    return ResponseEntity.ok(body);
  }
}
