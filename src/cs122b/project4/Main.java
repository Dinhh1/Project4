package cs122b.project4;

import cs122b.models.DPLPDocument;
import cs122b.utils.ConnectionManager;
import cs122b.utils.ThreadUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Main {
    private static String filePath = System.getProperty("user.dir") + "/resource/";
    private static String testFile = "final-data.xml";
    private static String largeFile = "dblp-data.xml";
    private static String fileName = filePath + largeFile;

    public static void main(String[] args) throws Exception {
        ConnectionManager.loadDataSource("root", "", true);
        System.out.println("Starting...");
        CS122BXMLParse parser = new CS122BXMLParse();
        parser.parseFile(new File(fileName));
        parser.addDocuments(parser.getParsedDocument());
        System.out.println("Finishing parsing");
    }
}
