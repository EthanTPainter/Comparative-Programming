#Any imports???

#Given a positive integer n, return list of its prime factors in increasing order.
#Include correct number of occurrences (i.e. 24 -> [2,2,2,3])step out into the night
def prime_factors(n):
    #Check if prime factors are successfully printing
    primeNumList = []
    list = []

    #Check if input is less than 2. If so, return empty list
    if n < 2:
        return list

    #Input must be 2 or greater
    #Loop through all possible prime numbers between 2 and n
    loopCheck = 2

    #range from 2(MIN) to n (n+1)
    for loopCheck in range(2,n+1):
        #Prime numbers greater than 1
        if loopCheck > 1:
            for i in range (2,loopCheck):
                if(loopCheck % i) == 0:
                    break
            else:
                primeNumList.append(loopCheck)

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
#Answer is
def pass_fail(f, xs):

    return 1

def powerset(xs):

    return 1

def matrix_product(xss, yss):

    return 1