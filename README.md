# interp4j maven plugin

This is a maven plugin to support [interp4j library](https://github.com/KhadanovichSergey/interp4j)
for maven-based projects.

## How to use it?

***

To interpolate strings in your project during compile time, you have to declare it inside `build` tag in the `pom.xml`
file of your project. Attach `interpolate` goal to `generate-sources` phase of the maven lifecycle.

```xml

<plugin>
    <groupId>dev.khbd.interp4j</groupId>
    <artifactId>interp4j-maven-plugin</artifactId>
    <version>LATEST_VERSION</version>
    <executions>
        <!-- interpolate strings in production files -->
        <execution>
            <id>interpolate</id>
            <phase>generate-sources</phase>
            <goals>
                <goal>interpolate</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

Consequently, when `mvn clean compile` is run on your project, the source code is first rewritten
by [interp4j](https://github.com/KhadanovichSergey/interp4j) before compilation.

## How to interpolate strings in tests?

***

To interpolate strings in your test files, add another execution block and attach `test-interpolate` goal
to `generate-test-sources` phase of the maven lifecycle.

```xml

<plugin>
    ...
    <!-- interpolate strings in test files -->
    <execution>
        <id>interpolate-test</id>
        <phase>generate-test-sources</phase>
        <goals>
            <goal>test-interpolate</goal>
        </goals>
    </execution>
</plugin>
```