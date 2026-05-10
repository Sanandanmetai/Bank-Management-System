/**
 * Loan.java
 * ---------
 * Model class representing a Loan application.
 *
 * OOP Concepts: Encapsulation, Constructors, Getters/Setters
 */
public class Loan {

    private int    loanId;
    private int    accountId;
    private double amount;
    private String status;   // "PENDING", "APPROVED", "REJECTED"

    // ── Default Constructor ───────────────────────────────────────────────────
    public Loan() {}

    // ── Parameterized Constructor ─────────────────────────────────────────────
    public Loan(int loanId, int accountId, double amount, String status) {
        this.loanId    = loanId;
        this.accountId = accountId;
        this.amount    = amount;
        this.status    = status;
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public int    getLoanId()    { return loanId;    }
    public int    getAccountId() { return accountId; }
    public double getAmount()    { return amount;    }
    public String getStatus()    { return status;    }

    // ── Setters ───────────────────────────────────────────────────────────────
    public void setLoanId(int loanId)       { this.loanId    = loanId;    }
    public void setAccountId(int accountId) { this.accountId = accountId; }
    public void setAmount(double amount)    { this.amount    = amount;    }
    public void setStatus(String status)    { this.status    = status;    }
}
