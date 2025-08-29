package com.fablen;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Description: -ea -XX:+PrintGCDetails
 * </p>
 *
 * @author fablen
 * @version v1.0.0
 * @date 2020-10-28 15:58
 * @see
 */
public class OutOfMemoryErrorDemo {
    public static void main(String[] args) {
        List<Object> objectList = new ArrayList<>();
        while (true) {
            objectList.add(new Object());
        }
    }

}
