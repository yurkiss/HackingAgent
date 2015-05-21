#HackingAgent

This source code makes injection into another application using Javassist and java.lang.instrument packages. I use JavaAgent because destination jars were digitally signed.
Before that App parse web start JNLP file, download jars from the internet and starts destination application with -javaagent and other attributes from JNLP.
