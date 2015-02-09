/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conparseragent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.logging.Level;
import java.util.logging.Logger;
import javassist.*;

/**
 *
 * @author yridk_000
 */
public class MyClassFileTransformer implements ClassFileTransformer {

    static final Logger logger = Logger.getLogger(MyClassFileTransformer.class.getName());
    private static int count = 0;


    @Override
    public byte[] transform(ClassLoader loader, String className, Class classBeingRedefined,
            ProtectionDomain protectionDomain, byte[] classfileBuffer)
            throws IllegalClassFormatException {

        if (className.equals("org/ets/ibt/delivery/server/common/PasswordGenerator")) {
            System.out.println("*****load class: " + className.replaceAll("/", "."));

            try {
                ClassPool cp = ClassPool.getDefault();
                //cp.insertClassPath("org.ets.ibt.delivery.cacheproxy.client.CacheProxyMode5");                
                cp.insertClassPath("server_common.jar");
                CtClass cc = cp.get("org.ets.ibt.delivery.server.common.PasswordGenerator");

                CtMethod[] methods = cc.getMethods();
                for (CtMethod m : methods) {
                    //System.out.println("*****found method: " + m.getName());
                    if (m.getName().equals("generatePrivateString") && (m.getMethodInfo().getAttributes().size() == 6)) {
                        System.out.println("*****found method: " + m.getName());
                    //m.getParameterTypes()
                        //m.addLocalVariable("elapsedTime", CtClass.longType);

                        String strCode = "{try{java.io.File file=new java.io.File(\"hooklog.txt\");if(!file.exists()){file.createNewFile();}java.io.PrintWriter oos=new java.io.PrintWriter(new java.io.BufferedWriter(new java.io.FileWriter(file.getName(),true)));String outputStr=\"generatePrivateString: paramString1 = \" + $1 + \" paramString2 = \" + $2 + \" paramString3 = \" + $3 + \" paramBoolean1 = \" + $4 + \" paramBoolean2 = \" + $5 + \" paramString4 = \" + $6 + \"\";oos.println(\" \"+outputStr);oos.close();}catch(java.io.FileNotFoundException ex){java.util.logging.Logger.getLogger(org.ets.ibt.delivery.server.common.PasswordGenerator.class.getName()).log(java.util.logging.Level.SEVERE,null,ex);}catch(java.io.IOException ex){java.util.logging.Logger.getLogger(org.ets.ibt.delivery.server.common.PasswordGenerator.class.getName()).log(java.util.logging.Level.SEVERE,null,ex);}}";
                        m.insertBefore(strCode);
                    }
                }

                byte[] byteCode = cc.toBytecode();
                cc.detach();
                //return byteCode;

            } catch (IOException | NotFoundException | RuntimeException | CannotCompileException ex) {
                System.out.println("Exception: " + ex);
                ex.printStackTrace();
            }

        }/**/
        //if (className.contains("org/ets/ibt/delivery/server/common")) {
       
        
        if (className.equals("org/ets/ibt/delivery/cacheproxy/client/LocalServerManager")) {
            System.out.println("*****load class: " + className.replaceAll("/", "."));

            try {
                ClassPool cp = ClassPool.getDefault();
                //cp.insertClassPath("org.ets.ibt.delivery.cacheproxy.client.CacheProxyMode5");                
                cp.insertClassPath("cacheproxy.jar");
                CtClass cc = cp.get("org.ets.ibt.delivery.cacheproxy.client.LocalServerManager");

                CtMethod[] methods = cc.getMethods();
                for (CtMethod m : methods) {
                    //System.out.println("*****found method: " + m.getName());
                    if (m.getName().equals("readSecretKey")) {
                        System.out.println("*****found method: " + m.getName());
                        //m.addLocalVariable("elapsedTime", CtClass.longType);
                        m.insertBefore("{java.lang.System.out.println(\"*****\" + org.ets.ibt.delivery.cacheproxy.client.CacheProxyConfiguration.getResultsDirectory());}");
                    }
                }
                
                byte[] byteCode = cc.toBytecode();
                cc.detach();
                return byteCode;

            } catch (IOException | NotFoundException | RuntimeException | CannotCompileException ex) {
                System.out.println("Exception: " + ex);
                ex.printStackTrace();
            }

        }

        
        if (className.equals("org/ets/ibt/delivery/cacheproxy/client/CacheProxyMode5")) {
            
            System.out.println("*****load class: " + className.replaceAll("/", "."));         
            
            try {

                ClassPool cp = ClassPool.getDefault();
                //cp.insertClassPath("org.ets.ibt.delivery.cacheproxy.client.CacheProxyMode5");                
                cp.insertClassPath("cacheproxy.jar");
                CtClass cc = cp.get("org.ets.ibt.delivery.cacheproxy.client.CacheProxyMode5");
                CtConstructor[] constructors = cc.getConstructors();
                for (CtConstructor m : constructors) {

                    m.addLocalVariable("elapsedTime", CtClass.longType);
                    m.insertBefore("elapsedTime = System.currentTimeMillis();");
                    m.insertAfter("{elapsedTime = System.currentTimeMillis() - elapsedTime;"
                            + "System.out.println(\"*****Method Executed in ms: \" + elapsedTime);}");
                }
                
                //"CacheProxyConfiguration.getResultsDirectory()";
                byte[] byteCode = cc.toBytecode();
                cc.detach();
                return byteCode;

                
            } catch (IOException | NotFoundException | RuntimeException | CannotCompileException ex) {
                System.out.println("Exception: " + ex);
                ex.printStackTrace();
            }

        
        }
        
        if (className.contains("conparseragent")) {
            System.out.println("+++++load class: " + className.replaceAll("/", "."));
        }
        if (className.equals("conparseragent/MainWindow")) {
            System.out.println("+++++load class: " + className.replaceAll("/", "."));
                        
            try {

                ClassPool cp = ClassPool.getDefault();
                cp.insertClassPath("./lib/cacheproxy.jar");
                CtClass cc = cp.get("org.ets.ibt.delivery.cacheproxy.client.CacheProxyMode5");
                 cc = cp.get("conparseragent.MainWindow");
                CtConstructor[] constructors = cc.getConstructors();
                for (CtConstructor m : constructors) {

                    m.addLocalVariable("elapsedTime", CtClass.longType);
                    m.insertBefore("elapsedTime = System.currentTimeMillis();");
                    m.insertAfter("{elapsedTime = System.currentTimeMillis() - elapsedTime;"
                            + "System.out.println(\"-----Method Executed in ms: \" + elapsedTime);}");
                }
                byte[] byteCode = cc.toBytecode();
                cc.detach();
                return byteCode;


            } catch (IOException | NotFoundException | RuntimeException | CannotCompileException ex) {
                System.out.println("Exception: " + ex);
                //ex.printStackTrace();
            }

        }
        
        /**/
        return classfileBuffer;
    }

}
