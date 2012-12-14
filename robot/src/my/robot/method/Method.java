package my.robot.method;

public abstract class Method {

	protected String host;
	protected String page;
	public abstract String command();
	public Method(String host,String page){
		this.host=host;
		this.page=page;
	}
	
}
