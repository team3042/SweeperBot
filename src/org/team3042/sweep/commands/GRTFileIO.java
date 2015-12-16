/*
*https://code.google.com/p/grtframework/source/browse/trunk/CurrentBot/src/com/grt192/utils/GRTFileIO.java
*/
package org.team3042.sweep.commands;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.microedition.io.Connector;

import com.sun.squawk.io.BufferedReader;
import com.sun.squawk.microedition.io.FileConnection;

public class GRTFileIO {

    public GRTFileIO(){
        
    }
    
        public static String getFileContents(String filename) {
                String url = "file:///" + filename;
                String contents = "";
                try {
                        FileConnection c = (FileConnection) Connector.open(url);
                        BufferedReader buf = new BufferedReader(new InputStreamReader(c
                                        .openInputStream()));
                        String line = "";
                        while ((line = buf.readLine()) != null) {
                                contents += line + "\n";
                        }
                        c.close();
                } catch (IOException e) {
                        e.printStackTrace();
                }
                return contents;
        }

        public static void writeToFile(String filename, String contents) {
            System.out.println("Here");
                String url = "file:///" + filename;
                try {
                        FileConnection c = (FileConnection) Connector.open(url);
                        if(!c.exists()){
                            c.create();
                        }
                        OutputStreamWriter writer = new OutputStreamWriter(c
                                        .openOutputStream());
                        writer.write(contents+"\n");
                        c.close();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
}
