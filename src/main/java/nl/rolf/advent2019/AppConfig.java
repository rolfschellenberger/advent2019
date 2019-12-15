package nl.rolf.advent2019;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan(basePackages = {"nl.rolf"})
@EnableScheduling
public class AppConfig {
}
