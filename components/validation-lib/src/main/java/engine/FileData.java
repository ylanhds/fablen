package engine;
/**
 *
 * 文件数据封装类
 * 用于存储文件的二进制内容和媒体类型信息
 * @Author：zhangbaosheng
 * @Package：engine
 * @Project：validation-lib
 * @name：FileData
 * @Date：2025/8/9 13:50
 * @Filename：FileData
 */
public class FileData {
    /**
     * 文件的二进制内容
     */
    private final byte[] content;

    /**
     * 文件的MIME类型（媒体类型）
     */
    private final String mimeType;

    /**
     * 构造函数，初始化文件数据
     *
     * @param content 文件的二进制内容
     * @param mimeType 文件的MIME类型
     */
    public FileData(byte[] content, String mimeType) {
        this.content = content;
        this.mimeType = mimeType;
    }

    /**
     * 获取文件的二进制内容
     *
     * @return 文件内容的字节数组
     */
    public byte[] getContent() { return content; }

    /**
     * 获取文件的MIME类型
     *
     * @return 文件的MIME类型字符串
     */
    public String getMimeType() { return mimeType; }
}
