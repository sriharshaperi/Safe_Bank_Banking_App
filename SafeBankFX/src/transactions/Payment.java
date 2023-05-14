package transactions;

import enums.PaymentStatus;

@FunctionalInterface
public interface Payment {
    public PaymentStatus payment(double amount) throws Exception;
}
