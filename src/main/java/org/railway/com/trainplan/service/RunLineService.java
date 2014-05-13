package org.railway.com.trainplan.service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.javasimon.aop.Monitored;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * Created by star on 5/13/14.
 */
@Component
@Monitored
public class RunLineService {

    @Value("#{restConfig['rest.url']}")
    private String restUrl;

    public List<Map<String, Object>> getRunLine() {
        Client client = Client.create();
        WebResource webResource = client.resource(restUrl);
        webResource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get(String.class);
        return null;
    }
}
