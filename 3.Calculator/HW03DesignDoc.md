# Google STEP Homework 3 Design Document
*Carina Samson*
_______
<u>**Calculator**</u>
-------
*Algorithm*
1. Tokenize each character in the given expression and store each in a list of tokens (ordered by append)
    1. If the character is a number, read it and tokenize it as a number token
    2. If the character is an arithmetic PEMDAS operator, read it and tokenize it as its respective operator
2. Loop through the tokens and evaluate the operations using recursive calls
    1. Depending on the operator, will perform the respective operation according to PEMDAS
        1. Order of operations: Parentheses -> Multiply or Divide -> Add or Subtract
    2. Token evaluation will start by calling addAndSubtract() on index 0 of the string
        1. This ensures that all operations are performed on expression following PEMDAS order
        2. addAndSubtract() will call multiplyAndDivide() before performing any add and subtract operations to ensure PEMDAS order
        3. multiplyAndDivide() will call parentheses() before performing any multiply and divide operations to ensure PEMDAS order
        4. parentheses() will check for parentheses and call addAndSubtract() once again, performing operations within it via recursion until a closing parentheses is found (if opening parentheses isn't found, will return the number token and its index instead)
    3. All evaluation methods take in and return the tokens list and current index

*Diagram of Recursion*

https://docs.google.com/drawings/d/1lm7MZky4JFNXfyeBP3sRmJ95Kk1WrTmUh4suzigJo5Q/edit?usp=sharing

*Methods*
- `tokenize()`
    - `readNumber()`
    - `readOperator()`
- `evaluate()`
    - `addAndSubtract()`
    - `multiplyAndDivide()`
    - `parentheses()`