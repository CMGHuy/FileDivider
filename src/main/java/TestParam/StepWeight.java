package TestParam;

import java.util.*;

public class StepWeight {

    public StepWeight(){
        initialization();
    }

    // Weight values. It is considered as the measurement of how long the test step would take.
    private static final int FAST = 1;
    private static final int MEDIUM = 3;
    private static final int SLOW = 5;
    private static final int VERY_SLOW = 10;
    private static final int SUPER_SLOW = 30;

    List<String> fastKeyword = Arrays.asList(
        "ClickButton"
        ,"ClickLink"
        ,"GetValue"
        ,"InvokeAppletMenuItem"
        ,"InvokeMenuBarItem"
        ,"InvokeObject"
        ,"SelectCheckBox"
        ,"SelectPicklistValue"
        ,"SelectRecordinListApplet"
        ,"SelectVisibilityFilterValue"
        ,"VerifyError"
        ,"VerifyRecordCount"
        ,"VerifyState"
        ,"VerifyValue"
    );
    
    List<String> mediumKeyword = Arrays.asList(
        "InvokeObject"
        ,"GoToView"
        ,"HierarchicalList"
        ,"InputValue"
        ,"SendKeys"
        ,"SetDateTime"
        ,"Launch"
        ,"LogOut"
    );

    List<String> slowKeyword = Arrays.asList(
        "MultiSelectRecordsinListApplet"
        ,"SortColumn"
        ,"TreeExplorer"
        ,"Wait"
    );

    List<String> verySlowKeyword = Arrays.asList(
        "FileDownload"
    );

    List<String> superSlowKeyword = Arrays.asList(
        "InvokePerl"
    );

    Map<String, Integer> stepWeight = new HashMap<>();

    public void initialization(){
        addWeight(fastKeyword, FAST);
        addWeight(mediumKeyword, MEDIUM);
        addWeight(slowKeyword, SLOW);
        addWeight(verySlowKeyword, VERY_SLOW);
        addWeight(superSlowKeyword, SUPER_SLOW);
    }

    public void addWeight(List<String> listKeyword, int weightValue){
        for(String keyword : listKeyword){
            stepWeight.put(keyword, weightValue);
        }
    }

    public int getWeight(String keyword){
        return stepWeight.getOrDefault(keyword, 1);
    }
}
