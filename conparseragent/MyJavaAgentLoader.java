/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conparseragent;

/**
 *
 * @author yridk_000
 */
import com.sun.tools.attach.VirtualMachine;
import java.lang.management.ManagementFactory;

import java.util.logging.Logger;

public class MyJavaAgentLoader {

    static final Logger logger = Logger.getLogger(MyJavaAgentLoader.class.getName());

    private static final String jarFilePath = "d:\\Dropbox\\Java\\conParserAgent\\dist\\conParserAgent.jar";

    public static void loadAgent() {
        logger.info("dynamically loading javaagent");
        String nameOfRunningVM = ManagementFactory.getRuntimeMXBean().getName();
        int p = nameOfRunningVM.indexOf('@');
        String pid = nameOfRunningVM.substring(0, p);
        //pid = "3608";
        try {
            VirtualMachine vm = VirtualMachine.attach(pid);
            vm.loadAgent(jarFilePath, "");
            vm.detach();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}