package soa.web;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Controller
public class SearchController {

	@Autowired
	  private ProducerTemplate producerTemplate;

	@RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping(value="/search")
    @ResponseBody
    public Object search(@RequestParam("q") String q) {
        // Create the query headers
        Map<String,Object> headers = new HashMap<String,Object>();
        String[] splitted = q.split("max:");
        if(q != null){
             if(splitted.length>1) {
            // Max count is specified
            String message = splitted[0].trim();
            try {
                // Converts the count from String to Integer
                Integer count = new Integer(splitted[1].trim());
                headers.put("CamelTwitterKeywords",message);
                headers.put("CamelTwitterCount",count);
            } catch(NumberFormatException ex){
                // If max count is not a number, set max value = 10
                System.out.println("Warning: not a number specified for twitter counts");
                headers.put("CamelTwitterKeywords",message);
                headers.put("CamelTwitterCount",10);
                }
            } else {
                // If don't specify max, set max value = 10
                String message = splitted[0].trim();
                headers.put("CamelTwitterKeywords",message);
                headers.put("CamelTwitterCount",10);
            }
        }
       
        return producerTemplate.requestBodyAndHeaders("direct:search", "", headers);
    }
}