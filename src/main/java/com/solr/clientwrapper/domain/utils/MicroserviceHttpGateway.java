package com.solr.clientwrapper.domain.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Data
public class MicroserviceHttpGateway {

    private final Logger log = LoggerFactory.getLogger(MicroserviceHttpGateway.class);

    private String apiEndpoint;
    private Object requestBodyDTO;

    public JSONObject postRequest(){

        JSONObject jsonObject=null;

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost http = new HttpPost(apiEndpoint);

        ObjectMapper objectMapper = new ObjectMapper();

        try {

            String objJackson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestBodyDTO);
            StringEntity entity = new StringEntity(objJackson);

            http.setEntity(entity);
            http.setHeader("Accept", "application/json");
            http.setHeader("Content-type", "application/json");

            CloseableHttpResponse response = client.execute(http);
            HttpEntity entityResponse = response.getEntity();
            String result = EntityUtils.toString(entityResponse);
            log.debug("RESPONSE: "+ result);

            jsonObject= new JSONObject(result );

            client.close();

        } catch (Exception e) {

            e.printStackTrace();
            log.error(e.toString());

        }

        return jsonObject;

    }

    public JSONObject putRequest(){

        JSONObject jsonObject=null;

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPut http = new HttpPut(apiEndpoint);

        ObjectMapper objectMapper = new ObjectMapper();

        try {

            String objJackson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestBodyDTO);
            StringEntity entity = new StringEntity(objJackson);

            http.setEntity(entity);
            http.setHeader("Accept", "application/json");
            http.setHeader("Content-type", "application/json");

            CloseableHttpResponse response = client.execute(http);
            HttpEntity entityResponse = response.getEntity();
            String result = EntityUtils.toString(entityResponse);
            log.debug("RESPONSE: "+ result);

            jsonObject= new JSONObject(result );

            client.close();

        } catch (Exception e) {

            e.printStackTrace();
            log.error(e.toString());

        }

        return jsonObject;

    }

    public JSONObject deleteRequest(){

        JSONObject jsonObject=null;

        CloseableHttpClient client = HttpClients.createDefault();
        HttpDelete http = new HttpDelete(apiEndpoint);

        ObjectMapper objectMapper = new ObjectMapper();

        try {

            String objJackson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestBodyDTO);
            StringEntity entity = new StringEntity(objJackson);

            http.setHeader("Accept", "application/json");
            http.setHeader("Content-type", "application/json");

            CloseableHttpResponse response = client.execute(http);
            HttpEntity entityResponse = response.getEntity();
            String result = EntityUtils.toString(entityResponse);
            log.debug("RESPONSE: "+ result);

            jsonObject= new JSONObject(result );

            client.close();

        } catch (Exception e) {

            e.printStackTrace();
            log.error(e.toString());

        }

        return jsonObject;

    }

    public JSONObject getRequest(){

        JSONObject jsonObject=null;

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet http = new HttpGet(apiEndpoint);

        ObjectMapper objectMapper = new ObjectMapper();

        try {

            String objJackson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestBodyDTO);
            StringEntity entity = new StringEntity(objJackson);

            http.setHeader("Accept", "application/json");
            http.setHeader("Content-type", "application/json");

            CloseableHttpResponse response = client.execute(http);
            HttpEntity entityResponse = response.getEntity();
            String result = EntityUtils.toString(entityResponse);
            log.debug("RESPONSE: "+ result);

            jsonObject= new JSONObject(result );

            client.close();

        } catch (Exception e) {

            e.printStackTrace();
            log.error(e.toString());

        }

        return jsonObject;

    }

}
