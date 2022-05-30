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
                response = response.concat(";" + "From: " +
                        m.getUsername1() + "\nTo: " +
                        m.getUsername2() + "\nM: " +
                        m.getMessage());
        }
        return response;
    }

    @PostMapping
    public void createMessage(@RequestBody String mes)
    {
        String[] ar = mes.split(",");
        Message message = new Message(1, ar[0], ar[1], ar[2]);
        messageRepository.save(message);

    }

}
