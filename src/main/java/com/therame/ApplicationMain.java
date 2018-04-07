package com.therame;

import com.therame.model.User;
import com.therame.repository.jpa.ExerciseRepository;
import com.therame.repository.jpa.UserRepository;
import com.therame.repository.solr.SolrUserRepository;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
@EnableAutoConfiguration
@EnableSolrRepositories(basePackages = "com.therame.repository.solr", multicoreSupport = true)
@EnableJpaRepositories(basePackages = "com.therame.repository.jpa")
public class ApplicationMain {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationMain.class, args);


    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
                                   String unencryptedPassword, User.Type type, User therapist) {
        User newUser = new User();
        newUser.generateConfirmationToken();
        newUser.setActive(true);
        newUser.setEnabled(true);
        newUser.setEmail(email);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setType(type);
        newUser.setTherapist(therapist);
        newUser.setPassword(passwordEncoder().encode(unencryptedPassword));
        return newUser;
    }

    @Bean
    public CommandLineRunner loadInitialUsers(UserRepository userRepository) {
        return (args) -> {

            ArrayList<User> initUsers = new ArrayList<>();
            initUsers.add(
            createAndInitUser("jayericco@gmail.com",
                            "Jay", "Ricco",
                            "hulkhoganisgod",
                            User.Type.PATIENT, null));

            /*initUsers.forEach(user -> {
                System.out.println(user);
                userRepository.findByEmail(user.getEmail())
                        .ifPresent(existingUser -> {System.out.println(existingUser); userRepository.delete(existingUser);});
                userRepository.save(user);
            });*/
        };
    }

}