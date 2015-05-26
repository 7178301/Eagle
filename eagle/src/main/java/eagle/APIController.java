package eagle;

import eagle.sdkInterface.positioning.AbsolutePosition;

public abstract class APIController{
	static private String apiVersion = "0.0.1";
	private AbsolutePosition home;
	private AbsolutePosition currentPosition;
	
	public String getAPIVersion(){
		return apiVersion;
	}
}
