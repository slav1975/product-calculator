package pl.reactive11.product_calculator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

import static java.nio.file.Files.readAllBytes;

public class TestUtils {

    private static ObjectMapper mapper = new ObjectMapper();


    public static String getContent(String fileName){
        try {
            File resource = new ClassPathResource(fileName).getFile();
            return new String(readAllBytes(resource.toPath()));
        }catch (IOException e){
            throw new RuntimeException("Content " + fileName + "can't be loaded.");
        }
    }

}
