/**
 * Account.java
 * ------------
 * Model class representing a Customer Bank Account.
 *
 * OOP Concepts: Encapsulation, Constructors, Getters/Setters
 */
public class Account {

    // ── Private fields (Encapsulation) ───────────────────────────────────────
    private int    accountId;
    private String name;
    private String email;
    private String phone;
    private double balance;
    private String password;

    // ── Default Constructor ───────────────────────────────────────────────────
    public Account() {}

    // ── Parameterized Constructor ─────────────────────────────────────────────
    public Account(int accountId, String name, String email,
                   String phone, double balance, String password) {
        this.accountId = accountId;
        this.name      = name;
        this.email     = email;
        this.phone     = phone;
        this.balance   = balance;
        this.password  = password;
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public int    getAccountId() { return accountId; }
    public String getName()      { return name;      }
    public String getEmail()     { return email;     }
    public String getPhone()     { return phone;     }
    public double getBalance()   { return balance;   }
    public String getPassword()  { return password;  }

    // ── Setters ───────────────────────────────────────────────────────────────
    public void setAccountId(int accountId)    { this.accountId = accountId; }
    public void setName(String name)           { this.name      = name;      }
    public void setEmail(String email)         { this.email     = email;     }
    public void setPhone(String phone)         { this.phone     = phone;     }
    public void setBalance(double balance)     { this.balance   = balance;   }
    public void setPassword(String password)   { this.password  = password;  }

    // ── toString (useful for debugging) ───────────────────────────────────────
    @Override
    public String toString() {
        return "Account{id=" + accountId + ", name=" + name +
               ", email=" + email + ", balance=" + balance + "}";
    }
}
