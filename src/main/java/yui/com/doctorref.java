package yui.com;

import ikari.com.doctor2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by yui on 2017/11/24.
 */
public class doctorref {
    public static void main(String[] args) throws IOException {
        File file = new File("hello2.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write("hello world");
        bufferedWriter.close();

    }
}
