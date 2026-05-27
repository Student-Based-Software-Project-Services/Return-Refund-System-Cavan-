package repository;

import database.DBConnection;
import java.sql.Connection;

public class RepoManager {
    private static RepoManager instance;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final ReturnRequestRepository returnRequestRepository;
    private final TransactionLogRepository transactionLogRepository;

    public RepoManager() {
        Connection conn = DBConnection.getConnection();
        userRepository = new UserRepository(conn);
        customerRepository = new CustomerRepository(conn);
        returnRequestRepository = new ReturnRequestRepository(conn);
        transactionLogRepository = new TransactionLogRepository(conn);
    }

    public static RepoManager getInstance() {
        if (instance == null) {
            instance = new RepoManager();
        }
        return instance;
    }

    public UserRepository getUserRepository() { return userRepository; }
    public CustomerRepository getCustomerRepository() { return customerRepository; }
    public ReturnRequestRepository getReturnRequestRepository() { return returnRequestRepository; }
    public TransactionLogRepository getTransactionLogRepository() { return transactionLogRepository; }
}