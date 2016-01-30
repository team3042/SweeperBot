/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3042.sweep;

import com.sun.squawk.microedition.io.FileConnection;
import java.io.IOException;
import java.io.PrintStream;
import javax.microedition.io.Connector;

/**
 *
 * @author Robotics
 */
public class FileIO {
    
    FileConnection file;
    PrintStream writer;
    
    public boolean openFile(String filename) {
        String url = "file:///" + filename;
        boolean succeed = false;
        try {
            file = (FileConnection) Connector.open(url);
            if (file.exists()) {
                file.delete();
                //file.close();
                //file = (FileConnection) Connector.open(url);
            }
            file.create();
            writer = new PrintStream(file.openOutputStream());
            succeed = true;
        }   catch (IOException ex) {
                System.out.println("Could not open file");
            }
        return succeed;
    }
    
    public void writeToFile(String content) {
        writer.println(content);
        writer.flush();
    }
    
    public void closeFile() {
        try {
            file.close();
        } catch (IOException ex) {}
    }
    
}
