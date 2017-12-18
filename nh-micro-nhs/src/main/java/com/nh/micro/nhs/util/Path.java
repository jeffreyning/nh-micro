package com.nh.micro.nhs.util;

import java.util.ArrayList;
import java.util.List;


public class Path {
    /**
     * @param args
     */
    public static void main(String[] args) {
        /**
         * windows
         */
        System.out.println("-------------------- test join --------------------");
        System.out.println("result: " + Path.join("D:\\mytest", "a\\..\\b.txt"));
        System.out.println("result: " + Path.join("D:\\mytest", "a\\..\\.\\b.txt"));
        System.out.println("result: " + Path.join("D:\\mytest\\.\\", "a\\..\\.\\b.txt"));
        System.out.println("result: " + Path.join("D:\\mytest\\\\\\\\", "a\\..\\b.txt"));
        System.out.println("result: " + Path.join("D:\\mytest", ".\\a\\..\\..\\..\\..\\..\\b.txt"));

        /**
         * unix
         */
        System.out.println("-------------------- test join --------------------");
        System.out.println("result: " + Path.join("/home/mytest", ".\\a\\..\\..\\..\\..\\..\\b.txt"));
        System.out.println("result: " + Path.join("mytest/mytest", ".\\a\\..\\..\\..\\..\\..\\b.txt"));
        System.out.println("result: " + Path.getParent("/user/user.jsp"));
        System.out.println("result: " + Path.join("/user/", "../include/header.jsp"));
        System.out.println("result: " + Path.join("/user", "../include/header.jsp"));

        /**
         *
         */
        System.out.println("-------------------- test join --------------------");
        System.out.println("result: " + Path.join("/home/mytest", ""));
        System.out.println("result: " + Path.join("mytest/mytest", ""));
        System.out.println("result: " + Path.join("////////", ""));

        /**
         *
         */
        System.out.println("-------------------- test contains --------------------");
        System.out.println("contains: " + Path.contains("D:\\workspace2/ayada\\webapp", "\\..\\outTest.jsp"));
        System.out.println("contains: " + Path.contains("D:\\workspace2/ayada\\webapp", "\\outTest.jsp"));

        /**
         *
         */
        System.out.println("-------------------- test getStrictPath --------------------");
        System.out.println("strictPath: " + Path.getStrictPath("D:"));
        System.out.println("strictPath: " + Path.getStrictPath("D:\\"));
        System.out.println("strictPath: " + Path.getStrictPath("D:\\test///a\\b.html"));
        System.out.println("strictPath: " + Path.getStrictPath("////test///a\\b.html"));
        System.out.println("strictPath: " + Path.getStrictPath("test///a\\b.html"));
        System.out.println("strictPath: " + Path.getStrictPath("../../../../test///a\\b.html"));
        System.out.println("strictPath: " + Path.getStrictPath("//////home/user//\\test///a\\b.html"));
        System.out.println("strictPath: " + Path.getStrictPath("//////home/user//\\test///"));
        System.out.println("strictPath: " + Path.getStrictPath("///////"));

        /**
         *
         */
        System.out.println("-------------------- test getParent --------------------");
        System.out.println("parent: " + Path.getParent("D:\\"));
        System.out.println("parent: " + Path.getParent("D:\\a.html"));
        System.out.println("parent: " + Path.getParent("D:\\a\\b\\c.html"));
        System.out.println("parent: " + Path.getParent("c.html"));
        System.out.println("parent: " + Path.getParent("///////a/b/c.html"));
        System.out.println("parent: " + Path.getParent("///////a.html"));
        System.out.println("parent: " + Path.getParent("///////a/b/c/"));
        System.out.println("parent: " + Path.getParent("////////"));
        System.out.println("parent: " + Path.getParent("\\\\\\\\"));

        /**
         *
         */
        System.out.println(Path.contains("D:\\workspace2\\generator\\webapp\\template", "/template/generator/frame.jsp"));
    }

    /**
     * @param path
     * @return String
     */
    public static String getExtension(String path) {
        if(path != null && path.length() > 0) {
            char c = '0';
            int i = path.length() - 1;

            for(; i > -1; i--) {
                c = path.charAt(i);

                if(c == '.' ) {
                    break;
                }
                else if(c == '/' || c == '\\' || c == ':') {
                    break;
                }
            }

            if(c == '.') {
                return path.substring(i + 1);
            }
        }
        return "";
    }

    /**
     * @param parent
     * @param child
     * @return boolean
     */
    public static boolean contains(String parent, String child) {
        String full = join(parent, child);
        String temp = join(parent, "");
        return full.startsWith(temp);
    }

    /**
     * @param parent
     * @param child
     * @return String
     */
    public static String join(String parent, String child) {
        List<String> parts = split(parent + "/" + child);
        return join(parts);
    }

    /**
     * @param path
     * @return String
     */
    public static String getParent(String path) {
        List<String> parts = split(path);

        if(parts.size() > 0) {
            if(parts.size() == 1) {
                String root = parts.get(0);

                if(root.equals("/") || root.endsWith(":")) {
                    return root;
                }
                else {
                    return "";
                }
            }
            else {
                parts.remove(parts.size() - 1);
            }
            return join(parts);
        }
        else {
            return "";
        }
    }

    /**
     * @param path
     * @return String
     */
    public static String getStrictPath(String path) {
        char c;
        StringBuilder buffer = new StringBuilder();

        for(int i = 0, length = path.length(); i < length; i++) {
            c = path.charAt(i);

            if(c == '\\' || c == '/') {
                if(buffer.length() < 1 || buffer.charAt(buffer.length() - 1) != '/') {
                    buffer.append("/");
                }
            }
            else {
                buffer.append(c);
            }
        }
        return buffer.toString();
    }

    /**
     * @param parent
     * @param child
     * @return String
     */
    public static String getRelativePath(String parent, String child) {
        String full = join(child, "");
        String temp = join(parent, "");

        if(full.startsWith(temp)) {
            return full.substring(temp.length());
        }
        return null;
    }

    /**
     * @param path
     * @return List<String>
     */
    public static List<String> split(String path) {
        char c;
        String root = null;
        StringBuilder buffer = new StringBuilder();
        List<String> stack = new ArrayList<String>();

        for(int i = 0, length = path.length(); i < length; i++) {
            c = path.charAt(i);

            if(c == '\\' || c == '/') {
                if(root == null) {
                    root = buffer.toString();
                }
                push(stack, buffer.toString());
                buffer.setLength(0);
            }
            else {
                buffer.append(c);
            }
        }

        push(stack, buffer.toString());

        if(root != null) {
            if(!root.endsWith(":") && root.length() < 1) {
                stack.add(0, "/");
            }
        }
        return stack;
    }

    /**
     * @param paths
     * @return String
     */
    public static String join(List<String> paths) {
        StringBuilder buffer = new StringBuilder();

        if(paths.size() > 0) {
            for(String part : paths) {
                buffer.append(part);

                if(!part.equals("/")) {
                    buffer.append("/");
                }
            }
        }

        if(buffer.length() > 1) {
            buffer.deleteCharAt(buffer.length() - 1);
        }
        return buffer.toString();
    }

    /**
     * @param stack
     * @param name
     */
    private static void push(List<String> stack, String name) {
        if(name.length() < 1 || name.equals(".")) {
            return;
        }
        else {
            if(name.equals("..")) {
                if(stack.size() > 0) {
                    String parent = stack.get(stack.size() - 1);

                    if(!parent.endsWith(":")) {
                        stack.remove(stack.size() - 1);
                    }
                }
            }
            else {
                stack.add(name);
            }
        }
    }
}
