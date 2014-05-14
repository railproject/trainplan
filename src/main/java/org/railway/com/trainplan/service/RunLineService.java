package org.railway.com.trainplan.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javasimon.aop.Monitored;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by star on 5/13/14.
 */
@Component
@Monitored
public class RunLineService {

    private final static Log logger = LogFactory.getLog(RunLineService.class);

    @Value("#{restConfig['SERVICE_URL']}")
    private String restUrl;

    public List<Map<String, Object>> getRunLine(String date, String bureau) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> params = new HashMap<String, String>();
        params.put("sourceTime", date + " 00:00:00");
        params.put("targetTime", date + " 23:59:59");
        params.put("code", "01"); // 看板01  分析10
        params.put("timeFormat", "yyyy-MM-dd hh:mm:ss");
        params.put("sourceBureauShortName", bureau);
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        try {
            String values=  objectMapper.writeValueAsString(params);
            logger.debug(values);
            Client client = Client.create();

            WebResource webResource = client.resource(restUrl + "/rail/plan/Trainlines?query");
            ClientResponse response = webResource.type("application/json").accept("application/json").post(ClientResponse.class, values);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }
            String resp = response.getEntity(String.class);
            Map<String, Object> result = objectMapper.readValue(resp, Map.class);
            data = (List<Map<String, Object>>) result.get("data");
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e);
        }
        return data;
    }

    public List<Map<String, Object>> getRunLineSTN(String line_id) {
        logger.debug("line_id: " + line_id);
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> lineStn = new ArrayList<Map<String, Object>>();
        try {
            Client client = Client.create();

            WebResource webResource = client.resource(restUrl + "/rail/plan/Trainlines/" + line_id);
            ClientResponse response = webResource.type("application/json").accept("application/json").get(ClientResponse.class);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }
            String resp = response.getEntity(String.class);
            Map<String, Object> result = objectMapper.readValue(resp, Map.class);
            List<Map<String, Object>> data = (List<Map<String, Object>>) result.get("data");
            Map<String, Object> map = data.get(0);
            Map<String, Map<String, Object>> timeTable = (Map<String, Map<String, Object>>) map.get("scheduleDto");
            lineStn.add(timeTable.get("sourceItemDto"));
            List<Map<String, Object>> routingTable = (List<Map<String, Object>>) timeTable.get("routeItemDtos");
            lineStn.addAll(routingTable);
            lineStn.add(timeTable.get("targetItemDto"));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e);
        }
        return lineStn;
    }


    public static void main(String[] args) {
        /*ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> params = new HashMap<String, String>();
        params.put("sourceTime", "2014-05-11 00:00:00");
        params.put("targetTime", "2014-05-11 23:59:59");
        params.put("code", "01"); // 看板01  分析10
        params.put("timeFormat", "yyyy-MM-dd hh:mm:ss");
        params.put("sourceBureauShortName", "京");

        try {
            String values=  objectMapper.writeValueAsString(params);
            logger.debug(values);
            Client client = Client.create();
            ClientResponse response = client.resource("http://10.1.191.135:7003/rail/plan/Trainlines?query").type("application/json").accept("application/json").post(ClientResponse.class, values);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }


            String resp = response.getEntity(String.class);
            Map<String, Object> result = objectMapper.readValue(resp, Map.class);
            List<Map<String, Object>> data = (List<Map<String, Object>>) result.get("data");
            logger.debug(resp);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> params = new HashMap<String, String>();
        params.put("sourceTime", "2014-05-11 00:00:00");
        params.put("targetTime", "2014-05-11 23:59:59");
        params.put("code", "01"); // 看板01  分析10
        params.put("timeFormat", "yyyy-MM-dd hh:mm:ss");
        params.put("sourceBureauShortName", "京");

        try {
            String values=  objectMapper.writeValueAsString(params);
            logger.debug(values);
            Client client = Client.create();
            WebResource webResource = client.resource("http://10.1.191.135:7003/rail/plan/Trainlines/98aa7f7d-11e0-40c2-88f9-2ebef739271e");
            ClientResponse response = webResource.type("application/json").accept("application/json").get(ClientResponse.class);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }


            String resp = response.getEntity(String.class);
            logger.debug(resp);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
