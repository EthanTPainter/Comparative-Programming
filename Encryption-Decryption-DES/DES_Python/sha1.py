# @author: Ethan Painter
# Compiled: python (could use pycrypto)
# Last Updated: 5/24/2018
# Originally Created: 10/23/2017

from Crypto.Hash import SHA
import sys
import string
import time

#checks if the files have the correct extensions
def fileExt():
	if(sys.argv[1][-4:] == ".txt"):
		print "Hashing ", (sys.argv[1])
		return 1
	else:
		return -1

validFile = fileExt()

if(validFile == -1):
	print "incorrect file type"
	sys.exit()

h = SHA.new()

#for each .txt file in command line arguments generate a message digest (hash) and print the time taken in seconds for each file
for i in range(1, len(sys.argv)):
	fin = open(sys.argv[i], "r", 0)
        start = time.time()
	for readBlock in iter(lambda: fin.read(1), ""):
		h.update(readBlock)
	print h.hexdigest()
	end = time.time()
	print "Time taken:", end - start, "seconds"
