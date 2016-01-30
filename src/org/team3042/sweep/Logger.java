/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3042.sweep;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author NewUser
 */
public class Logger extends FileIO{
    private static final String FILE_DATE_FORMAT = "yyyy-MM-dd-hhmmss";
	private static final String LOG_TIME_FORMAT = "hh:mm:ss";
	
	String dir = "log/";
	
	boolean useConsole;
	boolean useFile;
	int level;
	String cls;
	
	public Logger(boolean useConsole, boolean useFile, int level) {
                this.useConsole = useConsole;
		this.useFile = useFile;
		this.level = level;
                
		if(useFile) {
			//Naming file with timestamp
			Date now = new Date();
                        String time = now.toString();
                        String filename = "logs/" + time.substring(4).replace(' ', '-').replace(':','-');
			
			this.useFile = this.openFile(filename);
		}
		
	}
	
	public void log(String message, int level) {
		
		this.level = (int) SmartDashboard.getNumber("Logger level");
		
		if(level <= this.level) {
			
			//Adding timestamp to log
			Date now = new Date();
			String time = now.toString().substring(11, 19);
			
			message = time + "\t" + message;
                        
			if(useConsole) {
				System.out.println(message);
			}
			if(useFile) {
				this.writeToFile(message);
			}
		}
	}
}
