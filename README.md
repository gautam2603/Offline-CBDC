# 🏦 OFFLINE CBDC – RBI HARBRINGER HACKATHON (PS-2)  
*A secure, hardware-backed, offline digital cash prototype*

---

## ⭐ Overview

Offline CBDC is a SIM-grade, hardware-secured offline wallet that enables CBDC transfers without internet, using:

- Android App (BLE + QR + P2P logic)  
- Secure-Element (JavaCard-style) mock  
- Mock Bank Backend  
- Offline issuance, offline spending & offline receiving  
- Double-spend protection & token-level security  

This project demonstrates a working prototype for the RBI Harbinger Hackathon – Problem Statement 2:  
“Innovative, secure, offline CBDC payments with hardware support.”



## 🧩 System Architecture (ASCII Diagram)

```text
+---------------------------+
|      RBI Mock Server      |
|  Issues & Verifies Tokens |
+-------------+-------------+
              |
              | Online (only for issuance/sync)
              v
+---------------------+     BLE / QR     +----------------------+
|    Sender Android   | <--------------> |   Receiver Android   |
|   Offline CBDC App  |                  |   Offline CBDC App   |
+----------+----------+                  +-----------+----------+
           | APDU-like calls                           |
           v                                           v
     +-----------+                                +-----------+
     |  Secure   |                                |  Secure   |
     |  Element  |                                |  Element  |
     |  (Mock)   |                                |  (Mock)   |
     +-----------+                                +-----------+
```


## 🔐 Key Features

✔ Fully Offline Payments  
- Works with Bluetooth LE, QR codes
- No internet required for spending or receiving

✔ Hardware-Security Model  
- Tokens issued with signatures  
- Anti-replay & double-spend protection  
- Secure-element simulated logic

✔ Denominated CBDC Notes  
- Bank issues ₹10, ₹50, ₹100 digital notes  
- Each with unique token IDs

✔ Transparent User Wallet  
- Stored Balance  
- Token list  
- Transaction history

---

## 🏛 Core Components

### 1. Android App (Jetpack Compose)
Handles:  
- Token issuance  
- Offline send  
- Offline receive  
- Parsing transfer packets  
- Encryption mocks  
- UI/UX & animations  

Screens: Home, Wallet, Issue Notes, Pay (BLE), Scan & Pay (QR), Success Screens.

---

### 2. Token Repository (Logic Layer)
Manages:  
- Token creation  
- Spending  
- Receiving  
- Transaction list  
- Denomination splitting  
- UI-friendly logs

---

### 3. Mock Secure-Element
Simulated functions:  
STORE_TOKEN  
SPEND_TOKEN  
RECEIVE_TOKEN  
Monotonic counters  
Token validity checks

---

### 4. Backend (Future)
RBI mock node:  
- Issues tokens with issueDenominated()  
- Reconciliation endpoint (planned)

---

## 💵 Token Issuance Flow

User enters amount → app splits into notes → each note becomes:  
tokenId (UUID), denomination (10/50/100), signature.

Example: issuing ₹160 → 100 + 50 + 10 notes.

---

## 🔄 Offline Payment Flow

Select device →  Secure handshake →  Enter amount →  Enter PIN →  Secure Element signs packet →  Transfer via BLE →  Receiver SE validates & stores →  Local ledger updates.

---

## 📱 Recent Transactions

Color-coded:  
- Green (+) = received  
- Red (–) = spent  
- Blue (bank) = issuance logs

---

## 🚀 Why This Solves Harbinger PS-2

- Works offline  
- Simulates secure hardware  
- Prevents double spending  
- Realistic CBDC lifecycle  
- Clean separation of concerns  
- Implementable in hackathon timeframe  
- Mimics digital cash behavior  
- No dependency on telecom or internet

---

## 📦 Project Structure

/ui  
  /screens  
  /theme  

/viewmodel  
  TokenViewModel.kt  

/data  
  TokenRepository.kt  

/models  
  Token.kt  
  TransferPacket.kt  
  TransactionItem.kt

---

## 🛠 Tech Stack

Kotlin + Jetpack Compose  
Android BLE  
QR: ZXing  
Mock JavaCard SE  
MVVM  

---

## 📝 Example Token Structure (as plain text)

tokenId: UUID  
amount: 50  
signature: bank_mock_signature  
spent: false  

---

## 👑 Contribution & Build Notes

- No internet required once app starts  
- Pure local-state prototype  
- Bank issuance is fully offline  

---
