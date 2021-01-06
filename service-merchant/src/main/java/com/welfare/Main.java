package com.welfare;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.SimpleCommandLinePropertySource;

@SpringBootApplication
public class Main {

    private static  final String PROFILES = "spring.profiles.active";
    private static String env = null;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Main.class);
        application.setBannerMode(Banner.Mode.OFF);
        SimpleCommandLinePropertySource commandLineArgs = new SimpleCommandLinePropertySource(args);
        env = commandLineArgs.getProperty(PROFILES);
        if (env == null) {
            env = "dev";
        }
        application.setAdditionalProfiles(env);
        application.run(args);
    }
}
