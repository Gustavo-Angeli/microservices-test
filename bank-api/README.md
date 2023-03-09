
## API Reference

#### Create a account

```http
  POST /api/bank/v1/create
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `accountName` | `String` | **Required**. The name of your account |
| `accountPassword` | `String` | **Required**. Your password |
| `accountBalance` | `Double` | how much money your account starts |

#### Login in one account

```http
  POST /api/bank/v1/login
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `accountName` | `String` | **Required**. Your account name |
| `accountPassword` | `String` | **Required**. The password of your account|

#### Deposit money

```http
  PUT /api/bank/v1/deposit
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `accountName` | `String` | **Required**. Target account name |
| `depositValue` | `Double` | **Required**. The amount of money the account will receive|

#### Transfer money

```http
  PUT /api/bank/v1/transfer
```

To use this method, you need to place accesToken,
generated in /api/bank/v1/login,
in the authorization header.

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `Authorization (Header param)` | `String` | **Required**. The token that is generated when logging in |
| `destinyAccountName` | `String` | **Required**. Target account name |
| `valueTransfer` | `Double` | **Required**. The amount of money the account will receive|




