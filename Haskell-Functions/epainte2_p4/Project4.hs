{- Name: Ethan Painter
   G#: G00915079
   netID: epainte2
-}

module Project4 where

import Control.Monad        -- many useful functions
import Control.Concurrent   -- threadDelay, forkIO, MVar..., Chan...
import Data.IORef           -- newIORef, readIORef, writeIORef
import System.Environment   -- getArgs
import System.Random        -- randomRIO
import Debug.Trace

{-
-- download BoundedChan from hackage for this one.
-- You'll get: BC.BoundedChan, BC.newBoundedChan, BC.readChan, BC.writeChan

-- import qualified Control.Concurrent.BoundedChan as BC
-}

--Chairs Objects
type Chair = String
type Chairs = [String]

strWinner :: String
strWinner = ""

--Main Runner
main :: IO ()
main = do
       --Get Args
       valsChan <- newChan
       outputs <- newChan
       winner <- newChan
       args <- getArgs
       let getSomeArgs = args
       let startNum = numFromArgs getSomeArgs
       let numTotalChairs = startNum
       --Initialize values for Chairs
       let chairs = initChairs (startNum-1)
       --Test Initialization of Chairs
       --testPrintChairs chairs

       --Initialize values for Players (threads)
       --Start musical chairs loop
       startMusicalChairsLoop 1 1 chairs (length chairs) valsChan outputs winner
       return ()

--Helper
--Start Musical Chairs Loop by creating the player threads
--And creating the search for musical chairs
--Int for setting/Not setting chairs initially, Int Current Round, Chairs List, Size of chairs, Int Channel, String Channel
startMusicalChairsLoop :: Int -> Int -> Chairs -> Int -> Chan String -> Chan String -> Chan String -> IO ()
startMusicalChairsLoop _ _ chairs 0 _ _ winner= do
    --No chair left, so return winner
    --Insert winner from Winner Channel here
    v <- readChan winner
    putStrLn $ v
    --Don't forget "END" to show main has completely stopped
    putStrLn $ "END"
    return ()
startMusicalChairsLoop set roundNum chairs chairLen strInChan strOutChan strWinner =
    if set == 1
    then do
        putStrLn $ "BEGIN " ++ show(chairLen+1)++" PLAYERS\n"
        putStrLn $ "Round "++(show roundNum)++"\nMusic Off"
        --Set String In Channel to chairs
        addChairsToInputChannel chairs strInChan
        --Fork Player Threads to find chairs
        startPlayers 1 (chairLen+2) strInChan strOutChan chairLen strWinner
        --Make announcer to collect all Out Channel prints
        announcer (chairLen+1) strOutChan
        --Increment Round Counter
        let newRound = roundNum + 1
        --Start Recursive loop with removed chair and decreased length
        --Remove Last chair and decrease chair len by 1
        startMusicalChairsLoop 0 newRound (init chairs) (chairLen - 1) strInChan strOutChan strWinner
    else do
        putStrLn $ "Round "++(show roundNum)++"\nMusic Off"
        --Set String In Channel to chairs
        addChairsToInputChannel chairs strInChan
        --startPlayers 1 (chairLen+2) strInChan strOutChan
        announcer (chairLen+1) strOutChan
        --Increment Round Counter
        let newRound = roundNum + 1
        --Start Recursive loop with removed chair and decreased length
        --Remove Last chair and decrease chair len by 1
        startMusicalChairsLoop 0 newRound (init chairs) (chairLen - 1) strInChan strOutChan strWinner

--Helper
--Add All Chairs to Input Channel
addChairsToInputChannel :: Chairs -> Chan String -> IO ()
addChairsToInputChannel [] strInChan = do
    --Add Value to show final input (Exclude this player/chair)
    writeChan strInChan $ "-1"
    return ()
addChairsToInputChannel chairs strInChan = do
    writeChan strInChan $ (head chairs)
    addChairsToInputChannel (tail chairs) strInChan

--Helper
--Fork Threads given
--Int start, Int max, Input channel, Output channel, and number of chairs
startPlayers :: Int -> Int -> Chan String -> Chan String -> Int -> Chan String -> IO()
startPlayers start max inChan outChan numChairs strWinner =
    if start == max
    then return ()
    else do
    forkIO $ executePlayer start inChan outChan numChairs strWinner
    startPlayers (start + 1) max inChan outChan numChairs strWinner

--Helper
--Get Fork Output (putStrLn)
--name of thread, output String Channel
executePlayer :: Int -> Chan String -> Chan String -> Int -> Chan String -> IO ()
executePlayer num inChan outChan numChairs strWinner = do
    v <- readChan inChan
    if v == "-1"
    then do
    --Lost the game
         threadDelay 200000
         writeChan outChan $ "P"++(show num)++" lost\n"
         return ()
    else do
    --Found a seat and survived for next round
    writeChan outChan $ "P"++ (show num)++" sat in "++v
    --Save string winner
    if numChairs == 1
    then writeChan strWinner $ "P"++(show num) ++ " wins!"
    else do
    threadDelay 1000000
    executePlayer num inChan outChan (numChairs - 1) strWinner

--Helper
--If input is [], return 10
--else return num at head
numFromArgs :: [String] -> Int
numFromArgs [] = 10
numFromArgs a = (read (head a)::Int)

--Helper
--Initialize Chairs Array
initChairs :: Int -> Chairs
initChairs 0 = []
initChairs numChairs = initChairs (numChairs-1) ++ [("C"++show(numChairs))]

--Helper
--TESTING Print Chairs
testPrintChairs :: Chairs -> IO ()
testPrintChairs [] = return ()
testPrintChairs list = do
    putStrLn $ (head list)
    testPrintChairs (tail list)

--Helper
--Announce print statements to standard output
--Number of print lines, String output channel -> Standard out
announcer :: Int -> Chan String -> IO ()
announcer 0 _ = return ()
announcer n out = do
  v <- readChan out
  putStrLn $ v
  announcer (n-1) out