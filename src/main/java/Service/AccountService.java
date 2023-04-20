package Service;

import java.util.*;
import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account register(Account a){
        return accountDAO.addUser(a);
    }

    public List<Account> getAccounts(){
        return accountDAO.getAllAccounts();
    }
}



