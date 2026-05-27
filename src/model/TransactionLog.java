package model;

import java.sql.Timestamp;

public class TransactionLog {
    private int id;
    private int userId;
    private String username;
    private String action;
    private String details;
    private Timestamp logTime;

    public TransactionLog() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public Timestamp getLogTime() { return logTime; }
    public void setLogTime(Timestamp logTime) { this.logTime = logTime; }
}