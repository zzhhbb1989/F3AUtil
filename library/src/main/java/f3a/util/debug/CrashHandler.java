package f3a.util.debug;

import android.util.Log;

import java.lang.Thread.UncaughtExceptionHandler;

import f3a.util.storage.SDCardLogUtil;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告. 
 *
 * @author user
 *
 */
public class CrashHandler implements UncaughtExceptionHandler {
	
	public static final String TAG = "CrashHandler";
	
	//系统默认的UncaughtException处理类   
	private UncaughtExceptionHandler mDefaultHandler;
	//CrashHandler实例  
	private static CrashHandler instance = new CrashHandler();
	//程序的Context对象  
	
	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
	}
	
	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		return instance;
	}
	
	/**
	 * 当UncaughtException发生时会转入该函数来处理 
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
        SDCardLogUtil.formattedLogCrash(Log.getStackTraceString(ex));
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
	}
}