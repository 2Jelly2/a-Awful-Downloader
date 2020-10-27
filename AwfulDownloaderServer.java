import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;

public class AwfulDownloaderServer extends Thread
{
	public AwfulDownloaderServer()
	{
		userAgent = "Awful Downloader Server v0.1";
		port = 7788;
	}
	
	public static void main(String args[])
	{
		new AwfulDownloaderServer().start();
	}

	@Override
	public void run()
	{
		try
		{
			server = new ServerSocket(port);
			while(true)
			{
                response(server.accept());
            }
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void response(Socket client)
	{
		try
		{
			resolve(client.getInputStream());
			
			OutputStream outputStream = client.getOutputStream();
			outputStream.write("HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=utf-8\r\n\r\n".getBytes());

			InputStream inputStream = new FileInputStream(requestPath.replaceFirst("/", ""));
			byte[] buffer = new byte[8 * 1024];
			int bytesNum;
			while((bytesNum = inputStream.read(buffer)) != -1)
			{
				outputStream.write(buffer, 0, bytesNum);
			}
			inputStream.close();
			
			outputStream.write("\r\n".getBytes());
			client.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void resolve(InputStream header)
	{
		String[] requestPara;
		try
		{
			byte[] buffer = new byte[8 * 1024];
			int bytesNum;
			String headerText = "";
			while((bytesNum = header.read(buffer)) != -1)
			{
				headerText += new String(buffer, "UTF-8");
			}
			//requestPara = new String(header.readNBytes(header.available()), "UTF-8").replaceAll("\r\n", " ").split(" ");
			requestPara = headerText.replaceAll("\r\n", " ").split(" ");
			requestMethod = requestPara[0];
			requestPath = requestPara[1];
			requestProt = requestPara[2];
			requestSrc = requestPara[3];
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private String userAgent;
	private int port;
	private ServerSocket server;
	private String requestMethod, requestPath, requestProt, requestSrc;
}