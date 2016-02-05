package real.zani.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

import lombok.extern.slf4j.Slf4j;
import real.zani.dto.MessageDto;
import real.zani.entity.UserEntity;
import real.zani.exception.MessageException;
import real.zani.exception.UserNotFoundException;
import real.zani.repository.UserRepository;

@Slf4j
@Service
public class MessageService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Value("${gcm.api_key}")
	private String gcmApiKey;
	
		
	public void sendMessage(MessageDto messageDto) throws UserNotFoundException, MessageException {
		UserEntity userEntity = userRepository.findByLoginId(messageDto.getFriendLoginId());
		if(userEntity == null) {
			throw new UserNotFoundException();
		}
		
		Sender sender = new Sender(gcmApiKey); 
		String regId = userEntity.getGcmRegId();
		Message message = new Message.Builder().addData("title", messageDto.getSendName()).addData("message", messageDto.getMessage())
				.build();
		List<String> list = new ArrayList<String>();
		list.add(regId);
		MulticastResult multiResult;
		
		try {
			multiResult = sender.send(message, list, 5);
			if (multiResult != null) {
				List<Result> resultList = multiResult.getResults();
				for (Result result : resultList) {
					System.out.println(result.getMessageId());
				}
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
	
}
