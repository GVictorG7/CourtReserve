package ro.courtreserve.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    /**
     * Bean for injecting the {@link ModelMapper} object
     *
     * @return the {@link ModelMapper} bean
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
