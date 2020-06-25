package com.manifesting.fileConvertor.UtilClasses;

import com.manifesting.fileConvertor.database.dms.entity.DMSModel;
import com.manifesting.fileConvertor.database.dms.repository.DMSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class GetFunctionFromDms implements CommandLineRunner {


    @Override
    public void run ( String... args ) throws Exception {

    }

    @Autowired
    DMSRepository dmsRepository;

    public String getFunctionFromDms ( String id ) {
        DMSModel obj = dmsRepository.findById(id).orElse(null);
        if (null != obj)
            return obj.get_function();
        else return "null";
    }


}
