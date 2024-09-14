package com.example.calculateservice.config;

import io.micrometer.common.KeyValues;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.micrometer.KafkaRecordSenderContext;
import org.springframework.kafka.support.micrometer.KafkaTemplateObservationConvention;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    public static final String SERVICE_ONE_TOPIC = "service.one.topic";
    public static final String SERVICE_TWO_TOPIC = "service.two.topic";

    private final KafkaProperties kafkaProperties; // Autowired. Get data from application.yaml (prefix =
                                                   // "spring.kafka")

    @Bean
    public Map<String, Object> producerConfigs() {
        // The Producer Configuration is a simple key-value map
        // we build our map passing the default values for the producer and overriding
        // the default Kafka key and value serializers.
        // The producer will serialize keys as Strings using the Kafka library’s
        // StringSerializer and will do the same
        // for values but this time using JSON, with a JsonSerializer, in this case
        // provided by Spring Kafka.
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildProducerProperties(null));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        // Spring Boot provides an auto-configured instance of KafkaTemplate. However, to enable Kafka tracing with Spring Boot we need to customize that instance.
        // Here’s the implementation of the KafkaTemplate bean inside the producer app’s main class. In order to enable tracing,
        // we need to invoke the setObservationEnabled method. By default, the Micrometer module generates some generic tags.
        // We want to add at least the name of the target topic and the Kafka message key.
        // Therefore we are creating our custom implementation of the KafkaTemplateObservationConvention interface.
        var t = new KafkaTemplate<>(producerFactory());
        t.setObservationEnabled(true);
        t.setObservationConvention(new KafkaTemplateObservationConvention() {
            @Override
            public KeyValues getLowCardinalityKeyValues(KafkaRecordSenderContext context) {
                return KeyValues.of("topic", context.getDestination(),
                    "id", String.valueOf(context.getRecord().key()));
            }
        });
        return t;
    }

    /**
     * In order to generate and export traces on the consumer side we need to override the ConcurrentKafkaListenerContainerFactory bean.
     * For the container listener factory, we should obtain the ContainerProperties instance and
     * then invoke the setObservationEnabled method. The same as before
     * we can create a custom implementation of the KafkaTemplateObservationConvention interface to include the additional tags (optionally).
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(ConsumerFactory<String, String> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();

        //The following code enable observation in the consumer listener
        factory.getContainerProperties().setObservationEnabled(true);
        factory.setConsumerFactory(consumerFactory);

        return factory;
    }

    // When we inject a NewTopic bean, we’re instructing the Kafka’s AdminClient
    // bean (already in the context)
    // to create a topic with the given configuration
    @Bean
    public NewTopic serviceOneTopic() {
        return TopicBuilder.name(SERVICE_ONE_TOPIC).partitions(1).replicas(1).build();
    }

}