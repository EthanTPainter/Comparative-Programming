{-
    Name: Ethan Painter
    G#: G00915079
    netID: epainte2
-}

module Homework6 where

import Control.Monad.State  --State, runState, get, put, guard
import Control.Concurrent   --threadDelay
import Data.Char            --toUpper

type Name = String
type FamilyTree = [(Name,Name)]

--Maybe Monad Definitions (30%)

--Main
isSubstring :: (Eq a) => [a] -> [a] -> Bool
isSubstring list1 [] = False
isSubstring [] _ = True
isSubstring x y
    | getPrefix x y = True
    | isSubstring x (tail y) = True
    | otherwise = False

--Helper
getPrefix :: (Eq a) => [a] -> [a] -> Bool
getPrefix [] ys = True
getPrefix (x:xs) [] = False
getPrefix (x:xs) (y:ys) = (x == y) && getPrefix xs ys

--Main
--assuming the list acts as a set
indexOf :: (Eq a) => [a] -> [a] -> Maybe Int
indexOf x [] = Nothing
indexOf [] _ = Just 0
indexOf x y = if (isSubstring x y) then Just (getIndex (head x) y 0) else Nothing

--Helper
getIndex :: (Eq a) => a -> [a] -> Int -> Int
getIndex x list num =
    if (x == (head list))
    then num
    else getIndex x (tail list) (num + 1)

--Main
parent :: Name -> FamilyTree -> Maybe Name
parent name [] = Nothing
parent name tree =
    if (name == getFirst (head tree))
    then Just (getSecond (head tree))
    else parent name (tail tree)

--Helper
getFirst :: (Name,Name) -> Name
getFirst (v,_) = v

--Helper
getSecond :: (Name,Name) -> Name
getSecond (_,v) = v

--Main
--Use do notation and rely on sample list
ancestors :: Name -> FamilyTree -> Maybe [Name]
ancestors name [] = Nothing
ancestors name tree
    | hasParent name tree = do
        --Has Name has parent so grab parent
        par <- parent name tree
        --Check for grandparent
        if hasParent par tree
        then do
            gran <- parent par tree
            if hasParent gran tree
            then do
                gran2 <- parent gran tree
                if hasParent gran2 tree
                then do
                    gran3 <- parent gran2 tree
                    if hasParent gran3 tree
                    then do
                        gran4 <- parent gran3 tree
                        if gran4 == ("Object"::Name)
                        then return [par,gran,gran2,gran3,gran4]
                        else ancestors name []
                    else if gran3 == ("Object"::Name)
                         then return [par,gran,gran2,gran3]
                         else ancestors name []
                else if gran2 == ("Object"::Name)
                     then return [par,gran,gran2]
                     else ancestors name []
            else if gran == ("Object"::Name)
                 then return [par,gran]
                 else ancestors name []
        --Check if par is object
        else if par == ("Object"::Name)
             then return [par]
             else ancestors name []
    | otherwise = Nothing

--Helper
hasParent :: Name -> FamilyTree -> Bool
hasParent name [] = False
hasParent name tree =
    if (name == getFirst (head tree))
    then True
    else hasParent name (tail tree)

--Main
headMaybe :: [a] -> Maybe a
headMaybe [] = Nothing
headMaybe list = Just (head list)

--Main
--NOT DONE
leastUpperBound :: Name -> Name -> FamilyTree -> Maybe Name
leastUpperBound name1 name2 tree = do
    list1 <- ancestors name1 tree
    list2 <- ancestors name2 tree
    if (headMaybe list1) == Nothing
    then Nothing
    else if (headMaybe list2) == Nothing
        then Nothing
        else if (checkListUpper list1 (Just name2)) /= Nothing
             then Just name2
             else if (checkListUpper list2 (Just name1)) /= Nothing
                  then Just name1
                  else findLeastUpper list1 list2

--Helper
findLeastUpper :: [Name] -> [Name] -> Maybe Name
findLeastUpper lst1 [] = Nothing
findLeastUpper lst1 lst2 =
    if (checkListUpper lst1 (headMaybe lst2)) /= Nothing
    then checkListUpper lst1 (headMaybe lst2)
    else findLeastUpper lst1 (tail lst2)

--Helper
checkListUpper :: [Name] -> Maybe Name -> Maybe Name
checkListUpper list name =
    if list == []
    then Nothing
    else if (headMaybe list) == name
         then name
         else checkListUpper (tail list) name

--State Monad (40%)
--Main
partition :: (a->Bool) -> [a] -> ([a],[a])
partition f list = do
    (fst $ runState (partitionM f list) [], snd $ runState(partitionM f list) [])

--Main
partitionM :: (a->Bool) -> [a] -> State [a] [a]
partitionM f list = do
    let filtered = filter f list
    let notFiltered = notFilter f list
    addToFails notFiltered --NOT pass function (False)
    return filtered

--Helper
notFilter :: (a -> Bool) -> [a] -> [a]
notFilter f = filter (not . f)

--Helper
addToFails ::  [a] -> State [a] ()
addToFails [] = return ()
addToFails lst = do
    curVals <- get
    put $ curVals ++ [(head lst)]
    addToFails (tail lst)

--Main
plainBalanced :: String -> Bool
plainBalanced string = (plainHelper string "")

--Helper
--String, Number of open brackets left, type of most recent open bracket
--Types: 1 for paren, 2 for braces, 3 for brackets
plainHelper :: String -> String -> Bool
plainHelper [] "" = True
plainHelper [] _ = False
plainHelper string buildStr
    --Open Braces
    | (head string) == '(' = plainHelper (tail string) (buildStr ++ "(")
    | (head string) == '{' = plainHelper (tail string) (buildStr ++ "{")
    | (head string) == '[' = plainHelper (tail string) (buildStr ++ "[")
    --Closed Braces
    | (head string) == ')' =
        if ((length buildStr) > 0) && ((last buildStr) == '(')
        then plainHelper (tail string) (init buildStr)
        else False
    | (head string) == '}' =
        if ((length buildStr) > 0) && ((last buildStr) == '{')
        then plainHelper (tail string) (init buildStr)
        else False
    | (head string) == ']' =
        if ((length buildStr) > 0) && ((last buildStr) == '[')
        then plainHelper (tail string) (init buildStr)
        else False
    --Otherwise
    | otherwise = plainHelper (tail string) buildStr

--Main
balancedM :: String -> State [Char] Bool
balancedM [] = do
    charList <- get
    if (length charList) > 0
    then return False
    else return True
balancedM string
    | (head string) == '(' = do
                             addToCharList '('
                             balancedM (tail string)
    | (head string) == '{' = do
                             addToCharList '{'
                             balancedM (tail string)
    | (head string) == '[' = do
                             addToCharList '['
                             balancedM (tail string)
    | (head string) == ')' = do
                             charList <- get
                             if (length charList) > 0
                             then do
                                  let lastChar = last charList
                                  if (lastChar == '(')
                                  then do
                                       removeFromCharList lastChar
                                       balancedM (tail string)
                                  else return False
                             else return False
    | (head string) == '}' = do
                             charList <- get
                             if (length charList) > 0
                             then do
                                  let lastChar = last charList
                                  if (lastChar == '{')
                                  then do
                                       removeFromCharList lastChar
                                       balancedM (tail string)
                                  else return False
                             else return False
    | (head string) == ']' = do
                             charList <- get
                             if (length charList) > 0
                             then do
                                  let lastChar = last charList
                                  if (lastChar == '[')
                                  then do
                                        removeFromCharList lastChar
                                        balancedM (tail string)
                                  else return False
                             else return False
    | otherwise = balancedM (tail string)

--Helper
addToCharList :: Char -> State [Char] ()
addToCharList newChar = do
    charList <- get
    put $ charList ++ [newChar]
    return ()

--Helper
removeFromCharList :: Char -> State [Char] ()
removeFromCharList removeChar = do
    charList <- get
    put $ (init charList)
    return ()

--Main
balanced :: String -> Bool
balanced string = do
    fst $ runState (balancedM string) []

--List Monad (15%) (Use do-notation and List)
--Main
divisors :: Int -> [Int]
divisors num = do
               let one = [1]
               if num == 1 then one else divisorsHelper num 1
--Helper
divisorsHelper :: Int -> Int -> [Int]
divisorsHelper num idx = do
                         if (num < idx)
                         then []
                         else if mod num idx == 0
                              then idx : divisorsHelper (num) (idx + 1)
                              else divisorsHelper (num) (idx + 1)

--Main
geometric :: Int -> Int -> [Int]
geometric startVal factor = do helpGeo startVal factor 0
--Helper
helpGeo :: Int -> Int -> Int -> [Int]
helpGeo startVal factor startIdx = do
    if startIdx == 0
    then startVal:helpGeo startVal factor (startIdx + 1)
    else (startVal * factor):helpGeo (startVal * factor) factor (startIdx + 1)

--Main
--Infinite list M_1, M_2, M_3 following form:  M_n = 2^n - 1
mersennes :: [Int]
mersennes = do helpMers 0
--Helper
helpMers :: Int -> [Int]
helpMers start = do
     startingVal <- [(2^1) - 1]
     newVal <- [(2^(start + 1)) - 1]
     if start == 0
     then startingVal:helpMers (start + 1)
     else newVal:helpMers (start + 1)

--Main
share4way :: Int -> [(Int,Int,Int,Int)]
share4way 1 = [(1,0,0,0)]
share4way value = helpShare value value 0 0 0

helpShare :: Int -> Int -> Int -> Int -> Int -> [(Int,Int,Int,Int)]
helpShare 0 _ _ _ _ = []
helpShare value fstVal sndVal thdVal frtVal
    | fstVal == value = (helpShare value (value-1) 1 0 0) ++ [(value,0,0,0)]
    | fstVal > 1 && sndVal >= 2 = (helpShare value fstVal (sndVal-1) (thdVal+1) frtVal) ++ [(fstVal, sndVal, thdVal, frtVal)]
    | fstVal == (value-1) && sndVal == 1 = (helpShare value (fstVal-1) (sndVal+1) thdVal frtVal) ++ [(fstVal, sndVal, thdVal, frtVal)]
    | fstVal > 1 && sndVal == 1 && thdVal == 1 = (helpShare value (fstVal-1) (sndVal) (thdVal) (frtVal+1)) ++ [(fstVal,sndVal,thdVal,frtVal)]
    | fstVal == 1 && sndVal == 1 && thdVal == 1 && frtVal == 1 = [(fstVal,sndVal,thdVal,frtVal)]
    | otherwise = []

--The IO Monad (15%)
--read one Int from the keyboard and return it.
readNum :: IO Int
readNum = readLn

--Given an integer n, ask for that many integers via the keyboard and return them.
readNums :: Int -> IO[Int]
readNums num = do
               array <- readNumsHelper num
               return array

readNumsHelper :: Int -> IO [Int]
readNumsHelper 0 = return []
readNumsHelper num = do
                     line <- getLine
                     nextInt <- readNumsHelper (num - 1)
                     let someNum = read line::Int
                     return (someNum:nextInt)

--Given a list of string-- s, echo them one at a time, with a second delay between.
slowEcho :: [String] -> IO()
slowEcho [] = return ()
slowEcho list = do
                let headItem = head list
                putStrLn (show headItem)
                threadDelay 1000000
                slowEcho (tail list)

--Given two file names, open them, and check/answer if they have the exact same contents, ignoring case.
crudeDiff :: FilePath -> FilePath -> IO Bool
crudeDiff file1 file2 = do
                        fileContents1 <- readFile file1
                        fileContents2 <- readFile file2
                        let ignoreCaseContents1 = map toUpper fileContents1
                        let ignoreCaseContents2 = map toUpper fileContents2
                        return (ignoreCaseContents1 == ignoreCaseContents2)

--Given two filenames, go open them, dig through them and check if they have the same balanced parentheses/braces/square brackets. Ignore all other characters
sameBrackets :: FilePath -> FilePath -> IO Bool
sameBrackets file1 file2 = do
               list1Contents <- readFile file1
               list2Contents <- readFile file2
               let boolOne = balanced list1Contents
               let boolTwo = balanced list2Contents
               return (boolOne && boolTwo)