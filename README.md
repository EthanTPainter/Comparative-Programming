# Comparative-Programming

## Concurrent-And-Distributed-Samples
- Projects focusing on concurrent/distributed concepts in programming
- Projects rely on balancing multiple processes/threads at once
- 1) One sample project is about managing brdige threads
  - Each car is a thread, bridge represents the shared resource
  - Using locks and signals, maange how cars are allowed to enter, cross, and exit the bridge
- 2) Another sample project includes creating and simulating a tiny unix shell
  - Balancing foreground and background processes
  - as well as stopped or interrupted processes

## Encryption-Decryption-DES
- Encryption and Decryption projects for data encryption standard (DES)
- DES is old and insecure with modern computers, so use only for educational opportunities
- Includes implementations of DES modes: ECB, CBC, CFB, and OFB
- Implementations priovided in various languages: C, Python, Java
- For modern encryption standards, see Advanced Encryption Standard (AES)

## Python Functions
- Includes file for basic python functions (epainte2)
- Includes testing file for verifying functionality (tester)

## Image-Smashing
- Includes files for reducing size of images (image resizing by seam carving)
- Also based on Adobe Photoshop tool to scale pictures based on RGB values
- Searches for path with least energy vertically and/or horizontally
- Based on Grid layout of image with nodes and RGB values
- Includes tester file for verifying functionality (P1Tester)
- Resources to help understand the project's purpose:
  - Seam Carving Overview by Josh Hug (http://nifty.stanford.edu/2015/hug-seam-carving/)
  - Original SIG GRAPH video to complement the website listed above (https://www.youtube.com/watch?v=6NcIJXTlugc)\
- LIMITATIONS: Current implementation is used for relatively small, easy to process images. Values are moved
  around using Int variables/methods, so upper bound of integer values is a concern with larger images and 
  larger values to process

## Haskell-Functions
- Basic functions created in Haskell (Help learn the language)
- Mirrors functions created in "Python Functions" provided above

## Image-Smashing-Hs 
- Haskell approach to Image-Smashing (image resizing by seam carving)
- Doesn't include limitation found in java version because of Haskell's data types
  (No integer building restriction/upper bound)
