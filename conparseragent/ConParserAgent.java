/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conparseragent;

import java.lang.instrument.Instrumentation;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;

/**
 *
 * @author yridk_000
 */
public class ConParserAgent {

    /**
     * @param args the command line arguments
     * @param inst
     */
    
    public static void premain(String args, Instrumentation inst) {
        System.out.println("Hello! I`m java agent");

        Handler ch = new ConsoleHandler();
        java.util.logging.Logger.getLogger(ConParserAgent.class.getName()).addHandler(ch);
        java.util.logging.Logger.getLogger(ConParserAgent.class.getName()).log(java.util.logging.Level.SEVERE, "Hello");
        
        try {
            java.io.File file = new java.io.File("C:\\AMD\\hooklog.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            java.text.SimpleDateFormat lSDateFormat = new java.text.SimpleDateFormat("yyyyMMdd HH:mm:ss");
            java.io.PrintWriter oos = new java.io.PrintWriter(new java.io.BufferedWriter(new java.io.FileWriter(file.getName(), true)));
            String outputStr = "generatePrivateString: paramString1 = \" + $1 + \" paramString2 = \" + $2 + \" paramString3 = \" + $3 + \" paramBoolean1 = \" + $4 + \" paramBoolean2 = \" + $5 + \" paramString4 = \" + $6 + \"";
            oos.println(lSDateFormat.format(new java.util.Date().getTime()) + " " + outputStr);
            oos.close();

        } catch (java.io.FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(ConParserAgent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (java.io.IOException ex) {
            java.util.logging.Logger.getLogger(ConParserAgent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }/**/
        System.out.println("Bye! I`m java agent");
    }
    
}
