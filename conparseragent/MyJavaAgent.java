/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conparseragent;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.instrument.*;

public class MyJavaAgent {

    static final Logger logger = Logger.getLogger(MyJavaAgent.class.getName());

    private static Instrumentation instrumentation;

    /**
     * JVM hook to statically load the javaagent at startup.
     * 
     * After the Java Virtual Machine (JVM) has initialized, the premain method
     * will be called. Then the real application main method will be called.
     * 
     * @param args
     * @param inst
     * @throws Exception
     */
    public static void premain(String args, Instrumentation inst) throws Exception {
        logger.log(Level.INFO, "premain method invoked with args: {0} and inst: {1}", new Object[]{args, inst});
        instrumentation = inst;
        instrumentation.addTransformer(new MyClassFileTransformer());
        
        /*
        Class[] classes = inst.getAllLoadedClasses(); // Получаем список уже загруженных классов, которые могут быть изменены. Классы, которые ещё не загружены, будут изменены при загрузке
        ArrayList<Class> classList = new ArrayList<Class>();
        for (int i = 0; i < classes.length; i++) {
            if (inst.isModifiableClass(classes[i])) { // Если класс можно изменить, добавляем его в список
                classList.add(classes[i]);
            }
        }

        // Reload classes, if possible.
        Class[] workaround = new Class[classList.size()];
        try {
            inst.retransformClasses(classList.toArray(workaround)); // Запускаем процесс трансформации
        } catch (UnmodifiableClassException e) {
            System.err.println("MainClass was unable to retransform early loaded classes: " + e);
        }
        */
    }

    /**
     * JVM hook to dynamically load javaagent at runtime.
     * 
     * The agent class may have an agentmain method for use when the agent is
     * started after VM startup.
     * 
     * @param args
     * @param inst
     * @throws Exception
     */
    public static void agentmain(String args, Instrumentation inst) throws Exception {
        logger.log(Level.INFO, "agentmain method invoked with args: {0} and inst: {1}", new Object[]{args, inst});
        instrumentation = inst;
        instrumentation.addTransformer(new MyClassFileTransformer());
    }

    /**
     * Programmatic hook to dynamically load javaagent at runtime.
     */
    public static void initialize() {
        if (instrumentation == null) {
            MyJavaAgentLoader.loadAgent();
        }
    }

    public static String getResultsDirectory() {
        System.out.println("*********************************************");
        System.out.println("***************** YES ***********************");
        System.out.println("*********************************************");
        return "D:\\";
    }

    
}