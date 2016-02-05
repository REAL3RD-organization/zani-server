package real.zani.config;

import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Baek on 2016-01-28.
 */
@Configuration
public class DozerMapperConfig {
    @Bean
    public DozerBeanMapper dozer() {
        DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
        return dozerBeanMapper;
    }
}
