package smvcj;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Config
{
	private volatile static Config Instance = new Config();

	//Document root path
	public String __DOC_ROOT__;

	//Service information
	public String __SERVICE_NAME__;
	public String __SERVICE_HOST__;

	//Database information
	public String __DB_HOST__;
	public String __DB_NAME__;
	public String __DB_CHAR__;
	public String __DB_USER__;
	public String __DB_PASS__;

	//Log information
	public boolean __LOG_MODE__;
	public String  __LOG_PATH__;

	//Path information
	public String __CONTROLLER_BACKEND__;
	public String __CONTROLLER_UI__     ;
	public String __MODEL__             ;

	//URL information
	public String __URL_CTL_BACKEND__;
	public String __URL_CTL_UI__     ;
	public String __CSS__;
	public String __IMG__;
	public String __JS__ ;

	public Config()
	{

		__DOC_ROOT__     = "E:/SMVC4JSP/WebContent/";

		__SERVICE_NAME__ = "smvcj";
		__SERVICE_HOST__ = "http://localhost:9080/smvcj/";

		__DB_HOST__      = "localhost";
		__DB_NAME__      = "db_test";
		__DB_USER__      = "root";
		__DB_PASS__      = "snpu04";
		__DB_CHAR__      = "utf8";

		Date             date     = new Date();
		SimpleDateFormat format   = new SimpleDateFormat( "yyyyMMdd", Locale.KOREA);
		String           yyyyMMdd = format.format( date);

		__LOG_MODE__     = true;
		__LOG_PATH__     = __DOC_ROOT__ + "_log/log_" + yyyyMMdd + ".txt";

		__CONTROLLER_BACKEND__ = __DOC_ROOT__ + "__controller_backend/";
		__CONTROLLER_UI__      = __DOC_ROOT__ + "__controller_ui/";
		__MODEL__              = __DOC_ROOT__ + "__model/";

		__URL_CTL_BACKEND__    = __SERVICE_HOST__ + "__controller_backend/";
		__URL_CTL_UI__         = __SERVICE_HOST__ + "__controller_ui/";
		__CSS__                = __SERVICE_HOST__ + "css";
		__IMG__                = __SERVICE_HOST__ + "img";
		__JS__                 = __SERVICE_HOST__ + "js";

	}

	public static Config getInstance()
	{
		return Instance;
	}
}
