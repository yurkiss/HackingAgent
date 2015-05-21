#HackingAgent

This source code makes injection into another application using Javassist and java.lang.instrument packages. I use JavaAgent because destination jars were digitally signed.
Before that, App parses WebStart JNLP file, downloads jars from the internet and starts destination application with -javaagent and other attributes from JNLP.
