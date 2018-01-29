#Name: Ethan Painter
#Info: epainte2, G00915079

#No imports

#Given a positive integer n, return list of its prime factors in increasing order.
#Include correct number of occurrences (i.e. 24 -> [2,2,2,3])step out into the night
def prime_factors(n):
    #Check if prime factors are successfully printing
    divisorList = []
    primeNumList = []
    list = []

    #Check if input is less than 2. If so, return empty list
    if n < 2:
        return list

    #Check for all known divisors of given number n
    #Check for prime divisors within list of all divisors
    #Go through prime divisor list and return multiples of divisors
    count = 0
    for i in range(2,n+1):
        if n % i == 0:
            #This number is divisible. Add to list
            divisorList.append(i)
            count = count + 1

    #Print Debug
    print("All Divisible Factors: ")
    x = 0
    while(x < len(divisorList)):
        print(divisorList[x])
        x = x + 1

    #Input must be 2 or greater
    #Loop through all possible prime numbers between 2 and n
    count = 0
    while count < len(divisorList):
        isPrime = 1
        innerCount = 2
        while innerCount <= divisorList[count] and isPrime == 1:
            if divisorList[count] % innerCount == 0 and divisorList[count] != innerCount:
                isPrime = 0
            if innerCount == divisorList[count]:
                primeNumList.append(innerCount)
            innerCount = innerCount + 1
        count = count + 1

    print("All Divisible Prime Factors: ")
    x = 0
    while(x < len(primeNumList)):
        print(primeNumList[x])
        x = x + 1

    #Now check for prime num occurences in
    #primeNumList has all recorded prime numbers between 2 and n
    index = 0
    while(n != 1):
        if n % primeNumList[index] == 0:
            n = n / primeNumList[index]
            list.append(primeNumList[index])
        else:
            index = index + 1
    return list
    #Problems with last test

#Given two integers
#Determine if both share a common divisor.
#If so, return false. If not, return true
def coprime(a, b):
    #Get prime factors for both values
    list_a = prime_factors(a)
    list_b = prime_factors(b)

    #Loop through list_a checking if each index is in list_b
    counter = 0
    while(counter < len(list_a)):
        counter2 = 0
        while(counter2 < len(list_b)):
            if(list_a[counter] == list_b[counter2]):
                #Found
                return False
            else:
                counter2 = counter2 + 1
        counter = counter + 1
    #Did not found a match
    return True

#Given non-negative integer, calculate n-th tribonacci number
#Where it starts with 1,1,1,...
def trib(n):
    #Base Cases
    if n < 0:
        return 0
    if n >= 0 and n < 3:
        return 1

    #If n is greater than or equal to 3, we need to create a list and properly add values to it
    list = []
    list.append(1)  #0
    list.append(1)  #1
    list.append(1)  #2
    counter = 3
    while(counter <= n):
        list.append(list[counter-3] + list[counter-2]+ list[counter-1])
        counter = counter + 1

    return list[counter-1]

#Given list of integers (xs) and list of
def max_new(xs, olds):
    #If base list is empty, return None
    if len(xs) == 0:
        return None
    #Loop through olds to eliminate from current list (xs)
    counter = 0
    while counter < len(olds):
        selected = olds[counter]
        counterXS = 0
        while counterXS < len(xs):
            if xs[counterXS] == olds[counter]:
                #Remove element from list xs
                del xs[counterXS]
            else:
                counterXS = counterXS + 1
        counter = counter + 1

    #Current list now has removed all values from olds
    #Just loop through list xs to check for max
    max = None
    count = 0
    while count < len(xs):
        if max == None:
            max = xs[count]
            count = count + 1
        elif xs[count] > max:
            max = xs[count]
            count = count + 1
        else:
            count = count + 1

    return max


#Defined for 'zip_with' below
def add(x,y): return x+y
def mul(x,y): return x*y

#Given a function (f): add, mul
#Given two lists with arguments
#Answer is as long as shortest of the two inputs
def zip_with(f, xs, ys):
    #Check function (f) for add
    list1Counter = 0
    list2Counter = 0
    final_list = []
    while list1Counter < len(xs) and list2Counter < len(ys):
        final_list.append(f(xs[list1Counter], ys[list2Counter]))
        list1Counter = list1Counter + 1
        list2Counter = list2Counter + 1

    return final_list

#Defined for 'pass_fail' below
def even(x): return x%2==0
def pos(x): return x>0
def big(x): return x>10
def cap(x): return 65 <= ord(x) <= 90
def lengthy(x): return len(x) > 3

#Given a predicate function (f) and a list of values (xs)
#Answer is two lists: one holds items that passed the predicate,
#and the other holds items that didn't pass the predicate
def pass_fail(f, xs):
    list_pass = []
    list_fail = []
    count = 0

    while count < len(xs):
        if f(xs[count]):
            list_pass.append(xs[count])
        else:
            list_fail.append(xs[count])
        count = count + 1

    x = 0
    y = 0
    return list_pass, list_fail

    #Not needed ???
    output = "["
    while x < len(list_pass):
        if x == len(list_pass)-1:
            output += str(list_pass[x])
        else:
            output += str(list_pass[x]) + ","
        x = x + 1

    output += "],["
    while y < len(list_fail):
        if y == len(list_fail)-1:
            output += str(list_fail[y])
        else:
            output += str(list_fail[y])+ ","
        y = y + 1
    output += "]"
    return output

#Use to reduce integer to binary solution
#Used to create binary interpretations for power set
def convert_to_binary(length, n):
    binary = 0
    binaryList = []
    #Get square value
    count = 1
    sum = 1
    while count < length:
        sum = sum * 2
        count = count + 1

    while length != 0:
        #Number includes current sum
        if n >= sum:
            n = n - sum
            sum = sum / 2
            binaryList.append(1)
        else:
            sum = sum / 2
            binaryList.append(0)
        length = length - 1

    #print(binaryList)
    return binaryList

#Given a list of values, create the power set
#Includes the empty set as well
def powerset(xs):
    outterList = []
    outterList.append([])
    #No numbers in list (Empty)
    if len(xs) == 0:
        return outterList

    #Get Squared number of expected number of sets
    sampleCount = 0
    total = 1
    while sampleCount < len(xs):
        total = total * 2
        sampleCount = sampleCount + 1

    #Assuming numbers exist in the list
    x = 1
    #x = 1 because empty set is included
    while x < total:
        #Selecting only one element per list
        binaryList = convert_to_binary(len(xs), x)
        j = 0
        loopList = []
        while j < len(xs):
            #If 1 from binary list, add to loop List
            if binaryList[j] == 1:
                loopList.append(xs[j])
            j = j + 1
        #Append loopList to power set (overall list) to return
        outterList.append(loopList)
        x = x + 1
    return outterList

def matrix_product(xss, yss):
    #Check if matrices are compatible
    #Check if matrix has 1 row (one list)
    #If xss[0] is an int (and isn't another list)
    if len(xss) > 1 and isinstance(xss[0],int):
        xRows = 1
        xCols = len(xss)
    else:
        xRows = len(xss)
        xCols = len(xss[0])

    if len(yss) > 1 and isinstance(yss[0],int):
        yRows = 1
        yCols = len(yss)
    else:
        yRows = len(yss)
        yCols = len(yss[0])

    #Debugging print statement
    #print("X Rows: " + str(xRows) + "\nX Cols: " + str(xCols) + "\nY Rows: " + str(yRows) + "\nY Cols: " + str(yCols))

    #Solution for when Rows and Cols are both greater than or equal to 2
    #If xss columns # != yss rows #
    if xCols != yRows:
        return None

    #Initialize matrix and loop through all values to multiply and store
    lastMatrix = [[0 for row in range(yCols)] for col in range(xRows)]
    for x in range(xRows):
        for y in range(yCols):
            for z in range(xCols):
                lastMatrix[x][y] += xss[x][z] * yss[z][y]
    return lastMatrix
