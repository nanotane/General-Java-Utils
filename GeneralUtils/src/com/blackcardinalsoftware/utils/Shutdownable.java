package com.blackcardinalsoftware.utils;

/**
 * This is a convenience interface for classes that need to be shut down. This
 * is to make things easier in grouping and shutting down components
 *
 * @author Ian
 *
 */
public interface Shutdownable {

    /**
     * All tasks that must be completed before we shutdown the program should be
     * done here.
     */
    public void shutdown();
}
