package smvcj;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Log{

	public Log(){

	}

	public static void logWrite( String _msg ){

		Config cfg = new Config();

		Date             date     = new Date();
		SimpleDateFormat dateFM   = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
		String           dateStr  = dateFM.format(date);

		if(cfg.__LOG_MODE__){

			String msg = "["+dateStr+"]"+ _msg;

			System.out.println(msg);

			try{

				BufferedWriter fw = new BufferedWriter(new FileWriter(cfg.__LOG_PATH__,true));

				fw.write(msg);
				fw.newLine();
				fw.flush();
				fw.close();

			}catch(Exception ex){

				System.out.println("LogWrite error : " + ex.getMessage());

			}
		}
	}
}
