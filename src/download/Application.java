package download;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

public class Application {
	private static int threadCount = 50;

	public static void main(String[] args) throws Exception {

		String downloadURL = "http://www.cloudcompare.org/release/CloudCompare_v2.10.2_setup_x64.exe";
		String filePath = "C:\\Users\\lenovo\\Downloads/test.exe";
		downloadFile(downloadURL, filePath);
		downloadFile(downloadURL, filePath, threadCount);
	}

	public static byte[] readInputStream(InputStream inputStream) throws IOException {
		int bufferSize = 1000;
		byte[] buffer = new byte[bufferSize];
		int len = 0;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			byteArrayOutputStream.write(buffer, 0, len);
		}
		byteArrayOutputStream.close();
		return byteArrayOutputStream.toByteArray();
	}

	public static void downloadFile(String downloadURL, String filePath) {
		try {
			Calendar c1 = Calendar.getInstance();
			InputStream inputStream = new URL(downloadURL).openStream();
			byte[] data = readInputStream(inputStream);
			FileOutputStream fileOutputStream = new FileOutputStream(filePath, false);
			fileOutputStream.write(data);
			System.out.println("ok");
			Calendar c2 = Calendar.getInstance();
			System.out.println((c2.getTimeInMillis() - c1.getTimeInMillis()) / 1000.0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void downloadFile(String downloadURL, String filePath, int threadCount) {
		try {
			Calendar startCalendar = Calendar.getInstance();
			URLConnection urlConnection = new URL(downloadURL).openConnection();
			long contentLength = urlConnection.getContentLengthLong();
			urlConnection.setConnectTimeout(100000);

			RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "rw");
			randomAccessFile.setLength(contentLength);
			randomAccessFile.close();

			MonitorThread monitorThread = new MonitorThread(0, threadCount, startCalendar);
			monitorThread.start();
			long blockSize = contentLength / threadCount;
			for (int i = 0; i < threadCount; i++) {
				long startIndex = blockSize * i;
				long endIndex = 0;
				if (i == threadCount) {
					endIndex = contentLength;
				} else {
					endIndex = blockSize * i + blockSize - 1;
				}
				DownloadThread downloadThread = new DownloadThread(downloadURL, filePath, startIndex, endIndex, i,
						monitorThread);
				downloadThread.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
