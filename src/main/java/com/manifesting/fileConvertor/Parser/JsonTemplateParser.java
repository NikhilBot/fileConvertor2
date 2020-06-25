package com.manifesting.fileConvertor.Parser;

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
public class JsonTemplateParser{

    //TODO
    // change hardcoded values with config and constant files and rename some variables
    @Autowired
    GetFunctionFromDms getFunctionFromDms;

    private final Map<?,?> map;

    public JsonTemplateParser () throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        map = mapper.readValue(Paths.get("C:\\Users\\Nikhil\\Desktop\\Renderer\\JonSpecInputFiles\\json2.json").toFile() , Map.class);
    }


    public String getFileFormat(){
        return (String)map.get("fileFormat");
    }
    public String parseJsonTemplate(String keyName) throws IOException {
        StringBuilder jsonTemplateString = new StringBuilder();
        parseArrayList((ArrayList<?>) map.get(keyName) , jsonTemplateString);
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
        else if (objectToParse.containsKey("attributeId")) parseDataBlock(objectToParse , jsonTemplateString);
        else parseAttributeMap(objectToParse , jsonTemplateString);
    }

    /**
     * @param attributeMap       attribute list to append
     * @param jsonTemplateString string to store the template
     */
    private void parseAttributeMap ( HashMap<?, ?> attributeMap , StringBuilder jsonTemplateString ) {
        for (Map.Entry<?, ?> entry : attributeMap.entrySet()) {
            jsonTemplateString.append("Attribute tag: ").append(entry.getKey()).append("\n");
            parseDataBlock((LinkedHashMap<?, ?>) entry.getValue() , jsonTemplateString);
            jsonTemplateString.append("End tag: ").append(entry.getKey()).append("\n");
        }
    }

    private void parseDataBlock ( LinkedHashMap<?, ?> dataObject , StringBuilder jsonTemplateString ) {
        jsonTemplateString.append(getFunctionFromDms.getFunctionFromDms((String) dataObject.get("attributeId"))).append("\n");
    }

    private void parseXMLBlock ( LinkedHashMap<?, ?> xmlObject , StringBuilder jsonTemplateString ) throws IOException {
        jsonTemplateString.append("Start tag: ").append(xmlObject.get("tag")).append("\n");
        if (xmlObject.containsKey("tagAttributeMap") && xmlObject.get("tagAttributeMap") != null) {
            parseArrayList((ArrayList<?>) xmlObject.get("tagAttributeMap") , jsonTemplateString);
        }
        parseArrayList((ArrayList<?>) xmlObject.get("body") , jsonTemplateString);
        jsonTemplateString.append("End tag: ").append(xmlObject.get("tag")).append("\n");
    }

    private void parseConditionalBlock ( LinkedHashMap<?, ?> conditionalBlockObject , StringBuilder jsonTemplateString ) throws IOException {
        jsonTemplateString.append("IF\n");
        parseObject((LinkedHashMap<?, ?>) conditionalBlockObject.get("lhsVariable") , jsonTemplateString);
        jsonTemplateString.append(conditionalBlockObject.get("operator")).append("\n");
        parseObject((LinkedHashMap<?, ?>) conditionalBlockObject.get("rhsVariable") , jsonTemplateString);
        parseArrayList((ArrayList<?>) conditionalBlockObject.get("body") , jsonTemplateString);
        jsonTemplateString.append("ELSE\n");
        if (conditionalBlockObject.containsKey("elseBody"))
            parseArrayList((ArrayList<?>) conditionalBlockObject.get("elseBody") , jsonTemplateString);
        jsonTemplateString.append("ENDIF\n");
    }

    private void parseLoopBlock ( LinkedHashMap<?, ?> loopBlockObject , StringBuilder jsonTemplateString ) throws IOException {
        jsonTemplateString.append("FOR\n");
        parseObject((LinkedHashMap<?, ?>) loopBlockObject.get("loopOverListDataBlock") , jsonTemplateString);
        parseArrayList((ArrayList<?>) loopBlockObject.get("body") , jsonTemplateString);
        jsonTemplateString.append("ENDFOR\n");
    }

}
