package com.manifesting.fileConvertor.Parser;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manifesting.fileConvertor.UtilClasses.GetFunctionFromDms;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Log4j2
@Component
public class VelocityTemplate {

    //TODO : remove hardcoded data with config files and add logs
    @Autowired
    GetFunctionFromDms getFunctionFromDms;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private String GENERATE_START_TAG = "#generateStartTag(\"%s\")\n";
    private String GENERATE_TAG = "#generateTag(\"%s\",%s)\n";
    private String GENERATE_START_TAG_ATTR = "#generateStartTagAttr(\"%s\",%s)\n";
    private String END_TAG = "#endTag(\"%s\")\n";
    private String IF = "#if(%s %s %s)\n";
    private String ELSE = "#else\n";
    private String END = "#end\n";
    private String FOR = "#foreach(%s in %s)\n";
    private String partFile = "#foreach ($var in $MyVariable.getAllPFTrackingId())\n";
    private String batchFile = "#foreach ($var in $MyVariable.getAllBFTrackingId())\n";
    private final Map<?, ?> map;

    public VelocityTemplate () throws IOException {
        map = objectMapper.readValue(Paths.get("C:\\Users\\Nikhil\\Desktop\\Renderer\\JonSpecInputFiles\\json2.json").toFile() , Map.class);
    }


    public String parseVelocityTemplate ( String keyName ) throws IOException {
        StringBuilder jsonTemplateString = new StringBuilder();
        switch (keyName)
        {
            case "partFileElement":
                jsonTemplateString.append(partFile);
                break;
            case "batchFileElement":
                jsonTemplateString.append(batchFile);
                break;
            default:
        }
        parseArrayList((ArrayList<?>) map.get(keyName) , jsonTemplateString);
        jsonTemplateString.append(END);
        return String.valueOf(jsonTemplateString);

    }

    /**
     * @param listToParse        Array list to be parsed
     * @param jsonTemplateString String to store the template
     */
    private void parseArrayList ( ArrayList<?> listToParse , StringBuilder jsonTemplateString ) throws IOException {
        for (Object listObject : listToParse)
            if (listObject instanceof String) {
                String s[] = ((String) listObject).split("\\.");
            } else if (listObject instanceof ArrayList) {
                parseArrayList((ArrayList<?>) listObject , jsonTemplateString);
            } else {
                parseObject((LinkedHashMap<?, ?>) listObject , jsonTemplateString);
            }
    }

    /**
     * @param objectToParse      object to parse
     * @param jsonTemplateString string to store the template
     */
    private void parseObject ( LinkedHashMap<?, ?> objectToParse , StringBuilder jsonTemplateString ) throws IOException {
        if (objectToParse.containsKey("loopOverListDataBlock")) parseLoopBlock(objectToParse , jsonTemplateString);
        else if (objectToParse.containsKey("operator")) parseConditionalBlock(objectToParse , jsonTemplateString);
        else if (objectToParse.containsKey("tag")) parseXMLBlock(objectToParse , jsonTemplateString);
        else if (objectToParse.containsKey("attributeId")) parseDataBlock(objectToParse);
    }

    /**
     * @param attributeMapList attribute list to append
     */
    private String parseAttributeMap ( ArrayList<?> attributeMapList ) throws JsonProcessingException {
        LinkedHashMap<Object, Object> tagAttributeMap = new LinkedHashMap<>();
        HashMap<?, ?> attributeMap = (HashMap<?, ?>) attributeMapList.get(1);
        for (Map.Entry<?, ?> entry : attributeMap.entrySet()) {
            tagAttributeMap.put(entry.getKey() , parseDataBlock((LinkedHashMap<?, ?>) entry.getValue()));
        }
        return objectMapper.writeValueAsString(tagAttributeMap);
    }

    private String parseDataBlock ( LinkedHashMap<?, ?> dataObject ) {
        return getFunctionFromDms.getFunctionFromDms((String) dataObject.get("attributeId"));
    }

    private String parseDataBody ( ArrayList<?> dataBodyList ) {
        for (Object listObject : dataBodyList)
            if (listObject instanceof String) {
            } else if (listObject instanceof ArrayList) {
               return parseDataBody((ArrayList<?>) listObject);
            } else {
                return parseDataBlock((LinkedHashMap<?, ?>) listObject);
            }
        return null;
    }

    private void parseXMLBlock ( LinkedHashMap<?, ?> xmlObject , StringBuilder jsonTemplateString ) throws IOException {
        if ((Boolean) xmlObject.get("dataOnlyBlock")) {
            jsonTemplateString.append(String.format(GENERATE_TAG , xmlObject.get("tag") , parseDataBody((ArrayList <?>) xmlObject.get("body"))));
        } else {
            if (xmlObject.containsKey("tagAttributeMap") && null != xmlObject.get("tagAttributeMap")) {
                jsonTemplateString.append(String.format(GENERATE_START_TAG_ATTR , xmlObject.get("tag") , parseAttributeMap((ArrayList<?>) xmlObject.get("tagAttributeMap"))));
            } else jsonTemplateString.append(String.format(GENERATE_START_TAG , xmlObject.get("tag")));
        }
        parseArrayList((ArrayList<?>) xmlObject.get("body") , jsonTemplateString);
        jsonTemplateString.append(String.format(END_TAG , xmlObject.get("tag")));

    }

    private void parseConditionalBlock ( LinkedHashMap<?, ?> conditionalBlockObject , StringBuilder jsonTemplateString ) throws IOException {
        jsonTemplateString.append(String.format(IF ,
                parseDataBlock((LinkedHashMap<?, ?>) conditionalBlockObject.get("lhsVariable")) ,
                conditionalBlockObject.get("operator") ,
                parseDataBlock((LinkedHashMap<?, ?>) conditionalBlockObject.get("rhsVariable")))
        );
        parseArrayList((ArrayList<?>) conditionalBlockObject.get("body") , jsonTemplateString);
        jsonTemplateString.append(ELSE);
        parseArrayList((ArrayList<?>) conditionalBlockObject.get("elseBody") , jsonTemplateString);
        jsonTemplateString.append(END);
    }


    private void parseLoopBlock ( LinkedHashMap<?, ?> loopBlockObject , StringBuilder jsonTemplateString ) throws IOException {
        jsonTemplateString.append(String.format(FOR ,
                "$var2" , parseDataBlock((LinkedHashMap<?, ?>) loopBlockObject.get("loopOverListDataBlock"))));
        parseArrayList((ArrayList<?>) loopBlockObject.get("body") , jsonTemplateString);
        jsonTemplateString.append(END);
    }
}