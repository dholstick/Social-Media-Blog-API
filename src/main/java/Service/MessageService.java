package Service;

import java.util.*;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    MessageDAO messageDAO;

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
        if(messageDAO.getMessageById(id) == null){
            return null;
        } else{
            messageDAO.updateMessage(id, message);
            return messageDAO.getMessageById(id);
        }
    }

    public Message newMessage(Message message){
        return messageDAO.createMessage(message);
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message GetMessageById(int id){
        return messageDAO.getMessageById(id);
    }

    public List<Message> getAllMessagesId(int posted){
        return messageDAO.getAllMessagesById(posted);
    }
}