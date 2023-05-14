package transactions;

import enums.PaymentStatus;

@FunctionalInterface
public interface Deposit {
   public PaymentStatus deposit(double amount) throws Exception;
}
