CREATE TABLE `users` (
  `user_id` varchar(100) NOT NULL,
  `name` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(100) NOT NULL,
  `phone` bigint NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `credit_score` int DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `phone_UNIQUE` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `beneficiary_users` (
  `beneficiary_user_id` varchar(100) NOT NULL,
  `user_id` varchar(100) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`beneficiary_user_id`),
  UNIQUE KEY `beneficiary_user_id_UNIQUE` (`beneficiary_user_id`),
  KEY `user_id_idx` (`user_id`),
  CONSTRAINT `fk_user_id_beneficiary_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `credit_cards` (
  `credit_card_id` varchar(100) NOT NULL,
  `card_number` bigint NOT NULL,
  `security_code` int NOT NULL,
  `pin_number` int NOT NULL,
  `total_credit_limit` double NOT NULL,
  `remaining_credit_limit` double NOT NULL,
  `card_category` varchar(45) NOT NULL,
  `card_provider` varchar(45) NOT NULL,
  `user_id` varchar(100) NOT NULL,
  `valid_thru` timestamp NOT NULL,
  `last_payment_date` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`credit_card_id`),
  UNIQUE KEY `credit_card_id_UNIQUE` (`credit_card_id`),
  UNIQUE KEY `card_number_UNIQUE` (`card_number`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`),
  CONSTRAINT `fk_user_id_credit_cards` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `savings_accounts` (
  `account_id` varchar(100) NOT NULL,
  `account_number` bigint NOT NULL,
  `account_balance` double NOT NULL,
  `user_id` varchar(100) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`account_id`),
  UNIQUE KEY `account_id_UNIQUE` (`account_id`),
  UNIQUE KEY `account_number_UNIQUE` (`account_number`),
  KEY `fk_user_id_savings_accounts_idx` (`user_id`),
  CONSTRAINT `fk_user_id_savings_accounts` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `transactions` (
  `transaction_id` varchar(100) NOT NULL,
  `transaction_name` varchar(45) NOT NULL,
  `transaction_category` varchar(45) NOT NULL,
  `transaction_type` varchar(45) NOT NULL,
  `transaction_mode` varchar(45) NOT NULL,
  `amount` double NOT NULL,
  `user_id` varchar(100) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `due_date` timestamp NULL DEFAULT NULL,
  `payment_status` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`),
  UNIQUE KEY `transaction_id_UNIQUE` (`transaction_id`),
  KEY `fk_user_id_transactions_idx` (`user_id`),
  CONSTRAINT `fk_user_id_transactions` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

