// SnappyCompress.java
package com.fablen.compress.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.xerial.snappy.SnappyInputStream;
import org.xerial.snappy.SnappyOutputStream;

import com.fablen.compress.AbstractCompress;

/**
 * Snappy（以前称Zippy）是Google基于LZ77的思路用C++语言编写的快速数据压缩与解压程序库，
 * 并在2011年开源。它的目标并非最大压缩率或与其他压缩程序库的兼容性，而是非常高的速度和合理的压缩率
 *
 * 依赖于snappy-java包
 *
 * @author it
 */
public class SnappyCompress extends AbstractCompress {

    @Override
    protected OutputStream createOutputStream(OutputStream output) throws IOException {
        return new SnappyOutputStream(output);
    }

    @Override
    protected InputStream createInputStream(InputStream input) throws IOException {
        return new SnappyInputStream(input);
    }
}
