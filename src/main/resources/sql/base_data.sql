-- Test data from PDF - PROG3-TD-Final-23-Avril-2026
-- Members (16 unique members - some appear in multiple collectivities)
-- Collectivity 1 members (C1-M1 to C1-M8)
INSERT INTO member (id, firstname, lastname, birthdate, gender, address, phone, profession, email, occupation, joined_at) VALUES
('C1-M1', 'Prenom membre 1', 'Nom membre 1', '1980-02-01', 'MALE', 'Lot II V M Ambato.', 341234567, 'Riziculteur', 'member.1@fed-agri.mg', 'PRESIDENT', '2025-01-01'),
('C1-M2', 'Prenom membre 2', 'Nom membre 2', '1982-03-05', 'MALE', 'Lot II F Ambato.', 321234567, 'Agriculteur', 'member.2@fed-agri.mg', 'VICE_PRESIDENT', '2025-01-01'),
('C1-M3', 'Prenom membre 3', 'Nom membre 3', '1992-03-10', 'MALE', 'Lot II J Ambato.', 331234567, 'Collecteur', 'member.3@fed-agrimg', 'SECRETARY', '2025-01-01'),
('C1-M4', 'Prenom membre 4', 'Nom membre 4', '1988-05-22', 'FEMALE', 'Lot A K 50 Ambato.', 381234567, 'Distributeur', 'member.4@fed-agri.mg', 'TREASURER', '2025-01-01'),
('C1-M5', 'Prenom membre 5', 'Nom membre 5', '1999-08-21', 'MALE', 'Lot UV 80 Ambato.', 373434567, 'Riziculteur', 'member.5@fed-agri.mg', 'SENIOR', '2025-01-01'),
('C1-M6', 'Prenom membre 6', 'Nom membre 6', '1998-08-22', 'FEMALE', 'Lot UV 6 Ambato.', 372234567, 'Riziculteur', 'member.6@fed-agri.mg', 'SENIOR', '2025-01-01'),
('C1-M7', 'Prenom membre 7', 'Nom membre 7', '1998-01-31', 'MALE', 'Lot UV 7 Ambato.', 374234567, 'Riziculteur', 'member.7@fed-agri.mg', 'SENIOR', '2025-01-01'),
('C1-M8', 'Prenom membre 6', 'Nom membre 8', '1975-08-20', 'MALE', 'Lot UV 8 Ambato.', 370234567, 'Riziculteur', 'member.8@fed-agri.mg', 'SENIOR', '2025-01-01');

-- Collectivity 2 members (same as C1 but different roles)
INSERT INTO member (id, firstname, lastname, birthdate, gender, address, phone, profession, email, occupation, joined_at) VALUES
('C2-M5', 'Prenom membre 5', 'Nom membre 5', '1999-08-21', 'MALE', 'Lot UV 80 Ambato.', 373434567, 'Riziculteur', 'member.5@fed-agri.mg', 'PRESIDENT', '2025-01-01'),
('C2-M6', 'Prenom membre 6', 'Nom membre 6', '1998-08-22', 'FEMALE', 'Lot UV 6 Ambato.', 372234567, 'Riziculteur', 'member.6@fed-agri.mg', 'VICE_PRESIDENT', '2025-01-01'),
('C2-M7', 'Prenom membre 7', 'Nom membre 7', '1998-01-31', 'MALE', 'Lot UV 7 Ambato.', 374234567, 'Riziculteur', 'member.7@fed-agri.mg', 'SECRETARY', '2025-01-01'),
('C2-M8', 'Prenom membre 6', 'Nom membre 8', '1975-08-20', 'MALE', 'Lot UV 8 Ambato.', 370234567, 'Riziculteur', 'member.8@fed-agri.mg', 'TREASURER', '2025-01-01');

-- Collectivity 3 members (new members C3-M1 to C3-M8)
INSERT INTO member (id, firstname, lastname, birthdate, gender, address, phone, profession, email, occupation, joined_at) VALUES
('C3-M1', 'Prenom membre 9', 'Nom membre 9', '1988-01-02', 'MALE', 'Lot 33 J Antsirabe', 34034567, 'Apiculteur', 'member.9@fed-agri.mg', 'PRESIDENT', '2025-01-01'),
('C3-M2', 'Prenom membre 10', 'Nom membre 10', '1982-03-05', 'MALE', 'Lot 2 J Antsirabe', 338634567, 'Agriculteur', 'member.10@fed-agri.mg', 'VICE_PRESIDENT', '2025-01-01'),
('C3-M3', 'Prenom membre 11', 'Nom membre 11', '1992-03-12', 'MALE', 'Lot 8 KM Antsirabe', 338234567, 'Collecteur', 'member.11@fed-agrimg', 'SECRETARY', '2025-01-01'),
('C3-M4', 'Prenom membre 12', 'Nom membre 12', '1988-05-10', 'FEMALE', 'Lot A K 50 Antsirabe', 382334567, 'Distributeur', 'member.12@fed-agri.mg', 'TREASURER', '2025-01-01'),
('C3-M5', 'Prenom membre 13', 'Nom membre 13', '1999-08-11', 'MALE', 'Lot UV 80 Antsirabe.', 373365567, 'Apiculteur', 'member.13@fed-agri.mg', 'SENIOR', '2025-01-01'),
('C3-M6', 'Prenom membre 14', 'Nom membre 14', '1998-08-09', 'FEMALE', 'Lot UV 6 Antsirabe.', 378234567, 'Apiculteur', 'member.14@fed-agri.mg', 'SENIOR', '2025-01-01'),
('C3-M7', 'Prenom membre 15', 'Nom membre 15', '1998-01-13', 'MALE', 'Lot UV 7 Antsirabe', 374914567, 'Apiculteur', 'member.15@fed-agri.mg', 'SENIOR', '2025-01-01'),
('C3-M8', 'Prenom membre 16', 'Nom membre 16', '1975-08-02', 'MALE', 'Lot UV 8 Antsirabe', 370634567, 'Apiculteur', 'member.16@fed-agri.mg', 'SENIOR', '2025-01-01');

-- Referees (references table) - members refer other members
-- For C1-M3: referees are C1-M1, C1-M2
INSERT INTO reference (id_member_refered, id_member_referer) VALUES
('C1-M3', 'C1-M1'),
('C1-M3', 'C1-M2'),
('C1-M4', 'C1-M1'),
('C1-M4', 'C1-M2'),
('C1-M5', 'C1-M1'),
('C1-M5', 'C1-M2'),
('C1-M6', 'C1-M1'),
('C1-M6', 'C1-M2'),
('C1-M7', 'C1-M1'),
('C1-M7', 'C1-M2'),
('C1-M8', 'C1-M6'),
('C1-M8', 'C1-M7');

-- Collectivities
INSERT INTO collectivity (id, federation_number, name, location, president_id, treasurer_id, vice_president_id, secretary_id) VALUES
('col-1', 1, 'Mpanorina', 'Ambatondrazaka', 'C1-M1', 'C1-M4', 'C1-M2', 'C1-M3'),
('col-2', 2, 'Dobo voalohany', 'Ambatondrazaka', 'C2-M5', 'C2-M8', 'C2-M6', 'C2-M7'),
('col-3', 3, 'Tantely mamy', 'Brickaville', 'C3-M1', 'C3-M4', 'C3-M2', 'C3-M3');

-- Member-collectivity relationships
-- Collectivity 1
INSERT INTO member_collectivity (id_member, id_collectivity) VALUES
('C1-M1', 'col-1'), ('C1-M2', 'col-1'), ('C1-M3', 'col-1'), ('C1-M4', 'col-1'),
('C1-M5', 'col-1'), ('C1-M6', 'col-1'), ('C1-M7', 'col-1'), ('C1-M8', 'col-1');
-- Collectivity 2 (using same member IDs as C1-M1 to C1-M8 but they represent same persons)
INSERT INTO member_collectivity (id_member, id_collectivity) VALUES
('C1-M1', 'col-2'), ('C1-M2', 'col-2'), ('C1-M3', 'col-2'), ('C1-M4', 'col-2'),
('C2-M5', 'col-2'), ('C2-M6', 'col-2'), ('C2-M7', 'col-2'), ('C2-M8', 'col-2');
-- Collectivity 3
INSERT INTO member_collectivity (id_member, id_collectivity) VALUES
('C3-M1', 'col-3'), ('C3-M2', 'col-3'), ('C3-M3', 'col-3'), ('C3-M4', 'col-3'),
('C3-M5', 'col-3'), ('C3-M6', 'col-3'), ('C3-M7', 'col-3'), ('C3-M8', 'col-3');

-- Membership Fees
INSERT INTO membership_fee (id, label, status, frequency, eligible_from, amount, id_collectivity) VALUES
('cot-1', 'Cotisation annuelle', 'ACTIVE', 'ANNUALLY', '2026-01-01', 100000, 'col-1'),
('cot-2', 'Cotisation annuelle', 'ACTIVE', 'ANNUALLY', '2026-01-01', 100000, 'col-2'),
('cot-3', 'Cotisation annuelle', 'ACTIVE', 'ANNUALLY', '2026-01-01', 50000, 'col-3');

-- Financial Accounts
-- Collectivity 1
INSERT INTO financial_account (id, account_type, amount, holder_name, mobile_banking_service, mobile_number) VALUES
('C1-A-CASH', 'CASH', 0, NULL, NULL, NULL),
('C1-A-MOBILE-1', 'MOBILE_BANKING', 0, 'Mpanorina', 'ORANGE_MONEY', 370489612);
-- Collectivity 2
INSERT INTO financial_account (id, account_type, amount, holder_name, mobile_banking_service, mobile_number) VALUES
('C2-A-CASH', 'CASH', 0, NULL, NULL, NULL),
('C2-A-MOBILE-1', 'MOBILE_BANKING', 0, 'Dobo voalohany', 'ORANGE_MONEY', 320489612);
-- Collectivity 3
INSERT INTO financial_account (id, account_type, amount, holder_name, mobile_banking_service, mobile_number) VALUES
('C3-A-CASH', 'CASH', 0, NULL, NULL, NULL);

-- Member Payments and Transactions for Collectivity 1 (2026-01-01)
-- C1-M1 pays 100000 to C1-A-CASH (CASH)
INSERT INTO member_payment (id, id_member, id_membership_fee, id_financial_account, amount, payment_mode, creation_date) VALUES
('pay-C1-M1-1', 'C1-M1', 'cot-1', 'C1-A-CASH', 100000, 'CASH', '2026-01-01');
INSERT INTO collectivity_transaction (id_collectivity, id_member, id_financial_account, amount, payment_mode, creation_date) VALUES
('col-1', 'C1-M1', 'C1-A-CASH', 100000, 'CASH', '2026-01-01');

-- C1-M2 pays 100000 to C1-A-CASH (CASH)
INSERT INTO member_payment (id, id_member, id_membership_fee, id_financial_account, amount, payment_mode, creation_date) VALUES
('pay-C1-M2-1', 'C1-M2', 'cot-1', 'C1-A-CASH', 100000, 'CASH', '2026-01-01');
INSERT INTO collectivity_transaction (id_collectivity, id_member, id_financial_account, amount, payment_mode, creation_date) VALUES
('col-1', 'C1-M2', 'C1-A-CASH', 100000, 'CASH', '2026-01-01');

-- C1-M3 pays 100000 to C1-A-CASH (CASH)
INSERT INTO member_payment (id, id_member, id_membership_fee, id_financial_account, amount, payment_mode, creation_date) VALUES
('pay-C1-M3-1', 'C1-M3', 'cot-1', 'C1-A-CASH', 100000, 'CASH', '2026-01-01');
INSERT INTO collectivity_transaction (id_collectivity, id_member, id_financial_account, amount, payment_mode, creation_date) VALUES
('col-1', 'C1-M3', 'C1-A-CASH', 100000, 'CASH', '2026-01-01');

-- C1-M4 pays 100000 to C1-A-CASH (CASH)
INSERT INTO member_payment (id, id_member, id_membership_fee, id_financial_account, amount, payment_mode, creation_date) VALUES
('pay-C1-M4-1', 'C1-M4', 'cot-1', 'C1-A-CASH', 100000, 'CASH', '2026-01-01');
INSERT INTO collectivity_transaction (id_collectivity, id_member, id_financial_account, amount, payment_mode, creation_date) VALUES
('col-1', 'C1-M4', 'C1-A-CASH', 100000, 'CASH', '2026-01-01');

-- C1-M5 pays 100000 to C1-A-CASH (CASH)
INSERT INTO member_payment (id, id_member, id_membership_fee, id_financial_account, amount, payment_mode, creation_date) VALUES
('pay-C1-M5-1', 'C1-M5', 'cot-1', 'C1-A-CASH', 100000, 'CASH', '2026-01-01');
INSERT INTO collectivity_transaction (id_collectivity, id_member, id_financial_account, amount, payment_mode, creation_date) VALUES
('col-1', 'C1-M5', 'C1-A-CASH', 100000, 'CASH', '2026-01-01');

-- C1-M6 pays 100000 to C1-A-CASH (CASH)
INSERT INTO member_payment (id, id_member, id_membership_fee, id_financial_account, amount, payment_mode, creation_date) VALUES
('pay-C1-M6-1', 'C1-M6', 'cot-1', 'C1-A-CASH', 100000, 'CASH', '2026-01-01');
INSERT INTO collectivity_transaction (id_collectivity, id_member, id_financial_account, amount, payment_mode, creation_date) VALUES
('col-1', 'C1-M6', 'C1-A-CASH', 100000, 'CASH', '2026-01-01');

-- C1-M7 pays 100000 to C1-A-MOBILE-1 (MOBILE_BANKING)
INSERT INTO member_payment (id, id_member, id_membership_fee, id_financial_account, amount, payment_mode, creation_date) VALUES
('pay-C1-M7-1', 'C1-M7', 'cot-1', 'C1-A-MOBILE-1', 100000, 'MOBILE_BANKING', '2026-01-01');
INSERT INTO collectivity_transaction (id_collectivity, id_member, id_financial_account, amount, payment_mode, creation_date) VALUES
('col-1', 'C1-M7', 'C1-A-MOBILE-1', 100000, 'MOBILE_BANKING', '2026-01-01');

-- C1-M8 pays 60000 to C1-A-MOBILE-1 (MOBILE_BANKING)
INSERT INTO member_payment (id, id_member, id_membership_fee, id_financial_account, amount, payment_mode, creation_date) VALUES
('pay-C1-M8-1', 'C1-M8', 'cot-1', 'C1-A-MOBILE-1', 60000, 'MOBILE_BANKING', '2026-01-01');
INSERT INTO collectivity_transaction (id_collectivity, id_member, id_financial_account, amount, payment_mode, creation_date) VALUES
('col-1', 'C1-M8', 'C1-A-MOBILE-1', 60000, 'MOBILE_BANKING', '2026-01-01');

-- Member Payments and Transactions for Collectivity 2 (2026-01-01)
-- C2-M1 (same as C1-M1) pays 60000 to C2-A-CASH (CASH)
INSERT INTO member_payment (id, id_member, id_membership_fee, id_financial_account, amount, payment_mode, creation_date) VALUES
('pay-C2-M1-1', 'C1-M1', 'cot-2', 'C2-A-CASH', 60000, 'CASH', '2026-01-01');
INSERT INTO collectivity_transaction (id_collectivity, id_member, id_financial_account, amount, payment_mode, creation_date) VALUES
('col-2', 'C1-M1', 'C2-A-CASH', 60000, 'CASH', '2026-01-01');

-- C2-M2 (same as C1-M2) pays 90000 to C2-A-CASH (CASH)
INSERT INTO member_payment (id, id_member, id_membership_fee, id_financial_account, amount, payment_mode, creation_date) VALUES
('pay-C2-M2-1', 'C1-M2', 'cot-2', 'C2-A-CASH', 90000, 'CASH', '2026-01-01');
INSERT INTO collectivity_transaction (id_collectivity, id_member, id_financial_account, amount, payment_mode, creation_date) VALUES
('col-2', 'C1-M2', 'C2-A-CASH', 90000, 'CASH', '2026-01-01');

-- C2-M3 (same as C1-M3) pays 100000 to C2-A-CASH (CASH)
INSERT INTO member_payment (id, id_member, id_membership_fee, id_financial_account, amount, payment_mode, creation_date) VALUES
('pay-C2-M3-1', 'C1-M3', 'cot-2', 'C2-A-CASH', 100000, 'CASH', '2026-01-01');
INSERT INTO collectivity_transaction (id_collectivity, id_member, id_financial_account, amount, payment_mode, creation_date) VALUES
('col-2', 'C1-M3', 'C2-A-CASH', 100000, 'CASH', '2026-01-01');

-- C2-M4 (same as C1-M4) pays 100000 to C2-A-CASH (CASH)
INSERT INTO member_payment (id, id_member, id_membership_fee, id_financial_account, amount, payment_mode, creation_date) VALUES
('pay-C2-M4-1', 'C1-M4', 'cot-2', 'C2-A-CASH', 100000, 'CASH', '2026-01-01');
INSERT INTO collectivity_transaction (id_collectivity, id_member, id_financial_account, amount, payment_mode, creation_date) VALUES
('col-2', 'C1-M4', 'C2-A-CASH', 100000, 'CASH', '2026-01-01');

-- C2-M5 pays 100000 to C2-A-CASH (CASH)
INSERT INTO member_payment (id, id_member, id_membership_fee, id_financial_account, amount, payment_mode, creation_date) VALUES
('pay-C2-M5-1', 'C2-M5', 'cot-2', 'C2-A-CASH', 100000, 'CASH', '2026-01-01');
INSERT INTO collectivity_transaction (id_collectivity, id_member, id_financial_account, amount, payment_mode, creation_date) VALUES
('col-2', 'C2-M5', 'C2-A-CASH', 100000, 'CASH', '2026-01-01');

-- C2-M6 pays 100000 to C2-A-CASH (CASH)
INSERT INTO member_payment (id, id_member, id_membership_fee, id_financial_account, amount, payment_mode, creation_date) VALUES
('pay-C2-M6-1', 'C2-M6', 'cot-2', 'C2-A-CASH', 100000, 'CASH', '2026-01-01');
INSERT INTO collectivity_transaction (id_collectivity, id_member, id_financial_account, amount, payment_mode, creation_date) VALUES
('col-2', 'C2-M6', 'C2-A-CASH', 100000, 'CASH', '2026-01-01');

-- C2-M7 pays 40000 to C2-A-MOBILE-1 (MOBILE_BANKING)
INSERT INTO member_payment (id, id_member, id_membership_fee, id_financial_account, amount, payment_mode, creation_date) VALUES
('pay-C2-M7-1', 'C2-M7', 'cot-2', 'C2-A-MOBILE-1', 40000, 'MOBILE_BANKING', '2026-01-01');
INSERT INTO collectivity_transaction (id_collectivity, id_member, id_financial_account, amount, payment_mode, creation_date) VALUES
('col-2', 'C2-M7', 'C2-A-MOBILE-1', 40000, 'MOBILE_BANKING', '2026-01-01');

-- C2-M8 pays 60000 to C2-A-MOBILE-1 (MOBILE_BANKING)
INSERT INTO member_payment (id, id_member, id_membership_fee, id_financial_account, amount, payment_mode, creation_date) VALUES
('pay-C2-M8-1', 'C2-M8', 'cot-2', 'C2-A-MOBILE-1', 60000, 'MOBILE_BANKING', '2026-01-01');
INSERT INTO collectivity_transaction (id_collectivity, id_member, id_financial_account, amount, payment_mode, creation_date) VALUES
('col-2', 'C2-M8', 'C2-A-MOBILE-1', 60000, 'MOBILE_BANKING', '2026-01-01');

-- Collectivity 3 has no payments/transactions (as per PDF)