package my.robot.method;

public class Get extends Method{

	public Get(String host,String page) {
		super(host,page);
	}

	@Override
	public String command() {
		StringBuffer sb = new StringBuffer();
		sb.append("GET "+page+" HTTP/1.0\r\n");
		sb.append("HOST:"+host+"\r\n");
		sb.append("\r\n");
		return sb.toString();
	}

}
