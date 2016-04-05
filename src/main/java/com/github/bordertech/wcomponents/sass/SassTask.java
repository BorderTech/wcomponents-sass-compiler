package com.github.bordertech.wcomponents.sass;

import java.util.ArrayList;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * Provides an Ant task to invoke the underlying Sass compiler without the performance hit of the Ant `java` task.
 * This task is not intended to be an adapter or facade; it aims to be as "thin" as possible and transparently expose the
 * API of the .underlying compiler implementation (Vaadin Sass Compiler). For this reason a change of sass compiler is likely
 * to change the API of this task.
 * Ideally this task would be provided "out of the box" by the Vaadin Sass Compiler and we could delete this task entirely.
 *
 * @author ricksbrown
 */
public class SassTask  extends Task {
	private static final char KEYVAL_SEPARATOR = ':';
	private static final byte MAX_ARG_COUNT = 6;
	private String in = null;
	private String out = null;
	private String urlMode = null;
	private Boolean minify = null;
	private Boolean compress = null;
	private Boolean ignoreWarnings;

	/**
	 * Set the path to the input file/s.
	 * @param in The sass file to compile or a directory containing sass files.
	 */
	public void setIn(final String in) {
		this.in = in;
	}

	/**
	 * Set the path to the output file/s.
	 * @param out The css file to generate or a directory to contain compiled css.
	 */
	public void setOut(final String out) {
		this.out = out;
	}

	/**
	 * @param urlMode See vaadin documentation.
	 * @see <a href="https://github.com/vaadin/sass-compiler/blob/master/src/main/java/com/vaadin/sass/SassCompiler.java">Vaadin Sass Compiler</a>
	 */
	public void setUrlmode(final String urlMode) {
		this.urlMode = urlMode;
	}

	/**
	 * @param minify See vaadin documentation.
	 * @see <a href="https://github.com/vaadin/sass-compiler/blob/master/src/main/java/com/vaadin/sass/SassCompiler.java">Vaadin Sass Compiler</a>
	 */
	public void setMinify(final boolean minify) {
		this.minify = minify;
	}

	/**
	 * @param compress See vaadin documentation.
	 * @see <a href="https://github.com/vaadin/sass-compiler/blob/master/src/main/java/com/vaadin/sass/SassCompiler.java">Vaadin Sass Compiler</a>
	 */
	public void setCompress(final boolean compress) {
		this.compress = compress;
	}

	/**
	 * @param ignoreWarnings See vaadin documentation.
	 * @see <a href="https://github.com/vaadin/sass-compiler/blob/master/src/main/java/com/vaadin/sass/SassCompiler.java">Vaadin Sass Compiler</a>
	 */
	public void setIgnorewarnings(final boolean ignoreWarnings) {
		this.ignoreWarnings = ignoreWarnings;
	}

	/**
	 * Generates an array of args that can be passed to the sass compiler.
	 * Only args that have been explicitly set will be included, default values are left to the underlying compiler implementation to determine.
	 * @return An array of args as set by the caller.
	 */
	private String[] generateArgs() {
		List<String> args = new ArrayList<>(MAX_ARG_COUNT);
		if (in != null) {
			args.add(in);
		} else {
			throw new BuildException("'in' must be set");
		}
		if (out != null) {
			args.add(out);
		}
		if (urlMode != null) {
			args.add("-urlMode" + KEYVAL_SEPARATOR + urlMode);
		}
		if (minify != null) {
			args.add("-minify" + KEYVAL_SEPARATOR + minify);
		}
		if (compress != null) {
			args.add("-compress" + KEYVAL_SEPARATOR + compress);
		}
		if (ignoreWarnings != null) {
			args.add("-ignore-warnings" + KEYVAL_SEPARATOR + ignoreWarnings);
		}
		String[] result = new String[args.size()];
		args.toArray(result);
		return result;
	}

	@Override
	public void execute() throws BuildException {
		String[] args = generateArgs();

		try {
			SassCompiler.main(args);
		} catch (Exception ex) {
			throw new BuildException(ex);
		}
	}
}
