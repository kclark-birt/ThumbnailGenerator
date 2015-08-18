package com.opentext.samplecode;

/*
 * @author Kristopher Clark
 */

import java.io.File;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.actuate.iportal.security.IUploadSecurityAdapter;

/*
 * NOTES:
 * 	The server needs to have the following donwloaded and running
 * 
 * phantomjs-1.98-windows = https://bitbucket.org/ariya/phantomjs/downloads/
 * 		Anything higher than 1.98 if on windows has a bug and this code will not run
 * 
 * selenium-server-standalone-2.47.1.jar = http://selenium-release.storage.googleapis.com/index.html?path=2.47/
 * 		This version is confirmed working on windows
 * 
 * Start these on the server with these commands
 * 		java -jar selenium-server-standalone-2.28.0.jar -role hub -timeout=20 -browserTimeout=25
 * 		phantomjs.exe --webdriver=9192 --max-disk-cache-size=10240 --disk-cache=true --webdriver-selenium-grid-hub=http://127.0.0.1:4444
 * 		phantomjs.exe --webdriver=9191 --max-disk-cache-size=10240 --disk-cache=true --webdriver-selenium-grid-hub=http://127.0.0.1:4444
 */

public class ThumbnailGenerator implements IUploadSecurityAdapter {
	private static final Logger LOGGER = Logger.getLogger(ThumbnailGenerator.class);
	
	private static final String ADMIN_USER      = "Administrator";
	private static final String ADMIN_PASSWORD  = "";
	private static final String IHUB_URL        = "http://localhost";
	private static final String WEB_DRIVER	    = "http://localhost";
	private static final String WEB_DRIVER_PORT = "4444";
	
	private static final String DEMO_REPORT = "%2FApplications%2FSample%20Application%2FReport%20Designs%2FchartExample1.rptdesign";
	private static final String DEMO_URL = IHUB_URL + ":8700/iportal/executereport.do?userid=" + ADMIN_USER + "&password=" + ADMIN_PASSWORD + "&invokeSumit=true&__executableName=";
	
	public static void main(String[] args) {
		try {
			new ThumbnailGenerator();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ThumbnailGenerator() {
		boolean result = verifyFile(null, DEMO_REPORT, null);
	}
	
    private void generateThumbnail(String report) throws Exception {
    	try {
            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
            desiredCapabilities.setJavascriptEnabled(true);
            desiredCapabilities.setCapability("takesScreenshot", true);


            int count = 1;
            WebDriver driver = new RemoteWebDriver(new URL(WEB_DRIVER +":" + WEB_DRIVER_PORT + "/wd/hub"), desiredCapabilities);
            long iStart = System.currentTimeMillis();
            driver.get(DEMO_URL + "" + report);
            driver = new Augmenter().augment(driver);
            
            // Sleep is needed to allow for report to finish executing
            Thread.sleep(5000);
            
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            System.out.println("File:" + srcFile);
            
            // Point this to some location in the iportal
            FileUtils.copyFile(srcFile, new File("c:\\tmp\\screenshot_" + count++ + ".png"));
            
            System.out.println("Single Page Time:" + (System.currentTimeMillis() - iStart));
            driver.quit();
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    }
	
	@Override
	public String getErrorMessage(HttpServletRequest request) {
		return null;
	}

	@Override
	public boolean isFileTypeAllowed(HttpServletRequest request, String fileType) {
		
		return true;
	}

	@Override
	public boolean verifyFile(HttpServletRequest request, String fileName, String dstFolder) {
		// In implementation change include dstFolder in generateTumbnail()
		
		try {
			if(fileName.contains(".rptdesign") || fileName.contains(".dashboard")) {
				generateThumbnail(fileName);
			}else{
				// Do nothing because we can't run it
			}
		} catch (Exception ex) {
			LOGGER.error(ex);
		}
		
		return false;
	}

}
