package TestParam;

// Test Script contains information about a test case such as name, weight, and number of steps
public class TestScript {
    private final String name;
    private final int weight;
    private final int numSteps;

    public TestScript(String name, int weight, int numSteps) {
        this.name = name;
        this.weight = weight;
        this.numSteps = numSteps;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public int getNumSteps() {
        return numSteps;
    }

    @Override
    public String toString() {
        return "TestScriptDescription{ " +
                "name='" + getName() + '\'' +
                ", weight=" + getWeight() +
                ", numSteps=" + getNumSteps() +
                " }";
    }
}
