package TestParam;

import java.util.ArrayList;

/* Test Part contains information about:
    Each divided test parts
    Which test scripts are included
    Total tests, steps, and weights
 */
public class TestPart {
    private final String testPartName;
    private final ArrayList<TestScript> contentTest;
    private final int totalTest;
    private final int totalStep;
    private final int totalWeight;

    public TestPart(String testPartName, ArrayList<TestScript> contentTest, int totalTest, int totalStep, int totalWeight) {
        this.testPartName = testPartName;
        this.contentTest = contentTest;
        this.totalTest = totalTest;
        this.totalStep = totalStep;
        this.totalWeight = totalWeight;
    }


    public String getContentTestDescription() {
        StringBuilder allTestDescription = new StringBuilder();

        for (TestScript testScript : contentTest) {
            String testScriptNumber = "Test Script " + (contentTest.indexOf(testScript) + 1) + ": ";
            allTestDescription.append(testScriptNumber)
                              .append(testScript.toString())
                              .append("\n");
        }

        return allTestDescription.toString();
    }

    public String getTestPartName() {
        return testPartName;
    }

    public ArrayList<TestScript> getAllTestScripts() {
        return contentTest;
    }

    public int getTotalTest() {
        return totalTest;
    }

    public int getTotalStep() {
        return totalStep;
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    public TestScript getTestScript(int index) {
        return contentTest.get(index);
    }

    @Override
    public String toString() {
        return "TestPart{" +
                " testPartName='" + getTestPartName() + '\'' +
                ", totalTest=" + getTotalTest() +
                ", totalStep=" + getTotalStep() +
                ", totalWeight=" + getTotalWeight() +
                ", contentTest=" +
                "\n{\n" + getContentTestDescription() +
                "}\n";
    }
}
