package Utils;

import TestParam.StepWeight;
import TestParam.TestPart;
import TestParam.TestScript;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CSVProcessor {

    private final int numberOfFiles;
    private int totalTest;
    private int totalStep;
    private int totalWeight;
    private int averageWeight;

    // Test Set contains all Test Parts.
    // Test Part contains Test Scripts.
    // Test Script contains all data rows
    public Map<String, ArrayList<String[]>> testSet = new LinkedHashMap<>();
    public ArrayList<TestPart> testPartsList = new ArrayList<>();
    public ArrayList<TestScript> testScriptsList = new ArrayList<>();

    public CSVProcessor(int numberOfFiles){
        this.numberOfFiles = numberOfFiles;
    }

    public Map<String, ArrayList<String[]>> readCsvFile(String csvFile) {
        BufferedReader csvReader = null;
        Pattern textSeparator = Pattern.compile("\\t");
        String readingLine;
        String testName = null;
        boolean readFirstTestName = true;

        try {
            csvReader = new BufferedReader(new InputStreamReader( new FileInputStream(csvFile), StandardCharsets.UTF_16LE));

            ArrayList<String[]> testSteps = new ArrayList<>();
            // Skip the header line, will add later after diving tests
            // noinspection UnusedAssignment
            readingLine = csvReader.readLine();

            while ((readingLine = csvReader.readLine()) != null){
                String[] dataRow = textSeparator.split(readingLine);

                /*  *****Create a HashMap mapping test name and its row****
                    The first test name is assigned to variable
                    It is then compared with consecutive rows
                    Until it reaches the end of the test script
                    The test name is assigned to the new one
                    Column 14 (0->13) is the test name column
                 */
                if (readFirstTestName) {
                    testName = dataRow[13];
                    readFirstTestName = false;
                }

                if (dataRow[13].equals(testName)) {
                    testSteps.add(dataRow);
                } else { // Start the new test script
                    // Create a shallow copy of testSteps ArrayList
                    ArrayList<String[]> allTestSteps = new ArrayList<>(testSteps);
                    // Put the test name and its data row into the HashMap
                    testSet.put(testName, allTestSteps);
                    // Assign new test name to the variable
                    testName = dataRow[13];
                    // Clear the old testSteps and add new data row
                    testSteps.clear();
                    testSteps.add(dataRow);
                }
                // For the last test script, the while loops stops after reading null
                // If do not have this line, the last test script
                // will not be put into the HashMap
                testSet.put(testName, testSteps);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (csvReader != null) {
                try {
                    csvReader.close();
                } catch (IOException ioException){
                    ioException.printStackTrace();
                }
            }
        }

        return testSet;
    }

    public int getAverageWeights(Map<String, ArrayList<String[]>> testSet){
        StepWeight stepWeight = new StepWeight();

        // Iterate every key value, then go through each mapped dataRow
        for (Iterator<String> testName = testSet.keySet().iterator(); testName.hasNext(); totalTest++) {
            String currentTestName = testName.next();
            int currentTestWeight = 0;
            int currentTestStep = 0;
            for (String[] testStep : testSet.get(currentTestName)) {
                currentTestWeight += stepWeight.getWeight(testStep[15]);
                currentTestStep++;
                totalWeight += stepWeight.getWeight(testStep[15]);
                totalStep++;
            }
            testScriptsList.add(new TestScript(currentTestName, currentTestWeight, currentTestStep));
        }

        averageWeight = totalWeight/ numberOfFiles;

        return averageWeight;

        /*
        Print all the Test Script
        for (TestScript testScript : testScriptsList) {
            System.out.println("Test Script " + (testScriptsList.indexOf(testScript)+1) + ": "  + testScript);
        }
*/
    }

    public void divideTest(String testPartName, Map<String, ArrayList<String[]>> testSet) {

        System.out.println("Dividing tests...");

        int averageWeight = getAverageWeights(testSet);
        int currentNumOfTest = 0;
        int currentWeight = 0;
        int currentSteps = 0;
        int currentTestPartNumber = 1;

        // List of Test Script included in the current Test Part
        ArrayList<TestScript> contentTestList = new ArrayList<>();

        for (TestScript testScript : testScriptsList) {
            String testPartNameUpdated = testPartName + "_" + currentTestPartNumber;
            if (currentWeight > averageWeight) {
                // Create a shallow copy of contentTestList ArrayList
                ArrayList<TestScript> fullWeightTestList = new ArrayList<>(contentTestList);
                testPartsList.add(new TestPart(testPartNameUpdated, fullWeightTestList, currentNumOfTest, currentSteps, currentWeight));
                contentTestList.clear();
                contentTestList.add(testScript);
                currentTestPartNumber++;

                currentNumOfTest = 0;
                currentWeight = 0;
                currentSteps = 0;
                currentNumOfTest++;
                currentWeight += testScript.getWeight();
                currentSteps += testScript.getNumSteps();
                continue;
            }
            currentNumOfTest++;
            currentWeight += testScript.getWeight();
            currentSteps += testScript.getNumSteps();
            contentTestList.add(testScript);
        }
        testPartsList.add(new TestPart(testPartName + "_" + numberOfFiles, contentTestList, currentNumOfTest, currentSteps, currentWeight));

/*
        Print all the Test Part and its included Test Script
        for (TestPart testPart : testPartsList) {
            System.out.println("Test Part " + (testPartsList.indexOf(testPart)+1) + ": \n"  + testPart);
        }
*/
    }

    public String getTestScriptContent(TestScript testScript) {
        StringBuilder allTestStepDescription = new StringBuilder();
        ArrayList<String[]> testContent = testSet.get(testScript.getName());
        for (String[] content : testContent) {
            // Problem with column Inputs (R), column 17 (0->16)
            // There's a value "San Francisco, CA 94128-0085;IPH2;IPH3"
            // which is affected by being changed "," to "\t"
            // Therefore, save the cell value, set it to blank, and then add it again after converting
            String columnInputValue = content[17];
            content[17] = "";
            String convertedDataRow = String.join(",", content);
            String[] rowElement = convertedDataRow.split(",");
            rowElement[17] = columnInputValue;
            String finalRow = String.join("\t", rowElement);
            allTestStepDescription.append(finalRow).append("\n");
        }
        return allTestStepDescription.toString();
    }

    public void writeCsvFiles(String outputLocation) {
        System.out.println("Writing files...");
        String header = "Master Suite Name\tProduct\tScrum Team\tTest Set Id\tTest Set Skip\tTest Set Sequence\tTest Script Sequence\tTest Script Id\tTest Script Skip\tTest Script Skip On Prior Abort\tTest Plan ID\tTest Case ID\tTest Set Description\tTest Script Description\tTest Step Description\tAction\tTarget Object\tInputs\tEnd Action\tScreenshot Required\tTest Step Sequence\tTest Step Id\tQA Test Plan Id\tQA Test Plan Name\tTest Set Name";

        for (TestPart testPart : testPartsList) {
            try {
                String outputFileLocation = outputLocation + testPart.getTestPartName() + ".csv";
                File testPartFile = new File(outputFileLocation);
                Writer csvWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(testPartFile), StandardCharsets.UTF_16LE));

                csvWriter.write('\uFEFF');
                csvWriter.write(header);
                csvWriter.append("\n");

                for (TestScript testScript : testPart.getAllTestScripts()) {
                    String dataRow = getTestScriptContent(testScript);
                    csvWriter.append(dataRow);
                }

                csvWriter.flush();
                csvWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Completed!");
    }

    public void summary(String summaryFileLocation) {
        BufferedWriter writer = null;
        try {
            String fileSummaryLocation = summaryFileLocation + "MasterSuiteInfo.txt";
            File fileSummary = new File(fileSummaryLocation);
            boolean fileExist = Files.deleteIfExists(fileSummary.toPath());
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileSummary)));

            writer.write("*******************************\n"
                    + "Master Suite information: "
                    + "\nTotal tests: " + totalTest
                    + "\nTotal steps: " + totalStep
                    + "\nTotal weights: " + totalWeight
                    + "\nAverage weights: " + averageWeight
                    + "\nNumber of divided parts: " + numberOfFiles
                    + "\n*******************************");

            writer.write("\nTest Part information: \n");
            for (TestPart testPart : testPartsList) {
                writer.write("Test Part " + (testPartsList.indexOf(testPart)+1) + ": \n"  + testPart);
                writer.write("*********************************************************************************************\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }
}
