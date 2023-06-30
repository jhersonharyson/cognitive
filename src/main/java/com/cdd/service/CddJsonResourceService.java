package com.cdd.service;

import com.cdd.model.CddMetrics;
import com.cdd.model.Rule;
import com.cdd.model.RuleStatementMapper;
import com.example.demo.utils.RealtimeState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.psi.PsiManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.ehcache.Cache;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class CddJsonResourceService implements CddResource {

    private static final String JSON_RESOURCE_CACHE_KEY = "jsonResource";
    private final Cache<String, Object> cache = CacheManagerService.getCache(JSON_RESOURCE_CACHE_KEY);

    @Override
    public Set<Rule> loadMetrics() {
        return this.loadJson().getRules();
    }

    public int limitOfComplexity() {
        return this.loadJson().getLimitOfComplexity();
    }

    private CddMetrics loadJson() {
        Object cached = cache.get(JSON_RESOURCE_CACHE_KEY);

        if(ObjectUtils.isNotEmpty(cached)){
            return (CddMetrics) cached;
        }

        ObjectMapper mapper = new ObjectMapper();
        var metrics = RealtimeState.getInstance().getCddMetrics();
        if(ObjectUtils.isNotEmpty(metrics)){
            log.info("metrics for realtime");
            return metrics;
        }
        log.info("metrics for file ");

        CddMetrics cddMetrics = null;
        try {
            cddMetrics = mapper.readValue(this.getJson(), CddMetrics.class);
            cddMetrics.setRules(mapperFriendlyLabelsToRules(cddMetrics.getRules()));
        } catch (JsonProcessingException e) {
            log.info("json exception");
        } catch (Exception e){
            log.info("Exception "+ e);
        }

        if(ObjectUtils.isEmpty(cddMetrics)){
            throw new ResourceNotFoundException("resource not found !");
        }
        cache.put(JSON_RESOURCE_CACHE_KEY, cddMetrics);
        return cddMetrics;
    }

    private Set<Rule> mapperFriendlyLabelsToRules(Set<Rule> rules) {
        if (rules != null) {
            return rules.stream().peek(rule -> {
                var statement = RuleStatementMapper.getByParameter(rule.getName());
                if(statement == null) return;
                rule.setName(statement.getStatement().name());
            }).filter(Objects::nonNull).collect(Collectors.toSet());
        }
        return null;
    }

    private String getJson() {
        try {
            String RULES_FILE = "/rules.json";
            var url = (ProjectManager.getInstance().getOpenProjects()[0].getBasePath() + RULES_FILE);
            var file = LocalFileSystem.getInstance().findFileByPath(url);
            if (file == null)
                return "{}";

//            DataContext dataContext = DataManager.getInstance().getDataContext();
            Project project = ProjectManager.getInstance().getOpenProjects()[0];//(Project) dataContext.getData(DataConstants.PROJECT);
            assert project != null;
            var psiFile = PsiManager.getInstance(project).findFile(file);
            return Objects.requireNonNull(psiFile).getText();
        } catch (Exception e) {
            return "{}";
        }

    }

}
