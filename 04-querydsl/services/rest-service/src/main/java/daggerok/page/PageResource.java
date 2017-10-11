package daggerok.page;

import daggerok.relationships.data.EngineerRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PageResource {

  final EngineerRepository repository;

  @GetMapping("/api/v5/engineers/page-info")
  public PagedResources.PageMetadata pageMetadata(final Pageable pageable,
                                                  final PagedResourcesAssembler resourcesAssembler) {

    val page = repository.findAllByOrderByModifiedAtDesc(pageable);

    return resourcesAssembler.toResource(page)
                             .getMetadata();
  }
}
