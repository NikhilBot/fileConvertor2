package com.manifesting.fileConvertor.Aggregator;

import com.manifesting.fileConvertor.UtilClasses.FunctionUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Log4j2
@Component
public class TagAggregator implements CommandLineRunner {

    @Autowired
    FunctionUtil functionUtil;

    private List<String> objList;
    private String listName;
    private final Stack<Map<?, ?>> mapStack = new Stack<>();
    private final Stack<String> stringStack = new Stack<>();
    private final ArrayList<Map<?, ?>> arrayMap;

    TagAggregator () {
        objList = new ArrayList<>();
        arrayMap = new ArrayList<>();
        listName = null;
    }


    public HashMap<Object, Object> aggregatePartFile ( String jsonTemplateString ) throws IOException {
        getPFObjList();
        for (String objectId : objList) {
            Scanner templateScanner = new Scanner(String.valueOf(jsonTemplateString));
            while (templateScanner.hasNextLine()) {
                String line = templateScanner.nextLine();
                if (line.startsWith("IF")) {
                    conditionalAggregator(templateScanner , objectId);
                } else if (line.startsWith("FOR")) {
                    loopAggregator(templateScanner , objectId);
                } else
                    addToMap(line , objectId);
            }
            templateScanner.close();
            arrayMap.add(mapStack.pop());
        }
        Map<String, ArrayList> map = new HashMap<>();
        map.put(listName , arrayMap);
        HashMap<Object, Object> map3 = new HashMap<>();
        map3.put("manifestDetail" , map);
        return map3;
    }


    public Map<Object, Object> aggregateBatchFile ( String temp ) throws IOException {
        listName = null;
        getBFObjList();
        for (String s : objList) {
            Scanner tempScanner = new Scanner(String.valueOf(temp));
            while (tempScanner.hasNextLine()) {
                String line = tempScanner.nextLine();
                addToMap(line , s);
            }
            tempScanner.close();
        }
        HashMap<Object, Object> map3 = new HashMap<>();
        map3.put(listName , mapStack.pop());
        return map3;
    }


    private void addToMap ( String lineToAdd , String objectId ) throws IOException {

        if (lineToAdd.startsWith("Start")) {
            mapStack.push(new HashMap<>());
            String tagName = lineToAdd.split(" ")[2];
            if (null == listName || tagName.equals(listName)) listName = tagName;
            else stringStack.push(tagName);

        } else if (lineToAdd.startsWith("Attribute")) {
            mapStack.push(new HashMap<>());
            stringStack.push(("-" + lineToAdd.split(" ")[2]));
        } else if (lineToAdd.startsWith("End")) {
            if (mapStack.size() > 1) {
                Map<Object, Object> redundantMap = (Map<Object, Object>) mapStack.pop();
                Map<Object, Object> mapToPush = (Map<Object, Object>) mapStack.peek();
                stringStack.pop();
                if (stringStack.empty()) {
                    for (Map.Entry<?, ?> entry : redundantMap.entrySet())
                        mapToPush.put(entry.getKey() , entry.getValue());
                } else {
                    String tagName = stringStack.peek();
                    if (!mapToPush.containsKey(tagName))
                        mapToPush.put(tagName , redundantMap);
                    else {
                        Map<Object, Object> mapToAdd = (Map<Object, Object>) mapToPush.get(tagName);
                        for (Map.Entry<?, ?> entry : redundantMap.entrySet())
                            mapToAdd.put(entry.getKey() , entry.getValue());
                    }
                }

            }
        } else {
            Scanner lineScanner = new Scanner(lineToAdd);
            String value = getFunctionResult(objectId , lineScanner.next());
            Map<String, String> tempMap = (Map<String, String>) mapStack.peek();
            String tagName = stringStack.peek();
            tempMap.put(tagName , value);
            lineScanner.close();

        }
    }

    private void conditionalAggregator ( Scanner templateScanner , String objectId ) throws IOException {
        String lineToAdd;
        if (evaluateCondition(objectId , templateScanner)) {
            while (templateScanner.hasNextLine()) {
                lineToAdd = templateScanner.nextLine();
                if (!lineToAdd.startsWith("ENDIF") && !lineToAdd.startsWith("ELSE")) addToMap(lineToAdd , objectId);
                else {
                    while (!templateScanner.nextLine().startsWith("ENDIF")) ;
                    break;
                }
            }
        } else {
            while (!templateScanner.nextLine().startsWith("ELSE")) ;
            while (templateScanner.hasNextLine()) {
                lineToAdd = templateScanner.nextLine();
                if (!lineToAdd.startsWith("ENDIF")) addToMap(lineToAdd , objectId);
                else break;
            }
        }
    }

    private void loopAggregator ( Scanner templateScanner , String objectId ) throws IOException {
        String functionName = templateScanner.nextLine();
        ArrayList<?> result = getListResult(objectId , functionName);
        StringBuilder loopString = new StringBuilder();
        String lineToAppend = templateScanner.nextLine();
        while (!lineToAppend.startsWith("ENDFOR")) {
            loopString.append(lineToAppend).append("\n");
            lineToAppend = templateScanner.nextLine();
        }
        for (Object listObjectId : result) {
            Scanner loopStringScanner = new Scanner(String.valueOf(loopString));
            while (loopStringScanner.hasNextLine()) {
                String loopStringLine = loopStringScanner.nextLine();
                if (loopStringLine.startsWith("ENDFOR")) break;
                if (loopStringLine.startsWith("Start") || loopStringLine.startsWith("End")) {
                    addToMap(loopStringLine , objectId);
                } else {
                    functionName = new Scanner(loopStringLine).next();
                    String value = getFunctionResult(objectId , listObjectId , functionName);
                    Map<String, String> tempMap = (Map<String, String>) mapStack.peek();
                    String tagName = stringStack.peek();
                    tempMap.put(tagName , value);
                }
            }
        }
    }

    private String getFunctionResult ( String objectId , Object listObjectId , String functionName ) {
        String result = "";
        try {
            Object[] obj = {objectId , listObjectId};
            Class<?>[] params = new Class[obj.length];
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] instanceof Long) {
                    params[i] = Long.TYPE;
                } else if (obj[i] instanceof String) {
                    params[i] = String.class;
                }
            }
            result = (String) functionUtil.getClass().getDeclaredMethod(functionName , params).invoke(functionUtil , obj);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.info("The method was not found");
            //e.printStackTrace();
        }
        return result;
    }


    private String getFunctionResult ( String objectId , String functionName ) {
        String result = "";
        try {
            Object[] obj = {objectId};
            Class<?>[] params = new Class[obj.length];
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    params[i] = String.class;
                }
            }
            result = (String) functionUtil.getClass().getDeclaredMethod(functionName , params).invoke(functionUtil , obj);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.info("The method was not found");
            //e.printStackTrace();
        }
        return result;
    }

    private ArrayList<?> getListResult ( String objectId , String functionName ) {
        List<?> result = new ArrayList<>();
        try {
            Object[] obj = {objectId};
            Class<?>[] params = new Class[obj.length];
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    params[i] = String.class;
                }
            }
            result = (ArrayList<?>) functionUtil.getClass().getDeclaredMethod(functionName , params).invoke(functionUtil , obj);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.info("The method was not found");
            // e.printStackTrace();
        }
        return (ArrayList<?>) result;
    }


    boolean evaluateCondition ( String objectId , Scanner templateScanner ) {
        String lhsVariable, rhsVariable, operator;
        lhsVariable = getFunctionResult(objectId , templateScanner.nextLine());
        operator = templateScanner.nextLine();
        rhsVariable = getFunctionResult(objectId , templateScanner.nextLine());
        switch (operator) {
            case "EQUAL":
                return lhsVariable.equals(rhsVariable);
            case "NOT_EQUAL":
                return !lhsVariable.equals(rhsVariable);
            case "LESS_THAN":
                return lhsVariable.compareTo(rhsVariable) < 0;
            case "GREATER_THAN":
                return !(lhsVariable.compareTo(rhsVariable) < 0);
            case "GREATER_OR_EQUAL":
                return lhsVariable.compareTo(rhsVariable) <= 0;
            case "LESS_OR_EQUAL":
                return !(lhsVariable.compareTo(rhsVariable) <= 0);
        }
        return true;
    }

    private void getPFObjList () {
        objList = functionUtil.getAllPFTrackingId();
    }

    private void getBFObjList () {
        objList = functionUtil.getAllBFTrackingId();
    }

    @Override
    public void run ( String... args ) throws Exception {

    }
}
