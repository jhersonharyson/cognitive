package com.cdd.service;

import com.cdd.model.CddMetrics;
import com.cdd.model.Rule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.psi.PsiManager;

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
            "      \"name\": \"TRY_STATEMENT\",\n" +
            "      \"cost\": 1\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"ANNOTATION\",\n" +
            "      \"cost\": 7\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"FOR_STATEMENT\",\n" +
            "      \"cost\": 3\n" +
            "    }\n" +
            "  ]\n" +
            "}";
    final private String RULES_FILE = "/rules.json";

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
            obj = mapper.readValue(this.getJson(), CddMetrics.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return Objects.requireNonNull(obj);
    }

    private String getJson(){
        try{
            var url = (ProjectManager.getInstance().getOpenProjects()[0].getBasePath() + RULES_FILE);
            var file = LocalFileSystem.getInstance().findFileByPath(url);
            if(file == null)
                return "{}";
            return Objects.requireNonNull(PsiManager.getInstance(ProjectManager.getInstance().getDefaultProject()).findFile(file)).getText();
        }catch (Exception e){
            return "{}";
        }

    }

}
