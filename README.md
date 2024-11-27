<div align= "center">
    <img src="https://github.com/Spatio-Temporal-Lab/start-db-doc/blob/main/icon/logo.png?raw=true" alt="">
</div>

<div align= "center">

# START-DB

---

**English** | [中文]() · [Changelog]() · [Doc](https://github.com/Spatio-Temporal-Lab/start-db-doc) · [Report Bug](https://github.com/Spatio-Temporal-Lab/start-db/issues) · [PR](https://github.com/Spatio-Temporal-Lab/start-db/pulls) 

[![License](https://img.shields.io/badge/license-GPLv3-green)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Java support](https://img.shields.io/badge/java-8-green)](https://openjdk.java.net/)
[![GitHub Stars](https://img.shields.io/github/stars/Spatio-Temporal-Lab/start-db)](https://github.com/chat2db/Chat2DB/stargazers)
[![GitHub Forks](https://img.shields.io/github/forks/Spatio-Temporal-Lab/start-db)](https://github.com/chat2db/Chat2DB/fork)
[![GitHub Contributors](https://img.shields.io/github/contributors/Spatio-Temporal-Lab/start-db)](https://github.com/chat2db/Chat2DB/graphs/contributors)

</div>

### Features

TODO

## Building START-DB from Source

---
### For Window Users

1. Download and install **Docker Desktop**

   > Here is the official link: [Docker Desktop](https://www.docker.com/products/docker-desktop/)
   
   If the installWizard tells you to enable WSL, please click "Yes" and continue.

   > About how to enable WSL manually: [Windows Subsystem for Linux Documentation](https://learn.microsoft.com/en-US/windows/wsl/)

2. Install the latest version of **IntelliJ IDEA**

   > Here is the official link: [IntelliJ IDEA](https://www.jetbrains.com/idea/)

3. Download and install **OracleJDK 1.8 (JAVA 8)**

   > Here is the official link: [OracleJDK](https://www.oracle.com/java/technologies/downloads/)

   On Windows, there is no special steps, just click "Next" and wait for it to be done.
   Sometimes, the installer will not add JDK to PATH variables automatically due to certain reasons, which means you have to do it by yourself.

   If you are not sure whether the installer had done that, open the Command Prompt and input: `java -version`

   The output message should be like:

   ```text
   java version "1.8.0_351"
   Java(TM) SE Runtime Environment (build 1.8.0_351-b10)
   Java HotSpot(TM) 64-Bit Server VM (build 25.351-b10, mixed mode)
   ```

   If not, go to the Advanced system settings and click "Environment Variables" to add the JDK to PATH variables.
4. Clone code
    ```shell
    git@github.com:Spatio-Temporal-Lab/start-db.git
    ```
5. Open **start-db** as a project using **IntelliJ IDEA**, and set java(1.8) and scala(2.12.8) environment.

6. Open the terminal in **IntelliJ IDEA** and run the following command:
    ```shell
    cd docker
    cd new-all 
    docker-compose up -d mysql-local geomesa-hbase-local
    ```

7. Add two lines in your hosts file:
   ```
   127.0.0.1 mysql-local
   127.0.0.1 geomesa-hbase-local
   ```
8. Click "Maven > cupid-db > Lifecycle > package(Skip Tests)"in **IntelliJ IDEA** and packaging will begin.

## Starting START-DB Server

---