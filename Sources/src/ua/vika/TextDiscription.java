package ua.vika;

import java.io.*;
import java.net.URI;
import java.net.URL;

/**
 * Created by zidd on 3/24/14.
 */
public class TextDiscription {
    private String text;

    public TextDiscription(String path) {
        FileInputStream fis;
        InputStreamReader isr;

        BufferedReader br;
        //FileReader fr;
        StringBuilder sb = new StringBuilder();
        try {
            //fr = new FileReader(new File(path));

            fis = new FileInputStream(new File(path));
            isr = new InputStreamReader(fis,"UTF8");
            br = new BufferedReader(isr);

           // br = new BufferedReader(fr);
            String s;
            while((s = br.readLine()) != null)
                sb.append(s + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        text = sb.toString();
    }

    @Override
    public String toString() {
        return text;
    }

    public static void main(String[] args) {
        TextDiscription text = new TextDiscription("C:\\users\\zidd\\Desktop\\schedule.txt");
        System.out.println(text);
    }
}


