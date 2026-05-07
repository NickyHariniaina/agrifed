
-- ============================================================
-- 1. COLLECTIVITÉS (Tableau 1)
-- ============================================================
INSERT INTO collectivity (id, location, federation_number, name) VALUES
                                                                     ('col-1', 'Ambatondrazaka', 1, 'Mpanorina'),
                                                                     ('col-2', 'Ambatondrazaka', 2, 'Dobo voalohany'),
                                                                     ('col-3', 'Brickaville',   3, 'Tantely mamy');

-- ============================================================
-- 2. MEMBRES (Tableaux 2, 3, 4)
-- joined_at = 01/01/2026 pour tous les anciens membres
-- ============================================================

-- ── Collectivité 1 ──────────────────────────────────────────
INSERT INTO member (id, firstname, lastname, birthdate, gender, address, profession, phone, email, occupation, joined_at) VALUES
                                                                                                                              ('C1-M1', 'Prénom membre 1',  'Nom membre 1',  '1980-02-01', 'MALE',   'Lot II V M Ambato.', 'Riziculteur', 341234567,  'member.1@fed-agri.mg', 'PRESIDENT',      '2026-01-01'),
                                                                                                                              ('C1-M2', 'Prénom membre 2',  'Nom membre 2',  '1982-03-05', 'MALE',   'Lot II F Ambato.',   'Agriculteur', 321234567,  'member.2@fed-agri.mg', 'VICE_PRESIDENT', '2026-01-01'),
                                                                                                                              ('C1-M3', 'Prénom membre 3',  'Nom membre 3',  '1992-03-10', 'MALE',   'Lot II J Ambato.',   'Collecteur',  331234567,  'member.3@fed-agri.mg', 'SECRETARY',      '2026-01-01'),
                                                                                                                              ('C1-M4', 'Prénom membre 4',  'Nom membre 4',  '1988-05-22', 'FEMALE', 'Lot A K 50 Ambato.', 'Distributeur',381234567,  'member.4@fed-agri.mg', 'TREASURER',      '2026-01-01'),
                                                                                                                              ('C1-M5', 'Prénom membre 5',  'Nom membre 5',  '1999-08-21', 'MALE',   'Lot UV 80 Ambato.',  'Riziculteur', 373434567,  'member.5@fed-agri.mg', 'SENIOR',         '2026-01-01'),
                                                                                                                              ('C1-M6', 'Prénom membre 6',  'Nom membre 6',  '1998-08-22', 'FEMALE', 'Lot UV 6 Ambato.',   'Riziculteur', 372234567,  'member.6@fed-agri.mg', 'SENIOR',         '2026-01-01'),
                                                                                                                              ('C1-M7', 'Prénom membre 7',  'Nom membre 7',  '1998-01-31', 'MALE',   'Lot UV 7 Ambato.',   'Riziculteur', 374234567,  'member.7@fed-agri.mg', 'SENIOR',         '2026-01-01'),
                                                                                                                              ('C1-M8', 'Prénom membre 8',  'Nom membre 8',  '1975-08-20', 'MALE',   'Lot UV 8 Ambato.',   'Riziculteur', 370234567,  'member.8@fed-agri.mg', 'SENIOR',         '2026-01-01');

-- ── Collectivité 3 (membres propres, avant col-2 qui réutilise C1-Mx) ───────
INSERT INTO member (id, firstname, lastname, birthdate, gender, address, profession, phone, email, occupation, joined_at) VALUES
                                                                                                                              ('C3-M1', 'Prénom membre 9',  'Nom membre 9',  '1988-01-02', 'MALE',   'Lot 33 J Antsirabe', 'Apiculteur',  34034567,   'member.9@fed-agri.mg',  'PRESIDENT',      '2026-01-01'),
                                                                                                                              ('C3-M2', 'Prénom membre 10', 'Nom membre 10', '1982-03-05', 'MALE',   'Lot 2 J Antsirabe',  'Agriculteur', 338634567,  'member.10@fed-agri.mg', 'VICE_PRESIDENT', '2026-01-01'),
                                                                                                                              ('C3-M3', 'Prénom membre 11', 'Nom membre 11', '1992-03-12', 'MALE',   'Lot 8 KM Antsirabe', 'Collecteur',  338234567,  'member.11@fed-agri.mg', 'SECRETARY',      '2026-01-01'),
                                                                                                                              ('C3-M4', 'Prénom membre 12', 'Nom membre 12', '1988-05-10', 'FEMALE', 'Lot A K 50 Antsirabe','Distributeur',382334567, 'member.12@fed-agri.mg', 'TREASURER',      '2026-01-01'),
                                                                                                                              ('C3-M5', 'Prénom membre 13', 'Nom membre 13', '1999-08-11', 'MALE',   'Lot UV 80 Antsirabe.','Apiculteur', 373365567,  'member.13@fed-agri.mg', 'SENIOR',         '2026-01-01'),
                                                                                                                              ('C3-M6', 'Prénom membre 14', 'Nom membre 14', '1998-08-09', 'FEMALE', 'Lot UV 6 Antsirabe.', 'Apiculteur', 378234567,  'member.14@fed-agri.mg', 'SENIOR',         '2026-01-01'),
                                                                                                                              ('C3-M7', 'Prénom membre 15', 'Nom membre 15', '1998-01-13', 'MALE',   'Lot UV 7 Antsirabe',  'Apiculteur', 374914567,  'member.15@fed-agri.mg', 'SENIOR',         '2026-01-01'),
                                                                                                                              ('C3-M8', 'Prénom membre 16', 'Nom membre 16', '1975-08-02', 'MALE',   'Lot UV 8 Antsirabe',  'Apiculteur', 370634567,  'member.16@fed-agri.mg', 'SENIOR',         '2026-01-01');

-- ============================================================
-- 3. LIAISONS MEMBRES ↔ COLLECTIVITÉS (member_collectivity)
-- ============================================================

-- Collectivité 1
INSERT INTO member_collectivity (id_member, id_collectivity) VALUES
                                                                 ('C1-M1', 'col-1'), ('C1-M2', 'col-1'), ('C1-M3', 'col-1'), ('C1-M4', 'col-1'),
                                                                 ('C1-M5', 'col-1'), ('C1-M6', 'col-1'), ('C1-M7', 'col-1'), ('C1-M8', 'col-1');

-- Collectivité 2 (mêmes membres physiques que col-1, rôles différents)
INSERT INTO member_collectivity (id_member, id_collectivity) VALUES
                                                                 ('C1-M1', 'col-2'), ('C1-M2', 'col-2'), ('C1-M3', 'col-2'), ('C1-M4', 'col-2'),
                                                                 ('C1-M5', 'col-2'), ('C1-M6', 'col-2'), ('C1-M7', 'col-2'), ('C1-M8', 'col-2');

-- Collectivité 3
INSERT INTO member_collectivity (id_member, id_collectivity) VALUES
                                                                 ('C3-M1', 'col-3'), ('C3-M2', 'col-3'), ('C3-M3', 'col-3'), ('C3-M4', 'col-3'),
                                                                 ('C3-M5', 'col-3'), ('C3-M6', 'col-3'), ('C3-M7', 'col-3'), ('C3-M8', 'col-3');

-- ============================================================
-- 4. STRUCTURE DES COLLECTIVITÉS (president, vp, trésorier, secrétaire)
-- ============================================================

-- col-1 : M1=Président, M2=VP, M3=Secrétaire, M4=Trésorier
UPDATE collectivity SET
                        president_id      = 'C1-M1',
                        vice_president_id = 'C1-M2',
                        secretary_id      = 'C1-M3',
                        treasurer_id      = 'C1-M4'
WHERE id = 'col-1';

-- col-2 : M5=Président, M6=VP, M7=Secrétaire, M8=Trésorier (Tableau 3)
UPDATE collectivity SET
                        president_id      = 'C1-M5',
                        vice_president_id = 'C1-M6',
                        secretary_id      = 'C1-M7',
                        treasurer_id      = 'C1-M8'
WHERE id = 'col-2';

-- col-3 : C3-M1=Président, C3-M2=VP, C3-M3=Secrétaire, C3-M4=Trésorier
UPDATE collectivity SET
                        president_id      = 'C3-M1',
                        vice_president_id = 'C3-M2',
                        secretary_id      = 'C3-M3',
                        treasurer_id      = 'C3-M4'
WHERE id = 'col-3';

-- ============================================================
-- 5. RÉFÉRENCES (parrainages) — Tableaux 2, 3, 4
-- ============================================================

-- Col-1 : M3 et M4 parrainés par M1 + M2
INSERT INTO reference (id_member_refered, id_member_referer) VALUES
                                                                 ('C1-M3', 'C1-M1'), ('C1-M3', 'C1-M2'),
                                                                 ('C1-M4', 'C1-M1'), ('C1-M4', 'C1-M2'),
                                                                 ('C1-M5', 'C1-M1'), ('C1-M5', 'C1-M2'),
                                                                 ('C1-M6', 'C1-M1'), ('C1-M6', 'C1-M2'),
                                                                 ('C1-M7', 'C1-M1'), ('C1-M7', 'C1-M2'),
                                                                 ('C1-M8', 'C1-M6'), ('C1-M8', 'C1-M7');

-- Col-3 : parrainages internes
INSERT INTO reference (id_member_refered, id_member_referer) VALUES
                                                                 ('C3-M3', 'C3-M1'), ('C3-M3', 'C3-M2'),
                                                                 ('C3-M4', 'C3-M1'), ('C3-M4', 'C3-M2'),
                                                                 ('C3-M5', 'C3-M1'), ('C3-M5', 'C3-M2'),
                                                                 ('C3-M6', 'C3-M1'), ('C3-M6', 'C3-M2'),
                                                                 ('C3-M7', 'C3-M1'), ('C3-M7', 'C3-M2'),
                                                                 ('C3-M8', 'C3-M1'), ('C3-M8', 'C3-M2');

-- ============================================================
-- 6. COMPTES FINANCIERS DE BASE (données initiales conservées)
-- ============================================================

-- Col-1
INSERT INTO financial_account (id, account_type, amount) VALUES
    ('C1-A-CASH', 'CASH', 0);
INSERT INTO financial_account (id, account_type, amount, holder_name, mobile_banking_service, mobile_number) VALUES
    ('C1-A-MOBILE-1', 'MOBILE_BANKING', 0, 'Mpanorina', 'ORANGE_MONEY', 370489612);

-- Col-2
INSERT INTO financial_account (id, account_type, amount) VALUES
    ('C2-A-CASH', 'CASH', 0);
INSERT INTO financial_account (id, account_type, amount, holder_name, mobile_banking_service, mobile_number) VALUES
    ('C2-A-MOBILE-1', 'MOBILE_BANKING', 0, 'Dobo voalohany', 'ORANGE_MONEY', 320489612);

-- Col-3 (caisse de base uniquement)
INSERT INTO financial_account (id, account_type, amount) VALUES
    ('C3-A-CASH', 'CASH', 0);

-- ============================================================
-- NOUVELLES DONNÉES DU 6 MAI 2026
-- ============================================================

-- ============================================================
-- 7. NOUVEAUX COMPTES FINANCIERS — Col-3 (point 1)
-- ============================================================

-- Deux comptes bancaires
INSERT INTO financial_account (id, account_type, amount, bank_name, bank_code, bank_branch_code, bank_account_number, bank_account_key, holder_name) VALUES
                                                                                                                                                         ('C3-A-BANK-1', 'BANK', 0, 'BMOI', 4, 1, 1234567890, 12, 'Koto'),
                                                                                                                                                         ('C3-A-BANK-2', 'BANK', 0, 'BRED', 8, 3, 4567890123, 58, 'Naivo');

-- Un compte mobile money
INSERT INTO financial_account (id, account_type, amount, holder_name, mobile_banking_service, mobile_number) VALUES
    ('C3-A-MOBILE-1', 'MOBILE_BANKING', 0, 'Kolo', 'MVOLA', 341889612);

-- ============================================================
-- 8. COTISATIONS (point 2) — Tableaux 12, 13, 14
-- ============================================================

-- a) Collectivité 1
INSERT INTO membership_fee (id, label, status, frequency, eligible_from, amount, id_collectivity) VALUES
                                                                                                      ('cot-1', 'Cotisation annuelle', 'ACTIVE',   'ANNUALLY',    '2026-01-01', 200000, 'col-1'),
                                                                                                      ('cot-2', 'Famangiana',          'ACTIVE',   'PUNCTUALLY',  '2026-04-30',  20000, 'col-1');

-- b) Collectivité 2
INSERT INTO membership_fee (id, label, status, frequency, eligible_from, amount, id_collectivity) VALUES
                                                                                                      ('cot-3', 'Cotisation annuelle', 'ACTIVE',   'ANNUALLY', '2026-01-01', 200000, 'col-2'),
                                                                                                      ('cot-4', 'Cotisation 2025',     'INACTIVE', 'ANNUALLY', '2025-01-01', 100000, 'col-2');

-- c) Collectivité 3
INSERT INTO membership_fee (id, label, status, frequency, eligible_from, amount, id_collectivity) VALUES
    ('cot-5', 'Cotisation mensuelle', 'ACTIVE', 'MONTHLY', '2026-04-01', 25000, 'col-3');

-- ============================================================
-- 9. PAIEMENTS ET TRANSACTIONS (point 3)
-- ============================================================

-- ── Collectivité 1 (Tableau 15) ─────────────────────────────
INSERT INTO member_payment (id, id_member, id_membership_fee, id_financial_account, amount, payment_mode, creation_date) VALUES
                                                                                                                             ('pay-c1-m1', 'C1-M1', 'cot-1', 'C1-A-CASH',     200000, 'CASH',           '2026-01-01'),
                                                                                                                             ('pay-c1-m2', 'C1-M2', 'cot-1', 'C1-A-CASH',     200000, 'CASH',           '2026-01-01'),
                                                                                                                             ('pay-c1-m3', 'C1-M3', 'cot-1', 'C1-A-MOBILE-1', 200000, 'MOBILE_BANKING', '2026-01-01'),
                                                                                                                             ('pay-c1-m4', 'C1-M4', 'cot-1', 'C1-A-MOBILE-1', 200000, 'MOBILE_BANKING', '2026-01-01'),
                                                                                                                             ('pay-c1-m5', 'C1-M5', 'cot-1', 'C1-A-MOBILE-1', 150000, 'MOBILE_BANKING', '2026-01-01'),
                                                                                                                             ('pay-c1-m6', 'C1-M6', 'cot-1', 'C1-A-CASH',     100000, 'CASH',           '2026-05-01'),
                                                                                                                             ('pay-c1-m7', 'C1-M7', 'cot-1', 'C1-A-CASH',      60000, 'CASH',           '2026-05-01'),
                                                                                                                             ('pay-c1-m8', 'C1-M8', 'cot-1', 'C1-A-CASH',      90000, 'CASH',           '2026-05-01');

INSERT INTO collectivity_transaction (id, id_collectivity, id_member, id_financial_account, amount, payment_mode, creation_date) VALUES
                                                                                                                                     ('tx-c1-m1', 'col-1', 'C1-M1', 'C1-A-CASH',     200000, 'CASH',           '2026-01-01'),
                                                                                                                                     ('tx-c1-m2', 'col-1', 'C1-M2', 'C1-A-CASH',     200000, 'CASH',           '2026-01-01'),
                                                                                                                                     ('tx-c1-m3', 'col-1', 'C1-M3', 'C1-A-MOBILE-1', 200000, 'MOBILE_BANKING', '2026-01-01'),
                                                                                                                                     ('tx-c1-m4', 'col-1', 'C1-M4', 'C1-A-MOBILE-1', 200000, 'MOBILE_BANKING', '2026-01-01'),
                                                                                                                                     ('tx-c1-m5', 'col-1', 'C1-M5', 'C1-A-MOBILE-1', 150000, 'MOBILE_BANKING', '2026-01-01'),
                                                                                                                                     ('tx-c1-m6', 'col-1', 'C1-M6', 'C1-A-CASH',     100000, 'CASH',           '2026-05-01'),
                                                                                                                                     ('tx-c1-m7', 'col-1', 'C1-M7', 'C1-A-CASH',      60000, 'CASH',           '2026-05-01'),
                                                                                                                                     ('tx-c1-m8', 'col-1', 'C1-M8', 'C1-A-CASH',      90000, 'CASH',           '2026-05-01');

-- ── Collectivité 2 (Tableau 16) ─────────────────────────────
INSERT INTO member_payment (id, id_member, id_membership_fee, id_financial_account, amount, payment_mode, creation_date) VALUES
                                                                                                                             ('pay-c2-m1', 'C1-M1', 'cot-3', 'C2-A-CASH',     120000, 'CASH',           '2026-01-01'),
                                                                                                                             ('pay-c2-m2', 'C1-M2', 'cot-3', 'C2-A-CASH',     180000, 'CASH',           '2026-01-01'),
                                                                                                                             ('pay-c2-m3', 'C1-M3', 'cot-3', 'C2-A-CASH',     200000, 'CASH',           '2026-01-01'),
                                                                                                                             ('pay-c2-m4', 'C1-M4', 'cot-3', 'C2-A-CASH',     200000, 'CASH',           '2026-01-01'),
                                                                                                                             ('pay-c2-m5', 'C1-M5', 'cot-3', 'C2-A-CASH',     200000, 'CASH',           '2026-01-01'),
                                                                                                                             ('pay-c2-m6', 'C1-M6', 'cot-3', 'C2-A-CASH',     200000, 'CASH',           '2026-01-01'),
                                                                                                                             ('pay-c2-m7', 'C1-M7', 'cot-3', 'C2-A-MOBILE-1',  80000, 'MOBILE_BANKING', '2026-01-01'),
                                                                                                                             ('pay-c2-m8', 'C1-M8', 'cot-3', 'C2-A-MOBILE-1', 120000, 'MOBILE_BANKING', '2026-01-01');

INSERT INTO collectivity_transaction (id, id_collectivity, id_member, id_financial_account, amount, payment_mode, creation_date) VALUES
                                                                                                                                     ('tx-c2-m1', 'col-2', 'C1-M1', 'C2-A-CASH',     120000, 'CASH',           '2026-01-01'),
                                                                                                                                     ('tx-c2-m2', 'col-2', 'C1-M2', 'C2-A-CASH',     180000, 'CASH',           '2026-01-01'),
                                                                                                                                     ('tx-c2-m3', 'col-2', 'C1-M3', 'C2-A-CASH',     200000, 'CASH',           '2026-01-01'),
                                                                                                                                     ('tx-c2-m4', 'col-2', 'C1-M4', 'C2-A-CASH',     200000, 'CASH',           '2026-01-01'),
                                                                                                                                     ('tx-c2-m5', 'col-2', 'C1-M5', 'C2-A-CASH',     200000, 'CASH',           '2026-01-01'),
                                                                                                                                     ('tx-c2-m6', 'col-2', 'C1-M6', 'C2-A-CASH',     200000, 'CASH',           '2026-01-01'),
                                                                                                                                     ('tx-c2-m7', 'col-2', 'C1-M7', 'C2-A-MOBILE-1',  80000, 'MOBILE_BANKING', '2026-01-01'),
                                                                                                                                     ('tx-c2-m8', 'col-2', 'C1-M8', 'C2-A-MOBILE-1', 120000, 'MOBILE_BANKING', '2026-01-01');

-- ── Collectivité 3 (Tableau 17) ─────────────────────────────
-- Paiements d'avril 2026
INSERT INTO member_payment (id, id_member, id_membership_fee, id_financial_account, amount, payment_mode, creation_date) VALUES
                                                                                                                             ('pay-c3-m1-apr', 'C3-M1', 'cot-5', 'C3-A-BANK-1', 25000, 'BANK_TRANSFER', '2026-04-01'),
                                                                                                                             ('pay-c3-m2-apr', 'C3-M2', 'cot-5', 'C3-A-BANK-1', 25000, 'BANK_TRANSFER', '2026-04-01'),
                                                                                                                             ('pay-c3-m3-apr', 'C3-M3', 'cot-5', 'C3-A-BANK-1', 25000, 'BANK_TRANSFER', '2026-04-01'),
                                                                                                                             ('pay-c3-m4-apr', 'C3-M4', 'cot-5', 'C3-A-BANK-1', 25000, 'BANK_TRANSFER', '2026-04-01'),
                                                                                                                             ('pay-c3-m5-apr', 'C3-M5', 'cot-5', 'C3-A-BANK-2', 25000, 'BANK_TRANSFER', '2026-04-01'),
                                                                                                                             ('pay-c3-m6-apr', 'C3-M6', 'cot-5', 'C3-A-BANK-2', 25000, 'BANK_TRANSFER', '2026-04-01'),
                                                                                                                             ('pay-c3-m7-apr', 'C3-M7', 'cot-5', 'C3-A-CASH',   25000, 'CASH',          '2026-04-01'),
                                                                                                                             ('pay-c3-m8-apr', 'C3-M8', 'cot-5', 'C3-A-CASH',   25000, 'CASH',          '2026-04-01');

INSERT INTO collectivity_transaction (id, id_collectivity, id_member, id_financial_account, amount, payment_mode, creation_date) VALUES
                                                                                                                                     ('tx-c3-m1-apr', 'col-3', 'C3-M1', 'C3-A-BANK-1', 25000, 'BANK_TRANSFER', '2026-04-01'),
                                                                                                                                     ('tx-c3-m2-apr', 'col-3', 'C3-M2', 'C3-A-BANK-1', 25000, 'BANK_TRANSFER', '2026-04-01'),
                                                                                                                                     ('tx-c3-m3-apr', 'col-3', 'C3-M3', 'C3-A-BANK-1', 25000, 'BANK_TRANSFER', '2026-04-01'),
                                                                                                                                     ('tx-c3-m4-apr', 'col-3', 'C3-M4', 'C3-A-BANK-1', 25000, 'BANK_TRANSFER', '2026-04-01'),
                                                                                                                                     ('tx-c3-m5-apr', 'col-3', 'C3-M5', 'C3-A-BANK-2', 25000, 'BANK_TRANSFER', '2026-04-01'),
                                                                                                                                     ('tx-c3-m6-apr', 'col-3', 'C3-M6', 'C3-A-BANK-2', 25000, 'BANK_TRANSFER', '2026-04-01'),
                                                                                                                                     ('tx-c3-m7-apr', 'col-3', 'C3-M7', 'C3-A-CASH',   25000, 'CASH',          '2026-04-01'),
                                                                                                                                     ('tx-c3-m8-apr', 'col-3', 'C3-M8', 'C3-A-CASH',   25000, 'CASH',          '2026-04-01');

-- Paiements de mai 2026
INSERT INTO member_payment (id, id_member, id_membership_fee, id_financial_account, amount, payment_mode, creation_date) VALUES
                                                                                                                             ('pay-c3-m1-may', 'C3-M1', 'cot-5', 'C3-A-BANK-1',  25000, 'BANK_TRANSFER', '2026-05-01'),
                                                                                                                             ('pay-c3-m2-may', 'C3-M2', 'cot-5', 'C3-A-BANK-1',  25000, 'BANK_TRANSFER', '2026-05-01'),
                                                                                                                             ('pay-c3-m3-may', 'C3-M3', 'cot-5', 'C3-A-MOBILE-1', 15000, 'MOBILE_BANKING','2026-05-01'),
                                                                                                                             ('pay-c3-m4-may', 'C3-M4', 'cot-5', 'C3-A-MOBILE-1', 15000, 'MOBILE_BANKING','2026-05-01'),
                                                                                                                             ('pay-c3-m5-may', 'C3-M5', 'cot-5', 'C3-A-BANK-2',  20000, 'BANK_TRANSFER', '2026-05-01'),
                                                                                                                             ('pay-c3-m6-may', 'C3-M6', 'cot-5', 'C3-A-BANK-2',  25000, 'BANK_TRANSFER', '2026-05-01'),
                                                                                                                             ('pay-c3-m7-may', 'C3-M7', 'cot-5', 'C3-A-CASH',     5000, 'CASH',          '2026-05-01'),
                                                                                                                             ('pay-c3-m8-may', 'C3-M8', 'cot-5', 'C3-A-CASH',     5000, 'CASH',          '2026-05-01');

INSERT INTO collectivity_transaction (id, id_collectivity, id_member, id_financial_account, amount, payment_mode, creation_date) VALUES
                                                                                                                                     ('tx-c3-m1-may', 'col-3', 'C3-M1', 'C3-A-BANK-1',   25000, 'BANK_TRANSFER', '2026-05-01'),
                                                                                                                                     ('tx-c3-m2-may', 'col-3', 'C3-M2', 'C3-A-BANK-1',   25000, 'BANK_TRANSFER', '2026-05-01'),
                                                                                                                                     ('tx-c3-m3-may', 'col-3', 'C3-M3', 'C3-A-MOBILE-1', 15000, 'MOBILE_BANKING','2026-05-01'),
                                                                                                                                     ('tx-c3-m4-may', 'col-3', 'C3-M4', 'C3-A-MOBILE-1', 15000, 'MOBILE_BANKING','2026-05-01'),
                                                                                                                                     ('tx-c3-m5-may', 'col-3', 'C3-M5', 'C3-A-BANK-2',   20000, 'BANK_TRANSFER', '2026-05-01'),
                                                                                                                                     ('tx-c3-m6-may', 'col-3', 'C3-M6', 'C3-A-BANK-2',   25000, 'BANK_TRANSFER', '2026-05-01'),
                                                                                                                                     ('tx-c3-m7-may', 'col-3', 'C3-M7', 'C3-A-CASH',      5000, 'CASH',          '2026-05-01'),
                                                                                                                                     ('tx-c3-m8-may', 'col-3', 'C3-M8', 'C3-A-CASH',      5000, 'CASH',          '2026-05-01');

-- ============================================================
-- 10. NOUVEAUX ADHÉRENTS (point 4) — Tableaux 18, 19, 20
-- joined_at selon le tableau ; données <random> inventées
-- ============================================================

-- ── Col-1 : 4 nouveaux juniors (Tableau 18) ─────────────────
INSERT INTO member (id, firstname, lastname, birthdate, gender, address, profession, phone, email, occupation, joined_at) VALUES
                                                                                                                              ('C1-NEW-1', 'Tojo',   'Rakoto',   '2000-05-10', 'MALE',   'Lot A1 Ambato.', 'Agriculteur', 340000001, 'new1.c1@fed-agri.mg', 'JUNIOR', '2026-04-01'),
                                                                                                                              ('C1-NEW-2', 'Fara',   'Rabe',     '2001-03-22', 'FEMALE', 'Lot A2 Ambato.', 'Agriculteur', 340000002, 'new2.c1@fed-agri.mg', 'JUNIOR', '2026-04-01'),
                                                                                                                              ('C1-NEW-3', 'Hery',   'Andria',   '1999-07-15', 'MALE',   'Lot A3 Ambato.', 'Riziculteur', 340000003, 'new3.c1@fed-agri.mg', 'JUNIOR', '2026-05-01'),
                                                                                                                              ('C1-NEW-4', 'Miora',  'Rasoa',    '2002-11-08', 'FEMALE', 'Lot A4 Ambato.', 'Riziculteur', 340000004, 'new4.c1@fed-agri.mg', 'JUNIOR', '2026-06-01');

INSERT INTO member_collectivity (id_member, id_collectivity) VALUES
                                                                 ('C1-NEW-1', 'col-1'), ('C1-NEW-2', 'col-1'),
                                                                 ('C1-NEW-3', 'col-1'), ('C1-NEW-4', 'col-1');

INSERT INTO reference (id_member_refered, id_member_referer) VALUES
                                                                 ('C1-NEW-1', 'C1-M1'), ('C1-NEW-1', 'C1-M2'),
                                                                 ('C1-NEW-2', 'C1-M1'), ('C1-NEW-2', 'C1-M2'),
                                                                 ('C1-NEW-3', 'C1-M1'), ('C1-NEW-3', 'C1-M2'),
                                                                 ('C1-NEW-4', 'C1-M1'), ('C1-NEW-4', 'C1-M2');

-- ── Col-2 : 3 nouveaux juniors (Tableau 19) ─────────────────
INSERT INTO member (id, firstname, lastname, birthdate, gender, address, profession, phone, email, occupation, joined_at) VALUES
                                                                                                                              ('C2-NEW-1', 'Tiana',  'Rahari',   '2001-01-20', 'FEMALE', 'Lot B1 Ambato.', 'Agriculteur', 340000005, 'new1.c2@fed-agri.mg', 'JUNIOR', '2026-03-01'),
                                                                                                                              ('C2-NEW-2', 'Njaka',  'Ravelo',   '2000-09-14', 'MALE',   'Lot B2 Ambato.', 'Riziculteur', 340000006, 'new2.c2@fed-agri.mg', 'JUNIOR', '2026-03-01'),
                                                                                                                              ('C2-NEW-3', 'Soa',    'Randria',  '1999-12-03', 'FEMALE', 'Lot B3 Ambato.', 'Agriculteur', 340000007, 'new3.c2@fed-agri.mg', 'JUNIOR', '2026-03-01');

INSERT INTO member_collectivity (id_member, id_collectivity) VALUES
                                                                 ('C2-NEW-1', 'col-2'), ('C2-NEW-2', 'col-2'), ('C2-NEW-3', 'col-2');

INSERT INTO reference (id_member_refered, id_member_referer) VALUES
                                                                 ('C2-NEW-1', 'C1-M1'), ('C2-NEW-1', 'C1-M2'),
                                                                 ('C2-NEW-2', 'C1-M1'), ('C2-NEW-2', 'C1-M2'),
                                                                 ('C2-NEW-3', 'C1-M1'), ('C2-NEW-3', 'C1-M2');

-- ── Col-3 : 6 nouveaux juniors (Tableau 20) ─────────────────
INSERT INTO member (id, firstname, lastname, birthdate, gender, address, profession, phone, email, occupation, joined_at) VALUES
                                                                                                                              ('C3-NEW-1', 'Bao',    'Rajoana',  '2001-06-11', 'MALE',   'Lot C1 Brickaville.', 'Apiculteur', 340000008, 'new1.c3@fed-agri.mg', 'JUNIOR', '2026-01-01'),
                                                                                                                              ('C3-NEW-2', 'Zo',     'Ratsimba', '2000-08-25', 'FEMALE', 'Lot C2 Brickaville.', 'Apiculteur', 340000009, 'new2.c3@fed-agri.mg', 'JUNIOR', '2026-02-01'),
                                                                                                                              ('C3-NEW-3', 'Dina',   'Rafidy',   '2002-02-17', 'FEMALE', 'Lot C3 Brickaville.', 'Apiculteur', 340000010, 'new3.c3@fed-agri.mg', 'JUNIOR', '2026-02-01'),
                                                                                                                              ('C3-NEW-4', 'Lanto',  'Razaka',   '1999-04-30', 'MALE',   'Lot C4 Brickaville.', 'Apiculteur', 340000011, 'new4.c3@fed-agri.mg', 'JUNIOR', '2026-03-01'),
                                                                                                                              ('C3-NEW-5', 'Mamy',   'Rakoto',   '2001-10-05', 'MALE',   'Lot C5 Brickaville.', 'Apiculteur', 340000012, 'new5.c3@fed-agri.mg', 'JUNIOR', '2026-03-01'),
                                                                                                                              ('C3-NEW-6', 'Voavy',  'Randevo',  '2000-07-19', 'FEMALE', 'Lot C6 Brickaville.', 'Apiculteur', 340000013, 'new6.c3@fed-agri.mg', 'JUNIOR', '2026-03-01');

INSERT INTO member_collectivity (id_member, id_collectivity) VALUES
                                                                 ('C3-NEW-1', 'col-3'), ('C3-NEW-2', 'col-3'), ('C3-NEW-3', 'col-3'),
                                                                 ('C3-NEW-4', 'col-3'), ('C3-NEW-5', 'col-3'), ('C3-NEW-6', 'col-3');

INSERT INTO reference (id_member_refered, id_member_referer) VALUES
                                                                 ('C3-NEW-1', 'C3-M1'), ('C3-NEW-1', 'C3-M2'),
                                                                 ('C3-NEW-2', 'C3-M1'), ('C3-NEW-2', 'C3-M2'),
                                                                 ('C3-NEW-3', 'C3-M1'), ('C3-NEW-3', 'C3-M2'),
                                                                 ('C3-NEW-4', 'C3-M1'), ('C3-NEW-4', 'C3-M2'),
                                                                 ('C3-NEW-5', 'C3-M1'), ('C3-NEW-5', 'C3-M2'),
                                                                 ('C3-NEW-6', 'C3-M1'), ('C3-NEW-6', 'C3-M2');