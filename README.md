***
# Mongoose
![honk.png](assets/honk.png)

![Sonatype Nexus (Releases)](https://img.shields.io/nexus/r/games.negative.mongoose/mongoose?server=https%3A%2F%2Frepo.negative.games&nexusVersion=3&logo=sonatype&label=version)
![Discord](https://img.shields.io/discord/822346437240815656?logo=discord&label=discord)

**mongoose** is a simple MongoDB helper to allow you to easily connect, collect and interact with your MongoDB database. honk honk
***
# Installation Process
## Add Maven Repository
```xml
<repository>
    <id>Negative Games</id>
    <url>https://repo.negative.games/repository/maven-releases/</url>
</repository>
```

## Add Maven Dependency
```xml
<dependency>
    <groupId>games.negative.mongoose</groupId>
    <artifactId>mongoose</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```

## Add Required Dependency
```xml
<!-- MongoDB -->
<dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>mongodb-driver-sync</artifactId>
    <version>4.11.0</version>
    <scope>compile</scope>
</dependency>
```

## Optional Shading/Relocation
In case other software in your JVM is using this library,
you should shade it into your jar and relocate it to your preferred namespace using the `maven-shade-plugin`.
```xml 
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>3.5.0</version>
    <executions>
        <execution>
            <phase>package</phase>
            <goals>
                <goal>shade</goal>
            </goals>
            <configuration>
                <createDependencyReducedPom>false</createDependencyReducedPom>
                <relocations>
                    <relocation>
                        <pattern>games.negative.mongoose</pattern>
                        <shadedPattern>[YOUR NAMESPACE].mongoose</shadedPattern>
                    </relocation>
                </relocations>
            </configuration>
        </execution>
    </executions>
</plugin>
```
***
# Usage
A comprehensive example class can be found [here](https://github.com/Negative-Games/mongoose/blob/main/src/main/java/games/negative/mongoose/example/Example.java).

