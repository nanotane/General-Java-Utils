package com.blackcardinalsoftware.lang;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * A language bundle associated with the given class. This object should be
 * created through the {@link LanguageUtils} util class as it will hook into the
 * util and change it's bundle when the class is told to load a different
 * language bundle.
 *
 * @author Ian
 *
 */
public class LangBundle {

    public static final String BUNDLE_LOCATION = "/bundles/";
    public static final String ORGANIZED_BUNDLES = "/bundles/bundles_orginized/";
    private ResourceBundle mResourceBundle = null;
    private Locale mCurrentLocale = null;
    private final Class<?> mAssociatedClass;

    /**
     * Constructor. Remember to name the lang bundles something short so that there
     * is not tons of code clutter
     * <p>
     * If there is no resource bundle associated with our class and name combo, then
     * we will print out a message to console and a stack trace. All subsequent
     * {@link #g(String)} calls will simply return the given string
     *
     * @param pAssociatedClass the associated class
     * @param pLocale          the locale that we want
     */
    protected LangBundle(Class<?> pAssociatedClass, Locale pLocale) {
	mAssociatedClass = pAssociatedClass;
	loadNewBundle(pLocale);
    }

    /**
     * Load a new language bundle for our class
     *
     * @param pLocale the locale we want to load
     */
    protected void loadNewBundle(Locale pLocale) {
	mCurrentLocale = pLocale;
	try {
	    /*
	     * Whats happening here? So because of how the bundling system works, we first
	     * have to load the file location into our class loader before we can have the
	     * ResourceBundle class load it. After we do that, we only need to pass in the
	     * base file name WITHOUT the language name (en_US for example) in it as it will
	     * figure out what the name should be based on the passed in language.
	     *
	     * Resource bundle has a system for finding the name as follows in order of
	     * Precedence. In the example we are looking at poland polish, with the OS in
	     * english:
	     *
	     * Label_pl_PL_UNIX
	     *
	     * Label_pl_PL
	     *
	     * Label_pl
	     *
	     * Label_en_US
	     *
	     * Label_en
	     *
	     * Label
	     *
	     *
	     */
	    Path test = Paths.get("resources/" + BUNDLE_LOCATION);
	    URL location = test.toUri().toURL();
	    URL[] container = { location };
	    ClassLoader classLoader = new URLClassLoader(container);
	    String baseFileName = mAssociatedClass.getCanonicalName().replace(".", "_");
	    mResourceBundle = ResourceBundle.getBundle(baseFileName, pLocale, classLoader);
	} catch (MissingResourceException pNoBundle) {
	    System.out.println("ERROR: No language bundle associated with the class " + mAssociatedClass.getName()
		    + " for language " + pLocale.getLanguage());
	    pNoBundle.printStackTrace();
	} catch (Throwable pAllOthers) {
	    pAllOthers.printStackTrace();
	}
    }

    /**
     * Get the string from our loaded bundle. This is a one character method in
     * order to help compress the code and make it less cluttered since this will be
     * used a lot
     *
     * @param pCorrespondingText the corresponding text we want a value for
     * @return the string associated with the text, or the text given if no resource
     *         bundle could be found
     */
    public String g(String pCorrespondingText) {
	String text = pCorrespondingText;
	if (mResourceBundle == null) {
	    return text;
	}
	try {
	    text = mResourceBundle.getString(pCorrespondingText);
	} catch (NullPointerException pNull) {
	    System.out.println("ERROR: the passed in text was NULL! The word 'NULL' will be returned");
	    text = "NULL";
	    pNull.printStackTrace();
	} catch (MissingResourceException pNoBundle) {
	    System.out.println("ERROR: no lang resource for the given text " + pCorrespondingText);
	    pNoBundle.printStackTrace();
	}
	return text;
    }

    /**
     * Same as {@link #g(String)} but we will add a space at the end of the returned
     * text
     *
     * @param pCorrespondingText the corresponding text we want a value for
     * @return the string associated with the text, or the text given if no resource
     *         bundle could be found
     */
    public String gs(String pCorrespondingText) {
	StringBuilder builder = new StringBuilder(g(pCorrespondingText));
	return builder.append(" ").toString();
    }

    /**
     * @return the current locale that this bundle has loaded
     */
    public Locale getLocale() {
	return mCurrentLocale;
    }
}
