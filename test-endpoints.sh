#!/bin/bash

BASE_URL="http://localhost:8080"

echo "=========================================="
echo "Testing All Endpoints - Agrifed API"
echo "=========================================="

# ===========================================
# 1. POST /collectivities (Create collectivities)
# ===========================================
echo -e "\n--- POST /collectivities ---"

curl -X POST "$BASE_URL/collectivities" \
  -H "Content-Type: application/json" \
  -d '[{
    "location": "Antananarivo",
    "federationApproval": true,
    "structure": {
      "president": "C1-M1",
      "vicePresident": "C1-M2",
      "treasurer": "C1-M4",
      "secretary": "C1-M3"
    },
    "members": ["C1-M1", "C1-M2", "C1-M3", "C1-M4", "C1-M5", "C1-M6", "C1-M7", "C1-M8", "C1-NEW-1", "C1-NEW-2"]
  }'

# ===========================================
# 2. GET /collectivities/{id} (Get collectivity by ID)
# ===========================================
echo -e "\n--- GET /collectivities/col-1 ---"
curl -X GET "$BASE_URL/collectivities/col-1" \
  -H "Accept: application/json"

echo -e "\n--- GET /collectivities/col-2 ---"
curl -X GET "$BASE_URL/collectivities/col-2" \
  -H "Accept: application/json"

echo -e "\n--- GET /collectivities/col-3 ---"
curl -X GET "$BASE_URL/collectivities/col-3" \
  -H "Accept: application/json"

echo -e "\n--- GET /collectivities/col-999 (Not Found) ---"
curl -X GET "$BASE_URL/collectivities/col-999" \
  -H "Accept: application/json"

# ===========================================
# 3. PUT /collectivities/{id}/informations (Update identity)
# ===========================================
echo -e "\n--- PUT /collectivities/col-1/informations (Set name) ---"
curl -X PUT "$BASE_URL/collectivities/col-1/informations" \
  -H "Content-Type: application/json" \
  -d '{"name": "Mpanorina", "number": 1}'

echo -e "\n--- PUT /collectivities/col-1/informations (Conflict - name already set) ---"
curl -X PUT "$BASE_URL/collectivities/col-1/informations" \
  -H "Content-Type: application/json" \
  -d '{"name": "New Name"}'

# ===========================================
# 4. GET /collectivities/{id}/financialAccounts (Get financial accounts)
# ===========================================
echo -e "\n--- GET /collectivities/col-1/financialAccounts ---"
curl -X GET "$BASE_URL/collectivities/col-1/financialAccounts" \
  -H "Accept: application/json"

echo -e "\n--- GET /collectivities/col-1/financialAccounts?at=2026-05-01 ---"
curl -X GET "$BASE_URL/collectivities/col-1/financialAccounts?at=2026-05-01" \
  -H "Accept: application/json"

# ===========================================
# 5. GET /collectivities/{id}/membershipFees (Get membership fees)
# ===========================================
echo -e "\n--- GET /collectivities/col-1/membershipFees ---"
curl -X GET "$BASE_URL/collectivities/col-1/membershipFees" \
  -H "Accept: application/json"

echo -e "\n--- GET /collectivities/col-2/membershipFees ---"
curl -X GET "$BASE_URL/collectivities/col-2/membershipFees" \
  -H "Accept: application/json"

# ===========================================
# 6. POST /collectivities/{id}/membershipFees (Create membership fees)
# ===========================================
echo -e "\n--- POST /collectivities/col-1/membershipFees ---"
curl -X POST "$BASE_URL/collectivities/col-1/membershipFees" \
  -H "Content-Type: application/json" \
  -d '[{
    "eligibleFrom": "2026-06-01",
    "frequency": "MONTHLY",
    "amount": 25000,
    "label": "New Monthly Fee"
  }]'

# ===========================================
# 7. GET /collectivities/{id}/transactions (Get transactions)
# ===========================================
echo -e "\n--- GET /collectivities/col-1/transactions?from=2026-01-01&to=2026-04-30 ---"
curl -X GET "$BASE_URL/collectivities/col-1/transactions?from=2026-01-01&to=2026-04-30" \
  -H "Accept: application/json"

echo -e "\n--- GET /collectivities/col-3/transactions?from=2026-04-01&to=2026-05-31 ---"
curl -X GET "$BASE_URL/collectivities/col-3/transactions?from=2026-04-01&to=2026-05-31" \
  -H "Accept: application/json"

# ===========================================
# 8. POST /members (Create members)
# ===========================================
echo -e "\n--- POST /members ---"
curl -X POST "$BASE_URL/members" \
  -H "Content-Type: application/json" \
  -d '[{
    "firstName": "Test",
    "lastName": "User",
    "birthDate": "1990-01-01",
    "gender": "MALE",
    "address": "Test Address",
    "profession": "Tester",
    "phoneNumber": 123456,
    "email": "test@test.com",
    "occupation": "JUNIOR",
    "collectivityIdentifier": "col-1",
    "referees": ["C1-M1", "C1-M2"],
    "registrationFeePaid": true,
    "membershipDuesPaid": true
  }]'

# ===========================================
# 9. POST /members/{id}/payments (Create member payments)
# ===========================================
echo -e "\n--- POST /members/C1-M1/payments ---"
curl -X POST "$BASE_URL/members/C1-M1/payments" \
  -H "Content-Type: application/json" \
  -d '[{
    "amount": 25000,
    "paymentMode": "CASH",
    "accountCreditedIdentifier": "C1-A-CASH",
    "membershipFeeIdentifier": "cot-1"
  }]'

# ===========================================
# 10. GET /collectivities/{id}/statistics (Local statistics)
# ===========================================
echo -e "\n--- GET /collectivities/col-1/statistics?from=2026-01-01&to=2026-04-30 ---"
curl -X GET "$BASE_URL/collectivities/col-1/statistics?from=2026-01-01&to=2026-04-30" \
  -H "Accept: application/json"

echo -e "\n--- GET /collectivities/col-3/statistics?from=2026-04-01&to=2026-05-31 ---"
curl -X GET "$BASE_URL/collectivities/col-3/statistics?from=2026-04-01&to=2026-05-31" \
  -H "Accept: application/json"

# ===========================================
# 11. GET /collectivities/statistics (Overall statistics - spec path /collectivites/statistics)
# ===========================================
echo -e "\n--- GET /collectivities/statistics?from=2026-01-01&to=2026-04-30 ---"
curl -X GET "$BASE_URL/collectivities/statistics?from=2026-01-01&to=2026-04-30" \
  -H "Accept: application/json"

# ===========================================
# 12. GET /collectivities/{id}/activities (Get activities)
# ===========================================
echo -e "\n--- GET /collectivities/col-1/activities ---"
curl -X GET "$BASE_URL/collectivities/col-1/activities" \
  -H "Accept: application/json"

# ===========================================
# 13. POST /collectivities/{id}/activities (Create activities with executive date)
# ===========================================
echo -e "\n--- POST /collectivities/col-1/activities (With executive date) ---"
curl -X POST "$BASE_URL/collectivities/col-1/activities" \
  -H "Content-Type: application/json" \
  -d '[{
    "label": "Test Activity",
    "activityType": "MEETING",
    "executiveDate": "2026-12-15",
    "memberOccupationConcerned": ["JUNIOR", "SENIOR"]
  }]'

echo -e "\n--- POST /collectivities/col-1/activities (With recurrence rule) ---"
curl -X POST "$BASE_URL/collectivities/col-1/activities" \
  -H "Content-Type: application/json" \
  -d '[{
    "label": "Weekly Training",
    "activityType": "TRAINING",
    "recurrenceRule": {
      "weekOrdinal": 3,
      "dayOfWeek": "SA"
    },
    "memberOccupationConcerned": ["JUNIOR"]
  }]'

echo -e "\n--- POST /collectivities/col-1/activities (FAIL - both date and recurrence) ---"
curl -X POST "$BASE_URL/collectivities/col-1/activities" \
  -H "Content-Type: application/json" \
  -d '[{
    "label": "Bad Activity",
    "activityType": "OTHER",
    "executiveDate": "2026-12-15",
    "recurrenceRule": {
      "weekOrdinal": 3,
      "dayOfWeek": "SA"
    }
  }]'

# ===========================================
# 14. GET /collectivities/{id}/activities/{activityId}/attendance (Get attendance)
# ===========================================
echo -e "\n--- First create activity for attendance test ---"
ACTIVITY_ID=$(curl -s -X POST "$BASE_URL/collectivities/col-1/activities" \
  -H "Content-Type: application/json" \
  -d '[{
    "label": "Attendance Test",
    "activityType": "MEETING",
    "executiveDate": "2026-12-20"
  }]'

echo -e "\n--- GET /collectivities/col-1/activities/$ACTIVITY_ID/attendance ---"
curl -X GET "$BASE_URL/collectivities/col-1/activities/$ACTIVITY_ID/attendance" \
  -H "Accept: application/json"

# ===========================================
# 15. POST /collectivities/{id}/activities/{activityId}/attendance (Record attendance)
# ===========================================
echo -e "\n--- POST /collectivities/col-1/activities/$ACTIVITY_ID/attendance ---"
curl -X POST "$BASE_URL/collectivities/col-1/activities/$ACTIVITY_ID/attendance" \
  -H "Content-Type: application/json" \
  -d '[{
    "memberIdentifier": "C1-M1",
    "attendanceStatus": "ATTENDED"
  }, {
    "memberIdentifier": "C1-M2",
    "attendanceStatus": "MISSING"
  }]'

echo -e "\n--- POST /collectivities/col-1/activities/$ACTIVITY_ID/attendance (FAIL - already confirmed) ---"
curl -X POST "$BASE_URL/collectivities/col-1/activities/$ACTIVITY_ID/attendance" \
  -H "Content-Type: application/json" \
  -d '[{
    "memberIdentifier": "C1-M1",
    "attendanceStatus": "MISSING"
  }]'

# ===========================================
echo -e "\n=========================================="
echo "All endpoint tests completed!"
echo "=========================================="
