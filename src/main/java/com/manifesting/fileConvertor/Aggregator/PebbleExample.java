package com.manifesting.fileConvertor.Aggregator;

import com.manifesting.fileConvertor.Parser.PebbleTemplateParser;
import com.manifesting.fileConvertor.UtilClasses.FunctionUtil;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.FileLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class PebbleExample {

    @Autowired
    FunctionUtil functionUtil;
    @Autowired
    PebbleTemplateParser pebbleTemplateParser;
    public void pebbleTemplateExample() throws IOException {

        String partFileSpec;
        String batchFileSpec;
        partFileSpec = pebbleTemplateParser.parsePebbleTemplate("partFileElement");
        batchFileSpec = pebbleTemplateParser.parsePebbleTemplate("batchFileElement");
        System.out.println(partFileSpec + "\n" + batchFileSpec);
        String sourceDirectory = "C:\\Users\\Nikhil\\Desktop\\Project\\fileConvertor\\src\\main\\resources\\templates";

        FileWriter fw = new FileWriter(sourceDirectory+"\\peb2.pebble");
        fw.write(batchFileSpec+partFileSpec);
        fw.close();

        FileLoader loader = new FileLoader();
        loader.setPrefix("C:\\Users\\Nikhil\\Desktop\\Project\\fileConvertor\\src\\main\\resources\\templates");
        loader.setSuffix(".pebble");
        PebbleEngine engine = new PebbleEngine.Builder().loader(loader).build();

        PebbleTemplate compiledTemplate = engine.getTemplate("peb");

        Map<String, Object> context = new HashMap<>();
        context.put("MyVariable", functionUtil);

        try {
            // Create the output file
            Writer out = new BufferedWriter(new OutputStreamWriter( new FileOutputStream( new File( "C:\\Users\\Nikhil\\Desktop\\Project\\fileConvertor\\pebbleOutput.xml" ) ), "UTF8") );
            compiledTemplate.evaluate(out, context);
            out.close();
        } catch ( Exception e ){
            e.printStackTrace();
        }

    }
}
