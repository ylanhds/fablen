package com.fablen.compress.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;

import org.apache.commons.io.output.ByteArrayOutputStream;
import com.fablen.compress.AbstractCompress;

/**
 * Bzip2压缩实现，依赖于apache commons-compress包
 *
 * Bzip2特点：
 * - 压缩率高，适合存储敏感型应用
 * - 压缩速度较慢，但解压速度中等
 * - 适合对压缩率要求高而对速度要求不高的场景
 */
public class Bzip2Compress extends AbstractCompress {

    @Override
    protected OutputStream createOutputStream(OutputStream output) throws IOException {
        return new BZip2CompressorOutputStream(output);
    }

    @Override
    protected InputStream createInputStream(InputStream input) throws IOException {
        return new BZip2CompressorInputStream(input);
    }

    @Override
    public byte[] compress(byte[] data, int level) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try (OutputStream cs = new BZip2CompressorOutputStream(os, level)) {
            cs.write(data);
        }
        return os.toByteArray();
    }
}
