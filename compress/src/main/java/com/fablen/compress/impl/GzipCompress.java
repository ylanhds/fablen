// GzipCompress.java
package com.fablen.compress.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.fablen.compress.AbstractCompress;

/**
 * gzip算法
 *
 * <p>继承关系,基于DeflaterOutputStream的包装</p>
 * <pre>
 * java.lang.Object
 *   java.io.OutputStream
 *     java.io.FilterOutputStream
 *       java.util.zip.DeflaterOutputStream
 *         java.util.zip.GZIPOutputStream
 * </pre>
 *
 * @author it
 */
public class GzipCompress extends AbstractCompress {

    private static final int DEFAULT_LEVEL = java.util.zip.Deflater.DEFAULT_COMPRESSION;

    @Override
    protected OutputStream createOutputStream(OutputStream output) throws IOException {
        return new GZIPOutputStream(output);
    }

    protected OutputStream createOutputStream(OutputStream output, int level) throws IOException {
        return new GZIPOutputStream(output, 8192, true) {
            {
                def.setLevel(level);
            }
        };
    }

    @Override
    protected InputStream createInputStream(InputStream input) throws IOException {
        return new GZIPInputStream(input);
    }

    @Override
    public byte[] compress(byte[] data, int level) throws IOException {
        java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream();
        try (OutputStream cs = createOutputStream(os, level)) {
            cs.write(data);
        }
        return os.toByteArray();
    }

    @Override
    public byte[] compress(byte[] data) throws IOException {
        return compress(data, DEFAULT_LEVEL);
    }

    @Override
    public void compress(InputStream input, OutputStream output) throws IOException {
        try (OutputStream cos = createOutputStream(output)) {
            byte[] buffer = new byte[8192];
            int len;
            while ((len = input.read(buffer)) != -1) {
                cos.write(buffer, 0, len);
            }
        }
    }

    @Override
    public void uncompress(InputStream input, OutputStream output) throws IOException {
        try (InputStream cis = createInputStream(input)) {
            byte[] buffer = new byte[8192];
            int len;
            while ((len = cis.read(buffer)) != -1) {
                output.write(buffer, 0, len);
            }
        }
    }
}
