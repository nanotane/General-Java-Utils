package com.blackcardinalsoftwate.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.blackcardinalsoftware.utils.ShutdownableHolderUtil;

/**
 * This simple class is in charge of reading and writing to text files
 *
 * @author Ian
 *
 */
public class TextFileIO {

    /**
     * Executor for threading
     */
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    /**
     * Initialization-on-demand holder. This is used so that we do not initilalize
     * the model until we first call getInstance.
     *
     * @author Ian
     *
     */
    private static class instanceHolder {
	private static final TextFileIO INSTANCE = new TextFileIO();
    }

    /**
     * @return the instance of our file read writer
     */
    public static TextFileIO getInstance() {
	return instanceHolder.INSTANCE;
    }

    private TextFileIO() {
	ShutdownableHolderUtil.getInstance().addToShutdown(() -> mExecutor.shutdown());
    }

    /**
     * Write out the given string contents into a specific text file
     *
     * @param contents  the contents that we want to write out
     * @param pLocation the location for the file
     */
    public void writeAll(List<String> contents, File pLocation) {
	if (pLocation != null) {
	    mExecutor.submit(() -> {

		// make the file and handle exceptions
		FileWriter fw = null;
		try {
		    fw = new FileWriter(pLocation);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		// print things out
		PrintWriter pw = new PrintWriter(fw, true);
		for (int i = 0; i < contents.size(); i++) {
		    pw.println(contents.get(i));
		}

		// close everything
		try {
		    fw.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		pw.close();
	    });
	}
    }

    /**
     * Read the contents of the file and return them as an array list
     *
     * @param fileName
     * @return contents an array list
     */
    public Future<ArrayList<String>> read(String fileName) {
	return mExecutor.submit(read0(fileName));
    }

    private Callable<ArrayList<String>> read0(String fileName) {
	return () -> {
	    // This will reference one line at a time
	    String line = null;
	    ArrayList<String> contents = new ArrayList<String>();

	    try {
		// FileReader reads text files in the default encoding.
		FileReader fileReader = new FileReader(fileName);

		// Always wrap FileReader in BufferedReader.
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		while ((line = bufferedReader.readLine()) != null) {
		    contents.add(line);
		}

		// Always close files.
		bufferedReader.close();
		return contents;
	    } catch (FileNotFoundException ex) {
		ex.printStackTrace();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    // if we got here, there was an error
	    return null;
	};
    }
}
