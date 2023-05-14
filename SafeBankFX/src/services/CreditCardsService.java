package services;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import dao.CreditCardsDAO;
import enums.CardCategory;
import enums.CardProvider;
import models.CreditCard;
import models.User;
import utils.CreditCardUtils;

public class CreditCardsService {
	
	public static boolean createNewCreditCard(
			String userId, User user, CardCategory cardCategory) throws Exception {
		CreditCard creditCard = new CreditCard();
		System.out.println(user);
//		CardCategory cardCategory = CreditCardUtils.getCardCategory(user);
		System.out.println(cardCategory);
		
		double totalCreditLimit = 
				CreditCardUtils.getTotalCreditLimit(cardCategory);
		
		long cardNumber = CreditCardUtils.generateCardNumber();
		int securityCode = CreditCardUtils.generateSecurityCode();
		
		CardProvider cardProvider = 
				CreditCardUtils.getCardProvider(cardNumber);
		
		Date validThru = CreditCardUtils.getValidThru(new Date());
		
		creditCard.setPinNumber(CreditCardUtils.generatePin());
		creditCard.setCreditCardId(UUID.randomUUID());
		creditCard.setCardNumber(cardNumber);
		creditCard.setSecurityCode(securityCode);
		creditCard.setTotalCreditLimit(totalCreditLimit);
		creditCard.setRemainingCreditLimit(totalCreditLimit);
		creditCard.setCardCategory(cardCategory);
		creditCard.setCardProvider(cardProvider);
		creditCard.setValidThru(validThru);
		
		CreditCard userCard = CreditCardsDAO.getCreditCardByUserId(userId);
		if(userCard == null || userCard.getCreditCardId() == null) {
			boolean cardCreated = 
					CreditCardsDAO.createNewCreditCard(userId, creditCard);
			
			if(cardCreated) {
				System.out.println("Created Credit Card for User "+user.getName());
				CreditCard userCreditCard = 
						CreditCardsDAO.getCreditCardByUserId(userId);
				user.setCreditCard(userCreditCard);
				return true;
			}
			else {
				System.out.println("Failed Creating Credit Card for "+user.getName());
				return false;
			}
		}
		else  {
			System.out.println("Credit Card Already exists");
			return false;
		}
	}
}
