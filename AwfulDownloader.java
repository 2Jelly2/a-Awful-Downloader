import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.util.StringTokenizer;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import javax.net.ssl.HttpsURLConnection;

class AwfulDownloader
{
	public AwfulDownloader(String urlText)
	{
		try
		{
			URL url = new URL(urlText);
			URLConnection coNe;
			
			switch(url.getProtocol())
			{
				case "http":
				{
					HttpURLConnection coNeHTTP = (HttpURLConnection) url.openConnection();
					coNeHTTP.setRequestMethod("GET");
					coNeHTTP.setRequestProperty("User-Agent", userAgent);
					//System.out.println("Response Code: " + coNeHTTP.getResponseCode());
					coNe = coNeHTTP;
					break;
				}
				case "https":
				{
					HttpsURLConnection coNeHTTPS = (HttpsURLConnection) url.openConnection();
					coNeHTTPS.setRequestMethod("GET");
					coNeHTTPS.setRequestProperty("User-Agent", userAgent);
					//System.out.println("Response Code: " + coNeHTTPS.getResponseCode());
					coNe = coNeHTTPS;
					System.out.println(coNe.getRequestProperties());
					System.out.println(coNeHTTPS.getRequestProperties());
					break;
				}
				default:
				{
					System.out.println("Unsupported Protocol!");
					return;
				}
			}
			fileName = getFilename(url.getPath());
			OutputStream outputStream = new FileOutputStream(fileName == null ? "output.txt" : fileName);
			
			long startTime = System.currentTimeMillis();
			InputStream inputStream = coNe.getInputStream();
			byte buffer[] = new byte[inputStream.available()];
			int bytesNum;
			while((bytesNum = inputStream.read(buffer)) > 0)
			{
				outputStream.write(buffer, 0, bytesNum);
			}
			outputStream.close();
			System.out.println("Download Complete.\nTime Cost: " + (System.currentTimeMillis() - startTime));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String args[])
	{
		//String defaultURL = "https://osananajimi.moe/content/images/size/w2000/2020/07/DSC01321.jpg";
		String defaultURL = "https://api64.ipify.org/";
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