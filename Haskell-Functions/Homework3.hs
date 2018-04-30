{- Name: Ethan Painter
   G#: G00915079
   netID: epainte2
   Due Date: 2/26/18
-}
module Homework3 where
import Prelude hiding (zipWith)

{- FUNCTION 1 START-}
--HELPER method
--Check if a number is prime
isPrime :: Int -> Bool
isPrime n = getFactorsList n == [1,n]

--HELPER method
--Used to get the numbers divisible by given n
getFactorsList :: Int -> [Int]
getFactorsList n = [x | x <- [1..n], mod n x == 0] 
{- List of all divisors/factors of a given number n -}

--HELPER METHOD 
--My implementation of elem
my_elem _ [] = False
my_elem ele (x:xs)
    | ele == x  = True
    | otherwise = my_elem ele xs
{- Similar to elem -}

--HELPER METHOD
--Put primes in a list
primes :: [Int]
primes = 2 : filter isPrime [3..]
{- Get list of primes relying on isPrime function filtering -}

--Get all prime factors of given int value
{- Odd errors with spacing so I had to list where/variables in an odd format... so eh -}
{- n < 2? empty list. n mod x = 0? add to list and change n. else change head/tail of primes -}
primeFactors :: Int -> [Int]
primeFactors n = currentNum n (head primes) (tail primes) where
  currentNum n x xs
    | n < 2           = []
    | n `mod` x == 0  = x : currentNum(n `div` x) x xs
    | otherwise       = currentNum n (head xs) (tail xs)
{- FUNCTION 1 END -}


{- FUNCTION 2 START -}
--HELPER METHOD
checkForCommonFactor :: [Int] -> [Int] -> Bool
checkForCommonFactor _ []                = False
checkForCommonFactor [] _                = False
checkForCommonFactor (x:xs) (y:ys)
    | x /= 1 && x == y || my_elem x ys  = True
    | otherwise                         = checkForCommonFactor xs (y:ys)

--Given two positive integers a and b, return whether they are coprime
--Assuming two positive integers are not the same
{- if both numbers are prime then True. Else, check factor list for both nums -}
coprime :: Int -> Int -> Bool
coprime n1 n2
    | isPrime n1 && isPrime n2              = True
    | otherwise                             = not (checkForCommonFactor (getFactorsList n1) (getFactorsList n2))
{- FUNCTION 2 END -}


{- FUNCTION 3 START -}
{- Helper function to loop through summed amounts -}
tribHelper :: Int -> Int -> Int -> Int -> Int
tribHelper x y z 1 = z
tribHelper x y z n = tribHelper y z (x+y+z) (n-1)

--Given a non negative integer n, calculate the nth tribonacci number
{- Linear Time Based Algorithm (rather than exponential) -}
trib :: Int -> Int
trib 0 = 1
trib 1 = 1
trib 2 = 1
trib n = tribHelper 1 1 1 (n-1)
{- FUNCTION 3 END -}


{- FUNCTION 4 START -}
--HELPER METHOD
--Get maximum value from a list 
getMaxFromList :: [Int] -> Int
getMaxFromList [x] = x
getMaxFromList (x:xs)
    | (getMaxFromList xs) > x = getMaxFromList xs
    | otherwise               = x

--HELPER METHOD
--Remove any element in (x:xs) that exists in list (y:ys)
removeElements :: [Int] -> [Int] -> [Int]
removeElements [] _ = []
removeElements (x:xs) (y:ys)
    | my_elem x (y:ys)        = removeElements xs (y:ys)
    | otherwise               = x : removeElements xs (y:ys)

--Given a list of integers and a list of olds integers, find and return "Just" the largest integer
--When there is no such number, return Nothing
maxNew :: [Int] -> [Int] -> Maybe Int
maxNew [] _          = Nothing
maxNew (x:xs) []     = Just (getMaxFromList (x:xs) )
maxNew (x:xs) (y:ys)
    | removeElements (x:xs) (y:ys) == []   = Nothing
    | otherwise                            = Just (getMaxFromList $ removeElements (x:xs) (y:ys) )
{- FUNCTION 4 END -}

{- FUNCTION 5 START -}
--Given two argument function and two lists of arguments, create a list of the results
--applying the function to each same indexed pair
zipWith :: (a->b->c) -> [a] -> [b] -> [c]
zipWith f _ []          = []
zipWith f [] _          = []
zipWith f (a:as) (b:bs) = f a b : zipWith f as bs
{- FUNCTION 5 END -}


{- FUNCTION 6 START -}
{- ADDED FUNCTIONS FOR big, pos, cap, and lengthy below for testing purposes
--HELPER METHOD
--Determine if a number is "big"
big :: Int -> Bool
big x
    | x > 10    = True
    | otherwise = False

--HELPER METHOD
--Determine if a number is positive
pos :: Int -> Bool
pos x
    | x > 0     = True
    | otherwise = False

--HELPER METHOD
--Determine if the character is a capital
cap :: Char -> Bool
cap x
    | (64 <= ord x) && (ord x <= 90) = True
    | otherwise                      = False

--HELPER METHOD
--Determine if the char string is lengthy
lengthy :: [a] -> Bool
lengthy xs
    | my_length xs > 3      = True
    | otherwise          = False

--HELPER METHOD
--My implementation of length 
my_length :: [a] -> Int
my_length [] = 0
my_length (_:xs) = 1 + length xs       -}

--HELPER METHOD
--Substitute for filter
my_filter :: (a -> Bool) -> [a] -> [a]
my_filter f xs = [ x | x <- xs, f x]

--Given a predicate function (returns a bool), and a list of values, return a pair of two lists
--Left list is the items that pass the predicate and right list is those that don't
passFail :: (a -> Bool) -> [a] -> ([a],[a])
passFail _ [] = ([],[])
passFail f xs = (my_filter f xs, my_filter (not . f ) xs)
{- FUNCTION 6 END -}


{- FUNCTION 7 -}
--Given a predicate function that returns a Bool, and a list of values
--return a pair of two lists: list1 that pases the predicate, and list2 that doesn't
powerset :: [Int] -> [[Int]]
powerset [] = [[]]
powerset (x:xs) = [x:ys | ys <- powerset xs] ++ powerset xs
{- FUNCTION 7 END -}


{- FUNCTION 8 START -}
--Need to add "Num a" to calculateCell and calculateRow 
--to type signature because of error message related to my_foldl1

--HELPER METHOD
--my foldl1 implementation
my_foldl1 :: (a -> a -> a) -> [a] -> a
my_foldl1 f [x]     = x
my_foldl1 f [x,y]   = f x y
my_foldl1 f (x:y:zs)  = f (f x y) (my_foldl1 f zs)

--HELPER METHOD
--Calculate the Cell's value
--Variable c stands for column and r stands for row
--Utilize my_fold1 implementation and previously created zipWith
calculateCell :: Num a => [a] -> [a] -> a
calculateCell c r = my_foldl1 (+) $ zipWith (*) c r 

--HELPER METHOD
--Calculate the Row value
calculateRow :: Num a => [a] -> [[a]] -> [a]
calculateRow _ [] = []
calculateRow x (y:ys) = calculateCell x y : calculateRow x ys 

--HELPER METHOD
--Calculate 
matrixProductStart :: [[Int]] -> [[Int]] -> [[Int]]
matrixProductStart [[]] [[]] = [[]]
matrixProductStart [] _ = [[]]
matrixProductStart (x:xs) y = calculateRow x y : matrixProduct xs y

--HELPER METHOD
--my implementation of transpose a given 2D list
--r stands for rows in this case
my_transpose :: [[a]] -> [[a]]
my_transpose [[]]       = []
my_transpose [[],_]     = []
my_transpose ([x]:xs) = [x : map tail' xs]
  where 
    tail' [x] = x
my_transpose r          = (map head r) : my_transpose (map tail r)

--HELPER METHOD
--my implementation of sum (because everything needs to be reimplemented)
sum' :: [Int] -> Int
sum' [] = 0
sum' (x:xs) = x + sum xs

--Finalize matrix multiplication from other methods
matrixProduct :: [[Int]] -> [[Int]] -> [[Int]]
--Getting empty list added on. Could make a helper to remove empty list 
--matrixProduct x y = matrixProductStart x $ my_transpose y
--Most Helper methods not utilized because I realized I could use sum and zipWith really easily...
--Reimplemented sum (called sum') and used earlier implementation of zipWith (above)
matrixProduct x y = [[sum' $ zipWith (*) xRow yCol | yCol <- (my_transpose y)] | xRow <- x]
{- FUNCTION 8 END -}