package com.cdd.service;

import com.cdd.state.JavaClassesSettings;
import com.cdd.state.JavaClassesState;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.AllClassesSearch;

import java.util.*;
import java.util.stream.Collectors;

public class JavaClassesService {
    public static void run() {
//        String command = "java -verbose:class";
//
//        Process proc = null;
//        try {
//            proc = Runtime.getRuntime().exec(command);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // Read the output
//
//        assert proc != null;
//        BufferedReader reader =
//                new BufferedReader(new InputStreamReader(proc.getInputStream()));
//
//        String line = "";
//        StringBuilder output = new StringBuilder();
//        while (true) {
//            try {
//                if ((line = reader.readLine()) == null) break;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            System.out.print(line + "\n");
//            output.append(line).append("\n");
//        }
//
//        try {
//            proc.waitFor();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        saveState(getInfo(output.toString()));

    }

    public static void saveState(JavaClassesState state) {
        JavaClassesSettings.getInstance().loadState(state);
    }

    private static JavaClassesState getInfo(String rawOutput) {
        var state = new JavaClassesState();
        var version = Arrays.stream(rawOutput.toLowerCase().split("[/|\\\\]")).filter(token -> token.contains("java")).findFirst().orElse(null);
        var classes = Arrays.stream(rawOutput.replaceAll("\\[Opened |\\[Loaded ", "").split(" .*]\\n")).filter(token -> token.toLowerCase().contains("list")).collect(Collectors.toSet());

        state.setJavaVersion(version);
        state.setRawListOfClasses(rawOutput);
        state.setListOfClasses(classes);
        return state;
    }

    public static Set<String> getListOfQualifiers() {
        var state = JavaClassesSettings.getInstance().getState();
        if (state == null || state.getJavaVersion() == null) {
            run();
        }
        return JavaClassesSettings.getInstance().getState().getListOfClasses();
    }

    public static boolean isJavaClass(String classname) {
        return getListOfQualifiers().stream().anyMatch(classes -> classes.equals(classname.replace("import ", "").replace(";", "")));
    }

    public static boolean isProjectClass(String classname) {
//        AllClassesSearch.search(GlobalSearchScope.projectScope(ProjectManager.getInstance().getDefaultProject()), ProjectManager.getInstance().getDefaultProject());
        return getListOfQualifiers().stream().anyMatch(classes -> classes.equals(classname.replace("import ", "").replace(";", "").trim()));
    }
    public static boolean isSameClass(String classname) {
        return getListOfQualifiers().stream().anyMatch(classes -> classes.equals(classname));
    }

    public static void addQualifier(Set<String> qualifiers) {
        var listOfQualifiers = getListOfQualifiers();
        qualifiers.forEach(qualifier -> {
            if(!listOfQualifiers.contains(qualifier)){

                listOfQualifiers.add(qualifier);

                var state = new JavaClassesState();
                state.setListOfClasses(listOfQualifiers);

                saveState(state);
            }

        });
    }
}
