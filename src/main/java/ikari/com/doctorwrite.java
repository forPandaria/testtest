package ikari.com;

import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;

public class doctorwrite {
    @Test
    public void myprint() throws IOException {
        FileWriter fileWriter = new FileWriter("test2.xml", true);

        for(int i=0; i<10; i++){
            fileWriter.write("<nihao"+ i+">\n");
        }
        fileWriter.flush();
        fileWriter.close();
    }
}
