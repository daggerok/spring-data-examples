package daggerok.ws;

import daggerok.domain.country.Country;
import daggerok.domain.country.CountryRepository;
import daggerok.gen.GetCountryRequest;
import daggerok.gen.GetCountryResponse;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@RequiredArgsConstructor
public class CountryEndpoint {

  public static final String COUNTRIES_NAMESPACE_URI = "http://github.com/daggerok/services/soap-service";

  final CountryRepository countryRepository;

  @ResponsePayload
  @PayloadRoot(namespace = COUNTRIES_NAMESPACE_URI, localPart = "getCountryRequest")
  public GetCountryResponse getCountry(@RequestPayload final GetCountryRequest request) {

    val name = request.getName();
    val country = countryRepository.findFirstByName(name)
                                   .orElseThrow(() -> new RuntimeException("country " + name + " wasn't found."));

    return mapToGetCountryResponse(country);
  }

  private GetCountryResponse mapToGetCountryResponse(final Country country) {

    val toCountry = new daggerok.gen.Country();
    toCountry.setName(country.getName());
    toCountry.setCapital(country.getCapital());
    toCountry.setCurrency(country.getCurrency());
    toCountry.setPopulation(country.getPopulation());

    val getCountryResponse = new GetCountryResponse();
    getCountryResponse.setCountry(toCountry);

    return getCountryResponse;
  }
}
