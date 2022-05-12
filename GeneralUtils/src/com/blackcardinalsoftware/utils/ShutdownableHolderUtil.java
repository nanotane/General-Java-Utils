package com.blackcardinalsoftware.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * This util class acts as a way for classes to add objects or threads to ensure
 * they will be shutdown when the program exits.
 *
 * @author Ian
 *
 */
public final class ShutdownableHolderUtil {

    /**
     * Initialization-on-demand holder. This is used so that we do not initilalize
     * the model until we first call getInstance.
     *
     * @author Ian
     *
     */
    private static class instanceHolder {
	private static final ShutdownableHolderUtil INSTANCE = new ShutdownableHolderUtil();
    }

    /**
     * @return the instance of our shutdownable holder
     */
    public static ShutdownableHolderUtil getInstance() {
	return instanceHolder.INSTANCE;
    }

    /**
     * Our list of all shutdownable objects
     */
    private final List<Shutdownable> mShutdownableObjects = new ArrayList<>();

    /**
     * Add the shutdownable process to our list to be shutdown
     *
     * @param pToShutdown the runnable that will be used to shut down
     */
    public void addToShutdown(Shutdownable pToShutdown) {
	mShutdownableObjects.add(pToShutdown);
    }

    /**
     * Run through all of our shutdownables and run them. THIS SHOULD ONLY BE CALLED
     * WHEN THE PROGRAM TERMINATES
     */
    public void shutdownAll() {
	mShutdownableObjects.forEach(pShutdownable -> {
	    // Add a little extra layer of protection in case the shutdown process causes an
	    // error. We do not want to hold up all shutdownables
	    try {
		pShutdownable.shutdown();
	    } catch (Throwable pThrown) {
		System.out.println("ERROR WHEN RUNNING A SHUTDOWNABLE");
		pThrown.printStackTrace();
	    }
	});
    }
}
