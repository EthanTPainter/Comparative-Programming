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
--for transpose grid (allowed by Dr. M Synder in class)
import Data.List
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

gN = [[0,1,2,3,4,5],[6,7,8,9,10,11],[12,13,14,15,16,17]]
x1 = RGB 100 75 200
x2 = [RGB 100 75 200, RGB 100 100 200, RGB 100 100 200, RGB 100 100 200, RGB 200 125 200]
endNode1 = Node (1,1) (RGB 1 1 1) 1 1 No (No,No,No)
g1packets = G [[((0,0),RGB 100 75 200,46925),((0,1),RGB 100 100 200,34525),((0,2),RGB 100 100 200,39300),((0,3),RGB 100 100 200,58025),((0,4),RGB 200 125 200,67950)],[((1,0),RGB 150 30 180,17400),((1,1),RGB 150 50 180,21000),((1,2),RGB 100 120 180,17625),((1,3),RGB 100 120 180,10025),((1,4),RGB 100 120 180,30825)],[((2,0),RGB 100 75 100,37200),((2,1),RGB 100 80 100,34000),((2,2),RGB 100 85 100,39525),((2,3),RGB 100 95 100,48025),((2,4),RGB 100 110 100,67725)],[((3,0),RGB 200 100 10,23025),((3,1),RGB 200 100 10,10400),((3,2),RGB 200 100 10,20325),((3,3),RGB 210 200 10,23050),((3,4),RGB 255 0 10,30325)]]
--END TESTING

--Main
--How many columns are in the grid
width :: Grid a -> Int
width (G a) = length (head a)

--Main
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

--Helper (getRow, getCol)
--Get an RGB given a Grid RGB and Coord
getGridLoc :: Grid RGB -> Coord -> RGB
getGridLoc (G grid) (x,y) = getCol y 0 (getRow (G grid) x 0)

--Helper (getR, getGridLoc, getLeft, getRight)
--Get Horizontal R Energy
getRHorizEnergy :: Grid RGB -> Coord -> Int
getRHorizEnergy (G grid) (x,y) =
    (getR (getGridLoc (G grid) (x,(getLeft (width (G grid)) y))) -
    getR (getGridLoc (G grid) (x,(getRight (width (G grid)) y)))) *
    (getR (getGridLoc (G grid) (x, (getLeft (width (G grid)) y))) -
    getR (getGridLoc (G grid) (x, getRight (width (G grid)) y)))

--Helper (getG, getGridLoc, getLeft, getRight)
--Get Horizontal G Energy
getGHorizEnergy :: Grid RGB -> Coord -> Int
getGHorizEnergy (G grid) (x,y) =
    (getG (getGridLoc (G grid) (x,(getLeft (width (G grid)) y))) -
    getG (getGridLoc (G grid) (x,(getRight (width (G grid)) y)))) *
    (getG (getGridLoc (G grid) (x,(getLeft (width (G grid)) y))) -
    getG (getGridLoc (G grid) (x,(getRight (width (G grid)) y))))

--Helper (getB, getGridLoc, getLeft, getRight)
--Get Horizontal B Energy
getBHorizEnergy :: Grid RGB -> Coord -> Int
getBHorizEnergy (G grid) (x,y) =
    (getB (getGridLoc (G grid) (x,(getLeft (width (G grid)) y))) -
    getB (getGridLoc (G grid) (x,(getRight (width (G grid)) y)))) *
    (getB (getGridLoc (G grid) (x,(getLeft (width (G grid)) y))) -
    getB (getGridLoc (G grid) (x,(getRight (width (G grid)) y))))

--Helper (getRHorizEnergy, getGHorizEnergy, getBHorizEnergy)
--Calculate Horizontal Energy 
getHorizEnergy :: Grid RGB -> Coord -> NodeEnergy
getHorizEnergy (G grid) (x,y) =
    (getRHorizEnergy (G grid) (x,y)) + (getGHorizEnergy (G grid) (x,y)) + (getBHorizEnergy (G grid) (x,y))

--Helper (getR, getGridLoc, getAbove, getBelow)
--Calculate Vertical R Energy
getRVertEnergy :: Grid RGB -> Coord -> Int
getRVertEnergy (G grid) (x,y) =
    (getR (getGridLoc (G grid) ((getAbove (height (G grid)) x), y)) -
    getR (getGridLoc (G grid) ((getBelow (height (G grid)) x), y))) *
    (getR (getGridLoc (G grid) ((getAbove (height (G grid)) x), y)) -
    getR (getGridLoc (G grid) ((getBelow (height (G grid)) x), y)))

--Helper (getG, getGridLoc, getAbove, getBelow)
--Calculate Vertical G Energy
getGVertEnergy :: Grid RGB -> Coord -> Int
getGVertEnergy (G grid) (x,y) =
    (getG (getGridLoc (G grid) ((getAbove (height (G grid)) x), y)) -
    getG (getGridLoc (G grid) ((getBelow (height (G grid)) x), y))) *
    (getG (getGridLoc (G grid) ((getAbove (height (G grid)) x), y)) -
    getG (getGridLoc (G grid) ((getBelow (height (G grid)) x), y)))

--Helper (getB, getGridLoc, getAbove, getBelow)
--Calculate Vertical B Energy
getBVertEnergy :: Grid RGB -> Coord -> Int
getBVertEnergy (G grid) (x,y) =
    (getB (getGridLoc (G grid) ((getAbove (height (G grid)) x), y)) -
    getB (getGridLoc (G grid) ((getBelow (height (G grid)) x), y))) *
    (getB (getGridLoc (G grid) ((getAbove (height (G grid)) x), y)) -
    getB (getGridLoc (G grid) ((getBelow (height (G grid)) x), y)))

--Helper (getRVertEnergy, getGVertEnergy, getBVertEnergy)
--Calculate Vertical Energy
getVertEnergy :: Grid RGB -> Coord -> NodeEnergy
getVertEnergy (G grid) (x,y) =
    (getRVertEnergy (G grid) (x,y)) + (getGVertEnergy (G grid) (x,y)) + (getBVertEnergy (G grid) (x,y))

--Main (getHorizEnergy, getVertEnergy)
--What is the energy at Coords in a Grid RGB
energyAt :: Grid RGB -> Coord -> NodeEnergy
energyAt (G grid) (x,y) =
    (getHorizEnergy (G grid) (x,y)) + (getVertEnergy (G grid) (x,y))

--Helper
getEnergyRow :: Grid NodeEnergy -> Int -> Int -> [NodeEnergy]
getEnergyRow (G grid) selectRow startIdx =
    if selectRow == startIdx
    then (head grid)
    else getEnergyRow (G (tail grid)) selectRow (startIdx + 1)

--Helper
getEnergyCol :: Int -> Int -> [NodeEnergy] -> NodeEnergy
getEnergyCol selectCol startIdx grid =
    if selectCol == startIdx
    then (head grid)
    else getEnergyCol selectCol (startIdx + 1) (tail grid)

--Helper
--Give Grid Locations for non RGB grids
getEnergyGridLoc :: Grid NodeEnergy -> Coord -> NodeEnergy
getEnergyGridLoc (G grid) (x,y) = getEnergyCol y 0 (getEnergyRow (G grid) x 0)

--Helper
--Make a Grid NodeEnergy with initialized locations
--Given Grid of NodeEnergy, Numk Rows, Num Cols. Return Grid of NodeEnergy
makeGridNodeEnergy :: Int -> Int -> Grid NodeEnergy
makeGridNodeEnergy numRows numCols =
    (G ([[ 0 | j <- [1..numCols] ] | i <- [1..numRows] ]))

--Helper (energyAt)
--Calculate all energies in a row given Grid RGB, Row Idx num, Start Idx, End Idx (width)
calcRowEnergies :: Grid RGB -> Int -> Int -> Int -> [NodeEnergy]
calcRowEnergies (G rgb) selectRow startIdx endIdx =
    if startIdx == endIdx
    then []
    else energyAt (G rgb) (selectRow,startIdx) : calcRowEnergies (G rgb) selectRow (startIdx + 1) endIdx

--Helper (calcRowEnergies)
--Calculate all energies in a grid with a given
calcGridEnergies :: Grid RGB -> Int -> Int -> [[NodeEnergy]]
calcGridEnergies (G rgb) startColIdx endColIdx =
    if startColIdx == endColIdx
    then []
    else calcRowEnergies (G rgb) startColIdx 0 (width (G rgb)) : calcGridEnergies (G rgb) (startColIdx + 1) (height (G rgb))

--Main
--Calculate all energies in a Grid RGB
energies :: Grid RGB -> Grid NodeEnergy
energies (G rgb) = G (calcGridEnergies (G rgb) 0 (height (G rgb)))

--Main
--Create Triplets
nextGroups :: [a] -> a -> [(a,a,a)]
nextGroups list value =
    if length list == 1
    then [(value, head list, value) ]
    else if length list == 2
         then [(value, head list, head (tail list)),(head list, head (tail list),value)]
         else nextGroupHelper list value 1 (length list)

--Helper for nextGroup
--Creates triplets
nextGroupHelper :: [a] -> a -> Int -> Int -> [(a,a,a)]
nextGroupHelper list value curIdx quitNum=
    if curIdx == quitNum
    then [(head list, head (tail list), value)]
    else if curIdx == 1
         then [(value, head list, head (tail list))] ++ nextGroupHelper list value (curIdx + 1) quitNum
         else [(head list, head (tail list), head (tail (tail list)))] ++ nextGroupHelper (tail list) value (curIdx + 1) quitNum

--Helper
--Get Node Path Cost
getNodePathCost :: Node -> PathCost
getNodePathCost (Node _ _ _ v _ _) = v

--Helper
--May not be used.....
--Get Node Energy
getNodeEnergy :: Node -> NodeEnergy
getNodeEnergy (Node _ _ v _ _ _) = v

--Helper
--Get Minimum Node energy
getNodeMinEn :: Node -> Node -> NodeEnergy
getNodeMinEn node1 node2 =
    --ONLY assuming Node 1 and Node3 can be 'No'
    if (getNodePathCost node1) <= (getNodePathCost node2)
    then getNodePathCost node1
    else getNodePathCost node2

--Helper
--Repetitive because of GetNodeMinEn but adds a third node
--Assumes all nodes here are not "No"
getNodeMinEnThree :: Node -> Node -> Node -> NodeEnergy
getNodeMinEnThree node1 node2 node3 =
    if (getNodePathCost node1) <= (getNodePathCost node2) && (getNodePathCost node1) <= (getNodePathCost node3)
    then getNodePathCost node1
    else if (getNodePathCost node2) <= (getNodePathCost node1) && (getNodePathCost node2) <= (getNodePathCost node3)
    then getNodePathCost node2
    else getNodePathCost node3

--Helper
--Get Minimum Node to branch to
getNodeMin :: Node -> Node -> Node
getNodeMin node1 node2 =
    --ONLY assuming Node 1 and Node3 can be 'No'
    if (getNodePathCost node1) <= (getNodePathCost node2)
    then node1
    else node2

--Helper
--Repetitive because of GetNodeMinEn but adds a third node
--Assumes all nodes here are not "No"
getNodeMinThree :: Node -> Node -> Node -> Node
getNodeMinThree node1 node2 node3 =
    if (getNodePathCost node1) <= (getNodePathCost node2) && (getNodePathCost node1) <= (getNodePathCost node3)
    then node1
    else if (getNodePathCost node2) <= (getNodePathCost node1) && (getNodePathCost node2) <= (getNodePathCost node3)
    then node2
    else node3

--Main
--Build up a Node from a Packet
buildNodeFromPacket :: Packet -> (Node, Node, Node) -> Node
buildNodeFromPacket (cords, rgb, nodeEnergy) (n1, n2, n3) =
    if n1 == No && n2 == No && n3 == No
    then Node cords rgb nodeEnergy (nodeEnergy) No (n1,n2,n3)
    else if n1 == No
         --Node 2 and Node 3 must not be 'No'
         then Node cords rgb nodeEnergy (nodeEnergy + getNodeMinEn n2 n3) (getNodeMin n2 n3) (n1,n2,n3)
         else if n3 == No
         then Node cords rgb nodeEnergy (nodeEnergy + getNodeMinEn n1 n2) (getNodeMin n1 n2) (n1,n2,n3)
         else Node cords rgb nodeEnergy (nodeEnergy + getNodeMinEnThree n1 n2 n3) (getNodeMinThree n1 n2 n3) (n1,n2,n3)

--Main
--Build Row From Packets
buildRowFromPackets :: [Packet] -> [(Node,Node,Node)] -> [Node]
buildRowFromPackets [] [] = []
buildRowFromPackets (p:ps) (n:ns) = buildNodeFromPacket p (getFirstNode n,getSecondNode n,getThirdNode n)
                                    : buildRowFromPackets ps ns

--Helper
--Get first node
getFirstNode :: (Node,Node,Node) -> Node
getFirstNode (n1, n2, n3) = n1

--Helper
--Get second node
getSecondNode :: (Node,Node,Node) -> Node
getSecondNode (n1,n2,n3) = n2

--Helper
--Get Third node
getThirdNode :: (Node,Node,Node) -> Node
getThirdNode (n1,n2,n3) = n3

--Helper
--Build Packets into Nodes
helpPacketsToNodes :: Grid Packet -> [Node] -> [[Node]]
helpPacketsToNodes (G []) _ = []
--Number of Nodes matters here. Must change
helpPacketsToNodes (G (p:ps)) nodeList = buildRowFromPackets p (nextGroups nodeList No) : helpPacketsToNodes (G ps) (buildRowFromPackets p (nextGroups nodeList No))

--Helper
--Generate "No" Nodes in triplets for bottom row of packets
generateNoNodes :: Int -> Int -> [Node]
generateNoNodes startIdx endIdx =
    if startIdx == endIdx
    then []
    else [No] ++ generateNoNodes (startIdx + 1) endIdx

--Main
--Packets To Nodes
packetsToNodes :: Grid Packet -> Grid Node
packetsToNodes (G pack) = G (helpPacketsToNodes (G pack) (generateNoNodes 0 (width (G pack))))

--Main
--Find best vertical path from top to bottom
findVerticalPath :: Grid RGB -> Path
findVerticalPath (G rgb) = [(0,1)]

--Main
findHorizontalPath :: Grid RGB -> Path
findHorizontalPath = undefined

--Main
removeVerticalPath :: Grid RGB -> Path -> Grid RGB
removeVerticalPath = undefined

removeHorizontalPath :: Grid RGB -> Path -> Grid RGB
removeHorizontalPath = undefined

gridToFile :: Grid RGB -> FilePath -> IO ()
gridToFile = undefined

fileToGrid :: FilePath -> IO (Grid RGB)
fileToGrid = undefined

{-Secondary Author Note:
   Name: Ethan Painter
   G#:   G00915079
   netID: epainte2
-}