package hello;


import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(value = APIController.class, secure = false)
public class APIControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void getMessage() throws Exception {
			
		String name = "venus";
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/api/hello?name="+name)
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = (MvcResult) mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message", is("Hello "+name)))
				.andDo(print())
				.andReturn();
		System.out.println(result.getResponse());
		
	}

}
