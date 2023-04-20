package Controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import static org.mockito.Mockito.timeout;

import java.util.*;
import Model.Account;
import Model.Message;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /*public AccountService account = new AccountService();
    public MessageService message = new MessageService();*/
    AccountService account;
    MessageService message;

    public SocialMediaController(){
        this.account = new AccountService();
        this.message = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postRegisterHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getMessageHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::patchMessageHandler);
        app.get("/accounts/{account_id}/messages/", this::getAllMessageByIdHandler);
        return app;
    }
    // Register
    private void postRegisterHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Account newAccount = om.readValue(ctx.body(), Account.class);
        List<Account> allAccounts = account.getAccounts();

        String username = newAccount.getUsername();
        boolean goodPassword = false;
        boolean uniqueUser = true;
    

        if(newAccount.getPassword().length() >= 4){
            goodPassword = true;
        }
        for(Account i : allAccounts){
            if((i.getUsername().equals(username))){
                uniqueUser = false;
            }
        }

        if(username != null && username != "" && uniqueUser == true && goodPassword == true){
            Account a = account.register(newAccount);
            ctx.json(om.writeValueAsString(a));
            ctx.status(200);
        } else{
            ctx.status(400);
        }   
    }
    // Login
    private void postLoginHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Account user = om.readValue(ctx.body(), Account.class);
        List<Account> allAccounts = account.getAccounts();

        Account check = null;
        String username = user.getUsername();
        String password = user.getPassword();
        boolean logon = false;
            
        for(Account i : allAccounts){
            if(i.getUsername().equals(username) && i.getPassword().equals(password)){
                check = i;
                logon = true;
                break;
            }
        }

        if(logon == true){
            ctx.json(om.writeValueAsString(check));
            ctx.status(200);
        } else{
            ctx.status(401);
        }
    }
    // Create a message
    private void postMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Message newMessage = om.readValue(ctx.body(), Message.class);
        List<Account> allAccounts = account.getAccounts();
       
        boolean messageExists = false;
        boolean messageGoodLength = false;
        boolean validAccount = false;
        int posted = newMessage.getPosted_by();
        String text = newMessage.getMessage_text();


        if(text != "" && text != null){
                messageExists = true;
        }
        if(text.length() < 255){
            messageGoodLength = true;
        }
        for(Account i : allAccounts){
            if(i.getAccount_id() == posted){
                validAccount = true;
            }
        }

        if(validAccount && messageExists && messageGoodLength){
            Message newM = message.newMessage(newMessage);
            ctx.json(om.writeValueAsString(newM));
            ctx.status(200);
        } else{
            ctx.status(400);
        }
    }
    // Get all messages
    public void getMessageHandler(Context ctx){
        //MessageService message = new MessageService();
        List<Message> allMessages = new ArrayList<>();

        if(allMessages.size() == 0){
            ctx.json(new ArrayList<Message>());
        } else{
            ctx.json(allMessages);
        }
        
        ctx.status(200);
    }
    //Get message by id
    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException{
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message m = new Message();
        if(m.message_id == id){
            ctx.json(message.GetMessageById(id));
        }
        ctx.status(200);
       
    }
    // Delete a message
    private void deleteMessageHandler(Context ctx) throws JsonProcessingException{
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        List<Message> allMessages = message.getAllMessages();
        ObjectMapper om = new ObjectMapper();
        Message ms = om.readValue(ctx.body(), Message.class);

        for(Message m: allMessages){
            if(m.getMessage_id() == id){
                ms = m;
                message.deleteMessage(id);
            }
        }

        ctx.json(om.writeValueAsString(ms));
        ctx.status(200);
    }
    // Update a message
    private void patchMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Message m = om.readValue(ctx.body(), Message.class);
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message update = message.updateMessageById(id, m);
        boolean messageExists = false;
        boolean messageGoodLength = false;
        String text = m.getMessage_text();

        if(text != "" && text != null){
                messageExists = true;
        }
        if(text.length() < 255){
            messageGoodLength = true;
        }
        if(messageExists && messageGoodLength){
            ctx.json(om.writeValueAsString(update));
            ctx.status(200);
        } else{
            ctx.status(400);
        }
    }
    //Get all messages from a user
    private void getAllMessageByIdHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        int id = Integer.parseInt(ctx.pathParam("posted_by"));
        List<Message> allMessages = message.getAllMessages();
        List<Message> certain = new ArrayList<>();

        for(Message m : allMessages){
            if(m.getPosted_by() == id){
                m = om.readValue(ctx.body(), Message.class);
                certain.add(m);
            }
        }
        
        ctx.json(certain);
        ctx.status(200);
    }
}
    
    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    /*private void exampleHandler(Context context) {
        context.json("sample text");
    } */
        

