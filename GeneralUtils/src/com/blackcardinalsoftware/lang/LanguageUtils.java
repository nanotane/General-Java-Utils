package com.blackcardinalsoftware.lang;

import java.awt.im.InputContext;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * This is the Label Util. For language purposes, this class should be used to
 * get labels for the program, and labels should be placed into the
 * corresponding resource bundle files
 *
 * @author Ian
 *
 */
public final class LanguageUtils {

    private static final Map<Class<?>, LangBundle> LANGBUNDLES = new HashMap<>();

    /**
     * Get the {@link LangBundle} of the associated class based on the system
     * settings
     *
     * @param pAssociatedClass the class we want a lang bundle for
     * @return the associated LangBundle object
     */
    public static LangBundle getLangBundle(Class<?> pAssociatedClass) {
	return getLangBundle(pAssociatedClass, InputContext.getInstance().getLocale());
    }

    /**
     * Get the {@link LangBundle} based on the class and Locale given
     *
     * @param pAssociatedClass the class we want a lang bundle for
     * @param pLocale          the locale we want the lang bundle for
     * @return the associated LangBundle object
     */
    public static LangBundle getLangBundle(Class<?> pAssociatedClass, Locale pLocale) {
	LangBundle bundle = LANGBUNDLES.computeIfAbsent(pAssociatedClass, (pKey) -> new LangBundle(pKey, pLocale));
	// If what we loaded is not in the desired locale, lets load it now
	if (!bundle.getLocale().equals(pLocale)) {
	    bundle.loadNewBundle(pLocale);
	}
	return bundle;
    }

    /**
     * Load our desired class. This will go through and load our lang bundles for
     * all the classes we have kept track of
     *
     * @param pLocaleToLoad
     */
    public static void loadLanguage(Locale pLocaleToLoad) {
	LANGBUNDLES.forEach((pClass, pLangBundle) -> {
	    pLangBundle.loadNewBundle(pLocaleToLoad);
	});
    }
}
