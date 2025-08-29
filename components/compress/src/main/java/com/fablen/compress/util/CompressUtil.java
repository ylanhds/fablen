// CompressUtil.java
package com.fablen.compress.util;

import java.io.IOException;

import com.fablen.compress.Compress;
import com.fablen.compress.impl.Bzip2Compress;
import com.fablen.compress.impl.DeflaterCompress;
import com.fablen.compress.impl.GzipCompress;
import com.fablen.compress.impl.Lz4Compress;
import com.fablen.compress.impl.LzoCompress;
import com.fablen.compress.impl.SnappyCompress;

public enum CompressUtil {
    DEFLATER {
        Compress compress = new DeflaterCompress();

        public byte[] compress(byte[] data) throws IOException {
            return compress.compress(data);
        }

        public byte[] uncompress(byte[] data) throws IOException {
            return compress.uncompress(data);
        }
    },
    BZIP2 {
        Compress compress = new Bzip2Compress(); // 修复：原来是 LzoCompress

        public byte[] compress(byte[] data) throws IOException {
            return compress.compress(data);
        }

        public byte[] uncompress(byte[] data) throws IOException {
            return compress.uncompress(data);
        }
    },
    GZIP {
        Compress compress = new GzipCompress();

        public byte[] compress(byte[] data) throws IOException {
            return compress.compress(data);
        }

        public byte[] uncompress(byte[] data) throws IOException {
            return compress.uncompress(data);
        }
    },
    LZ4 {
        Compress compress = new Lz4Compress();

        public byte[] compress(byte[] data) throws IOException {
            return compress.compress(data);
        }

        public byte[] uncompress(byte[] data) throws IOException {
            return compress.uncompress(data);
        }
    },
    LZO {
        Compress compress = new LzoCompress();

        public byte[] compress(byte[] data) throws IOException {
            return compress.compress(data);
        }

        public byte[] uncompress(byte[] data) throws IOException {
            return compress.uncompress(data);
        }
    },
    SNAPPY {
        Compress compress = new SnappyCompress();

        public byte[] compress(byte[] data) throws IOException {
            return compress.compress(data);
        }

        public byte[] uncompress(byte[] data) throws IOException {
            return compress.uncompress(data);
        }
    };

    public byte[] compress(byte[] data) throws IOException {
        throw new AbstractMethodError();
    }

    public byte[] uncompress(byte[] data) throws IOException {
        throw new AbstractMethodError();
    }
}
