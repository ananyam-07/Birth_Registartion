package digit.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import digit.config.Configuration;
import static digit.config.ServiceConstants.*;

/**
 * Utility class for shortening URLs using an external URL shortening service.
 */
@Slf4j
@Component
public class UrlShortenerUtil {

    @Autowired
    private RestTemplate restTemplate; // REST client for making HTTP requests

    @Autowired
    private Configuration configs; // Configuration for URL shortening service

    /**
     * Shortens a given URL using an external URL shortening service.
     * 
     * @param url The original URL to be shortened
     * @return The shortened URL if successful, else returns the original URL
     */
    public String getShortenedUrl(String url){

        // Prepare request body
        HashMap<String, String> body = new HashMap<>();
        body.put(URL, url);

        // Construct the URL for the URL shortening service
        StringBuilder builder = new StringBuilder(configs.getUrlShortnerHost());
        builder.append(configs.getUrlShortnerEndpoint());

        // Call the external service to shorten the URL
        String res = restTemplate.postForObject(builder.toString(), body, String.class);

        // If response is empty, log the error and return original URL
        if (StringUtils.isEmpty(res)) {
            log.error(URL_SHORTENING_ERROR_CODE, URL_SHORTENING_ERROR_MESSAGE + url);
            return url;
        }

        // Return the shortened URL
        return res;
    }
}
