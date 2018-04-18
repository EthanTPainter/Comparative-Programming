{- Name: Ethan Painter
   G#:   G00915079
   netID: epainte2
-}

module Project3 where

-- optional: trace :: String -> a -> a, prints your string when
-- a's evaluation is forced.
import Debug.Trace
-- for writing files
import System.IO
------------------------------------------------------------------------
-- The following is needed for consistency between all our solutions.
------------------------------------------------------------------------

data RGB = RGB Int Int Int deriving (Show, Eq)
data Grid a = G [[a]] deriving (Show, Eq)

type Coord   = (Int,Int)
type Picture = Grid RGB
type Path    = [Coord]

type NodeEnergy = Int
type PathCost   = Int

type Packet = (Coord, RGB, NodeEnergy)  -- used much later
data Node = Node
    Coord             -- this location
    RGB               -- color info
    NodeEnergy        -- energy at this spot
    PathCost          -- cost of cheapest path from here to bottom.
    Node              -- ref to next node on cheapest path. Use No's when when we're at the bottom.
    (Node,Node,Node)  -- Three candidates we may connect to.
    | No   -- sometimes there is no next node and we use No as a placeholder.
    deriving (Show, Eq)
------------------------------------------------------------------------
--TESTING
g1 = G [
  [ RGB 100  75 200,  RGB 100 100 200,  RGB 100 100 200,  RGB 100 100 200,  RGB 200 125 200],
  [ RGB 150  30 180,  RGB 150  50 180,  RGB 100 120 180,  RGB 100 120 180,  RGB 100 120 180],
  [ RGB 100  75 100,  RGB 100  80 100,  RGB 100  85 100,  RGB 100  95 100,  RGB 100 110 100],
  [ RGB 200 100  10,  RGB 200 100  10,  RGB 200 100  10,  RGB 210 200  10,  RGB 255   0  10]]

g5 = G [
  [ RGB 0 0 0,  RGB 0 0 0,  RGB 10 20 30,  RGB 0 0 0,  RGB 0 0 0,  RGB 0 0 0] ,
  [ RGB 0 0 0,  RGB 2 3 4,  RGB  1  1  1,  RGB 5 6 7,  RGB 0 0 0,  RGB 0 0 0] ,
  [ RGB 0 0 0,  RGB 0 0 0,  RGB 60 50 40,  RGB 0 0 0,  RGB 0 0 0,  RGB 0 0 0] ,
  [ RGB 0 0 0,  RGB 0 0 0,  RGB  0  0  0,  RGB 0 0 0,  RGB 0 0 0,  RGB 0 0 0]
  ]

x1 = RGB 100 75 200
x2 = [RGB 100 75 200, RGB 100 100 200, RGB 100 100 200, RGB 100 100 200, RGB 200 125 200]
--END TESTING

--How many columns are in the grid
width :: Grid a -> Int
width (G a) = length (head a)

--How many rows are in the grid
height :: Grid a -> Int
height (G a) = length(a)

--Helper
--Get R value of RGB
getR :: RGB -> Int
getR (RGB a b c) = a

--Helper
--Get G value of RGB
getG :: RGB -> Int
getG (RGB a b c) = b

--Helper
--Get B value of RGB
getB :: RGB -> Int
getB (RGB a b c) = c

--Helper
--Get left horizontal X
getLeft :: Int -> Int -> Int
getLeft gridWidth x =
    if x == 0
    then gridWidth - 1
    else x - 1

--Helper
--Get right horizontal X
getRight :: Int -> Int -> Int
getRight gridWidth x =
    if x == gridWidth - 1
    then 0
    else x + 1

--Helper
--Get Above vertical y
getAbove :: Int -> Int -> Int
getAbove gridHeight y =
    if y == 0
    then gridHeight - 1
    else y - 1

getBelow :: Int -> Int -> Int
getBelow gridHeight y =
    if y == gridHeight - 1
    then 0
    else y + 1

--Helper
--Get Grid Row from Grid of RGB, Int (Row to select),
getRow :: Grid RGB -> Int -> Int -> [RGB]
getRow (G grid) selectRow startIdx =
    if selectRow == startIdx
    then (head grid)
    else getRow (G (tail grid)) selectRow (startIdx + 1)

--Helper
--Get Grid Column from Grid Row already selected
--Given row of RGB, a select Index, and a start Index
getCol :: Int -> Int -> [RGB] -> RGB
getCol selectCol startIdx grid =
    if selectCol == startIdx
    then (head grid)
    else getCol selectCol (startIdx + 1) (tail grid)

--Helper (getRow & getCol)
--Get an RGB given a Grid RGB and Coord
getGridLoc :: Grid RGB -> Coord -> RGB
getGridLoc (G grid) (x,y) = getCol y 0 (getRow (G grid) x 0)

--Helper
--Get Horizontal R Energy
getRHorizEnergy :: Grid RGB -> Coord -> Int
getRHorizEnergy (G grid) (x,y) =
    (getR (getGridLoc (G grid) (x,(getLeft (width (G grid)) y))) -
    getR (getGridLoc (G grid) (x,(getRight (width (G grid)) y)))) *
    (getR (getGridLoc (G grid) (x, (getLeft (width (G grid)) y))) -
    getR (getGridLoc (G grid) (x, getRight (width (G grid)) y)))

--Helper
--Get Horizontal G Energy
getGHorizEnergy :: Grid RGB -> Coord -> Int
getGHorizEnergy (G grid) (x,y) =
    (getG (getGridLoc (G grid) (x,(getLeft (width (G grid)) y))) -
    getG (getGridLoc (G grid) (x,(getRight (width (G grid)) y)))) *
    (getG (getGridLoc (G grid) (x,(getLeft (width (G grid)) y))) -
    getG (getGridLoc (G grid) (x,(getRight (width (G grid)) y))))

--Helper
--Get Horizontal B Energy
getBHorizEnergy :: Grid RGB -> Coord -> Int
getBHorizEnergy (G grid) (x,y) =
    (getB (getGridLoc (G grid) (x,(getLeft (width (G grid)) y))) -
    getB (getGridLoc (G grid) (x,(getRight (width (G grid)) y)))) *
    (getB (getGridLoc (G grid) (x,(getLeft (width (G grid)) y))) -
    getB (getGridLoc (G grid) (x,(getRight (width (G grid)) y))))

--Helper
--Calculate Horizontal Energy (getRHorizEnergy, getGHorizEnergy, getBHorizEnergy)
getHorizEnergy :: Grid RGB -> Coord -> NodeEnergy
getHorizEnergy (G grid) (x,y) =
    (getRHorizEnergy (G grid) (x,y)) + (getGHorizEnergy (G grid) (x,y)) + (getBHorizEnergy (G grid) (x,y))

--Helper
--Calculate Vertical R Energy
getRVertEnergy :: Grid RGB -> Coord -> Int
getRVertEnergy (G grid) (x,y) =
    (getR (getGridLoc (G grid) ((getAbove (height (G grid)) x), y)) -
    getR (getGridLoc (G grid) ((getBelow (height (G grid)) x), y))) *
    (getR (getGridLoc (G grid) ((getAbove (height (G grid)) x), y)) -
    getR (getGridLoc (G grid) ((getBelow (height (G grid)) x), y)))

--Helper
--Calculate Vertical G Energy
getGVertEnergy :: Grid RGB -> Coord -> Int
getGVertEnergy (G grid) (x,y) =
    (getG (getGridLoc (G grid) ((getAbove (height (G grid)) x), y)) -
    getG (getGridLoc (G grid) ((getBelow (height (G grid)) x), y))) *
    (getG (getGridLoc (G grid) ((getAbove (height (G grid)) x), y)) -
    getG (getGridLoc (G grid) ((getBelow (height (G grid)) x), y)))

--Helper
--Calculate Vertical B Energy
getBVertEnergy :: Grid RGB -> Coord -> Int
getBVertEnergy (G grid) (x,y) =
    (getB (getGridLoc (G grid) ((getAbove (height (G grid)) x), y)) -
    getB (getGridLoc (G grid) ((getBelow (height (G grid)) x), y))) *
    (getB (getGridLoc (G grid) ((getAbove (height (G grid)) x), y)) -
    getB (getGridLoc (G grid) ((getBelow (height (G grid)) x), y)))

--Helper
--Calculate Vertical Energy
getVertEnergy :: Grid RGB -> Coord -> NodeEnergy
getVertEnergy (G grid) (x,y) =
    (getRVertEnergy (G grid) (x,y)) + (getGVertEnergy (G grid) (x,y)) + (getBVertEnergy (G grid) (x,y))

--What is the energy at
energyAt :: Grid RGB -> Coord -> NodeEnergy
energyAt (G grid) (x,y) =
    (getHorizEnergy (G grid) (x,y)) + (getVertEnergy (G grid) (x,y))

{-
energies :: Grid RGB -> Grid NodeEnergy


findVerticalPath :: Grid RGB -> Path


findHorizontalPath :: Grid RGB -> Path


removeVerticalPath :: Grid RGB -> Path -> Grid RGB


removeHorizontalPath :: Grid RGB -> Path -> Grid RGB


gridToFile :: Grid RGB -> FilePath -> IO ()


fileToGrid :: FilePath -> IO (Grid RGB)
-}