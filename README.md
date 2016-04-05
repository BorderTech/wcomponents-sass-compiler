# wcomponents-sass-compiler

Thin wrapper around [Vaadin Sass Compiler](https://github.com/vaadin/sass-compiler) in order to:

1. Add an Ant task (which greatly improves performance over the Ant `java` task).
2. Support input/output directories rather than single files.

## Usage
Example maven usage:

```xml
<plugin>
	<artifactId>maven-antrun-plugin</artifactId>
	<version>1.8</version>
	<executions>
		<execution>
			<phase>generate-sources</phase>
			<configuration>
				<target>
					<delete dir="${css.dir}"/>
					<target name="compileSass">
						<path id="plugin.classpath">
							<path path="${maven.plugin.classpath}"/>
						</path>
						<taskdef name="sass" classname="com.github.bordertech.wcomponents.sass.SassTask" classpathref="plugin.classpath"/>
						<sass in="${sass.dir}" out="${css.dir}" urlmode="absolute"/>
					</target>
				</target>
			</configuration>
			<goals>
				<goal>run</goal>
			</goals>
		</execution>
	</executions>
	<dependencies>
		<dependency>
			<groupId>com.github.bordertech.wcomponents</groupId>
			<artifactId>wcomponents-sass-compiler</artifactId>
		</dependency>
		<dependency>
			<groupId>org.apache.ant</groupId>
			<artifactId>ant</artifactId>
			<version>1.9.6</version>
		</dependency>
	</dependencies>
</plugin>
```

It supports all options supported by the Vaadin Sass Compiler, attributes are all one word, all lower-case.

## Versions
Version 1.0.0 wraps Vaadin Sass Compiler version 0.9.13.
