package real.zani.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import real.zani.dto.AccessTokenDto;
import real.zani.dto.MessageDto;
import real.zani.exception.MessageException;
import real.zani.exception.UserNotFoundException;
import real.zani.service.MessageService;

@RestController
public class MessageController {
	
	@Autowired
	private MessageService messageService;
	
	@RequestMapping(value="/message", method = RequestMethod.POST)
	public void sendMessage(AccessTokenDto accessTokenDto, @Valid MessageDto messageDto) throws UserNotFoundException, MessageException {
		messageService.sendMessage(messageDto);
	}
	
	public void sendMessage(@Valid MessageDto messageDto) throws UserNotFoundException, MessageException {
		messageService.sendMessage(messageDto);
	}
}
