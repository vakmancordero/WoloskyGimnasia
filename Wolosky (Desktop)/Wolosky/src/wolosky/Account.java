package wolosky;

import java.io.Serializable;

/**
 *
 * @author VakSF
 */
public class Account implements Comparable<Account>, Serializable {
        
    private final String user;
    private final String password;

    public Account(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public String getUser() {
        return this.user;
    }

    public String getPassword() {
        return this.password;
    }

    @Override
    public String toString() {
        return this.user + " : " + this.password;
    }

    @Override
    public int compareTo(Account account) {
        return this.user.compareTo(account.getUser()) + 
                this.password.compareTo(account.getPassword());
    }

}
