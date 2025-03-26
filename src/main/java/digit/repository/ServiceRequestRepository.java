package digit.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.ServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Repository
@Slf4j  // Logger for logging service-related issues
public class ServiceRequestRepository {

    private ObjectMapper mapper;  // For JSON mapping and configuration
    private RestTemplate restTemplate;  // To make HTTP requests

    @Autowired
    public ServiceRequestRepository(ObjectMapper mapper, RestTemplate restTemplate) {
        this.mapper = mapper;  // Inject ObjectMapper
        this.restTemplate = restTemplate;  // Inject RestTemplate
    }

    /**
     * Makes a POST request to an external service and returns the response.
     * 
     * @param uri The URI of the external service
     * @param request The request object to be sent
     * @return The response from the external service
     */
    public Object fetchResult(StringBuilder uri, Object request) {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);  // Configure mapper to avoid failure on empty beans
        Object response = null;
        try {
            // Make a POST request to the external service and return the response as a Map
            response = restTemplate.postForObject(uri.toString(), request, Map.class);
        } catch (HttpClientErrorException e) {
            // Log and throw custom exception if service call fails
            log.error("External Service threw an Exception: ", e);
            throw new ServiceCallException(e.getResponseBodyAsString());
        } catch (Exception e) {
            // Log other exceptions that might occur during the request
            log.error("Exception while fetching from searcher: ", e);
        }

        return response;  // Return the service response
    }
}
