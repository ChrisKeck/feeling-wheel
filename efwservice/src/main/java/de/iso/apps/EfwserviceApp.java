package de.iso.apps;

import de.iso.apps.config.ApplicationProperties;
import de.iso.apps.config.DefaultProfileUtil;
import de.iso.apps.contracts.Topicable;
import de.iso.apps.service.dto.MailChangingDTO;
import io.github.jhipster.config.JHipsterConstants;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableConfigurationProperties({LiquibaseProperties.class, ApplicationProperties.class}) @EnableDiscoveryClient
public class EfwserviceApp {
    
    private static final Logger log = LoggerFactory.getLogger(EfwserviceApp.class);
    
    private final Environment env;
    
    
    public EfwserviceApp(Environment env) {
        this.env = env;
    }
    
    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(EfwserviceApp.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }
    
    private static void logApplicationStartup(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info("\n----------------------------------------------------------\n\t" +
                 "Application '{}' is running! Access URLs:\n\t" +
                 "Local: \t\t{}://localhost:{}{}\n\t" +
                 "External: \t{}://{}:{}{}\n\t" +
                 "Profile(s): \t{}\n----------------------------------------------------------",
                 env.getProperty("spring.application.name"),
                 protocol,
                 serverPort,
                 contextPath,
                 protocol,
                 hostAddress,
                 serverPort,
                 contextPath,
                 env.getActiveProfiles());
        
        String configServerStatus = env.getProperty("configserver.status");
        if (configServerStatus == null) {
            configServerStatus = "Not found or not setup for this application";
        }
        log.info("\n----------------------------------------------------------\n\t" +
                 "Config Server: \t{}\n----------------------------------------------------------", configServerStatus);
    }
    
    /**
     * Initializes efwservice.
     * <p>
     * Spring profiles can be configured with a program argument --spring.profiles.active=your-active-profile
     * <p>
     * You can find more information on how profiles work with JHipster on <a href="https://www.jhipster.tech/profiles/">https://www.jhipster.tech/profiles/</a>.
     */
    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(
            JHipsterConstants.SPRING_PROFILE_PRODUCTION)) {
            log.error("You have misconfigured your application! It should not run " +
                      "with both the 'dev' and 'prod' profiles at the same time.");
        }
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(
            JHipsterConstants.SPRING_PROFILE_CLOUD)) {
            log.error("You have misconfigured your application! It should not " +
                      "run with both the 'dev' and 'cloud' profiles at the same time.");
        }
    }
    
    
    @Autowired private KafkaProperties kafkaProperties;
    
    @Value("${tpd.topic-name}") private String topicName;
    @Value("${employee.topic-name}") private String employeeTopicName;
    // Producer configuration
    @Value("${tpd.messages-per-request}") private int numberofRequests;
    @Value("${employee.messages-per-request}") private int employeeNumberofRequests;
    // Producer configuration
    
    @Bean
    public KafkaTemplate<String, MailChangingDTO> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
    
    @Bean
    public ProducerFactory<String, MailChangingDTO> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }
    
    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildProducerProperties());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        var lst = new ArrayList<String>();
        lst.add("kafka:9200");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, lst);
        
        return props;
    }
    
    @Bean
    public NewTopic adviceTopic() {
        return new NewTopic(employeeTopicName, 3, (short) 1);
    }
    
    @Bean
    public Topicable topicableMailChanging() {
        return new TopicableMailChangingImpl();
    }
    
    public class TopicableMailChangingImpl implements Topicable<MailChangingDTO> {
        
        
        @Override
        public String getTopic() {
            return employeeTopicName;
        }
        
        @Override
        public int getRequestsPerMessage() {
            return employeeNumberofRequests;
        }
        
        @Override
        public KafkaTemplate<String, MailChangingDTO> getKafkaTemplate() {
            return kafkaTemplate();
        }
    }
    
    // Consumer configuration
    
    // If you only need one kind of deserialization, you only need to set the
    // Consumer configuration properties. Uncomment this and remove all others below.
    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "tpd-loggers");
        var lst = new ArrayList<String>();
        lst.add("kafka:9200");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, lst);
        
        return props;
    }
    
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MailChangingDTO> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MailChangingDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        
        return factory;
    }
    
    @Bean
    public ConsumerFactory<String, MailChangingDTO> consumerFactory() {
        JsonDeserializer<MailChangingDTO> jsonDeserializer = new JsonDeserializer<>();
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties(),
                                                 new StringDeserializer(),
                                                 jsonDeserializer);
    }
    
    
}
