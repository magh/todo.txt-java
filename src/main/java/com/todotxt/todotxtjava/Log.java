package com.todotxt.todotxtjava;

public class Log {

	public static void w(String tag, String msg){
		System.out.println("W/"+tag+" "+msg);
	}

	public static void w(String tag, String msg, Throwable t){
		System.out.println("W/"+tag+" "+msg+" "+t);
		t.printStackTrace();
	}

	public static void e(String tag, String msg){
		System.out.println("E/"+tag+" "+msg);
	}

	public static void e(String tag, String msg, Throwable t){
		System.out.println("E/"+tag+" "+msg+" "+t);
		t.printStackTrace();
	}

}
