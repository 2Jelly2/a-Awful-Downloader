import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.FileOutputStream;
//import java.util.ArrayList;
import java.util.StringTokenizer;
import java.net.URL;
import java.net.Socket;
//import java.net.InetAddress;
//import java.net.InetSocketAddress;

public class AwfulDownloader
{
	public AwfulDownloader(String urlText)
	{
		userAgent = "Awful Downloader v0.2";
		download(urlText);
	}
	
	public void download(String urlText)
	{
		try
		{
			URL url = new URL(urlText);
			int port = url.getPort() == -1 ? url.getDefaultPort() : url.getPort();
			Socket client = new Socket(url.getHost(), port);
			
			/*
			Socket client = new Socket();
            InetSocketAddress socketAddr = new InetSocketAddress(url.getHost(), port);
            client.connect(socketAddr);
            */
			
            String request = "GET " + url.getPath() + " HTTP/1.1\r\n";
            request += "Host: " + url.getHost() + ":" + port +"\r\n";
            request += "Connection: close\r\n";
            request += "User-Agent: " + userAgent + "\r\n";
            PrintWriter pWriter = new PrintWriter(client.getOutputStream(), true);
            pWriter.println(request);

			long startTime = System.currentTimeMillis();
			InputStream inputStream = client.getInputStream();
			OutputStream outputStream = new FileOutputStream(getFilename(url.getPath()));
			
			// Skip until contents
			// TBD: range for all escape chars that might appear?
			int bytesNum;
			byte buffer[] = new byte[1];
			//ArrayList<String> headers = new ArrayList<String>();
			//String temp = "";
			byte[] divider = {13, 10, 13, 10};
			int counter = 0;
			while((bytesNum = inputStream.read(buffer)) != -1)
			{
				if(buffer[0] == 13 || buffer[0] == 10)
				{
					if(buffer[0] == divider[counter])
					{
						counter ++;
						if(counter == divider.length)
						{
							break;
						}
					}
				}
				else
				{
					counter = 0;
				}
			}
			
			// Output contents to a file
			buffer = new byte[8 * 1024];
			while((bytesNum = inputStream.read(buffer)) != -1)
			{
				outputStream.write(buffer, 0, bytesNum);
				//buffer = inputStream.available() < buffer.length ? new byte[inputStream.available()] : new byte[8 * 1024];
			}
			
			client.close();
			System.out.println("Download Complete.\nTime Cost: " + (System.currentTimeMillis() - startTime) + "ms");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String args[])
	{
		//String defaultURL = "https://mirrors.neusoft.edu.cn/eclipse/oomph/epp/2020-09/R/eclipse-inst-jre-win64.exe";
		//String defaultURL = "https://osananajimi.moe/content/images/size/w2000/2020/07/DSC01321.jpg";
		//String defaultURL = "http://api64.ipify.org/";
		//String defaultURL = "http://lain.bgm.tv/pic/cover/l/7f/4e/209615_iAw8I.jpg";
		String defaultURL = "http://localhost:7788/op.txt";
		
		//AwfulDownloader pdl0 = new AwfulDownloader(args.length == 0 ? defaultURL : args[0]);
		new AwfulDownloader(args.length == 0 ? defaultURL : args[0]);
	}
	
	private String getFilename(String path)
	{
		String fileName = new String();
		StringTokenizer token = new StringTokenizer(path, "/");
		
		while(token.hasMoreTokens())
		{
			fileName = token.nextToken();
		}
		return fileName == null ? "output.txt" : fileName;
	}
	private String userAgent;
}