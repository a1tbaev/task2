private static final int RECORDS_PER_FILE = 4; for json count 

private static final String LOG_FILES_DIR = System.getProperty("user.home") + "/Desktop/parser/";  here will be create new files
    
private static final String STATE_FILE = System.getProperty("user.home") + "/Desktop/parser/state.txt";  for state. here will be save last checked json

http://127.0.0.1:9091/convert  -> host for postman
Headers - Key -> Content-Type,       value --> application/xml

Body - raw, XML
