package com.cashow.annotationdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.cashow.annotationdemo.auto.MyGeneratedClass;
import com.cashow.customprocessor.MyClassAnnotation;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;

@MyClassAnnotation(
        name = "World",
        text = "Hello ")
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.text_runtime)
    TextView textRuntime;

    @BindView(R.id.text_class)
    TextView textClass;

    @MyRuntimeAnnotation(name = "lala")
    private String test;

    @MyRuntimeAnnotation(name = "lalala", age = 30)
    private String test2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        processRuntimeAnnotation();
        processClassAnnotation();
    }

    /**
     * 解析运行时的注解：MyRuntimeAnnotation
     */
    public void processRuntimeAnnotation() {
        // 获取要解析的类
        Class cls = this.getClass();
        // 拿到所有Field
        Field[] declaredFields = cls.getDeclaredFields();
        StringBuilder stringBuilder = new StringBuilder();
        for(Field field : declaredFields){
            // 获取Field上的注解
            MyRuntimeAnnotation annotation = field.getAnnotation(MyRuntimeAnnotation.class);
            if(annotation != null){
                // 获取注解值
                String name = annotation.name();
                stringBuilder.append("\nname = ").append(name).append(" age = ").append(annotation.age());
            }
        }
        textRuntime.setText(stringBuilder.toString());
    }

    /**
     * 调用编译时注解动态生成的类
     */
    public void processClassAnnotation() {
        MyGeneratedClass myGeneratedClass = new MyGeneratedClass();
        String message = myGeneratedClass.getMessage();
        textClass.setText(message);
    }
}
