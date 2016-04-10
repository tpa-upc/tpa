package resources;

/**
 * Created by germangb on 10/04/16.
 */
public interface ResourceManager {

    interface ResourceManagerListener {

        /**
         * Called when a resource is loaded
         * @param string resource path
         * @param type resource type
         */
        void onLoaded (String string, Class<?> type);

        /**
         * Called when a resource fails loading
         * @param string resource path
         * @param type resource type
         * @param e thrown exception if any. null otherwise
         */
        void onFailed (String string, Class<?> type, Exception e);
    }

    /**
     * Set listener
     * @param listener
     */
    void setListener (ResourceManagerListener listener);

    /**
     * Get current listener
     * @return current listener. null if none is set
     */
    ResourceManagerListener getListener ();

    /**
     * Load a resource
     * @param file file path
     * @param type resource type
     */
    void load (String file, Class<?> type);

    /**
     * Get a loaded resource
     * @param file resource file
     * @param type resource type
     * @param <T> type
     * @return resource. null pointer if not loaded
     */
    <T> T get (String file, Class<T> type);

    /**
     * Call continuously to update resource loading.
     */
    void update ();

    /**
     * Check if all resources have been loaded
     * @return true if done loading, false otherwise
     */
    boolean isFinishedLoading ();

    /**
     * Block until all resources are loaded
     */
    void finishLoading ();
}
