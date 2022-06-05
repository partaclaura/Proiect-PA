package com.example.Messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/messages")
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("{username}")
    public String getAllMessages(@PathVariable String username)
    {
        String response = "";
        List<Message> messages = messageRepository.findAll();
        for(Message m : messages)
        {
            if(m.getUsername2().equals(username) || m.getUsername1().equals(username))
                response = response.concat(";" +
                        m.getUsername1() + ";" +
                        m.getUsername2() + ";" +
                        m.getMessage());
        }
        return response;
    }

    @PostMapping
    public void createMessage(@RequestBody String mes)
    {
        String[] ar = mes.split(",");
        List<Message> messages = messageRepository.findAll();
        int index = 0;
        for(Message message : messages)
            index++;
        Message message = new Message(index + 1, ar[0], ar[1], ar[2]);
        messageRepository.save(message);

    }

}
