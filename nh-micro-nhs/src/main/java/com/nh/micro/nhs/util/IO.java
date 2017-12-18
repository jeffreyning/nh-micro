package com.nh.micro.nhs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;


public class IO {
    /**
     * @param file
     * @param charset
     * @return String
     * @throws IOException
     */
    public static String read(File file, String charset) throws IOException {
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(file);

            int length = (int)(file.length());
            byte[] buffer = new byte[length];
            inputStream.read(buffer, 0, length);

            if(charset != null) {
                return new String(buffer, 0, buffer.length, charset);
            }
            else {
                return new String(buffer, 0, buffer.length);
            }
        }
        finally {
            close(inputStream);
        }
    }

    /**
     * @param inputStream
     * @param charset
     * @param bufferSize
     * @return String
     * @throws IOException
     */
    public static String read(InputStream inputStream, String charset, int bufferSize) throws IOException {
        InputStreamReader inputStreamReader = null;

        try {
            if(charset == null || charset.trim().length() < 1) {
                inputStreamReader = new InputStreamReader(inputStream);
            }
            else {
                inputStreamReader = new InputStreamReader(inputStream, charset);
            }

            int length = 0;
            char[] buffer = new char[bufferSize];
            StringBuilder result = new StringBuilder();

            while((length = inputStreamReader.read(buffer, 0, bufferSize)) > -1) {
                result.append(buffer, 0, length);
            }
            return result.toString();
        }
        finally {
            close(inputStreamReader);
        }
    }

    /**
     * @param bytes
     * @param file
     * @throws IOException
     */
    public static void write(File file, byte[] bytes) throws IOException {
        OutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(bytes);
            outputStream.flush();
        }
        finally {
            close(outputStream);
        }
    }

    /**
     * @param inputStream
     * @param file
     * @throws IOException
     */
    public static void write(InputStream inputStream, File file) throws IOException {
        OutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            copy(inputStream, outputStream);
        }
        finally {
            close(outputStream);
        }
    }

    /**
     * @param source
     * @param target
     * @throws IOException
     */
    public static void copy(File source, File target) throws IOException {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            File parent = target.getParentFile();

            if(parent.exists() == false) {
                parent.mkdirs();
            }

            inputStream = new FileInputStream(source);
            outputStream = new FileOutputStream(target);
            IO.copy(inputStream, outputStream, 8192);
        }
        finally {
            IO.close(inputStream);
            IO.close(outputStream);
        }
    }

    /**
     *
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        copy(inputStream, outputStream, 4096);
    }

    /**
     * @param inputStream
     * @param outputStream
     * @param bufferSize
     * @throws IOException
     */
    public static void copy(InputStream inputStream, OutputStream outputStream, int bufferSize) throws IOException {
        int length = 0;
        byte[] buffer = new byte[bufferSize];

        while((length = inputStream.read(buffer, 0, bufferSize)) > -1) {
            outputStream.write(buffer, 0, length);
        }

        outputStream.flush();
    }

    /**
     * @param inputStream
     * @param outputStream
     * @param bufferSize
     * @param size
     * @throws IOException
     */
    public static void copy(InputStream inputStream, OutputStream outputStream, int bufferSize, long size) throws IOException {
        if(size > 0) {
            int readBytes = 0;
            long count = size;
            int length = Math.min(bufferSize, (int)(size));
            byte[] buffer = new byte[length];

            while(count > 0) {
                if(count > length) {
                    readBytes = inputStream.read(buffer, 0, length);
                }
                else {
                    readBytes = inputStream.read(buffer, 0, (int)count);
                }

                if(readBytes > 0) {
                    outputStream.write(buffer, 0, readBytes);
                    count -= readBytes;
                }
                else {
                    break;
                }
            }

            outputStream.flush();
        }
    }

    /**
     * @param reader
     * @param writer
     * @throws IOException
     */
    public static void copy(Reader reader, Writer writer) throws IOException {
        copy(reader, writer, 2048);
    }

    /**
     * @param reader
     * @param writer
     * @param bufferSize
     * @throws IOException
     */
    public static void copy(Reader reader, Writer writer, int bufferSize) throws IOException {
        int length = 0;
        char[] buffer = new char[bufferSize];

        while((length = reader.read(buffer, 0, bufferSize)) > -1) {
            writer.write(buffer, 0, length);
        }

        writer.flush();
    }

    /**
     * @param resource
     */
    public static void close(java.io.Closeable resource) {
        if(resource != null) {
            try {
                resource.close();
            }
            catch(IOException e) {
            }
        }
    }

    /**
     * @param inputStream
     */
    public static void close(InputStream inputStream) {
        if(inputStream != null) {
            try {
                inputStream.close();
            }
            catch(IOException e) {
            }
        }
    }

    /**
     * @param outputStream
     */
    public static void close(OutputStream outputStream) {
        if(outputStream != null) {
            try {
                outputStream.close();
            }
            catch(IOException e) {
            }
        }
    }

    /**
     * @param reader
     */
    public static void close(Reader reader) {
        if(reader != null) {
            try {
                reader.close();
            }
            catch(IOException e) {
            }
        }
    }

    /**
     * @param writer
     */
    public static void close(Writer writer) {
        if(writer != null) {
            try {
                writer.close();
            }
            catch(IOException e) {
            }
        }
    }
}
