package digit.service;

import digit.config.BTRConfiguration;
import digit.kafka.Producer;
import digit.web.models.BirthRegistrationApplication;
import digit.web.models.BirthRegistrationRequest;
import digit.web.models.SMSRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service  // Marks this class as a service component for business logic
public class NotificationService {

    @Autowired
    private Producer producer;  // Kafka producer to send SMS notifications

    @Autowired
    private BTRConfiguration config;  // Configuration for the service

    @Autowired
    private RestTemplate restTemplate;  // RestTemplate for making external HTTP requests

    private static final String smsTemplate = "Dear {FATHER_NAME} and {MOTHER_NAME} your birth registration application has been successfully created on the system with application number - {APPNUMBER}.";  // SMS template

    /**
     * Prepares SMS notifications for parents and sends them via Kafka producer.
     * 
     * @param request The birth registration request containing the applications
     */
    public void prepareEventAndSend(BirthRegistrationRequest request) {
        List<SMSRequest> smsRequestList = new ArrayList<>();  // List to store SMS requests

        // Iterate through each birth registration application and prepare SMS requests for both father and mother
        request.getBirthRegistrationApplications().forEach(application -> {
            SMSRequest smsRequestForFather = SMSRequest.builder()
                    .mobileNumber(application.getFather().getMobileNumber())
                    .message(getCustomMessage(smsTemplate, application))
                    .build();
            SMSRequest smsRequestForMother = SMSRequest.builder()
                    .mobileNumber(application.getMother().getMobileNumber())
                    .message(getCustomMessage(smsTemplate, application))
                    .build();
            smsRequestList.add(smsRequestForFather);
            smsRequestList.add(smsRequestForMother);
        });

        // Send the SMS requests to Kafka topic
        for (SMSRequest smsRequest : smsRequestList) {
            producer.push(config.getSmsNotificationTopic(), smsRequest);  // Push SMS request to Kafka
            log.info("Messages: " + smsRequest.getMessage());  // Log the sent message
        }
    }

    /**
     * Customizes the SMS template with specific application details.
     * 
     * @param template The SMS template
     * @param application The birth registration application
     * @return Customized SMS message
     */
    private String getCustomMessage(String template, BirthRegistrationApplication application) {
        template = template.replace("{APPNUMBER}", application.getApplicationNumber());
        template = template.replace("{FATHER_NAME}", application.getFather().getName());
        template = template.replace("{MOTHER_NAME}", application.getMother().getName());
        return template;  // Return the personalized SMS message
    }
}
