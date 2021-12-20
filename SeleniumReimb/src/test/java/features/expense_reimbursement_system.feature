Feature: Expense Reimbursement System scenarios
  Scenario: Employee entering correct e-mail, username and password places them on Employee Reimbursement page
    Given Employee is on login page
    When Employee enters email username and password
    Then Employee is now on Employee Expense Reimbursements dashboard
  Scenario: Creating expense reimbursement request will result in new line item in expense reimbursements request list
    Given Employee has logged in and is on the Employee Expense Reimbursements dashboard
    When  Employee completes and submits Create Expense Reimbursement Request form
    Then  A new expense reimbursement request will appear in list