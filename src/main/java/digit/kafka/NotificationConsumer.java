package digit.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.service.NotificationService;
import digit.web.models.BirthRegistrationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Slf4j  // Logger for logging messages
public class NotificationConsumer {

    @Autowired
    private ObjectMapper mapper;  // ObjectMapper to map JSON to Java objects

    @Autowired
    private NotificationService notificationService;  // Service for processing and sending notifications

    /**
     * Kafka listener to listen for messages from the specified topic.
     * Listens to birth registration events and processes them.
     *
     * @param record The message record received from Kafka
     * @param topic The Kafka topic the message was received from
     */
    @KafkaListener(topics = {"${btr.kafka.create.topic}"})
    public void listen(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

        try {
            // Convert the received message (HashMap) into BirthRegistrationRequest object
            BirthRegistrationRequest request = mapper.convertValue(record, BirthRegistrationRequest.class);
            
            // Call the notification service to prepare the event and send notification
            notificationService.prepareEventAndSend(request);

        } catch (final Exception e) {
            // Log any error that occurs while processing the message
            log.error("Error while listening to value: " + record + " on topic: " + topic + ": ", e);
        }
    }
}
