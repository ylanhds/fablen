package com.fablen.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 重构gzip、bzip2、lz4、lzo重复代码
 * 提供压缩和解压的通用模板方法实现
 *
 * @author it
 */
public abstract class AbstractCompress implements Compress {

    // 使用更大的缓冲区以提高性能
    private static final int BUFFER_SIZE = 8192;

    // 构建模板方法
    protected abstract OutputStream createOutputStream(OutputStream output) throws IOException;
    protected abstract InputStream createInputStream(InputStream input) throws IOException;

    @Override
    public byte[] compress(byte[] data) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try (OutputStream cs = createOutputStream(os)) {
            cs.write(data);
        }
        return os.toByteArray();
    }

    @Override
    public byte[] uncompress(byte[] data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int len = 0;

        try (InputStream us = createInputStream(new ByteArrayInputStream(data))) {
            while ((len = us.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
        }
        return baos.toByteArray();
    }

    @Override
    public void compress(InputStream input, OutputStream output) throws IOException {
        try (OutputStream cos = createOutputStream(output)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = input.read(buffer)) != -1) {
                cos.write(buffer, 0, len);
            }
        }
    }

    @Override
    public void uncompress(InputStream input, OutputStream output) throws IOException {
        try (InputStream cis = createInputStream(input)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = cis.read(buffer)) != -1) {
                output.write(buffer, 0, len);
            }
        }
    }

    @Override
    public byte[] compress(byte[] data, int level) throws IOException {
        // 默认实现，子类可以重写以支持压缩级别
        return compress(data);
    }
}
