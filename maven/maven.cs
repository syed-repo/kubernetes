Install Maven
-------------
ubuntu@ip-172-31-34-214:~$ echo $JAVA_HOME
/usr/lib/jvm/java-8-openjdk-amd64/

ubuntu@ip-172-31-34-214:~$ echo $M2_HOME
/home/ubuntu/apache-maven-3.6.3
ubuntu@ip-172-31-34-214:~$

ubuntu@ip-172-31-34-214:~$ export PATH=$PATH:$M2
ubuntu@ip-172-31-34-214:~$ echo $PATH
/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games:/usr/local/games:/snap/bin:/home/ubuntu/apache-maven-3.6.3/bin
ubuntu@ip-172-31-34-214:~$

ubuntu@ip-172-31-34-214:~$ mvn -version
Apache Maven 3.6.3 (cecedd343002696d0abb50b32b541b8a6ba2883f)
Maven home: /home/ubuntu/apache-maven-3.6.3
Java version: 1.8.0_232, vendor: Private Build, runtime: /usr/lib/jvm/java-8-openjdk-amd64/jre
Default locale: en, platform encoding: UTF-8
OS name: "linux", version: "4.15.0-1051-aws", arch: "amd64", family: "unix"
ubuntu@ip-172-31-34-214:~$

mvn clean
mvn package

ubuntu@ip-172-31-34-214:~/kubernetes/maven$ tree
.
├── pom.xml
├── src
│   └── main
│       └── java
│           └── helloworld
│               └── HelloWorld.java
└── target
    ├── classes
    │   └── helloworld
    │       └── HelloWorld.class
    ├── hello-world-1.0.0.jar
    ├── maven-archiver
    │   └── pom.properties
    └── maven-status
        └── maven-compiler-plugin
            └── compile
                └── default-compile
                    ├── createdFiles.lst
                    └── inputFiles.lst

12 directories, 7 files
ubuntu@ip-172-31-34-214:~/kubernetes/maven$
