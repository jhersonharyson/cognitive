package com.cdd.service;

import com.cdd.model.CddMetrics;
import com.cdd.model.Rule;
import com.cdd.model.RuleStatementMapper;
import com.cdd.model.Statement;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.psi.PsiManager;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.velocity.exception.ResourceNotFoundException;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CddJsonResourceService implements CddResource {

    @Override
    public Set<Rule> loadMetrics() {
        return this.loadJson().getRules();
    }

    public int limitOfComplexity() {
        return this.loadJson().getLimitOfComplexity();
    }

    private CddMetrics loadJson() {
        ObjectMapper mapper = new ObjectMapper();

        CddMetrics obj = null;
        try {
            obj = mapper.readValue(this.getJson(), CddMetrics.class);
            obj.setRules(mapperFriendlyLabelsToRules(obj.getRules()));
        } catch (JsonProcessingException e) {
            System.out.println("json exception");
        } catch (Exception e){
            System.out.println("Exception "+ e);
        }

        if(ObjectUtils.isEmpty(obj)){
            throw new ResourceNotFoundException("resource not found !");
        }

        return obj;
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
