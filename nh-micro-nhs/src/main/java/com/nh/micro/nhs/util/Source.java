package com.nh.micro.nhs.util;


public class Source {
    private String home;
    private String path;
    private int type;
    private long lastModified;

    /**
     *
     */
    public static final int STATIC = 0;

    /**
     *
     */
    public static final int SCRIPT = 1;

    /**
     *
     */
    public Source() {
    }

    /**
     * @param home
     * @param path
     * @param type
     */
    public Source(String home, String path, int type) {
        this(home, path, type, 0L);
    }

    /**
     * @param home
     * @param path
     * @param type
     * @param lastModified
     */
    public Source(String home, String path, int type, long lastModified) {
        this.home = home;
        this.path = path;
        this.type = type;
        this.lastModified = lastModified;
    }

    /**
     * @param type
     * @param defaultValue
     * @return int
     */
    public static int valueOf(String type, int defaultValue) {
        if(type == null) {
            return defaultValue;
        }
        else if(type.equalsIgnoreCase("static")) {
            return Source.STATIC;
        }
        else if(type.equalsIgnoreCase("script")) {
            return Source.SCRIPT;
        }
        else {
            return defaultValue;
        }
    }

    /**
     * @param home the home to set
     */
    public void setHome(String home) {
        this.home = home;
    }

    /**
     * @return the home
     */
    public String getHome() {
        return this.home;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * @return the lastModified
     */
    public long getLastModified() {
        return this.lastModified;
    }

    /**
     * @param lastModified the lastModified to set
     */
    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return the type
     */
    public int getType() {
        return this.type;
    }
}
