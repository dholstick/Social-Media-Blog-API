package Service;

import java.util.*;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    MessageDAO messageDAO;
    Message message;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public Message deleteMessage(int id){
        return messageDAO.removeMessageById(id);
    }

    public Message updateMessageById(int id, Message message){
        Message update = messageDAO.getMessageById(id);
        if(message.getMessage_text().isBlank() || message.getMessage_text().length()>=255 || messageDAO.getMessageById(id) == null){
            return null;
        }

        messageDAO.updateMessage(id, message);
        String text = message.getMessage_text();
        update.setMessage_text(text);
        return update;
    }

    public Message newMessage(Message message){
        return messageDAO.createMessage(message);
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public List<Message> getAllMessagesId(int posted){
        return messageDAO.getAllMessagesById(posted);
    }

    public Message getMessageId(int id){
        return messageDAO.getMessageById(id);
    }
}