package epn.edu.ec.populator;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import epn.edu.ec.repository.CakeRepository;
import epn.edu.ec.repository.model.Cake;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CakeDatabasePopulator {
    private final String cakesUrl;
    private final CakeRepository cakeRepository;

    public CakeDatabasePopulator(@Value("${cakes.populator.url}") String cakesUrl, CakeRepository cakeRepository) {
        this.cakesUrl = cakesUrl;
        this.cakeRepository = cakeRepository;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void populateCakeDatabase() throws Exception {
        log.info("populating cake database from {}", cakesUrl);

        List<CakePopulatorResponse> cakesToPopulate = new ObjectMapper().readValue(new URL(cakesUrl), new TypeReference<>() {
        });

        List<Cake> cakes = cakesToPopulate.stream()
                .map(cake -> Cake.builder()
                        .title(cake.title)
                        .description(cake.desc)
                        .build())
                .collect(Collectors.toList());                

        cakeRepository.saveAll(cakes);

        log.info("cake database populated with {} cakes", cakes.size());
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data    
    @AllArgsConstructor
    private static class CakePopulatorResponse {
        private String title;
        private String desc;

        public CakePopulatorResponse() {
            
        }
    }
}
