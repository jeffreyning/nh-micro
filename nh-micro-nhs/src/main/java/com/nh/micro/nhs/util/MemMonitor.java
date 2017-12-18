package com.nh.micro.nhs.util;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Formatter;


public class MemMonitor {
    private long temp = 0L;
    private long start = 0L;
    private long end = 0L;
    private long max = 0L;
    private long used = 0L;
    private long free = 0L;
    private long total = 0L;
    private Object lock = new Object();

    /**
     *
     */
    public MemMonitor() {
        this.start = System.currentTimeMillis();
        this.test();
    }

    /**
     */
    public void test() {
        this.test(null, false, false);
    }

    /**
     * @param out
     */
    public void test(PrintWriter out) {
        this.test(out, true, true);
    }

    /**
     * @param out
     * @param head
     * @param detail
     */
    public void test(PrintWriter out, boolean head, boolean detail) {
        synchronized(this.lock) {
            Runtime runtime = Runtime.getRuntime();
            this.max = runtime.maxMemory();
            this.free = runtime.freeMemory();
            this.total = runtime.totalMemory();
            this.used = this.total - this.free;
            this.temp  = this.start;
            this.start = System.currentTimeMillis();
            this.end = this.start;

            if(out != null) {
                this.print(out, head, detail);
            }
        }
    }

    /**
     * @param out
     */
    public void print(PrintStream out) {
        this.print(new PrintWriter(out), true, true);
    }

    /**
     * @param out
     * @param head
     * @param detail
     */
    public void print(PrintStream out, boolean head, boolean detail) {
        this.print(new PrintWriter(out), head, detail);
    }

    /**
     * @param out
     * @param head
     * @param detail
     */
    public void print(PrintWriter out, boolean head, boolean detail) {
        if(head || detail) {
            StringBuilder buffer = new StringBuilder();
            Formatter formatter = new Formatter(buffer);

            if(head) {
                formatter.format("%-16s", "Max");
                formatter.format("%-10s", "");
                formatter.format("%-16s", "Free");
                formatter.format("%-10s", "");
                formatter.format("%-16s", "Used");
                formatter.format("%-10s", "");
                formatter.format("%-16s", "Total");
                formatter.format("%-10s", "");
                formatter.format("%-28s", "Start");
                formatter.format("%-28s", "End");
                formatter.format("%-20s", "Time");
                out.println(buffer.toString());
            }

            if(detail) {
                synchronized(this.lock) {
                    buffer.setLength(0);
                    long unit = 1024L * 1024L;
                    formatter.format("%-16s", this.max);
                    formatter.format("%-6s", (this.max / unit));
                    formatter.format("%-4s", "M");

                    formatter.format("%-16s", this.free);
                    formatter.format("%-6s", (this.free / unit));
                    formatter.format("%-4s", "M");

                    formatter.format("%-16s", this.used);
                    formatter.format("%-6s", (this.used / unit));
                    formatter.format("%-4s", "M");

                    formatter.format("%-16s", this.total);
                    formatter.format("%-6s", (this.total / unit));
                    formatter.format("%-4s", "M");

                    formatter.format("%1$tY-%1$2tm-%1$2td %1$2tH:%1$2tM:%1$2tS.%1$3tL     ", new Date(this.temp));
                    formatter.format("%1$tY-%1$2tm-%1$2td %1$2tH:%1$2tM:%1$2tS.%1$3tL     ", new Date(this.end));
                    formatter.format("%-16s", format(this.start - this.temp));
                    out.println(buffer.toString());
                }
            }
            formatter.close();
            out.flush();
        }
    }

    /**
     * @param millis
     * @return String
     */
    protected static String format(long millis) {
        long t1 = millis % (24L * 60L * 60L * 1000);
        long t2 = t1 % (60L * 60L * 1000);
        long t3 = t2 % (60L * 1000);
        long d = millis / (24L * 60L * 60L * 1000);
        long h = t1 / (60L * 60L * 1000);
        long M = t2 / (60L * 1000);
        long s = t3 / 1000;
        long S = millis % 1000;

        StringBuilder buffer = new StringBuilder();

        if(d < 10) {
            buffer.append("00");
        }
        else if(d < 100) {
            buffer.append("0");
        }

        buffer.append(d);
        buffer.append(" ");

        if(h < 10) {
            buffer.append("0");
        }

        buffer.append(h);
        buffer.append(":");

        if(M < 10) {
            buffer.append("0");
        }

        buffer.append(M);
        buffer.append(":");

        if(s < 10) {
            buffer.append("0");
        }

        buffer.append(s);
        buffer.append(":");

        if(S < 10) {
            buffer.append("0");
        }

        if(S < 100) {
            buffer.append("0");
        }

        buffer.append(S);
        return buffer.toString();
    }
}
