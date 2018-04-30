{--

 - copy Tester.hs, Tester_input.txt, fileA.txt, fileB.txt, fileC.txt, b1.txt, b2.txt, b3.txt, b4.txt
 - Make sure student's file is named Homework6.hs
 - make sure it contains "module Homework6 where" at the top.
 - run the following commands in order (build the executable for testing; feed it a file of inputs rather than type it out).

 prompt$ ghc --make -o tester Tester.hs
 prompt$  ./tester < Tester_inputs.txt

 - Check that the very end prints "line 1", "line 2", "line 3" on separate lines and one second apart.
   - If not, they lose a point.
 
 - a score of 78 test cases are tried. Subtract how many cases became "Errors" or "Failures".

 - the remaining 22 points are as follows:
   - Maybe:
     - +5 points: correctly used do-notation for ancestors.
   - State:
     - +4: partition calls partitionM/runState.
     - +5: partitionM uses do-notation.
     - +4: balanced calls balancedM/runState.
   - List:
     - +4: using do-notation on the four definitions.
   - IO: no extra checks.
   - +5 points: turned in on time, as/where directed.
--}


import Homework6(isSubstring,indexOf,parent,ancestors,headMaybe,leastUpperBound,partition,partitionM,plainBalanced,balancedM,balanced,divisors,geometric,mersennes,share4way,readNum,readNums,slowEcho,crudeDiff,sameBrackets)
import Test.HUnit
import Control.Monad.State

import Control.Exception
import Control.Monad


{-
tc1 :: (Show r, Show a) => String -> (a->r) -> r -> a -> Test
tc1 name f expected arg            = TestCase $ assertEqual (name ++" " ++ show arg                       ) expected (f arg           )

tc2 :: (Eq r, Show r, Show a, Show b) => String -> (a->b->r) -> r -> a -> b -> Test
tc2 name f expected arg1 arg2      = TestCase $ assertEqual (name ++" " ++ show arg1++" "++arg2           ) expected (f arg1 arg2     )

tc3 :: (Show r, Show a, Show b, Show c) => String -> (a->b->c->r) -> r -> a -> b -> c -> Test
tc3 name f expected arg1 arg2 arg3 = TestCase $ assertEqual (name ++" " ++ show arg1++" "++arg2++" "++arg3) expected (f arg1 arg2 arg3)
-}

sampletree = [
  ("Animal", "Object"),
  ("Cat","Animal"),
  ("Dog","Animal"),
  ("Siamese","Cat"),
  ("Calico","Cat"),
  ("Labrador","Dog"),
  ("Pug","Dog"),
  ("Book","Object"),
  ("Garbage","Can")
  ]

main = runTestTT $ TestList [ts1]

ts1 = TestList [

  -- need 30% for Maybe. 25 points so far. +5 for do-notation on ancestors.
  tc "isSubstring [3,4] [1,2,3,4,5]"   True  $ isSubstring [3,4]   [1,2,3,4,5],
  tc "isSubstring [2,4] [1,2,3,4,5]"   False $ isSubstring [2,4]   [1,2,3,4,5],
  tc "isSubstring [] [1,2,3,4,5"       True  $ isSubstring []      [1,2,3,4,5],
  tc "isSubstring [4,3,2] [1,2,3,4,5]" False $ isSubstring [4,3,2] [1,2,3,4,5],
  
  tc "indexOf [2,3]      [1,2,3,4,5]" (Just 1) $ indexOf [2,3]       [1,2,3,4,5],
  tc "indexOf [1,2,3,4,5][1,2,3,4,5]" (Just 0) $ indexOf [1,2,3,4,5] [1,2,3,4,5],
  tc "indexOf [3,4,5]    [1,2,3,4,5]" (Just 2) $ indexOf [3,4,5]     [1,2,3,4,5],
  tc "indexOf [2,4]      [1,2,3,4,5]" Nothing  $ indexOf [2,4]       [1,2,3,4,5],

  tc "parent \"Animal\" tree" (Just "Object") $ parent "Animal"  sampletree,
  tc "parent \"Object\" tree" Nothing         $ parent "Object"  sampletree,
  tc "parent \"Cat\" tree"    (Just "Cat")    $ parent "Siamese" sampletree,
  tc "parent \"Dog\" tree"    (Just "Dog")    $ parent "Pug"     sampletree,

  tc "ancestors1" (Just ["Animal", "Object"])      $ ancestors "Dog"            sampletree,
  tc "ancestors2" (Just ["Object"])                $ ancestors "Book"           sampletree,
  tc "ancestors3" Nothing                          $ ancestors "Garbage"        sampletree,
  tc "ancestors4" (Just ["Cat","Animal","Object"]) $ ancestors "Calico"         sampletree,
  tc "ancestors5" Nothing                          $ ancestors "NotEvenPresent" sampletree,

  tc "headMaybe1" Nothing  $ headMaybe ([]::[Int]),
  tc "headMaybe2" (Just 5) $ headMaybe [5],
  tc "headMaybe3" (Just 5) $ headMaybe [5,10,15],

  tc "leastUpperBound1" (Just "Animal") $ leastUpperBound "Pug" "Calico" sampletree,
  tc "leastUpperBound2" (Just "Animal") $ leastUpperBound "Pug" "Animal" sampletree,
  tc "leastUpperBound3" (Just "Object") $ leastUpperBound "Pug" "Book" sampletree,
  tc "leastUpperBound4" Nothing         $ leastUpperBound "Pug" "Garbage" sampletree,
  tc "leastUpperBound5" Nothing         $ leastUpperBound "NOTFOUND" "Book" sampletree,
  
  -- need 40% for State. 27% so far.
  -- +4: partition calls partitionM/runState.
  -- +5: partitionM uses do-notation.
  -- +4: balanced calls balancedM/runState.

  tc "partition1" ([2,4,6],[1,3,5]) $ partition even [1,2,3,4,5,6],
  tc "partition2" ([4,3,2],[8,7,6,5]) $ partition (<5) [8,7,6,5,4,3,2],
  tc "partition3" ([],[1,3,5]) $ partition even [1,3,5],
  tc "partition4" ([1,3,5],[]) $ partition odd [1,3,5],

  tc "partitionM1" ([2,4,6],[1,3,5]) $ runState (partitionM even [1,2,3,4,5,6]) [],
  tc "partitionM2" ([2,4,6],[1000,1,3,5]) $ runState (partitionM even [1,2,3,4,5,6]) [1000],
  tc "partitionM3" ([4,3],[7,6,5]) $ runState (partitionM (<5) [7,6,5,4,3]) [],
  tc "partitionM4" ([],[1,3,5]) $ runState (partitionM even [1,3,5]) [],
  tc "partitionM5" ([(5,3),(10,1)],[(6,7),(2,2)]) $ runState (partitionM heavy [(5,3), (6,7),(10,1),(2,2)] ) [],

  tc "plainBalanced1" True  $ plainBalanced "(){}[]",
  tc "plainBalanced2" True  $ plainBalanced "(([{}])[]{})",  tc "plainBalanced3" True  $ plainBalanced "(others [are] allowed)",
  tc "plainBalanced4" False $ plainBalanced "(()",
  tc "plainBalanced5" False $ plainBalanced "([)]",
  tc "plainBalanced6" True  $ plainBalanced "",

  tc "balancedM1" True  $ fst $ runState (balancedM "(){}[]") [],
  tc "balancedM2" True  $ fst $ runState (balancedM "(([{}])[]{})") [],
  tc "balancedM3" True  $ fst $ runState (balancedM "(others [are] allowed)") [],
  tc "balancedM4" False $ fst $ runState (balancedM "(()") [],
  tc "balancedM5" False $ fst $ runState (balancedM "([)]") [],
  tc "balancedM6" True  $ fst $ runState (balancedM "") [],
  
  tc "balanced1" True  $ balanced "(){}[]",
  tc "balanced2" True  $ balanced "(([{}])[]{})",
  tc "balanced3" True  $ balanced "(others [are] allowed)",
  tc "balanced4" False $ balanced "(()",
  tc "balanced5" False $ balanced "([)]",
  tc "balanced6" True  $ balanced "",

  -- need 15% for List.
  -- +4: do-notation used on the four definitions.
  
  tc "divisors 12" [1,2,3,4,6,12] $ divisors 12,
  tc "divisors 1" [1] $ divisors 1,
  tc "divisors 100" [1,2,4,5,10,20,25,50,100] $ divisors 100,
  tc "divisors 1117" [1,1117] $ divisors 1117,

  tc "geometric 1 5" [1,5,25,125,625,3125]   $ take 6 $ geometric 1 5,
  tc "geometric 2 5" [2,10,50,250,1250,6250] $ take 6 $ geometric 2 5,
  tc "geometric 5 1" [5,5,5,5,5,5]           $ take 6 $ geometric 5 1,
  tc "geometric 4 (-1)" [4,-4,4,-4,4,-4]     $ take 6 $ geometric 4 (-1),
  
  tc "mersennes" [1,3,7,15,31,63,127,255,511,1023] $ take 10 $ mersennes,

  tc "share4way 2" [(1,1,0,0),(2,0,0,0)] $ share4way 2,
  tc "share4way 4" [(1,1,1,1),(2,1,1,0),(2,2,0,0),(3,1,0,0),(4,0,0,0)] $ share4way 4,

  -- need 15% for IO.
  tcio "readNum gets 50" readNum 50,
  tcio "readNum gets -4" readNum (-4),

  tcio "readNums 3 2 4 6" (readNums 3) [2,4,6],
  tcio "readNums 1 10" (readNums 1) [10],
  tcio "readNums 0" (readNums 0) [],
    
  tcio "crudeDiff A A"  (crudeDiff "fileA.txt" "fileA.txt") True ,
  tcio "crudeDiff A B"  (crudeDiff "fileA.txt" "fileB.txt") True ,
  tcio "crudeDiff A C"  (crudeDiff "fileA.txt" "fileC.txt") False,

  tcio "sameBrackets 1 2" (sameBrackets "b1.txt" "b2.txt") True ,
  tcio "sameBrackets 2 2" (sameBrackets "b2.txt" "b2.txt") True ,
  tcio "sameBrackets 1 3" (sameBrackets "b1.txt" "b3.txt") False,
  tcio "sameBrackets 3 4" (sameBrackets "b3.txt" "b4.txt") False,
  tcio "sameBrackets 3 2" (sameBrackets "b3.txt" "b2.txt") False,
  
  tcio "slowEcho []" (slowEcho []) (),
  tcio "slowEcho [\"line 1\", 'line 2\", \"line 3\"]" (slowEcho ["line 1", "line 2", "line 3"]) ()

  
  
  

--  tcioFails "readNum can't parse 'hello'" readNum
  
  
 ]

tc s a b = TestCase $ assertEqual s a b

tcio s mio answer = TestLabel s $ TestCase $ do
  got <- mio
  assertEqual s answer got



--tcioFails s mio = TestLabel s $ TestCase $ assertException (PatternMatchFail "no") mio 

testPasses = TestCase $ assertException DivideByZero (evaluate $ 5 `div` 0)
testFails  = TestCase $ assertException DivideByZero (evaluate $ 5 `div` 1)

main2 = runTestTT $ TestList [ testPasses, testFails ]




-- from http://stackoverflow.com/questions/6147435/is-there-an-assertexception-in-any-of-the-haskell-test-frameworks
assertException :: (Exception e, Eq e) => e -> IO a -> IO ()
assertException ex action =
    handleJust isWanted (const $ return ()) $ do
        action
        assertFailure $ "Expected exception: " ++ show ex
  where isWanted = guard . (== ex)

                
                
{-

data Test = TestCase Assertion
          | TestList [Test]
          | TestLabel String Test

type Assertion = IO ()


-}



heavy (a,b) = a>b
