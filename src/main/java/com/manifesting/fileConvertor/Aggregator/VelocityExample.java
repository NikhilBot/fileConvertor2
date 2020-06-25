package com.manifesting.fileConvertor.Aggregator;

import com.manifesting.fileConvertor.Parser.VelocityTemplate;
import com.manifesting.fileConvertor.UtilClasses.FunctionUtil;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class VelocityExample {


    @Autowired
    FunctionUtil functionUtil;
    @Autowired
    VelocityTemplate velocityTemplate;

    VelocityEngine ve = new VelocityEngine();
    public void velocityExample() throws IOException {

        String partFileSpec;
        String batchFileSpec;
        partFileSpec = velocityTemplate.parseVelocityTemplate("partFileElement");
        batchFileSpec = velocityTemplate.parseVelocityTemplate("batchFileElement");
        System.out.println(partFileSpec + "\n" + batchFileSpec);
        String sourceDirectory = "C:\\Users\\Nikhil\\Desktop\\Project\\fileConvertor\\src\\main\\resources\\templates";

        FileWriter fw = new FileWriter(sourceDirectory+"\\vel2.vm");
        fw.write(batchFileSpec+partFileSpec);
        fw.close();
        // Create the velocity engine

        ve.setProperty( "resource.loaders", "file");
        ve.setProperty( "resource.loader.file.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
        ve.setProperty( "resource.loader.file.path", sourceDirectory);
        ve.setProperty( "resource.loader.file.cache", true);
        ve.setProperty( "resource.loader.file.modificationCheckInterval", "2");

        ve.init();

        Template t = ve.getTemplate( "vel.vm" );

        VelocityContext context = new VelocityContext();

        context.put("MyVariable", functionUtil );

        try {

            Writer out = new BufferedWriter(new OutputStreamWriter( new FileOutputStream( new File( "C:\\Users\\Nikhil\\Desktop\\Project\\fileConvertor\\velocityOutput.xml" ) ), "UTF8") );
            t.merge( context, out);
            out.close();
        } catch ( Exception e ){
            e.printStackTrace();
        }
    }

}
