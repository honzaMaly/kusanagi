package cz.jan.maly;

import cz.jan.maly.service.FileReplayParserService;
import cz.jan.maly.service.ReplayParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by Jan on 30-Oct-16.
 */
@SpringBootApplication(scanBasePackages = {
        "cz.jan.maly","cz.jan.maly.service"
})
@EnableScheduling
@EntityScan(basePackages = {"cz.jan.maly.entities"})
@EnableJpaRepositories(basePackages = {"cz.jan.maly.repositories"})
public class ParserApplication extends SpringBootServletInitializer implements CommandLineRunner {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ParserApplication.class);
    }

    @Autowired
    private ReplayParserService replayParserService;

    public static void main(String[] args) {
        SpringApplication.run(ParserApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        replayParserService.parseReplays();
    }

}
