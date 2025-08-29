package com.fablen.compress.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.jpountz.lz4.LZ4BlockInputStream;
import net.jpountz.lz4.LZ4BlockOutputStream;
import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;
import com.fablen.compress.AbstractCompress;

/**
 * LZ4压缩实现，依赖于lz4-java包
 *
 * LZ4特点：
 * - 压缩和解压速度极快
 * - 压缩率中等
 * - 适合对性能要求高的实时应用场景
 */
public class Lz4Compress extends AbstractCompress {

    private static final int BLOCK_SIZE = 8192;

    @Override
    protected OutputStream createOutputStream(OutputStream output) throws IOException {
        LZ4Compressor compressor = LZ4Factory.fastestInstance().fastCompressor();
        return new LZ4BlockOutputStream(output, BLOCK_SIZE, compressor);
    }

    @Override
    protected InputStream createInputStream(InputStream input) throws IOException {
        LZ4FastDecompressor decompressor = LZ4Factory.fastestInstance().fastDecompressor();
        return new LZ4BlockInputStream(input, decompressor);
    }
}
