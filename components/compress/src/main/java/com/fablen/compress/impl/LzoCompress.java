package com.fablen.compress.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.anarres.lzo.LzoAlgorithm;
import org.anarres.lzo.LzoCompressor;
import org.anarres.lzo.LzoDecompressor;
import org.anarres.lzo.LzoInputStream;
import org.anarres.lzo.LzoLibrary;
import org.anarres.lzo.LzoOutputStream;

import com.fablen.compress.AbstractCompress;

/**
 * LZO压缩实现，依赖于lzo-core包
 *
 * LZO特点：
 * - 解压速度非常快
 * - 压缩速度中等
 * - 压缩率中等，适合需要快速解压的场景
 */
public class LzoCompress extends AbstractCompress {

    @Override
    protected OutputStream createOutputStream(OutputStream output) throws IOException {
        LzoCompressor compressor = LzoLibrary.getInstance().newCompressor(LzoAlgorithm.LZO1X, null);
        return new LzoOutputStream(output, compressor);
    }

    @Override
    protected InputStream createInputStream(InputStream input) throws IOException {
        LzoDecompressor decompressor = LzoLibrary.getInstance().newDecompressor(LzoAlgorithm.LZO1X, null);
        return new LzoInputStream(input, decompressor);
    }
}
