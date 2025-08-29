// DeflaterCompress.java
package com.fablen.compress.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import com.fablen.compress.AbstractCompress;

/**
 * deflater压缩,基于java jdk
 *
 * <p>继承关系,基于FilterOutputStream的包装流</p>
 * <pre>
 * java.lang.Object
 *   java.io.OutputStream
 *     java.io.FilterOutputStream
 *       java.util.zip.DeflaterOutputStream
 * </pre>
 *
 * @author it
 */
public class DeflaterCompress extends AbstractCompress {

    @Override
    protected OutputStream createOutputStream(OutputStream output) throws IOException {
        return new DeflaterOutputStream(output, new Deflater());
    }

    @Override
    protected InputStream createInputStream(InputStream input) throws IOException {
        return new InflaterInputStream(input);
    }
}
