import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.util.StringTokenizer;
import java.net.Socket;
//import java.net.InetAddress;
//import java.net.InetSocketAddress;
import java.net.URL;

class AwfulDownloader
{
	public AwfulDownloader(String urlText)
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
            
			fileName = getFilename(url.getPath());
			OutputStream outputStream = new FileOutputStream(fileName == null ? "output.txt" : fileName);
			
			long startTime = System.currentTimeMillis();
            String request = "GET " + url.getPath() + " HTTP/1.1\r\n" + "Host: " + url.getHost() + ":" + port +"\r\n";
            PrintWriter pWriter = new PrintWriter(client.getOutputStream(),true);
            pWriter.println(request);
            
			InputStream inputStream = client.getInputStream();
			byte[] buffer = new byte[8 * 1024];
			int bytesNum;
			while((bytesNum = inputStream.read(buffer)) != -1)
			{
				outputStream.write(buffer, 0, bytesNum);
			}
			outputStream.close();
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
		String defaultURL = "http://api64.ipify.org/";
		AwfulDownloader pdl = new AwfulDownloader(args.length == 0 ? defaultURL : args[0]);
	}
	
	private String getFilename(String path)
	{
		StringTokenizer token = new StringTokenizer(path, "/");
		
		while(token.hasMoreTokens())
		{
			fileName = token.nextToken();
		}
		return fileName;
	}

	private String userAgent = "Awful Downloader v0.1";
	private String fileName;
}