package real.zani;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import real.zani.dto.AuthResultDto;
import real.zani.dto.MessageDto;
import real.zani.dto.UserDto;
import real.zani.repository.UserRepository;
import real.zani.service.MessageService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ZaniApplication.class)
@WebIntegrationTest("server.port=8888")
public class ZaniApplicationTests {
	@Autowired
	private WebApplicationContext wac;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private MessageService messageService;
	
	private MockMvc mock;
	
	private UserDto user1;
	private UserDto user2;
	
	private String accessToken;

	@Before
	public void setUp() throws Exception {
		
		this.mock = MockMvcBuilders.webAppContextSetup(wac).build();
		this.user1 = new UserDto();
		user1.setLoginId("test");
		user1.setPassword("test");
		user1.setNickname("tetstnick");
		
		this.user2 = new UserDto();
		user2.setLoginId("test2");
		user2.setPassword("test2");
		user2.setNickname("tetstnick2");
		user2.setGcmRegId("etDzrDZapJ4:APA91bHBhy1PeDRR2jO0mLxJGWc7KpEVSG1OuMUA7I6Gi9FdsRRi5CmAfu10nLfBHXkseLRM37X8MYMC3TATm59U6e42yATMpsa1-XeSX4u8hEYEq5xeSGEvzsYDRC8Bi-IP-Wl_wnlP");
	}
	
	public void removeTestUser() {
		userRepository.removeByLoginId(user1.getLoginId());
		userRepository.removeByLoginId(user2.getLoginId());
	}

	public void register() throws Exception {
		ResultActions resultActions = mock.perform(MockMvcRequestBuilders.post("/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(user1)))
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
		
		resultActions.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isCreated());
		
		//
		
		resultActions = mock.perform(MockMvcRequestBuilders.post("/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(user2)))
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
		
		resultActions.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isCreated());
	}
	
	public void login() throws Exception {
		
		ResultActions resultActions = mock.perform(MockMvcRequestBuilders.post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(user1)));
		
		String response = resultActions.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andReturn().getResponse().getContentAsString();
		
		AuthResultDto authResult = objectMapper.readValue(response, AuthResultDto.class);
		accessToken = authResult.getAccessToken();
	}
	
	public void findFriend() throws Exception {
		login();
		
		ResultActions resultActions = mock.perform(MockMvcRequestBuilders.post("/friend/find/" + user2.getLoginId())
				.contentType(MediaType.APPLICATION_JSON)
				.header("AccessToken", accessToken));
		
		resultActions.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	public void insertFriend() throws Exception {
		login();
		
		ResultActions resultActions = mock.perform(MockMvcRequestBuilders.post("/friend/add/" + user2.getLoginId())
				.contentType(MediaType.APPLICATION_JSON)
				.header("AccessToken", accessToken)
				.content(objectMapper.writeValueAsBytes(user2)));
		
		resultActions.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isCreated());
	}
	
	public void getFriends() throws Exception {
		login();
		
		ResultActions resultActions = mock.perform(MockMvcRequestBuilders.post("/friends")
				.contentType(MediaType.APPLICATION_JSON)
				.header("AccessToken", accessToken));
		
		resultActions.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void testAll() throws Exception {
		removeTestUser();
		register();
		login();
		findFriend();
		insertFriend();
		getFriends();
	}

	@Test
	public void push() throws Exception {
		MessageDto messageDto = new MessageDto();
		messageDto.setMessage("메세지");
		messageDto.setSendName("유정");
		messageDto.setFriendLoginId(user2.getLoginId());
		messageService.sendMessage(messageDto);
	}
}
