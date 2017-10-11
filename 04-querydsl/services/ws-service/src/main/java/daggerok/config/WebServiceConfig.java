package daggerok.config;

import lombok.val;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import static daggerok.ws.CountryEndpoint.COUNTRIES_NAMESPACE_URI;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

  @Bean
  public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
    val messageDispatcherServlet = new MessageDispatcherServlet();
    messageDispatcherServlet.setApplicationContext(applicationContext);
    messageDispatcherServlet.setTransformWsdlLocations(true);
    return new ServletRegistrationBean(messageDispatcherServlet, "/ws/*");
  }

  @Bean(name = "countriesSchema")
  public XsdSchema countriesSchema() {
    return new SimpleXsdSchema(new ClassPathResource("schema/countries.xsd"));
  }

  @Bean(name = "countries")
  public DefaultWsdl11Definition defaultWsdl11Definition() {
    val defaultWsdl11Definition = new DefaultWsdl11Definition();
    defaultWsdl11Definition.setPortTypeName("CountriesPort");
    defaultWsdl11Definition.setLocationUri("/ws");
    defaultWsdl11Definition.setTargetNamespace(COUNTRIES_NAMESPACE_URI);
    defaultWsdl11Definition.setSchema(countriesSchema());
    return defaultWsdl11Definition;
  }
}
