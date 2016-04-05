package com.github.bordertech.wcomponents.sass;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test SassTask.
 * @author ricksbrown
 */
public class SassTaskTest {

	/**
	 * Set up instance.
	 */
	public SassTaskTest() {
		scssFiles = new String[] { "base", "body", "_reset" };
	}

	private Path workingDirectory;
	private Path scssRootDir;
	private Path outputDir;
	private final String[] scssFiles;

	@Before
	public void setUp() throws Exception {

		this.workingDirectory = Files.createTempDirectory("sasstask");
		this.scssRootDir = this.workingDirectory.resolve("scss");
		this.outputDir = this.workingDirectory.resolve("output");

		Files.createDirectories(scssRootDir);
		Files.deleteIfExists(outputDir);
		Files.createDirectories(outputDir);
		for (String scssFile : scssFiles) {
			scssFile = scssFile + ".scss";
			Files.copy(this.getClass().getClassLoader().getResourceAsStream("scss/" + scssFile), scssRootDir.resolve(scssFile));
		}
	}

	@Test
	public void testExecute() {
		SassTask sassTask = new SassTask();
		sassTask.setIn(this.scssRootDir.toFile().getAbsolutePath());
		sassTask.setOut(this.outputDir.toFile().getAbsolutePath());
		sassTask.execute();

		for (String cssFile : scssFiles) {
			cssFile = cssFile + ".css";
			Path expected = outputDir.resolve(cssFile);
			if (cssFile.startsWith("_")) {
				Assert.assertFalse("Underscore files should not exist", expected.toFile().exists());
			} else {
				Assert.assertTrue(expected.toFile().getAbsolutePath() + " should exist", expected.toFile().exists());
			}
		}
	}
}
