/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package daggerok;

import daggerok.gen.Currency;
import daggerok.gen.GetCountryRequest;
import daggerok.gen.GetCountryResponse;
import lombok.val;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ClassUtils;
import org.springframework.ws.client.core.WebServiceTemplate;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class WsApplicationIntegrationTests {

  static final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
  static final WebServiceTemplate webServiceTemplate = new WebServiceTemplate(marshaller);

  @LocalServerPort
  int port = 0;

  String url;

  @Before
  public void init() throws Exception {

    marshaller.setPackagesToScan(ClassUtils.getPackageName(GetCountryRequest.class));
    marshaller.afterPropertiesSet();
    url = format("http://localhost:%d/ws", port);
  }

  @Test
  public void testSendAndReceive() {

    // given
    val request = new GetCountryRequest();
    request.setName("Spain");

    // when
    val response = call(request);

    // then
    assertThat(response, notNullValue());
    assertThat(response.getClass().isAssignableFrom(GetCountryResponse.class), Is.is(true));

    // and
    val getCountryResponse = (GetCountryResponse) response;
    val country = getCountryResponse.getCountry();
    assertThat(country.getCapital(), equalTo("Madrid"));
    assertThat(country.getCurrency(), equalTo(Currency.EUR));
  }

  <T> Object call(final T requestPayload) {
    return webServiceTemplate.marshalSendAndReceive(url, requestPayload);
  }
}
