package com.cashow.reflectdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getReflectionConstructors();

        getReflectionMethod();
        getReflectionDeclaredMethod();

        getReflectionField();
        getReflectionDeclaredField();

        loadPrivateMethod();
    }

    private void getReflectionConstructors() {
        MLog.d();
        // 获取 TestUser 的 Class
        Class mClass = TestUser.class;
        // 获取 TestUser 的 className
        String className = mClass.getName();

        try {
            // 获取 TestUser.class 的所有构造函数
            Constructor[] declaredConstructors = mClass.getDeclaredConstructors();
            for (Constructor constructor : declaredConstructors) {
                // 构造函数的修饰域（public, private, protected）
                int modifier = constructor.getModifiers();
                String modifierName = Modifier.toString(modifier);

                StringBuilder constructorStr = new StringBuilder();
                constructorStr.append(modifierName).append(" ").append(className).append("(");

                // 构造函数里所有参数的 Class
                Class[] parameterTypes = constructor.getParameterTypes();
                for (int i = 0; i < parameterTypes.length; i ++) {
                    Class parameterType = parameterTypes[i];
                    constructorStr.append(parameterType.getName());
                    if (i != parameterTypes.length - 1) {
                        constructorStr.append(", ");
                    }
                }
                constructorStr.append(")");

                MLog.d(constructorStr.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getReflectionMethod() {
        MLog.d();
        // 获取 TestUser 的 Class
        Class mClass = TestUser.class;

        try {
            Method[] methods = mClass.getMethods();
            for (Method method : methods) {
                // 方法的修饰域（public, private, protected）
                int modifier = method.getModifiers();
                String modifierName = Modifier.toString(modifier);

                StringBuilder constructorStr = new StringBuilder();
                constructorStr.append(modifierName).append(" ").append(method.getName()).append("(");

                // 方法里所有参数的 Class
                Class[] parameterTypes = method.getParameterTypes();
                for (int i = 0; i < parameterTypes.length; i ++) {
                    Class parameterType = parameterTypes[i];
                    constructorStr.append(parameterType.getName());
                    if (i != parameterTypes.length - 1) {
                        constructorStr.append(", ");
                    }
                }
                constructorStr.append(")");

                MLog.d(constructorStr.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getReflectionDeclaredMethod() {
        MLog.d();
        // 获取 TestUser 的 Class
        Class mClass = TestUser.class;

        try {
            Method[] methods = mClass.getDeclaredMethods();
            for (Method method : methods) {
                // 方法的修饰域（public, private, protected）
                int modifier = method.getModifiers();
                String modifierName = Modifier.toString(modifier);

                StringBuilder constructorStr = new StringBuilder();
                constructorStr.append(modifierName).append(" ").append(method.getName()).append("(");

                // 方法里所有参数的 Class
                Class[] parameterTypes = method.getParameterTypes();
                for (int i = 0; i < parameterTypes.length; i ++) {
                    Class parameterType = parameterTypes[i];
                    constructorStr.append(parameterType.getName());
                    if (i != parameterTypes.length - 1) {
                        constructorStr.append(", ");
                    }
                }
                constructorStr.append(")");

                MLog.d(constructorStr.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getReflectionField() {
        MLog.d();
        TestUser testUser = new TestUser(14, "userName");
        // 获取 TestUser 的 Class
        Class mClass = testUser.getClass();

        try {
            Field[] fields = mClass.getFields();
            for (Field field : fields) {
                // 可以通过 Field.getType() 方法取得字段类型（String, int 等等)
                Class fieldClass = field.getType();

                // 修饰域（public, private, protected）
                int modifier = field.getModifiers();
                String modifierName = Modifier.toString(modifier);

                field.setAccessible(true);

                Object value = field.get(testUser);

                if (value == null) {
                    MLog.d(modifierName + " " + fieldClass + " " + field.getName());
                } else {
                    MLog.d(modifierName + " " + fieldClass + " " + field.getName() + " = " + value.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getReflectionDeclaredField() {
        MLog.d();
        TestUser testUser = new TestUser(14, "userName");
        // 获取 TestUser 的 Class
        Class mClass = testUser.getClass();

        try {
            Field[] fields = mClass.getDeclaredFields();
            for (Field field : fields) {
                // 可以通过 Field.getType() 方法取得字段类型（String, int 等等)
                Class fieldClass = field.getType();

                // 修饰域（public, private, protected）
                int modifier = field.getModifiers();
                String modifierName = Modifier.toString(modifier);

                field.setAccessible(true);

                Object value = field.get(testUser);

                if (value == null) {
                    MLog.d(modifierName + " " + fieldClass + " " + field.getName());
                } else {
                    MLog.d(modifierName + " " + fieldClass + " " + field.getName() + " = " + value.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPrivateMethod() {
        MLog.d();
        TestUser testUser = new TestUser(15, "asdd");
        Class mClass = testUser.getClass();

        try {
            Method method = mClass.getDeclaredMethod("testPrivateUserDesc", String.class);
            method.setAccessible(true);
            String methodReturnValue = (String) method.invoke(testUser, "_suffix_str");
            MLog.d("methodReturnValue = " + methodReturnValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
