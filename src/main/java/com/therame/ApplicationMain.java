package com.therame;

import com.therame.model.*;
import com.therame.service.AssignmentService;
import com.therame.service.MediaStorageService;
import com.therame.service.UserService;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.TaskExecutorFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Collectors;

@SpringBootApplication
public class ApplicationMain{

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Value("${server.port}")
    private int httpsPort;

    @Value("${server.http.port}")
    private int httpPort;

    public static void main(String[] args) {
        SpringApplication.run(ApplicationMain.class, args);
    }

    // Turns out we need this in order to be able to successfully use multiprocessing/concurrent stuff prior to app start.
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        return executor;
    }
    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };

        tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());
        return tomcat;
    }

    private Connector initiateHttpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(8080);
        connector.setSecure(false);
        connector.setRedirectPort(8443);

        return connector;
    }

    private User createAndInitUser(String email, String firstName, String lastName,
                                   String unencryptedPassword, User.Type type, User therapist, Provider company) {
        User newUser = new User();
        newUser.setConfirmationToken(null);
        newUser.setActive(true);
        newUser.setEnabled(true);
        newUser.setEmail(email);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setType(type);
        newUser.setTherapist(therapist);
        newUser.setPassword(passwordEncoder().encode(unencryptedPassword));
        newUser.setProvider(company);
        return newUser;
    }
    private class Tuple<T, D> {
        private T p1;
        private D p2;

        public Tuple(T p1, D p2) {
            this.p1 = p1;
            this.p2 = p2;
        }
    }
    private Exercise createAndInitExercise(Provider provider, String title, String description) {
        Exercise newExercise = new Exercise();
        newExercise.setProvider(provider);
        newExercise.setTitle(title);
        newExercise.setMediaUrl("https://localhost:8443");
        newExercise.setDescription(description);
        return newExercise;
    }

    @Bean
    public CommandLineRunner loadInitialUsers(UserService userService,
                                              ProviderRepository providerRepository,
                                              AssignmentService assignmentService,
                                              MediaStorageService mediaStorageService) {
        return (args) -> {

            Provider johnsonPT = new Provider();
                    johnsonPT.setAddress("3141 Wallaby Way, Orlando, FL");
                    johnsonPT.setName("Johnson Physical Therapy");
                    johnsonPT.setPhone("617-777-7777");
                    johnsonPT.setHours("MWF 09:00-16:30");
                    johnsonPT.setDescription("A Great PT Place for a Great PT Face!");

            johnsonPT = providerRepository.save(johnsonPT);

            User sentinel =
                    createAndInitUser("sentinel@therame.com",
                            "Sentinel", "Prime",
                            "decepticons",
                            User.Type.ADMIN, null, null);
                sentinel = userService.createTestUser(sentinel);

            User johnsonPTAdmin =
                    createAndInitUser("admin@johnsonpt.com",
                            "John", "Johnson",
                            "johnson",
                            User.Type.ADMIN, null, johnsonPT);
                johnsonPTAdmin = userService.createTestUser(johnsonPTAdmin);

            User therapistJay =
                    createAndInitUser("riccoj@wit.edu",
                    "Jay", "Ricco",
                    "hulkhoganisgod",
                    User.Type.THERAPIST, null, johnsonPT);
                therapistJay = userService.createTestUser(therapistJay);

            User patientJay =
                    createAndInitUser("jayericco@gmail.com",
                            "Jae", "Bippo",
                            "hulkhoganisgod",
                            User.Type.PATIENT, therapistJay, johnsonPT);
                patientJay = userService.createTestUser(patientJay);

            Exercise birdDogs = createAndInitExercise(johnsonPT, "Bird Dogs",
                    "These are the best birddogs ever.");

            mediaStorageService.store(birdDogs, new File("./BirdDogs.MOV"));

            Exercise reverseCrunches = createAndInitExercise(johnsonPT, "Reverse Crunches",
                    "Don't mess up. ");

            mediaStorageService.store(reverseCrunches, new File("./ElongatedBackSpasms.MOV"));

            //don't remember if these are right, i forget the name to this one

            Exercise legUps = createAndInitExercise(johnsonPT, "Leg Ups",
                    "With regards to feedback...\n\nThis one's supposed to hurt.");

            mediaStorageService.store(legUps, new File("./LegUps.MOV"));

            assignmentService.createAssignment(patientJay.getId(), birdDogs.getId(), 0);
            assignmentService.createAssignment(patientJay.getId(), reverseCrunches.getId(), 0);
            assignmentService.createAssignment(patientJay.getId(), legUps.getId(), 0);

        };
    }
}