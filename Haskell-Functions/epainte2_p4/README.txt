Ethan Painter
epainte2
G00915079
CS 463 - Project 4 Write Up

1. The shared resource that represents the music is the main thread's startMusicalChairsLoop function.
 This function is called on line 47 (in main), the function type is described on line 54, and the function
 actually begins on line 55. The emcee controls the music on line 67 and 80. THe emcee printing is done via
 the announcer function(line 169). This functions prints all messages from the threads. For representing chairs,
 I declared type Chairs as a list of strings (line 24). These chairs were initialized depending on the number
 of number provided through args or the default number (10) on line 41. Line 41 is a reference to init Chairs
 (144,145,146) which initializes the list of chairs starting at C1 to C#. Example: Num = 9 so chairs =
 ["C1","C2",...,"C9"]. Also, after a round where players have won and lost, the list of chairs is simplified
 to remove the last available chair (Lines 78 and 89) through calls to startMusicalChairsLoop with modified
 chairs lists.

2. My strategy for players to find empty chairs was to allow players to select any available chair from a channel
(labeled in my code as strInChan or inChan, both short for Input Channel). Players or Threads try to read a value
from this input channel, and if the value is a valid input, ie. C1 or C2 or C#, then the thread prints out that
it successfully found a seat, while also removing this seat from other threads selecting this specific seat. Of
course, through print statements, some seats will not appear in order as they are grabbed by the threads. However,
the main concern that seats are not reused by other players is taken care of. Also, it's worth noting that after
all chairs are properly initialized, the last value stored here is "-1" (Reference: line 96), this way the last
player or thread that tries to read a value from the input channel is told it has the lost the game and therefore
end the thread's execution. After a round when all chairs are found and one player has lost, and the announcer has
printed the expected number of messages, then a newRound (oldRound + 1) is created (line 75 & 86) and a call is made
to startMusicalChairsLoop with a modified Chairs list, newRound Number, chair length, but the same input, output, and
winner channels (line 78 & 89). When the number of chairs is finally reduced to 0, a separate condition in the
function startMusicalChairsLoop (line 55), then the winner from the winner channel is read, printed, and END is
printed. Afterwards a return statement is used, which runs immediately into the return statement for main.

3. The challenges I faced when I originally started was thinking of how I wanted to use MVars or Channels to control
how I manage the shifting of data. A main challenge I had to overcome was how to stop the player thread from
blocking because of the last thread expecting a chair to read but not receiving any because the chairs were always
the number of players - 1. To solve this, I created a dummy string, "-1". This way the last thread reads it and
stops executing. Another challenge I had was maintaining the execution of threads after one round. This required me
to use startPlayers (line 105) in only one part of the startMusicalChairsLoop to avoid resetting the thread numbers
(reference: 71, Used in THEN (line 65) but not ELSE (line 79)). The last major challenge I had was understanding
how to save the winner thread. I couldn't implement a simple solution using a global variable or putStrLn, so I did
the hard change and added a third channel, specifically to save the final winner thread name. This is called when
the number of chairs is 0 (line 55) as described earlier, and the readChan blocking clal doesn't affect the program
because the thread will end either way (58 & 59). For cooperation, I created three major channels: Input, Output,
and Winner. Input stored all of the chairs available to be taken by players. Output was used to store/print values
by Winner stores one value: the winner thread name for final printing. Most of the program was managing the exchange
between the input and output channels.

4. The aspects of the task that were straightforward were the creation of chairs, creation of threads, and modification
of the list of chairs. The tasks that I found most challenging were implementing Channels, MVars, and managing reading
and writing from channels to avoid blocking (deadlock and livelock). These more challenging tasks caused me to be
more cautious with how I modified specific parts of my code structure.

5. A few bugs I ran into included livelock, accidentally resetting the threads after each round, and determining a method
to print out who won the game after the number of chairs was left to 0. The livelock bug was related to my attempts to read
input for the player thread, when no input was expected to be sent or read by the last thread. Also, early on in my testing,
I realized that during each of my calls to play a round of musical chairs, I was accidentally resetting the numbers for current
threads (essentially restarting new threads and not preserving the old threads). The last software fault was how to print out
who won the game after all players and threads had been used up. I realized I couldn't use a global variable because of the IO
monad, and I couldn't simply write the output to the output channel because the losing thread wouldn't be shown. The only
logical way to solve it for me was to create a new channel specifically for the winner print statement to be called later. I
debugged all of my errors by manually testing and tracking where the errors were occurring. Most challenges weren't too hard
to fully change or modify because of the limited complexity of the program.

6. Relying on Chan's was incredibly helpful for managing expected number of inputs to input and output channels.
 Managing these properly was important for adding or removing inputs accessible across threads.