import Utils.CSVProcessor;

import java.util.ArrayList;
import java.util.Map;

public class TestsDistribution {

    public static void main(String[] args) {
        String inputFile = args[0];
        String outputLocation = args[1];
        String outputFileName = args[2];
        String fileNum = args[3];

        /* -------- Example texts --------
        String inputFile = "C:\\Users\\h.cao\\Desktop\\Intellij\\FileDivider\\src\\main\\resources\\fileTest\\C2CTestSuiteINT_noduplicate.csv";
        String outputPath = "C:\\Users\\h.cao\\Desktop\\Intellij\\FileDivider\\src\\main\\java\\";
        String outputFileName = "test";
        String fileNum = "10";
        */

        // Read csv file
//        String inputFile = "C:\\Users\\h.cao\\Desktop\\Intellij\\FileDivider\\src\\main\\resources\\fileTest\\C2CTestSuiteINT.csv";
//        String outputLocation = "C:\\Users\\h.cao\\Desktop\\Intellij\\FileDivider\\src\\main\\resources\\fileTest\\";
//        String outputFileName = "test";
//        String fileNum = "10";

        int fileNumInt = Integer.parseInt(fileNum);

        Map<String, ArrayList<String[]>> testSet;

        CSVProcessor processor = new CSVProcessor(fileNumInt);
        System.out.println("Reading file...");
        testSet = processor.readCsvFile(inputFile);
        processor.divideTest(outputFileName, testSet);
        processor.writeCsvFiles(outputLocation);
        processor.summary("/home/ubuntu/mnt/executable/fileDivider/");
//        processor.summary("C:\\Users\\h.cao\\Desktop\\Intellij\\FileDivider\\src\\main\\resources\\fileTest\\");
    }
}
