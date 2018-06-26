package io.swagger.api;

public final class APILogger {
	private APILogger() {};
	
	public static void log(String requestResponse, String tag, Object... objects) {
		System.out.println("==========================================");
		System.out.println("API " + requestResponse + ": " + tag);
		System.out.println("------------------------------------------");
		for (Object o : objects) {
			System.out.println(o.toString());
		}
	}
	
	public static void request(String tag, Object... objects) {
		System.out.println("********************************************************************");
		log("Request", tag, objects);
	}
	
	public static void response(String tag, Object... objects) {
		log("Response", tag, objects);
	}
	
	public static void done(String log) {
		System.out.println("==========================================");
		System.out.println(log);
	}
}
