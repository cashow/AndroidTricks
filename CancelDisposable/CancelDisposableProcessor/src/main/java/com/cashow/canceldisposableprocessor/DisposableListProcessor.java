package com.cashow.canceldisposableprocessor;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.cashow.canceldisposableprocessor.DisposableList")
public class DisposableListProcessor extends AbstractProcessor {
    private Types mTypeUtils;
    private Elements mElementUtils;
    private Filer mFiler;
    private Messager mMessager;

    /**
     * 每一个注解处理器类都必须有一个空的构造函数。然而，这里有一个特殊的init()方法，它会被注解处理工具调用，并输入ProcessingEnviroment参数。ProcessingEnviroment提供很多有用的工具类Elements,Types和Filer。
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        // 初始化我们需要的基础工具
        mTypeUtils = processingEnv.getTypeUtils();
        mElementUtils = processingEnv.getElementUtils();
        mFiler = processingEnv.getFiler();
        mMessager = processingEnv.getMessager();
    }

    /**
     * 这相当于每个处理器的主函数main()。你在这里写你的扫描、评估和处理注解的代码，以及生成Java文件。输入参数RoundEnviroment，可以让你查询出包含特定注解的被注解元素。
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set == null || set.isEmpty()) {
            info(">>> set is null... <<<");
            return true;
        }

        info(">>> Found field, start... <<<");

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(DisposableList.class);

        if (elements == null || elements.isEmpty()) {
            info(">>> elements is null... <<<");
            return true;
        }

        // 遍历所有被注解了 @DisposableList 的元素
        for (Element annotatedElement : elements) {

            // 检查被注解为 @DisposableList 的元素是否是一个类
            if (annotatedElement.getKind() != ElementKind.CLASS) {
                error(annotatedElement, "Only classes can be annotated with @%s",
                        DisposableList.class.getSimpleName());
                return true; // 退出处理
            }

            analysisAnnotated(annotatedElement);
        }

        return true;
    }

    /**
     * 用来指定你使用的Java版本。通常这里返回SourceVersion.latestSupported()。然而，如果你有足够的理由只支持Java 7的话，你也可以返回SourceVersion.RELEASE_7。我推荐你使用前者。
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void analysisAnnotated(Element classElement) {
        String elementClassName = classElement.getSimpleName().toString();
        String elementLowerClassName = getClassVariableName(elementClassName);
        String generateClassName = elementClassName + "DisposableList";

        info("getReflectionField");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("package %s;\n\n" +
                "import io.reactivex.disposables.Disposable;\n\n" +
                "public class %s {\n" +
                "    public static void cancelAll(%s %s) {\n", getPackageName(classElement), generateClassName, elementClassName, elementLowerClassName));

        TypeElement typeElement = (TypeElement) classElement;

        for (String variableName : getVariableNameList(typeElement)) {
            stringBuilder.append(String.format("        cancalDisposable(%s.%s);\n", elementLowerClassName, variableName));
        }

        stringBuilder.append(
                "    }\n" +
                "\n" +
                "    private static void cancalDisposable(Object object) {\n" +
                "        if (!(object instanceof Disposable)) {\n" +
                "            return;\n" +
                "        }\n" +
                "        Disposable disposable = (Disposable) object;\n" +
                "        if (disposable != null && !disposable.isDisposed()) {\n" +
                "            disposable.dispose();\n" +
                "        }\n" +
                "    }\n" +
                "}");

        try { // write the file
            JavaFileObject source = mFiler.createSourceFile(getPackageName(classElement) + "." + generateClassName);
            Writer writer = source.openWriter();
            writer.write(stringBuilder.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            // Note: calling e.printStackTrace() will print IO errors
            // that occur from the file already existing after its first run, this is normal
        }
        info(">>> analysisAnnotated is finish... <<<");
    }

    /**
     * 返回 TypeElement 里不是 private 和 static 的 Element 的名称列表
     */
    private List<String> getVariableNameList(TypeElement typeElement) {
        List<String> variableNameList = new ArrayList<>();
        for (Element element : typeElement.getEnclosedElements()) {
            if (element.getKind() == ElementKind.FIELD) {
                VariableElement variableElement = (VariableElement) element;
                // 忽略掉不是 Disposable 的 Element
                if (!variableElement.asType().toString().equals("io.reactivex.disposables.Disposable")) {
                    continue;
                }

                // 如果有 private 或者 static 的 Disposable，报错
                if (element.getModifiers().contains(Modifier.PRIVATE) || element.getModifiers().contains(Modifier.STATIC)) {
                    error(typeElement, "Disposable %s cannot be private or static",
                            element.getSimpleName());
                }

                String variableName = element.getSimpleName().toString();
                variableNameList.add(variableName);
            }
        }
        return variableNameList;
    }

    /**
     * 将类名的首字母设成小写
     */
    private String getClassVariableName(String elementClassName) {
        char c[] = elementClassName.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        elementClassName = new String(c);
        return elementClassName;
    }

    /**
     * 获取元素的包名
     */
    private String getPackageName(Element classElement) {
        return mElementUtils.getPackageOf(classElement).getQualifiedName().toString();
    }

    private void error(Element e, String msg, Object... args) {
        mMessager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(msg, args),
                e);
    }

    private void info(String msg, Object... args) {
        mMessager.printMessage(
                Diagnostic.Kind.NOTE,
                String.format(msg, args));
    }
}
