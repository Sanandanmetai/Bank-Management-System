/**
 * Transaction.java
 * ----------------
 * Model class representing a bank transaction (DEPOSIT or WITHDRAW).
 *
 * OOP Concepts: Encapsulation, Constructors, Getters/Setters
 */
public class Transaction {

    private int    transactionId;
    private int    accountId;
    private String type;    // "DEPOSIT" or "WITHDRAW"
    private double amount;
    private String date;

    // ── Default Constructor ───────────────────────────────────────────────────
    public Transaction() {}

    // ── Parameterized Constructor ─────────────────────────────────────────────
    public Transaction(int transactionId, int accountId,
                       String type, double amount, String date) {
        this.transactionId = transactionId;
        this.accountId     = accountId;
        this.type          = type;
        this.amount        = amount;
        this.date          = date;
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public int    getTransactionId() { return transactionId; }
    public int    getAccountId()     { return accountId;     }
    public String getType()          { return type;          }
    public double getAmount()        { return amount;        }
    public String getDate()          { return date;          }

    // ── Setters ───────────────────────────────────────────────────────────────
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }
    public void setAccountId(int accountId)         { this.accountId     = accountId;     }
    public void setType(String type)                { this.type          = type;          }
    public void setAmount(double amount)            { this.amount        = amount;        }
    public void setDate(String date)                { this.date          = date;          }
}
