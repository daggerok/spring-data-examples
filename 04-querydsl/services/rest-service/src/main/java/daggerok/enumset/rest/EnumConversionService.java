package daggerok.enumset.rest;

import daggerok.enumset.data.catalog.Type;
import io.vavr.control.Try;
import lombok.val;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

//@Configuration
public class EnumConversionService extends WebMvcConfigurationSupport {

  @Override
  public FormattingConversionService mvcConversionService() {
    val conversionService = super.mvcConversionService();
    conversionService.addConverter(new StringTpStatusTypeConverter());
    return conversionService;
  }

  public static class StringTpStatusTypeConverter implements Converter<String, Type> {

    @Override
    public Type convert(final String source) {

      return Try.of(() -> Type.valueOf(source))
                .getOrElse(Type.NA);
    }
  }
}
