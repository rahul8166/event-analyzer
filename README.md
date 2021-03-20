# Events Analyzer

Analayze the logfile.txt and calculate the time duration for the event.
If the duration is more than 4ms then flag it as an alert.
Put all events data to HSQLDB (file based) with below details,
* Event ID
* Event Duration
* Type and/or Host if present
* Alert - if the event took longer than 4ms then true else false

## How to run?
* Generate a sample /apps/logs/logfile.txt,
    
        <windows>.\gradlew.bat run generateSampleLogFile
        <linux>.\gradlew run generateSampleLogFile

* Run Log-Analyzer
  
        <windows>.\gradlew.bat run --args="/apps/logs/"
        <linux>.\gradlew run --args="/apps/logs/"