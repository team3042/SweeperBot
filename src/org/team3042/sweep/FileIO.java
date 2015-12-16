/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3042.sweep;

import com.sun.squawk.microedition.io.FileConnection;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import javax.microedition.io.Connector;



/**
 *
 * @author Robotics
 */
public class FileIO {
    
    FileConnection c;
    PrintStream writer;
    
    public void openFile(String filename) {
        String url = "file:///" + filename;
        try {
            c = (FileConnection) Connector.open(url);
            if (c.exists()) {
                c.delete();
                c.close();
                c = (FileConnection) Connector.open(url);
            }
            c.create();
            writer = new PrintStream(c.openOutputStream());
        }   catch (IOException c) {
            c.printStackTrace();
        }
    }
    
    public void writeToFile(String content) {
        writer.println(content);
        writer.flush();
    }
    
    public void closeFile() {
        try {
            c.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
