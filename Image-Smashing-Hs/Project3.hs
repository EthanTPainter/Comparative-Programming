{- Name: Ethan Painter
   G#:   G00915079
   netID: epainte2
   Note: Functions labeled "Main" are required via test cases
         Functions labeled "Helper" are assistant functions to "Main" functions
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
--Get Above vertical Y
getAbove :: Int -> Int -> Int
getAbove gridHeight y =
    if y == 0
    then gridHeight - 1
    else y - 1

--Helper
--Get Below vertical Y
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
--Get Row of Node Energies (Int)
getEnergyRow :: Grid NodeEnergy -> Int -> Int -> [NodeEnergy]
getEnergyRow (G grid) selectRow startIdx =
    if selectRow == startIdx
    then (head grid)
    else getEnergyRow (G (tail grid)) selectRow (startIdx + 1)

--Helper
--Get energy for column (Int)
getEnergyCol :: Int -> Int -> [NodeEnergy] -> NodeEnergy
getEnergyCol selectCol startIdx grid =
    if selectCol == startIdx
    then (head grid)
    else getEnergyCol selectCol (startIdx + 1) (tail grid)

--Helper
--Give Grid Locations for Grid Int
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

--Helper
--Calculate all energies in a Grid RGB, but leave off G from final result
noGEnergies :: Grid RGB -> [[NodeEnergy]]
noGEnergies (G rgb) = (calcGridEnergies (G rgb) 0 (height (G rgb)))

--Main
--Create Triplets
nextGroups :: [a] -> a -> [(a,a,a)]
nextGroups list value =
    if length list == 1
    then [(value, head list, value) ]
    else if length list == 2
         then [(value, head list, head (tail list)),(head list, head (tail list),value)]
         else nextGroupHelper list value 1 (length list)

--Helper
--Helps creates triplets of when size is 3 or greater
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
helpPacketsToNodes :: Grid Packet -> [(Node,Node,Node)] -> [[Node]]
helpPacketsToNodes (G []) _ = []
helpPacketsToNodes (G p) nodeTriples =
    buildRowFromPackets (head p) nodeTriples :  helpPacketsToNodes (G (tail p)) (nextGroups (buildRowFromPackets (head p) nodeTriples) No)

--Helper
--Generate "No" Nodes in triplets for bottom row of packets
generateNoNodes :: Int -> Int -> [Node]
generateNoNodes startIdx endIdx =
    if startIdx == endIdx
    then []
    else [No] ++ generateNoNodes (startIdx + 1) endIdx

--Main
--Packets To Nodes
--Reverse function allowed from M. Snyder
packetsToNodes :: Grid Packet -> Grid Node
packetsToNodes (G pack) = G (reverse (helpPacketsToNodes (G (reverse pack)) (nextGroups (generateNoNodes 0 (length (head pack))) No)))

--Helper
--Make Minimum sum value from given values
--Given val, bot left, bot mid, bot right
makeMinSum :: Int -> Int -> Int -> Int -> Int -> Int
makeMinSum given x y z rowLen =
    if x == -1
    then given + getDuoMin y z
         else if z == -1
              then given + getDuoMin x y
              else if x <= y && x <= z
                   then given + x
                   else if y <= z
                        then given + y
                        else given + z
--Helper
--Get Min from two values
getDuoMin :: Int -> Int -> Int
getDuoMin v1 v2 =
    if v1 <= v2
    then v1
    else v2

--Helper
--200,000 is the max (Spot no available)
getBotLeft :: [Int] -> Int -> Int
getBotLeft oldRow x =
    if x == 0
    then -1
    else oldRow !! (x-1)

--200,000 max if spot not available
getBotRight :: [Int] -> Int -> Int
getBotRight oldRow x =
    if x == (length oldRow)-1
    then -1
    else oldRow !! (x+1)

--Helper
--Get new sum for rows
--Grid RGBs, Int row, Int current index
makeMinRow :: [Int] -> Int -> [Int] -> [Int]
makeMinRow newRow curIdx oldRow =
    if curIdx == (length newRow)
    then []
    else makeMinSum (newRow !! curIdx) (getBotLeft oldRow curIdx) (oldRow !! curIdx) (getBotRight oldRow curIdx) (length oldRow) : makeMinRow newRow (curIdx + 1) oldRow

--Helper
--REVERSE [[INT]] in FUNCTION CALL
makeMinGrid :: [[Int]] -> Int -> [Int] -> [[Int]]
makeMinGrid [] startColIdx olds = []
makeMinGrid new startColIdx olds =
    if startColIdx == 0
    then (head new) : makeMinGrid (tail new) (startColIdx + 1) (head new)
    else makeMinRow (head new) 0 olds : makeMinGrid (tail new) (startColIdx + 1) (makeMinRow (head new) 0 olds)

--Helper
--Find lowest Top Node given a Grid Int, and current lowest node
--Set current low to -1
getLowestTop :: [Int] -> Int -> Int -> Coord -> Coord
getLowestTop [] curIdx curLow (x,y) = (x,y)
getLowestTop list curIdx curLow (x,y) =
    if curIdx == 0
    then getLowestTop (tail list) (curIdx + 1) (head list) (0,curIdx)
    else if (head list) < curLow
         then getLowestTop (tail list) (curIdx + 1) (head list) (0,curIdx)
         else getLowestTop (tail list) (curIdx + 1) curLow (x,y)
--Helper
--Find Next smallest Value (return Coords)
jumpToSmallest :: [Int] -> [Int] -> Coord -> Coord
jumpToSmallest topList botList (x,y) =
    if y == 0
    --Just consider below and right
    then checkBelowRight (botList !! y) (botList !! (y+1)) (x,y)
    else if y == (length topList) - 1
    then checkLeftBelow (botList !! (y-1)) (botList !! y) (x,y)
    else checkAllThree (botList !! (y-1)) (botList !! y) (botList !! (y+1)) (x,y)

--Helper
--Get Check for below and right (num1 below, num2 right)
checkBelowRight :: Int -> Int -> Coord -> Coord
checkBelowRight num1 num2 (x,y) =
    if num1 <= num2
    then (x+1,y)
    else (x+1,y+1)

--Helper
--Get Check for left and below (num1 left, num2 below)
checkLeftBelow :: Int -> Int -> Coord -> Coord
checkLeftBelow num1 num2 (x,y) =
    if num1 <= num2
    then (x+1,y-1)
    else (x+1,y)

--Helper
--Get Check for all three nodes (num1 left, num2 below, num3 right)
checkAllThree :: Int -> Int -> Int -> Coord -> Coord
checkAllThree num1 num2 num3 (x,y) =
    if num1 <= num2 && num1 <= num3
    then (x+1,y-1)
    else if num2 <= num3
         then (x+1,y)
         else (x+1,y+1)

--Helper
--Find Min Path given a Grid of Ints, starting coordinates, size of original list
--List of Grid Int is intended to be Grid of summed energies (bottom to top implementation)
--Look at top row, grab lowest value/index, scan lower three values for min until bottom row
findVMinPath :: [[Int]] -> Coord -> Int -> Int -> [Coord]
findVMinPath list (x,y) size currentLine =
    if size > 1 && currentLine >= size
    then []
    else if x == 0 && size > 2 && currentLine < 1
         then getLowestTop (head list) 0 0 (0,0) : jumpToSmallest (head list) (head (tail list)) (getLowestTop (head list) 0 0 (0,0)) :
                    findVMinPath (tail list) (jumpToSmallest (head list) (head (tail list)) (getLowestTop (head list) 0 0 (0,0))) size (currentLine + 2)
        else if size == 1
            then [getLowestTop (head list) 0 0 (0,0)]
            else if size == 2
                then getLowestTop (head list) 0 0 (0,0) : [jumpToSmallest (head list) (head (tail list)) (getLowestTop (head list) 0 0 (0,0))]
                else jumpToSmallest (head list) (head (tail list)) (x,y) : findVMinPath (tail list) (jumpToSmallest (head list) (head (tail list)) (x,y)) size (currentLine + 1)

--Main
--Find best vertical path from top to bottom (Look for cheapest node at top and go from there)
findVerticalPath :: Grid RGB -> Path
findVerticalPath (G rgb) = findVMinPath (reverse (makeMinGrid (reverse (noGEnergies (G rgb))) 0 [])) (0,0) (height (G rgb)) 0

--Helper
--Given a list of Coord, invert x and y Coord for each
invertCoordsList :: [Coord] -> [Coord]
invertCoordsList [] = []
invertCoordsList list = (getY (head list),getX (head list)) : invertCoordsList (tail list)

--Helper
--Get X of Coord
getX :: Coord -> Int
getX (x,y) = x

--Helper
--Get Y of Coord
getY :: Coord -> Int
getY (x,y) = y

--Main
--Find Horizontal Path of a Grid RGB
findHorizontalPath :: Grid RGB -> Path
findHorizontalPath (G rgb) = invertCoordsList( findVMinPath (reverse (makeMinGrid (reverse (transpose (noGEnergies (G rgb)))) 0 [])) (0,0) (width (G rgb)) 0)

--Helper
--Given Row RGBs and Coord return new Row
removeCoordRow :: [RGB] -> Coord -> Int -> [RGB]
removeCoordRow [] _ curIdx= []
removeCoordRow rgb (x,y) curIdx =
    if y == curIdx
    then (tail rgb)
    else (head rgb) : removeCoordRow (tail rgb) (x,y) (curIdx + 1)

--Helper
--Given [[RGB]] and [Coord], return [[RGB]] without [Coord]
removeCoordGrid :: [[RGB]] -> [Coord] -> [[RGB]]
removeCoordGrid [] _ = []
removeCoordGrid rgb path = removeCoordRow (head rgb) (head path) 0 : removeCoordGrid (tail rgb) (tail path)

--Main
--Given a Grid RGB, Path, return Grid RGB with path removed
removeVerticalPath :: Grid RGB -> Path -> Grid RGB
removeVerticalPath (G rgb) path = G (removeCoordGrid rgb path)

--Main
--Given a Grid RGB, Path, return Grid RGB with path removed
--Make sure to transpose grid after removal to return to standard grid shape
removeHorizontalPath :: Grid RGB -> Path -> Grid RGB
removeHorizontalPath (G rgb) path = G (transpose (removeCoordGrid (transpose rgb) (invertCoordsList path)))

--Helper
--Get Sting of Row values
--Row of RGB, STRING = "", INT = 0, INT = length Row
getRowString :: [RGB] -> String -> Int -> Int-> String
getRowString rgb str idx len=
    if idx == len - 1
    then show (getR (head rgb)) ++ " " ++ show (getG (head rgb)) ++ " " ++ show (getB (head rgb))
    else show (getR (head rgb)) ++ " " ++ show (getG (head rgb)) ++ " " ++ show (getB (head rgb)) ++ "\n" ++ getRowString (tail rgb) str (idx + 1) len

--Helper
--Get String of Grid values in order from top row down
getGridString :: [[RGB]] -> String -> Int -> Int -> String
getGridString [] str idx len = str
getGridString rgb str idx len =
    if idx == len - 1
    then getRowString (head rgb) str 0 (length (head rgb))
    else getRowString (head rgb) str 0 (length (head rgb)) ++ "\n" ++ getGridString (tail rgb) str (idx + 1) len

--Main
--Given Grid RGB and name of PPM file, write the appropriate P3 format
--of PPM file formatting into the file, and save
gridToFile :: Grid RGB -> FilePath -> IO ()
gridToFile (G rgb) filePath = writeFile filePath ("P3\n" ++ show (width (G rgb)) ++ "\n" ++ show (height (G rgb)) ++ "\n" ++ "255\n" ++ getGridString rgb "" 0 (length rgb))

--Main
--Given filename, open it, read in the valid P3 formatted PPM contents,
--And create and return Grid RGB
fileToGrid :: FilePath -> IO (Grid RGB)
fileToGrid filePath = (testingFile (listLines filePath))

--Helper
--Compile all possible variables needed for making RGB Grid
testingFile :: IO[String] -> IO (Grid RGB)
testingFile list = do
    --New list
    modList <- list
    let format = (head modList)
    let width = read (head (tail modList)) :: Int
    let height = read (head (tail (tail modList))) :: Int
    let gridList = (tail (tail (tail (tail modList))))
    let curIdx = 0 :: Int
    let good1DList = makeRGBRowsGivenFile gridList (height * width * 3) 0
    return (G (make2D (height * width) width good1DList))

--Helper
--Make 2D List of RGB Rows
make2D :: Int -> Int -> [a] -> [[a]]
make2D 0 _ _ = []
make2D num width list = take width list : make2D (num - width) width (recurse width list)

--Helper
--Help Recurse for making 2D array
recurse :: Int -> [a] -> [a]
recurse num [] = []
recurse num list =
    if num > 0
    then recurse (num - 1) (tail list)
    else list

--Helper
--Make Row given list of file inputs
--String List, Width (length), current index -> RGB Row
makeRGBRowsGivenFile :: [String] -> Int -> Int -> [RGB]
makeRGBRowsGivenFile list width current =
    if current == width
    then []
    else RGB (read (head list) :: Int) (read (head (tail list)) :: Int) (read (head (tail (tail list))) :: Int) : makeRGBRowsGivenFile (tail (tail (tail list))) width (current + 3)

--Helper
listLines :: FilePath -> IO [String]
listLines = fmap words . readFile

{-Secondary Author Note:
   Name: Ethan Painter
   G#:   G00915079
   netID: epainte2
   Using this extra comment space as a backup for verification
-}