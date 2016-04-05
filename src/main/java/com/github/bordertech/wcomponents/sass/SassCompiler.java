package com.github.bordertech.wcomponents.sass;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thinly wrap Vaadin Sass Compiler so it supports input and output directories rather than single files.
 * Ideally we would not need this layer - it would be nice to be able to delete it in future.
 * @author ricksbrown
 */
public class SassCompiler {
	private static final String OUTPUT_EXTENSION = ".css";
	private static final Logger LOG = Logger.getLogger(SassCompiler.class.getName());

	/**
	 * No need to instantiate.
	 */
	private SassCompiler() {

	}

	/**
	 * Args are identical to vaadin sass compiler except in/out can be directories.
	 * @param args vaadin sass compiler args.
	 * @throws Exception Any exception reported by the underlying compiler.
	 */
	public static void main(final String[] args) throws Exception {
		if (args.length > 1) {
			String input = args[0];
			String output = args[1];
			File in = new File(input);
			File out = new File(output);
			if (!in.canRead() || !out.canWrite()) {
				// let the underlying compiler report an error
				callImpl(args);
			} else {
				File[] inputFiles = getInputFiles(in);
				for (File inputFile : inputFiles) {
					args[0] = inputFile.getAbsolutePath();
					args[1] = getOutput(inputFile, out).getAbsolutePath();
					callImpl(args);
				}
			}
		} else {
			// let the underlying compiler report an error
			callImpl(args);
		}
	}

	private static void callImpl(final String[] args) throws Exception {
		try {
			if (args.length > 0) {
				LOG.log(Level.FINE, "Compiling {0}", args[0]);
			}
			com.vaadin.sass.SassCompiler.main(args);
		} catch (AssertionError ignore) {
			// we don't care about vaadin's assertions...
		}
	}

	/**
	 * For the given sass input file create an OutputStream to which we will write the result of the
	 * Sass compilation.
	 *
	 * @param inputFile A sass input file to compile.
	 * @param out The path to an output directory.
	 * @return An output stream for the compiler to write to.
	 */
	private static File getOutput(final File inputFile, final File out) {
		String filename = inputFile.getName();
		if (out.getName().endsWith(OUTPUT_EXTENSION)) {
			// out is a path to a CSS file rather than a directory. Hopefully there is only one input file...
			return out;
		}
		if (filename.indexOf(".") > 0) {
			filename = filename.substring(0, filename.lastIndexOf("."));
		}
		filename += OUTPUT_EXTENSION;
		final Path output = out.toPath().resolve(filename);
		return output.toFile();
	}

	/**
	 * Get the sass files to compile.
	 * @param in A directory containing sass files to compile or a single sass file.
	 * @return An array of files to compile.
	 */
	private static File[] getInputFiles(final File in) {
		File[] files;
		if (in.isDirectory()) {
			files = in.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(final File dir, final String name) {
					return !name.startsWith("_") && name.endsWith(".scss");
				}
			});
		} else {
			files = new File[]{ in };
		}
		return files;
	}
}
