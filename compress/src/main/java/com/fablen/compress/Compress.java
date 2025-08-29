// Compress.java
package com.fablen.compress;

import java.io.IOException;

/**
 * 数据压缩和解压，提供顶层抽象
 * <ul>
 * <li>deflate</li>
 * <li>gzip</li>
 * <li>bzip2</li>
 * <li>lzo</li>
 * <li>lz4</li>
 * <li>snappy</li>
 * </ul>
 */
public interface Compress {

    /**
     * 数据压缩
     */
    byte[] compress(byte[] data) throws IOException;

    /**
     * 数据压缩（带压缩级别）
     */
    byte[] compress(byte[] data, int level) throws IOException;

    /**
     * 数据解压
     */
    byte[] uncompress(byte[] data) throws IOException;

    /**
     * 流式压缩
     */
    void compress(java.io.InputStream input, java.io.OutputStream output) throws IOException;

    /**
     * 流式解压
     */
    void uncompress(java.io.InputStream input, java.io.OutputStream output) throws IOException;
}
