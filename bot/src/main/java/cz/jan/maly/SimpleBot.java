package cz.jan.maly;

import bwapi.Unit;
import cz.jan.maly.service.AgentUnitFactory;
import cz.jan.maly.service.implementation.BotFacade;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SimpleBot extends BotFacade {

    private SimpleBot() {
        super(new AgentUnitFactory());
    }

    public static void main(String[] args) throws IOException, InterruptedException, IntrospectionException {

//        for (Field f : Order.class.getDeclaredFields()) {
//            if (Modifier.isStatic(f.getModifiers())) {
//                System.out.println("public static final APlayerTypeWrapper "+f.getName()+" = new APlayerTypeWrapper(PlayerType."+f.getName()+");");
//            }
//        }

        printFields(Unit.class);

        new SimpleBot().run();
    }

    static void printFields(Class aClass) {
        List<String> methodsNames = new ArrayList<>();
        List<String> returnType = new ArrayList<>();

        // Get the public methods associated with this class.
        Method[] methods = aClass.getMethods();
        for (Method method : methods) {
            if (method.getGenericReturnType().getTypeName().equals("void") || method.getName().equals("getClass")
                    || method.getName().equals("equals") || method.getName().equals("hashCode") || method.getName().equals("toString")) {
                continue;
            }
            methodsNames.add(method.getName());
            String[] nameSpace = method.getGenericReturnType().getTypeName().split("\\.");
            if (nameSpace[nameSpace.length - 1].equals("int") || nameSpace[nameSpace.length - 1].equals("String") || nameSpace[nameSpace.length - 1].equals("boolean")) {
                returnType.add(nameSpace[nameSpace.length - 1]);
            } else {
                returnType.add("A" + nameSpace[nameSpace.length - 1]);
            }
        }

        //print fields
        for (int i = 0; i < methodsNames.size(); i++) {
            String fieldName = methodsNames.get(i).replace("get", "");
            fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1, fieldName.length());
            System.out.println("@Getter");
            System.out.println("private final " + returnType.get(i) + " " + fieldName + ";");
            System.out.println();
        }

        System.out.println("-----");

        //init fields
        for (int i = 0; i < methodsNames.size(); i++) {
            String fieldName = methodsNames.get(i).replace("get", "");
            fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1, fieldName.length());
            System.out.println("this." + fieldName + "=u." + methodsNames.get(i) + "();");
        }

        System.out.println();
    }

}