#Reads and tokenizes numbers (both integers and decimals)
#@source: method adapted from code written by my group from last class (Group 7)
def readNumber(line, index):
    number = 0
    while index < len(line) and (line[index].isdigit() or line[index] == "."):
        if line[index] == ".":
            decimal_val = 0.1
            index += 1
            #Compiles characters into a decimal number until end or operator is reached.
            while index < len(line) and line[index].isdigit():
                number = number + decimal_val * int(line[index])
                decimal_val *= 0.1
                index += 1
        else:
            #If not a decimal, compiles characters as an integer in the same way
            number = number * 10 + int(line[index])
            index += 1
    #Tokenizes result as a "NUMBER"
    token = {"type": "NUMBER", "number": number}
    return token, index

#Reads and tokenizes operators
#@source: tokenizing implementation adapted from Haraken's source code
def readOperator(line, index, operator):
    if operator == "+":
        token = {"type": "PLUS"}
    elif operator == "-":
        token = {"type": "MINUS"}
    elif operator == "*":
        token = {"type": "MULTIPLY"}
    elif operator == "/":
        token = {"type": "DIVIDE"}
    elif operator == "(":
        token = {"type": "START"}
    elif operator == ")":
        token = {"type": "END"}
    return token, index + 1

#@source: method adapted from code written by my group from last class (Group 7)
def tokenize(line):
    tokens = []
    index = 0
    while index < len(line):
        #If character is a number, tokenize it as a number
        if line[index].isdigit():
            (token, index) = readNumber(line, index)
        #If character is a PEMDAS operator, tokenize it as its respective operator
        elif line[index] == "+" or "-" or "*" or "/" or "(" or ")":
            (token, index) = readOperator(line, index, line[index])
        #If character is neither number nor PEMDAS operator, error
        else:
            print("Invalid character found: " + line[index])
            exit(1)
        tokens.append(token)
    return tokens

#Handles addition and subtraction using recursion
#@source: recursion idea suggested by my mentor, Rakina
def addAndSubtract(tokens, index):
    #Calls multiplyAndDivide() and ensures that those operations are taken care of first 
    #(Multiplication and division first before addition and subtraction according to PEMDAS)
    (number, index) = multiplyAndDivide(tokens, index)
    answer = number
    while (index < len(tokens) and 
    (tokens[index]["type"] == "PLUS" or tokens[index]["type"] == "MINUS")):
        #If plus token exists, add number to subsequent product and/or quotient if it exists
        #(this checked once again using multiplyAndDivide() on the next index)
        if tokens[index]["type"] == "PLUS":
            (number, index) = multiplyAndDivide(tokens, index + 1)
            answer += number
        #If minus token exists, subtract number from subsequent product and/or quotient if it exists
        #(same check for multiply and divide tokens above occurs)
        elif tokens[index]["type"] == "MINUS":
            (number, index) = multiplyAndDivide(tokens, index + 1)
            answer -= number
    return (answer, index)

#Handles multiplication and division using recursion
#@source: recursion idea suggested by my mentor, Rakina
def multiplyAndDivide(tokens, index):
    #Calls parentheses() and ensures that parentheses operations are taken care of first 
    #(Parentheses first before multiplication and division according to PEMDAS)
    (number, index) = parentheses(tokens, index)
    answer = number
    while (index < len(tokens) and 
    (tokens[index]["type"] == "MULTIPLY" or tokens[index]["type"] == "DIVIDE")):
        #If multiply token exists, multiply number to the calculated value of operations kept in
        #parentheses (this checked once again using parentheses() on the next index)
        if tokens[index]["type"] == "MULTIPLY":
            (number, index) = parentheses(tokens, index + 1)
            answer *= number
        #If divide token exists, multiply number to the calculated value of operations enclosed in
        #parentheses (same check for parentheses tokens above occurs)
        elif tokens[index]["type"] == "DIVIDE":
            if number != 0:
                (number, index) = parentheses(tokens, index + 1)
                answer /= number
            #Handles zero division error
            else:
                print("Zero division error")
                exit(1)
    return (answer, index)

#Indicates whether or not there are parentheses and handles it using recursion
#@source: recursion idea suggested by my mentor, Rakina
def parentheses(tokens, index):
    #If token is number, return then token"s number
    if tokens[index]["type"] == "NUMBER":
        return (tokens[index]["number"], index + 1)
    #If token is starting parentheses, perform all operations within it
    elif tokens[index]["type"] == "START":
        #Ensures all operations inside existing parentheses are performed
        (number, index) = addAndSubtract(tokens, index + 1) 
        #Once operations are done (indicated by end parentheses), return calculated value
        if tokens[index]["type"] == "END":
            return (number, index + 1)

#Evaluates the entire expression"s tokens
#@source: recursion idea suggested by my mentor, Rakina
def evaluate(tokens):
    #Starts with using addAndSubtract because it ensures all operations in the expression 
    #are performed through recursion
    (number, index) = addAndSubtract(tokens, 0) 
    return number

#@source: method taken from Haraken's source code
def test(line):
    tokens = tokenize(line)
    actual_answer = evaluate(tokens)
    expected_answer = eval(line)
    if abs(actual_answer - expected_answer) < 1e-8:
        print("PASS! (%s = %f)" % (line, expected_answer))
    else:
        print("FAIL! (%s should be %f but was %f)" % (line, expected_answer, actual_answer))

# Add more tests to this function :)
def run_test():
    print("==== Test started! ====")
    #Haraken's Tests
    test("1+2")
    test("1.0+2.1-3")
    #NUMBERS
    test("1")
    test("12")
    test("1.2")
    #ADD
    test("1+2")
    test("1.1+2.2")
    test("1+2+3")
    #SUBTRACT
    test("1-2")
    test("1.1-2.2")
    test("1-2-3")
    #ADD AND SUBTRACT
    test("1+2-3")
    test("1-2+3")
    #MULTIPLY (with add and subtract)
    test("1*2")
    test("1.1*2.2")
    test("1*2+3")
    test("1+2*3")
    test("1*2-3")
    test("1-2*3")
    #DIVIDE (with add and subtract)
    test("1/2")
    test("1.1/2.2")
    test("1/2+3")
    test("1+2/3")
    test("1/2-3")
    test("1-2/3")
    #MULTIPLY AND DIVIDE
    test("1/2*3")
    test("1*2/3")
    #COMBINATION OF ALL
    test("1*2-3/4+5*6-7/8")
    test("8/7+6/5-4*3+2/1")
    test("1.1*2.2-3.3/4.4+5.5")
    test("1.1/2+3.3*4-5.5")
    #PARENTHESES COMBINATIONS
    test("(1)")
    test("(1+2)")
    test("(1+(2))")
    test("((1)-2)")
    test("(1.1)")
    test("((1.1)+2.2)")
    test("(1.1-(2.2))")
    test("2/(1+1)")
    test("2*(1+1)")
    test("3.3/(1.1-2.2)")
    test("3.3*(1.1+2.2)")
    test("(1+2)*4")
    test("(1+2)/4")
    test("(1+1)*(2+2)")
    test("(1+1)/(2+2)")
    test("(1+1)*(2+2)/(3+3)")
    test("(1+1)*((2+2)/(3+3))")
    test("(1.1+1)*((2.2+2)/(3.3+3))")
    test("1/2-3.3*(4+5.5)-6.6/7.7*(8-9)")
    test("(1/(2-3.3))*4+5.5-6.6/(7.7*8)-9")
    #ERRORS - these don't work for some reason
    #print(evaluate(tokenize("1/0"))) #Should cause zero division error 
    #print(evaluate(tokenize("a"))) #Should cause invalid character error
    print("==== Test finished! ====\n")

run_test()

while True:
  print('> ', end="")
  line = input()
  tokens = tokenize(line)
  answer = evaluate(tokens)
  print("answer = %f\n" % answer)