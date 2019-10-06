package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
//import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class Application implements CommandLineRunner {

	  //@Autowired
	  //private MessageService helloService;
	  
	  public static void main(String[] args) throws Exception {
	    SpringApplication app = new SpringApplication(Application.class);
	    app.run(args);
	  }
	  
	  @Override
	    public void run(String... args) throws Exception {
	    //System.out.println(helloService.getMessage());
	  }

}
