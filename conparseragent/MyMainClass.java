/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conparseragent;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;
import java.util.logging.*;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 *
 * @author yridk_000
 */
public class MyMainClass {
    
    static final Logger logger = Logger.getLogger(MyMainClass.class.getName());

    //java -javaagent:D:\Dropbox\Java\conParserAgent\dist\conParserAgent.jar -jar netx-no-debug.jar -jnlp dstApp.jnlp
    
    static {
        //MyJavaAgent.initialize();
    }

    
     public boolean keyFileForToday() {
         System.out.println("*********************************************");
         System.out.println("***************** YES ***********************");
         System.out.println("*********************************************");
        return true;
    }


     
    
    /**
     * Main method.
     *
     * @param args
     */
    public static void main(String[] args) {
        logger.info("main method invoked with args: {}");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        
        EventQueue.invokeLater(new Runnable() {
            
            @Override
            
            public void run() {
                JFrame mw = new MainWindow();
                mw.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mw.setVisible(true);
            }
        });
        
    }
    
}

class MainWindow extends JFrame {
    
    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 400;
    
    private final JTextArea statusLine;
    private final JProgressBar progressBar;
    
    public MainWindow() {
        setTitle("Java Agent Loader");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        
        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open");
        openItem.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                openFile();                
            }
        });
        
        JMenuItem getMethodsItem = new JMenuItem("Extract native libs");
        getMethodsItem.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                extractNativeLibs();                
            }
        });
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        fileMenu.add(openItem);
        fileMenu.add(getMethodsItem);
        fileMenu.add(exitItem);
        
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        
        setJMenuBar(menuBar);
        
        statusLine = new JTextArea();
        statusLine.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(statusLine);
        add(scrollPane, BorderLayout.CENTER);
                
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        add(progressBar, BorderLayout.SOUTH); 

        
    }
    
    public void openFile() {
        
        JFileChooser chooser = new JFileChooser(new File("."));
        chooser.setFileFilter(new FileFilter() {
            
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".jnlp");
            }
            
            @Override
            public String getDescription() {
                return "JNLP files";
            }
        });
        
        int r = chooser.showOpenDialog(this);
        if (r != JFileChooser.APPROVE_OPTION) {
            return;
        }
        final File file = chooser.getSelectedFile();
        
        JNLPDocParser jnlpParser = new JNLPDocParser(file);
        jnlpParser.addPropertyChangeListener(
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if ("progress".equals(evt.getPropertyName())) {
                            progressBar.setValue((Integer) evt.getNewValue());                            
                        }
                    }
                });
        jnlpParser.execute();
    }
    
    
    public void extractNativeLibs() {

        /*try {
         java.io.File file = new java.io.File("hooklog.txt");
         if (!file.exists()) {
         file.createNewFile();
         }
         java.text.SimpleDateFormat lSDateFormat = new java.text.SimpleDateFormat("yyyyMMdd HH:mm:ss");
         java.io.PrintWriter oos = new java.io.PrintWriter(new java.io.BufferedWriter(new java.io.FileWriter(file.getName(), true)));
         String outputStr = "generatePrivateString: paramString1 = \" + $1 + \" paramString2 = \" + $2 + \" paramString3 = \" + $3 + \" paramBoolean1 = \" + $4 + \" paramBoolean2 = \" + $5 + \" paramString4 = \" + $6 + \"";
         oos.println(lSDateFormat.format(new java.util.Date().getTime()) + " " + outputStr);
         oos.close();
            
         } catch (java.io.FileNotFoundException ex) {
         java.util.logging.Logger.getLogger(this.getClass().getName()).log(java.util.logging.Level.SEVERE, null, ex);
         } catch (java.io.IOException ex) {
         java.util.logging.Logger.getLogger(this.getClass().getName()).log(java.util.logging.Level.SEVERE, null, ex);
         }/**/

        try {
            String jarFile = "ibt_wnative.jar";
            String destDir = "";
            java.util.jar.JarFile jar = new java.util.jar.JarFile(jarFile);
            java.util.Enumeration enumEntries = jar.entries();
            while (enumEntries.hasMoreElements()) {
                java.util.jar.JarEntry file = (java.util.jar.JarEntry) enumEntries.nextElement();
                java.io.File f = new java.io.File(destDir + java.io.File.separator + file.getName());
                if (file.isDirectory()) { // if its a directory, create it
                    f.mkdir();
                    continue;
                }
                java.io.InputStream is = jar.getInputStream(file); // get the input stream
                java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
                while (is.available() > 0) {  // write contents of 'is' to 'fos'
                    fos.write(is.read());
                }
                fos.close();
                is.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


    class JNLPDocParser extends SwingWorker<String, String> {
        
        private Document doc;
        private DocumentBuilder builder;
        final Logger logger = Logger.getLogger(MyMainClass.class.getName());
        
        public String appMainClass;
        public ArrayList<String> argsArray;
        public HashMap<String, String> jarFiles = new HashMap<>();
        private final File file;
        
        public JNLPDocParser(File f) {
            
            this.file = f;
            
        }
        
        public void downloadJARs() {
            
            String sDir = ""; //"./ibt/";
            
            try {
                
                int i = 0;
                for (Entry<String, String> entry : jarFiles.entrySet()) {
                    String fileName = entry.getKey();
                    String fileURL = entry.getValue();
                                        
                    File newFile = new File(sDir + fileName);
                    if (!newFile.exists()) {
                        URL website = new URL(fileURL);
                        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                        FileOutputStream fos = new FileOutputStream(sDir + fileName);
                        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                        fos.close();
                        statusLine.append(fileName + " downloaded.\n\r");
                    } else {
                        statusLine.append(fileName + " already exists.\n\r");
                    }
                    
                    setProgress(100 * ++i / jarFiles.size());
                }
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
        
        public void startApp() {
            if (!appMainClass.isEmpty()) {
                StringBuilder sCmd = new StringBuilder();
                
                sCmd.append("java -javaagent:conParserAgent.jar -classpath ");
                
                for (Entry<String, String> entry : jarFiles.entrySet()) {
                    sCmd.append("" + entry.getKey() + ";");                    
                }
                
                sCmd.append(" org.ets.ibt.delivery.cacheproxy.client.CacheProxyMode5");
                                                
                for(int i = 0; i < argsArray.size(); i++ ){
                    sCmd.append(" \"" + argsArray.get(i) + "\"");
                }
                //sCmd.append("\n\r");
               
                statusLine.append(sCmd.toString());
                
                Runtime runTime = Runtime.getRuntime();
                try {
                    String line;
                    Process process = runTime.exec(sCmd.toString());
                    BufferedReader bri = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    BufferedReader bre = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    while ((line = bri.readLine()) != null) {
                        System.out.println(line);
                    }
                    bri.close();
                    while ((line = bre.readLine()) != null) {
                        System.out.println(line);
                    }
                    bre.close();

                } catch (IOException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
                
            }
            
        }
        
        @Override
        protected String doInBackground() throws Exception {
            if (builder == null) {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                builder = factory.newDocumentBuilder();
            }
            try {
                
                doc = builder.parse(file);
                
                //publish(file.toString());
                Element root = doc.getDocumentElement();
                Element appDesc = (Element) root.getElementsByTagName("application-desc").item(0);
                appMainClass = appDesc.getAttribute("main-class");

                //Arguments
                argsArray = new ArrayList<>();
                NodeList arguments = appDesc.getElementsByTagName("argument");
                for (int i = 0; i < arguments.getLength(); i++) {
                    Node item = arguments.item(i);
                    if (item instanceof Element) {
                        Element argument = (Element) item;
                        Text textNode = (Text) argument.getFirstChild();
                        argsArray.add(textNode.getData().trim());
                        logger.info(textNode.getData().trim());
                    }
                }

                //JARs
                Element resources = (Element) root.getElementsByTagName("resources").item(0);
                NodeList jars = resources.getElementsByTagName("jar");
                for (int i = 0; i < jars.getLength(); i++) {
                    Node item = jars.item(i);
                    if (item instanceof Element) {
                        Element jarElem = (Element) item;
                        String fileURL = jarElem.getAttribute("href").trim();
                        String[] arrName = fileURL.split("/");
                        String fileName = arrName[arrName.length - 1].trim();
                        jarFiles.put(fileName, fileURL);
                        logger.info(fileURL);
                        
                    }
                }
                
            } catch (Exception ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            
            downloadJARs();
            startApp();
            return "";            
        }
        
        @Override
        protected void done() {
            
            try {
                String result = get();
                
            } catch (InterruptedException | ExecutionException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
        
        
        /*
        @Override
        protected void process(List<String> data1) {
            statusLine.setText(data);
            
        }*/
        
    }
}
