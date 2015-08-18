# ThumbnailGenerator
Automatic Thumbnail Generation for iHub

This override's the default iHub IUploadFilter and intercepts the file being uploaded after it has been saved to disc.  By using Phantom JS and Selenium you can create a headless browser and auto-generate a thumbnail for a report or dashboard.

# iHub Integration

Upload the class or jar to

```<context root>\WEB-INF\lib```

Add or change the following in iPortal's web.xml

```<param-name>UPLOAD_SECURITY_ADAPTER< / param-name>```
```<param-value>com.opentext.samplecoe.ThumbnailGenerator< / param-value>```

#IMPORTANT#
* The server needs to have the following donwloaded and running

* phantomjs-1.98 can be downloaded from https://bitbucket.org/ariya/phantomjs/downloads/
Anything higher than 1.98 if on windows has a bug and this code will not run

* Selenium-server-standalone can be downloaded from http://selenium-release.storage.googleapis.com/index.html?path=2.47/
This version is confirmed working on windows

* Start these on the server with these commands

```java -jar selenium-server-standalone-2.28.0.jar -role hub -timeout=20 -browserTimeout=25```

```phantomjs.exe --webdriver=9192 --max-disk-cache-size=10240 --disk-cache=true --webdriver-selenium-grid-hub=http://127.0.0.1:4444```

```phantomjs.exe --webdriver=9191 --max-disk-cache-size=10240 --disk-cache=true --webdriver-selenium-grid-hub=http://127.0.0.1:4444```
