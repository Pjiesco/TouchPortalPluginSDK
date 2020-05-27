package com.github.ChristopheCVB.TouchPortal.AnnotationsProcessor;

import com.github.ChristopheCVB.TouchPortal.Annotations.TouchPortalPluginAnnotations;
import com.google.auto.service.AutoService;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashSet;
import java.util.Set;

@AutoService(Processor.class)
public class TouchPortalPluginAnnotationProcessor extends AbstractProcessor {
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.filer = processingEnv.getFiler();
        this.messager = processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(TouchPortalPluginAnnotations.Action.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment env) {
        this.messager.printMessage(Diagnostic.Kind.NOTE, this.getClass().getSimpleName()+".process");
        try {
            for (TypeElement typeElement : set) {
                this.messager.printMessage(Diagnostic.Kind.NOTE, typeElement.getSimpleName());
                for (Element element : env.getElementsAnnotatedWith(typeElement)) {
                    this.messager.printMessage(Diagnostic.Kind.NOTE, element.getSimpleName());
                    TouchPortalPluginAnnotations.Action action = element.getAnnotation(TouchPortalPluginAnnotations.Action.class);
                    this.messager.printMessage(Diagnostic.Kind.NOTE, action.id());
                    String actionFileName = "resources/action_" + action.id() + ".tpa";
                    FileObject actionFileObject = this.filer.createResource(StandardLocation.SOURCE_OUTPUT, "", actionFileName, element);
                    JSONObject jsonAction = new JSONObject();
                    jsonAction.put("name", action.name());
                    jsonAction.put("id", action.id());
                    Writer writer = actionFileObject.openWriter();
                    writer.write(jsonAction.toString());
                    writer.flush();
                    writer.close();
                }
            }
        } catch (IOException | JSONException ioException) {
            ioException.printStackTrace();
        }
        return true;
    }
}
