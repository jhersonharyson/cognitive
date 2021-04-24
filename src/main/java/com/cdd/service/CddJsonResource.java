package com.cdd.service;

import com.cdd.model.CddMetrics;
import com.cdd.model.Rule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;
import java.util.Set;

public class CddJsonResource implements CddResource {
    private String jsonString = "{\n" +
            "  \"limitOfComplexity\": 7,\n" +
            "  \"metricsExtends\" : [\n" +
            "    {\n" +
            "      \"file\": \"utils.js\",\n" +
            "      \"extend\": -2\n" +
            "    }\n" +
            "  ],\n" +
            "  \"rules\": [\n" +
            "    {\n" +
            "      \"name\": \"IF_STATEMENT\",\n" +
            "      \"cost\": 1\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"BLOCK_STATEMENT\",\n" +
            "      \"cost\": 1\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"FOR_STATEMENT\",\n" +
            "      \"cost\": 3\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    @Override
    public Set<Rule> loadMetrics() {
       return this.loadJson().getRules();
    }

    public int limitOfComplexity(){
        return  this.loadJson().getLimitOfComplexity();
    }

    private CddMetrics loadJson(){
        ObjectMapper mapper = new ObjectMapper();

        CddMetrics obj = null;
        try {
            obj = mapper.readValue(this.jsonString, CddMetrics.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return Objects.requireNonNull(obj);
    }

}
