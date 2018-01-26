# Based on testing harness dated 2017-06-02.

# STUDENTS: TO USE:
# 
# The following command will test all test cases on your file:
# 
#   python3 <thisfile.py> <your_one_file.py>
# 
# 
# You can also limit the tester to only the functions you want tested.
# Just add as many functions as you want tested on to the command line at the end.
# Example: to only run tests associated with func1 and func2, run this command:
# 
#   python3 <thisfile.py> <your_one_file.py> func1 func2
# 
# You really don't need to read the file any further, except that when
# a specific test fails, you'll get a line number - and it's certainly
# worth looking at those areas for details on what's being checked. This would
# all be the indented block of code starting with "class AllTests".


# INSTRUCTOR: TO PREPARE:
#  - add test cases to class AllTests. The test case functions' names must
# be precise - to test a function named foobar, the test must be named "test_foobar_#"
# where # may be any digits at the end, such as "test_foobar_13".
# - any extra-credit tests must be named "test_extra_credit_foobar_#"
# 
# - name all required definitions in REQUIRED_DEFNS, and all extra credit functions
#   in EXTRA_CREDIT_DEFNS. Do not include any unofficial helper functions. If you want
#   to make helper definitions to use while testing, those can also be added there for
#   clarity.
# 
# - to run on either a single file or all .py files in a folder (recursively):
#   python3 <thisfile.py> <your_one_file.py>
#   python3 <thisfile.py> <dir_of_files>
#   python3 <thisfile.py> .                    # current directory
# 
# A work in progress by Mark Snyder, Oct. 2015.
#  Edited by Yutao Zhong, Spring 2016.
#  Edited by Raven Russell, Spring 2017.
#  Edited by Mark Snyder, June 2017.


import unittest
import shutil
import sys
import os
import time

#import subprocess

import importlib

############################################################################
############################################################################
# BEGIN SPECIALIZATION SECTION (the only part you need to modify beyond 
# adding new test cases).

# name all expected definitions; if present, their definition (with correct
# number of arguments) will be used; if not, a decoy complainer function
# will be used, and all tests on that function should fail.
	
REQUIRED_DEFNS = [ "prime_factors","coprime","trib","max_new","zip_with","pass_fail","powerset","matrix_product" ]

# for method names in classes that will be tested
SUB_DEFNS = [ ]

# definitions that are used for extra credit
EXTRA_CREDIT_DEFNS = [ ]

# how many points are test cases worth?
weight_required = 2
weight_extra_credit = 1

# don't count extra credit; usually 100% if this is graded entirely by tests.
# it's up to you the instructor to do the math and add this up!
# TODO: auto-calculate this based on all possible tests.
total_points_from_tests = 100

# how many seconds to wait between batch-mode gradings? 
# ideally we could enforce python to wait to open or import
# files when the system is ready but we've got a communication
# gap going on.
DELAY_OF_SHAME = 1


# set it to true when you run batch mode... 
CURRENTLY_GRADING = False

# what temporary file name should be used for the student?
# This can't be changed without hardcoding imports below, sorry.
# That's kind of the whole gimmick here that lets us import from
# the command-line argument without having to qualify the names.
RENAMED_FILE = "student"

def same_items(xs,ys):
	if len(xs) != len(ys):
		return False
	for x in xs:
		if x not in ys:
			return False
	return True

# END SPECIALIZATION SECTION
############################################################################
############################################################################

# enter batch mode by giving a directory to work on as the only argument.
BATCH_MODE = len(sys.argv)==2 and (sys.argv[1] in ["."] or os.path.isdir(sys.argv[1]))

# This class contains multiple "unit tests" that each check
# various inputs to specific functions, checking that we get
# the correct behavior (output value) from completing the call.
class AllTests (unittest.TestCase):
		
	############################################################################
	
	# --------------------------------------------------------------------
	
	# counts
	def test_prime_factors_01(self): self.assertEqual(prime_factors(1),[])
	def test_prime_factors_02(self): self.assertEqual(prime_factors(2),[2])
	def test_prime_factors_03(self): self.assertEqual(prime_factors(3),[3])
	def test_prime_factors_04(self): self.assertEqual(prime_factors(4),[2,2])
	def test_prime_factors_05(self): self.assertEqual(prime_factors(5),[5])
	def test_prime_factors_06(self): self.assertEqual(prime_factors(50),[2,5,5])
	def test_prime_factors_07(self): self.assertEqual(prime_factors(66),[2,3,11])
	def test_prime_factors_08(self): self.assertEqual(prime_factors(100),[2,2,5,5])
	def test_prime_factors_09(self): self.assertEqual(prime_factors(463),[463])
	def test_prime_factors_10(self): self.assertEqual(prime_factors(512),[2,2,2,2,2,2,2,2,2])
	def test_prime_factors_11(self): self.assertEqual(prime_factors(117),[3,3,13])
	def test_prime_factors_12(self): self.assertEqual(prime_factors(1117),[1117])
	def test_prime_factors_13(self): self.assertEqual(prime_factors(123456789),[3,3,3607,3803])
	
	# --------------------------------------------------------------------
	
	def test_coprime_01(self): self.assertEqual(coprime(  2,   3), True  )
	def test_coprime_02(self): self.assertEqual(coprime(  3,   7), True  )
	def test_coprime_03(self): self.assertEqual(coprime(  3,  10), True  )
	def test_coprime_04(self): self.assertEqual(coprime( 10,  15), False )
	def test_coprime_05(self): self.assertEqual(coprime( 15,  10), False )
	def test_coprime_06(self): self.assertEqual(coprime( 50,  61), True  )
	def test_coprime_07(self): self.assertEqual(coprime(100, 200), False )
	def test_coprime_08(self): self.assertEqual(coprime( 97,  98), True  )
	def test_coprime_09(self): self.assertEqual(coprime( 66, 201), False )
	def test_coprime_10(self): self.assertEqual(coprime( 49,  28), False )
	def test_coprime_11(self): self.assertEqual(coprime(367, 463), True  )
	def test_coprime_12(self): self.assertEqual(coprime(330, 463), True  )
	def test_coprime_13(self): self.assertEqual(coprime(441,1000), True  )
	
	# --------------------------------------------------------------------
	
	def test_trib_01(self): self.assertEqual(trib(0),1)
	def test_trib_02(self): self.assertEqual(trib(2),1)
	def test_trib_03(self): self.assertEqual(trib(3),3)
	def test_trib_04(self): self.assertEqual(trib(4),5)
	def test_trib_05(self): self.assertEqual(trib(5),9)
	def test_trib_06(self): self.assertEqual(trib(6),17)
	def test_trib_07(self): self.assertEqual(trib(7),31)
	def test_trib_08(self): self.assertEqual(trib(10),193)
	def test_trib_09(self): self.assertEqual(trib(12),653)
	def test_trib_10(self): self.assertEqual(trib(13),1201)
	def test_trib_11(self): self.assertEqual(trib(14),2209)
	def test_trib_12(self): self.assertEqual(trib(20),85525)
	def test_trib_13(self): self.assertEqual(trib(30),37895489)
	
	# --------------------------------------------------------------------
	
	def test_max_new_01(self): self.assertEqual(max_new([],[]),None)
	def test_max_new_02(self): self.assertEqual(max_new([5],[]),5)
	def test_max_new_03(self): self.assertEqual(max_new([],[5]),None)
	def test_max_new_04(self): self.assertEqual(max_new([1,2,3,4,5,6],[5,6]),4)
	def test_max_new_05(self): self.assertEqual(max_new([1,5,3,4,6,2,4,4],[5,6]),4)
	def test_max_new_06(self): self.assertEqual(max_new([-5,-4,-3],[-4]),-3)
	def test_max_new_07(self): self.assertEqual(max_new([-3,3],[3]),-3)
	def test_max_new_08(self): self.assertEqual(max_new([1,1,1,1,1],[1]),None)
	def test_max_new_09(self): self.assertEqual(max_new([20,10],[200]),20)
	def test_max_new_10(self): self.assertEqual(max_new([1,2,3],[4,5,6]),3)
	def test_max_new_11(self): self.assertEqual(max_new([112,211,262,330,367,463],[330,367]),463)
	def test_max_new_12(self): self.assertEqual(max_new([112,211,262,330,367,463],[330,367,463]),262)
	
	# --------------------------------------------------------------------
	
	def test_zip_with_01(self):
		def add(x,y): return x+y
		self.assertEquals(zip_with(add, [1,2,3,4], [10,10,10,10]),[11,12,13,14])
	def test_zip_with_02(self):
		def add(x,y): return x+y
		self.assertEquals(zip_with(add, [1,2,3,4], [5,6,7,8]),[6,8,10,12])
	def test_zip_with_03(self):
		def mul(x,y): return x*y
		# first list has fewer elements.
		self.assertEquals(zip_with(mul, [2,3,4],  [5,5,5,5,5]), [10,15,20])
	def test_zip_with_04(self):
		def mul(x,y): return x*y
		# second list has fewer elements.
		self.assertEquals(zip_with(mul, [2,3,4,5,6,7,8],  [5,5,5]), [10,15,20])
		def mul(x,y): return x*y
		# first list has zero elements.
		self.assertEquals(zip_with(mul, [],  [5,5,5,5,5]), [])
	def test_zip_with_05(self):
		def mul(x,y): return x*y
		# second list has zero elements.
		self.assertEquals(zip_with(mul, [2,3,4,5,6,7,8],  []), [])
	def test_zip_with_06(self):
		def mul(x,y): return x*y
		# both lists have zero elements.
		self.assertEquals(zip_with(mul, [],  []), [])
	
	# double-weighting some tests.
	def test_zip_with_07(self):  self.test_zip_with_01()
	def test_zip_with_08(self):  self.test_zip_with_02()
	def test_zip_with_09(self):  self.test_zip_with_03()
	def test_zip_with_10(self): self.test_zip_with_04()
	def test_zip_with_11(self): self.test_zip_with_05()
	def test_zip_with_12(self): self.test_zip_with_06()
	
	# --------------------------------------------------------------------
	
	def test_pass_fail_01(self):
		def even(x): return x%2==0
		self.assertEqual(pass_fail(even,[1,2,3,4,5]),([2,4],[1,3,5]))
	def test_pass_fail_02(self):
		def even(x): return x%2==0
		self.assertEqual(pass_fail(even,[9,9,9]),([],[9,9,9]))
	def test_pass_fail_03(self):
		def even(x): return x%2==0
		self.assertEqual(pass_fail(even,[6,8,10]),([6,8,10],[]))
	def test_pass_fail_04(self):
		def pos(x): return x>0
		self.assertEqual(pass_fail(pos,[1,-1,3,-3,2,-2]),([1, 3, 2], [-1, -3, -2]))
	def test_pass_fail_05(self):
		def pos(x): return x>0
		self.assertEqual(pass_fail(pos,[-3,-4,-5]),([], [-3,-4,-5]))
	def test_pass_fail_06(self):
		def big(x): return x>10
		self.assertEqual(pass_fail(big,[8,9,10,11,12,13]),([11,12,13],[8,9,10]))
	def test_pass_fail_07(self):
		def big(x): return x>10
		self.assertEqual(pass_fail(big,[10,10,10]),([],[10,10,10]))
	def test_pass_fail_08(self):
		def cap(x): return 65 <= ord(x) <= 90
		self.assertEqual(pass_fail(cap,"Hello"),(['H'],['e','l','l','o']))
	def test_pass_fail_09(self):
		def cap(x): return 65 <= ord(x) <= 90
		self.assertEqual(pass_fail(cap,"CS 463"),(['C','S'],[' ','4','6','3']))
	def test_pass_fail_10(self):
		def cap(x): return 65 <= ord(x) <= 90
		self.assertEqual(pass_fail(cap,"RaNsOm!"),(['R','N','O'],['a','s','m','!']))
	def test_pass_fail_11(self):
		def lengthy(x): return len(x) > 3
		self.assertEqual(pass_fail(lengthy,["hello","hi",[1,2,3],[1,2,3,4,5,6]]),(["hello",[1,2,3,4,5,6]],["hi",[1,2,3]]))
	def test_pass_fail_12(self):
		def lengthy(x): return len(x) > 3
		self.assertEqual(pass_fail(lengthy,["compare across languages"]),(["compare across languages"],[]))
	
	# --------------------------------------------------------------------
	
	def test_powerset_01(self): self.assertTrue(same_items(powerset([]),[[]]))
	def test_powerset_02(self): self.assertTrue(same_items(powerset([1]),[[],[1]]))
	def test_powerset_03(self): self.assertTrue(same_items(powerset([5]),[[],[5]]))
	def test_powerset_04(self): self.assertTrue(same_items(powerset([1,2]),[[],[1],[2],[1,2]]))
	def test_powerset_05(self): self.assertTrue(same_items(powerset([2,1]),[[],[2],[1],[2,1]]))
	def test_powerset_06(self): self.assertTrue(same_items(powerset([1,2,3]),[[],[1],[2],[3],[1,2],[1,3],[2,3],[1,2,3]]))
	def test_powerset_07(self): self.assertTrue(same_items(powerset([3,1,2]),[[],[3],[1],[2],[3,1],[3,2],[1,2],[3,1,2]]))
	def test_powerset_08(self): self.assertTrue(same_items(powerset([-4,-5]),[[],[-4],[-5],[-4,-5]]))
	def test_powerset_09(self): self.assertTrue(same_items(powerset([1,2,3,4]),[[],[1],[2],[3],[4],[1,2],[1,3],[2,3],[1,4],[2,4],[3,4],[1,2,3],[1,2,4],[1,3,4],[2,3,4],[1,2,3,4]]))
	def test_powerset_10(self): self.assertTrue(same_items(powerset([0,2,4,6,8]),[[],[0],[2],[4],[6],[8],[0,2],[0,4],[2,4],[0,6],[2,6],[4,6],[0,8],[2,8],[4,8],[6,8],[0,2,4],[0,2,6],[0,4,6],[2,4,6],[0,2,8],[0,4,8],[2,4,8],[0,6,8],[2,6,8],[4,6,8],[0,2,4,6],[0,2,4,8],[0,2,6,8],[0,4,6,8],[2,4,6,8],[0,2,4,6,8]]))
	def test_powerset_11(self): self.assertTrue(same_items(powerset([4,3,2,1]),[[],[4],[3],[2],[1],[4,3],[4,2],[3,2],[4,1],[3,1],[2,1],[4,3,2],[4,3,1],[4,2,1],[3,2,1],[4,3,2,1]]))
	def test_powerset_12(self): self.assertTrue(same_items(powerset([9,8,7,6,5]),[[],[9],[8],[7],[6],[5],[9,8],[9,7],[8,7],[9,6],[8,6],[7,6],[9,5],[8,5],[7,5],[6,5],[9,8,7],[9,8,6],[9,7,6],[8,7,6],[9,8,5],[9,7,5],[8,7,5],[9,6,5],[8,6,5],[7,6,5],[9,8,7,6],[9,8,7,5],[9,8,6,5],[9,7,6,5],[8,7,6,5],[9,8,7,6,5]]))
	def test_powerset_13(self): self.assertTrue(same_items(powerset([0,1,2,3,4,5,6,7,8,9]),[[],[0],[1],[2],[3],[4],[5],[6],[7],[8],[9],[0,1],[0,2],[1,2],[0,3],[1,3],[2,3],[0,4],[1,4],[2,4],[3,4],[0,5],[1,5],[2,5],[3,5],[4,5],[0,6],[1,6],[2,6],[3,6],[4,6],[5,6],[0,7],[1,7],[2,7],[3,7],[4,7],[5,7],[6,7],[0,8],[1,8],[2,8],[3,8],[4,8],[5,8],[6,8],[7,8],[0,9],[1,9],[2,9],[3,9],[4,9],[5,9],[6,9],[7,9],[8,9],[0,1,2],[0,1,3],[0,2,3],[1,2,3],[0,1,4],[0,2,4],[1,2,4],[0,3,4],[1,3,4],[2,3,4],[0,1,5],[0,2,5],[1,2,5],[0,3,5],[1,3,5],[2,3,5],[0,4,5],[1,4,5],[2,4,5],[3,4,5],[0,1,6],[0,2,6],[1,2,6],[0,3,6],[1,3,6],[2,3,6],[0,4,6],[1,4,6],[2,4,6],[3,4,6],[0,5,6],[1,5,6],[2,5,6],[3,5,6],[4,5,6],[0,1,7],[0,2,7],[1,2,7],[0,3,7],[1,3,7],[2,3,7],[0,4,7],[1,4,7],[2,4,7],[3,4,7],[0,5,7],[1,5,7],[2,5,7],[3,5,7],[4,5,7],[0,6,7],[1,6,7],[2,6,7],[3,6,7],[4,6,7],[5,6,7],[0,1,8],[0,2,8],[1,2,8],[0,3,8],[1,3,8],[2,3,8],[0,4,8],[1,4,8],[2,4,8],[3,4,8],[0,5,8],[1,5,8],[2,5,8],[3,5,8],[4,5,8],[0,6,8],[1,6,8],[2,6,8],[3,6,8],[4,6,8],[5,6,8],[0,7,8],[1,7,8],[2,7,8],[3,7,8],[4,7,8],[5,7,8],[6,7,8],[0,1,9],[0,2,9],[1,2,9],[0,3,9],[1,3,9],[2,3,9],[0,4,9],[1,4,9],[2,4,9],[3,4,9],[0,5,9],[1,5,9],[2,5,9],[3,5,9],[4,5,9],[0,6,9],[1,6,9],[2,6,9],[3,6,9],[4,6,9],[5,6,9],[0,7,9],[1,7,9],[2,7,9],[3,7,9],[4,7,9],[5,7,9],[6,7,9],[0,8,9],[1,8,9],[2,8,9],[3,8,9],[4,8,9],[5,8,9],[6,8,9],[7,8,9],[0,1,2,3],[0,1,2,4],[0,1,3,4],[0,2,3,4],[1,2,3,4],[0,1,2,5],[0,1,3,5],[0,2,3,5],[1,2,3,5],[0,1,4,5],[0,2,4,5],[1,2,4,5],[0,3,4,5],[1,3,4,5],[2,3,4,5],[0,1,2,6],[0,1,3,6],[0,2,3,6],[1,2,3,6],[0,1,4,6],[0,2,4,6],[1,2,4,6],[0,3,4,6],[1,3,4,6],[2,3,4,6],[0,1,5,6],[0,2,5,6],[1,2,5,6],[0,3,5,6],[1,3,5,6],[2,3,5,6],[0,4,5,6],[1,4,5,6],[2,4,5,6],[3,4,5,6],[0,1,2,7],[0,1,3,7],[0,2,3,7],[1,2,3,7],[0,1,4,7],[0,2,4,7],[1,2,4,7],[0,3,4,7],[1,3,4,7],[2,3,4,7],[0,1,5,7],[0,2,5,7],[1,2,5,7],[0,3,5,7],[1,3,5,7],[2,3,5,7],[0,4,5,7],[1,4,5,7],[2,4,5,7],[3,4,5,7],[0,1,6,7],[0,2,6,7],[1,2,6,7],[0,3,6,7],[1,3,6,7],[2,3,6,7],[0,4,6,7],[1,4,6,7],[2,4,6,7],[3,4,6,7],[0,5,6,7],[1,5,6,7],[2,5,6,7],[3,5,6,7],[4,5,6,7],[0,1,2,8],[0,1,3,8],[0,2,3,8],[1,2,3,8],[0,1,4,8],[0,2,4,8],[1,2,4,8],[0,3,4,8],[1,3,4,8],[2,3,4,8],[0,1,5,8],[0,2,5,8],[1,2,5,8],[0,3,5,8],[1,3,5,8],[2,3,5,8],[0,4,5,8],[1,4,5,8],[2,4,5,8],[3,4,5,8],[0,1,6,8],[0,2,6,8],[1,2,6,8],[0,3,6,8],[1,3,6,8],[2,3,6,8],[0,4,6,8],[1,4,6,8],[2,4,6,8],[3,4,6,8],[0,5,6,8],[1,5,6,8],[2,5,6,8],[3,5,6,8],[4,5,6,8],[0,1,7,8],[0,2,7,8],[1,2,7,8],[0,3,7,8],[1,3,7,8],[2,3,7,8],[0,4,7,8],[1,4,7,8],[2,4,7,8],[3,4,7,8],[0,5,7,8],[1,5,7,8],[2,5,7,8],[3,5,7,8],[4,5,7,8],[0,6,7,8],[1,6,7,8],[2,6,7,8],[3,6,7,8],[4,6,7,8],[5,6,7,8],[0,1,2,9],[0,1,3,9],[0,2,3,9],[1,2,3,9],[0,1,4,9],[0,2,4,9],[1,2,4,9],[0,3,4,9],[1,3,4,9],[2,3,4,9],[0,1,5,9],[0,2,5,9],[1,2,5,9],[0,3,5,9],[1,3,5,9],[2,3,5,9],[0,4,5,9],[1,4,5,9],[2,4,5,9],[3,4,5,9],[0,1,6,9],[0,2,6,9],[1,2,6,9],[0,3,6,9],[1,3,6,9],[2,3,6,9],[0,4,6,9],[1,4,6,9],[2,4,6,9],[3,4,6,9],[0,5,6,9],[1,5,6,9],[2,5,6,9],[3,5,6,9],[4,5,6,9],[0,1,7,9],[0,2,7,9],[1,2,7,9],[0,3,7,9],[1,3,7,9],[2,3,7,9],[0,4,7,9],[1,4,7,9],[2,4,7,9],[3,4,7,9],[0,5,7,9],[1,5,7,9],[2,5,7,9],[3,5,7,9],[4,5,7,9],[0,6,7,9],[1,6,7,9],[2,6,7,9],[3,6,7,9],[4,6,7,9],[5,6,7,9],[0,1,8,9],[0,2,8,9],[1,2,8,9],[0,3,8,9],[1,3,8,9],[2,3,8,9],[0,4,8,9],[1,4,8,9],[2,4,8,9],[3,4,8,9],[0,5,8,9],[1,5,8,9],[2,5,8,9],[3,5,8,9],[4,5,8,9],[0,6,8,9],[1,6,8,9],[2,6,8,9],[3,6,8,9],[4,6,8,9],[5,6,8,9],[0,7,8,9],[1,7,8,9],[2,7,8,9],[3,7,8,9],[4,7,8,9],[5,7,8,9],[6,7,8,9],[0,1,2,3,4],[0,1,2,3,5],[0,1,2,4,5],[0,1,3,4,5],[0,2,3,4,5],[1,2,3,4,5],[0,1,2,3,6],[0,1,2,4,6],[0,1,3,4,6],[0,2,3,4,6],[1,2,3,4,6],[0,1,2,5,6],[0,1,3,5,6],[0,2,3,5,6],[1,2,3,5,6],[0,1,4,5,6],[0,2,4,5,6],[1,2,4,5,6],[0,3,4,5,6],[1,3,4,5,6],[2,3,4,5,6],[0,1,2,3,7],[0,1,2,4,7],[0,1,3,4,7],[0,2,3,4,7],[1,2,3,4,7],[0,1,2,5,7],[0,1,3,5,7],[0,2,3,5,7],[1,2,3,5,7],[0,1,4,5,7],[0,2,4,5,7],[1,2,4,5,7],[0,3,4,5,7],[1,3,4,5,7],[2,3,4,5,7],[0,1,2,6,7],[0,1,3,6,7],[0,2,3,6,7],[1,2,3,6,7],[0,1,4,6,7],[0,2,4,6,7],[1,2,4,6,7],[0,3,4,6,7],[1,3,4,6,7],[2,3,4,6,7],[0,1,5,6,7],[0,2,5,6,7],[1,2,5,6,7],[0,3,5,6,7],[1,3,5,6,7],[2,3,5,6,7],[0,4,5,6,7],[1,4,5,6,7],[2,4,5,6,7],[3,4,5,6,7],[0,1,2,3,8],[0,1,2,4,8],[0,1,3,4,8],[0,2,3,4,8],[1,2,3,4,8],[0,1,2,5,8],[0,1,3,5,8],[0,2,3,5,8],[1,2,3,5,8],[0,1,4,5,8],[0,2,4,5,8],[1,2,4,5,8],[0,3,4,5,8],[1,3,4,5,8],[2,3,4,5,8],[0,1,2,6,8],[0,1,3,6,8],[0,2,3,6,8],[1,2,3,6,8],[0,1,4,6,8],[0,2,4,6,8],[1,2,4,6,8],[0,3,4,6,8],[1,3,4,6,8],[2,3,4,6,8],[0,1,5,6,8],[0,2,5,6,8],[1,2,5,6,8],[0,3,5,6,8],[1,3,5,6,8],[2,3,5,6,8],[0,4,5,6,8],[1,4,5,6,8],[2,4,5,6,8],[3,4,5,6,8],[0,1,2,7,8],[0,1,3,7,8],[0,2,3,7,8],[1,2,3,7,8],[0,1,4,7,8],[0,2,4,7,8],[1,2,4,7,8],[0,3,4,7,8],[1,3,4,7,8],[2,3,4,7,8],[0,1,5,7,8],[0,2,5,7,8],[1,2,5,7,8],[0,3,5,7,8],[1,3,5,7,8],[2,3,5,7,8],[0,4,5,7,8],[1,4,5,7,8],[2,4,5,7,8],[3,4,5,7,8],[0,1,6,7,8],[0,2,6,7,8],[1,2,6,7,8],[0,3,6,7,8],[1,3,6,7,8],[2,3,6,7,8],[0,4,6,7,8],[1,4,6,7,8],[2,4,6,7,8],[3,4,6,7,8],[0,5,6,7,8],[1,5,6,7,8],[2,5,6,7,8],[3,5,6,7,8],[4,5,6,7,8],[0,1,2,3,9],[0,1,2,4,9],[0,1,3,4,9],[0,2,3,4,9],[1,2,3,4,9],[0,1,2,5,9],[0,1,3,5,9],[0,2,3,5,9],[1,2,3,5,9],[0,1,4,5,9],[0,2,4,5,9],[1,2,4,5,9],[0,3,4,5,9],[1,3,4,5,9],[2,3,4,5,9],[0,1,2,6,9],[0,1,3,6,9],[0,2,3,6,9],[1,2,3,6,9],[0,1,4,6,9],[0,2,4,6,9],[1,2,4,6,9],[0,3,4,6,9],[1,3,4,6,9],[2,3,4,6,9],[0,1,5,6,9],[0,2,5,6,9],[1,2,5,6,9],[0,3,5,6,9],[1,3,5,6,9],[2,3,5,6,9],[0,4,5,6,9],[1,4,5,6,9],[2,4,5,6,9],[3,4,5,6,9],[0,1,2,7,9],[0,1,3,7,9],[0,2,3,7,9],[1,2,3,7,9],[0,1,4,7,9],[0,2,4,7,9],[1,2,4,7,9],[0,3,4,7,9],[1,3,4,7,9],[2,3,4,7,9],[0,1,5,7,9],[0,2,5,7,9],[1,2,5,7,9],[0,3,5,7,9],[1,3,5,7,9],[2,3,5,7,9],[0,4,5,7,9],[1,4,5,7,9],[2,4,5,7,9],[3,4,5,7,9],[0,1,6,7,9],[0,2,6,7,9],[1,2,6,7,9],[0,3,6,7,9],[1,3,6,7,9],[2,3,6,7,9],[0,4,6,7,9],[1,4,6,7,9],[2,4,6,7,9],[3,4,6,7,9],[0,5,6,7,9],[1,5,6,7,9],[2,5,6,7,9],[3,5,6,7,9],[4,5,6,7,9],[0,1,2,8,9],[0,1,3,8,9],[0,2,3,8,9],[1,2,3,8,9],[0,1,4,8,9],[0,2,4,8,9],[1,2,4,8,9],[0,3,4,8,9],[1,3,4,8,9],[2,3,4,8,9],[0,1,5,8,9],[0,2,5,8,9],[1,2,5,8,9],[0,3,5,8,9],[1,3,5,8,9],[2,3,5,8,9],[0,4,5,8,9],[1,4,5,8,9],[2,4,5,8,9],[3,4,5,8,9],[0,1,6,8,9],[0,2,6,8,9],[1,2,6,8,9],[0,3,6,8,9],[1,3,6,8,9],[2,3,6,8,9],[0,4,6,8,9],[1,4,6,8,9],[2,4,6,8,9],[3,4,6,8,9],[0,5,6,8,9],[1,5,6,8,9],[2,5,6,8,9],[3,5,6,8,9],[4,5,6,8,9],[0,1,7,8,9],[0,2,7,8,9],[1,2,7,8,9],[0,3,7,8,9],[1,3,7,8,9],[2,3,7,8,9],[0,4,7,8,9],[1,4,7,8,9],[2,4,7,8,9],[3,4,7,8,9],[0,5,7,8,9],[1,5,7,8,9],[2,5,7,8,9],[3,5,7,8,9],[4,5,7,8,9],[0,6,7,8,9],[1,6,7,8,9],[2,6,7,8,9],[3,6,7,8,9],[4,6,7,8,9],[5,6,7,8,9],[0,1,2,3,4,5],[0,1,2,3,4,6],[0,1,2,3,5,6],[0,1,2,4,5,6],[0,1,3,4,5,6],[0,2,3,4,5,6],[1,2,3,4,5,6],[0,1,2,3,4,7],[0,1,2,3,5,7],[0,1,2,4,5,7],[0,1,3,4,5,7],[0,2,3,4,5,7],[1,2,3,4,5,7],[0,1,2,3,6,7],[0,1,2,4,6,7],[0,1,3,4,6,7],[0,2,3,4,6,7],[1,2,3,4,6,7],[0,1,2,5,6,7],[0,1,3,5,6,7],[0,2,3,5,6,7],[1,2,3,5,6,7],[0,1,4,5,6,7],[0,2,4,5,6,7],[1,2,4,5,6,7],[0,3,4,5,6,7],[1,3,4,5,6,7],[2,3,4,5,6,7],[0,1,2,3,4,8],[0,1,2,3,5,8],[0,1,2,4,5,8],[0,1,3,4,5,8],[0,2,3,4,5,8],[1,2,3,4,5,8],[0,1,2,3,6,8],[0,1,2,4,6,8],[0,1,3,4,6,8],[0,2,3,4,6,8],[1,2,3,4,6,8],[0,1,2,5,6,8],[0,1,3,5,6,8],[0,2,3,5,6,8],[1,2,3,5,6,8],[0,1,4,5,6,8],[0,2,4,5,6,8],[1,2,4,5,6,8],[0,3,4,5,6,8],[1,3,4,5,6,8],[2,3,4,5,6,8],[0,1,2,3,7,8],[0,1,2,4,7,8],[0,1,3,4,7,8],[0,2,3,4,7,8],[1,2,3,4,7,8],[0,1,2,5,7,8],[0,1,3,5,7,8],[0,2,3,5,7,8],[1,2,3,5,7,8],[0,1,4,5,7,8],[0,2,4,5,7,8],[1,2,4,5,7,8],[0,3,4,5,7,8],[1,3,4,5,7,8],[2,3,4,5,7,8],[0,1,2,6,7,8],[0,1,3,6,7,8],[0,2,3,6,7,8],[1,2,3,6,7,8],[0,1,4,6,7,8],[0,2,4,6,7,8],[1,2,4,6,7,8],[0,3,4,6,7,8],[1,3,4,6,7,8],[2,3,4,6,7,8],[0,1,5,6,7,8],[0,2,5,6,7,8],[1,2,5,6,7,8],[0,3,5,6,7,8],[1,3,5,6,7,8],[2,3,5,6,7,8],[0,4,5,6,7,8],[1,4,5,6,7,8],[2,4,5,6,7,8],[3,4,5,6,7,8],[0,1,2,3,4,9],[0,1,2,3,5,9],[0,1,2,4,5,9],[0,1,3,4,5,9],[0,2,3,4,5,9],[1,2,3,4,5,9],[0,1,2,3,6,9],[0,1,2,4,6,9],[0,1,3,4,6,9],[0,2,3,4,6,9],[1,2,3,4,6,9],[0,1,2,5,6,9],[0,1,3,5,6,9],[0,2,3,5,6,9],[1,2,3,5,6,9],[0,1,4,5,6,9],[0,2,4,5,6,9],[1,2,4,5,6,9],[0,3,4,5,6,9],[1,3,4,5,6,9],[2,3,4,5,6,9],[0,1,2,3,7,9],[0,1,2,4,7,9],[0,1,3,4,7,9],[0,2,3,4,7,9],[1,2,3,4,7,9],[0,1,2,5,7,9],[0,1,3,5,7,9],[0,2,3,5,7,9],[1,2,3,5,7,9],[0,1,4,5,7,9],[0,2,4,5,7,9],[1,2,4,5,7,9],[0,3,4,5,7,9],[1,3,4,5,7,9],[2,3,4,5,7,9],[0,1,2,6,7,9],[0,1,3,6,7,9],[0,2,3,6,7,9],[1,2,3,6,7,9],[0,1,4,6,7,9],[0,2,4,6,7,9],[1,2,4,6,7,9],[0,3,4,6,7,9],[1,3,4,6,7,9],[2,3,4,6,7,9],[0,1,5,6,7,9],[0,2,5,6,7,9],[1,2,5,6,7,9],[0,3,5,6,7,9],[1,3,5,6,7,9],[2,3,5,6,7,9],[0,4,5,6,7,9],[1,4,5,6,7,9],[2,4,5,6,7,9],[3,4,5,6,7,9],[0,1,2,3,8,9],[0,1,2,4,8,9],[0,1,3,4,8,9],[0,2,3,4,8,9],[1,2,3,4,8,9],[0,1,2,5,8,9],[0,1,3,5,8,9],[0,2,3,5,8,9],[1,2,3,5,8,9],[0,1,4,5,8,9],[0,2,4,5,8,9],[1,2,4,5,8,9],[0,3,4,5,8,9],[1,3,4,5,8,9],[2,3,4,5,8,9],[0,1,2,6,8,9],[0,1,3,6,8,9],[0,2,3,6,8,9],[1,2,3,6,8,9],[0,1,4,6,8,9],[0,2,4,6,8,9],[1,2,4,6,8,9],[0,3,4,6,8,9],[1,3,4,6,8,9],[2,3,4,6,8,9],[0,1,5,6,8,9],[0,2,5,6,8,9],[1,2,5,6,8,9],[0,3,5,6,8,9],[1,3,5,6,8,9],[2,3,5,6,8,9],[0,4,5,6,8,9],[1,4,5,6,8,9],[2,4,5,6,8,9],[3,4,5,6,8,9],[0,1,2,7,8,9],[0,1,3,7,8,9],[0,2,3,7,8,9],[1,2,3,7,8,9],[0,1,4,7,8,9],[0,2,4,7,8,9],[1,2,4,7,8,9],[0,3,4,7,8,9],[1,3,4,7,8,9],[2,3,4,7,8,9],[0,1,5,7,8,9],[0,2,5,7,8,9],[1,2,5,7,8,9],[0,3,5,7,8,9],[1,3,5,7,8,9],[2,3,5,7,8,9],[0,4,5,7,8,9],[1,4,5,7,8,9],[2,4,5,7,8,9],[3,4,5,7,8,9],[0,1,6,7,8,9],[0,2,6,7,8,9],[1,2,6,7,8,9],[0,3,6,7,8,9],[1,3,6,7,8,9],[2,3,6,7,8,9],[0,4,6,7,8,9],[1,4,6,7,8,9],[2,4,6,7,8,9],[3,4,6,7,8,9],[0,5,6,7,8,9],[1,5,6,7,8,9],[2,5,6,7,8,9],[3,5,6,7,8,9],[4,5,6,7,8,9],[0,1,2,3,4,5,6],[0,1,2,3,4,5,7],[0,1,2,3,4,6,7],[0,1,2,3,5,6,7],[0,1,2,4,5,6,7],[0,1,3,4,5,6,7],[0,2,3,4,5,6,7],[1,2,3,4,5,6,7],[0,1,2,3,4,5,8],[0,1,2,3,4,6,8],[0,1,2,3,5,6,8],[0,1,2,4,5,6,8],[0,1,3,4,5,6,8],[0,2,3,4,5,6,8],[1,2,3,4,5,6,8],[0,1,2,3,4,7,8],[0,1,2,3,5,7,8],[0,1,2,4,5,7,8],[0,1,3,4,5,7,8],[0,2,3,4,5,7,8],[1,2,3,4,5,7,8],[0,1,2,3,6,7,8],[0,1,2,4,6,7,8],[0,1,3,4,6,7,8],[0,2,3,4,6,7,8],[1,2,3,4,6,7,8],[0,1,2,5,6,7,8],[0,1,3,5,6,7,8],[0,2,3,5,6,7,8],[1,2,3,5,6,7,8],[0,1,4,5,6,7,8],[0,2,4,5,6,7,8],[1,2,4,5,6,7,8],[0,3,4,5,6,7,8],[1,3,4,5,6,7,8],[2,3,4,5,6,7,8],[0,1,2,3,4,5,9],[0,1,2,3,4,6,9],[0,1,2,3,5,6,9],[0,1,2,4,5,6,9],[0,1,3,4,5,6,9],[0,2,3,4,5,6,9],[1,2,3,4,5,6,9],[0,1,2,3,4,7,9],[0,1,2,3,5,7,9],[0,1,2,4,5,7,9],[0,1,3,4,5,7,9],[0,2,3,4,5,7,9],[1,2,3,4,5,7,9],[0,1,2,3,6,7,9],[0,1,2,4,6,7,9],[0,1,3,4,6,7,9],[0,2,3,4,6,7,9],[1,2,3,4,6,7,9],[0,1,2,5,6,7,9],[0,1,3,5,6,7,9],[0,2,3,5,6,7,9],[1,2,3,5,6,7,9],[0,1,4,5,6,7,9],[0,2,4,5,6,7,9],[1,2,4,5,6,7,9],[0,3,4,5,6,7,9],[1,3,4,5,6,7,9],[2,3,4,5,6,7,9],[0,1,2,3,4,8,9],[0,1,2,3,5,8,9],[0,1,2,4,5,8,9],[0,1,3,4,5,8,9],[0,2,3,4,5,8,9],[1,2,3,4,5,8,9],[0,1,2,3,6,8,9],[0,1,2,4,6,8,9],[0,1,3,4,6,8,9],[0,2,3,4,6,8,9],[1,2,3,4,6,8,9],[0,1,2,5,6,8,9],[0,1,3,5,6,8,9],[0,2,3,5,6,8,9],[1,2,3,5,6,8,9],[0,1,4,5,6,8,9],[0,2,4,5,6,8,9],[1,2,4,5,6,8,9],[0,3,4,5,6,8,9],[1,3,4,5,6,8,9],[2,3,4,5,6,8,9],[0,1,2,3,7,8,9],[0,1,2,4,7,8,9],[0,1,3,4,7,8,9],[0,2,3,4,7,8,9],[1,2,3,4,7,8,9],[0,1,2,5,7,8,9],[0,1,3,5,7,8,9],[0,2,3,5,7,8,9],[1,2,3,5,7,8,9],[0,1,4,5,7,8,9],[0,2,4,5,7,8,9],[1,2,4,5,7,8,9],[0,3,4,5,7,8,9],[1,3,4,5,7,8,9],[2,3,4,5,7,8,9],[0,1,2,6,7,8,9],[0,1,3,6,7,8,9],[0,2,3,6,7,8,9],[1,2,3,6,7,8,9],[0,1,4,6,7,8,9],[0,2,4,6,7,8,9],[1,2,4,6,7,8,9],[0,3,4,6,7,8,9],[1,3,4,6,7,8,9],[2,3,4,6,7,8,9],[0,1,5,6,7,8,9],[0,2,5,6,7,8,9],[1,2,5,6,7,8,9],[0,3,5,6,7,8,9],[1,3,5,6,7,8,9],[2,3,5,6,7,8,9],[0,4,5,6,7,8,9],[1,4,5,6,7,8,9],[2,4,5,6,7,8,9],[3,4,5,6,7,8,9],[0,1,2,3,4,5,6,7],[0,1,2,3,4,5,6,8],[0,1,2,3,4,5,7,8],[0,1,2,3,4,6,7,8],[0,1,2,3,5,6,7,8],[0,1,2,4,5,6,7,8],[0,1,3,4,5,6,7,8],[0,2,3,4,5,6,7,8],[1,2,3,4,5,6,7,8],[0,1,2,3,4,5,6,9],[0,1,2,3,4,5,7,9],[0,1,2,3,4,6,7,9],[0,1,2,3,5,6,7,9],[0,1,2,4,5,6,7,9],[0,1,3,4,5,6,7,9],[0,2,3,4,5,6,7,9],[1,2,3,4,5,6,7,9],[0,1,2,3,4,5,8,9],[0,1,2,3,4,6,8,9],[0,1,2,3,5,6,8,9],[0,1,2,4,5,6,8,9],[0,1,3,4,5,6,8,9],[0,2,3,4,5,6,8,9],[1,2,3,4,5,6,8,9],[0,1,2,3,4,7,8,9],[0,1,2,3,5,7,8,9],[0,1,2,4,5,7,8,9],[0,1,3,4,5,7,8,9],[0,2,3,4,5,7,8,9],[1,2,3,4,5,7,8,9],[0,1,2,3,6,7,8,9],[0,1,2,4,6,7,8,9],[0,1,3,4,6,7,8,9],[0,2,3,4,6,7,8,9],[1,2,3,4,6,7,8,9],[0,1,2,5,6,7,8,9],[0,1,3,5,6,7,8,9],[0,2,3,5,6,7,8,9],[1,2,3,5,6,7,8,9],[0,1,4,5,6,7,8,9],[0,2,4,5,6,7,8,9],[1,2,4,5,6,7,8,9],[0,3,4,5,6,7,8,9],[1,3,4,5,6,7,8,9],[2,3,4,5,6,7,8,9],[0,1,2,3,4,5,6,7,8],[0,1,2,3,4,5,6,7,9],[0,1,2,3,4,5,6,8,9],[0,1,2,3,4,5,7,8,9],[0,1,2,3,4,6,7,8,9],[0,1,2,3,5,6,7,8,9],[0,1,2,4,5,6,7,8,9],[0,1,3,4,5,6,7,8,9],[0,2,3,4,5,6,7,8,9],[1,2,3,4,5,6,7,8,9],[0,1,2,3,4,5,6,7,8,9]]))
	
	# --------------------------------------------------------------------
	
	def test_matrix_product_01(self): self.assertEqual(matrix_product([[1,2,3]], [[4],[5],[6]]), [[32]])
	def test_matrix_product_02(self): self.assertEqual(matrix_product([[4],[5],[6]],[[1,2,3]]), [[4, 8, 12], [5, 10, 15], [6, 12, 18]])
	def test_matrix_product_03(self): self.assertEqual(matrix_product([[1,2],[3,4]] , [[5,6],[7,8]]),[[19, 22], [43, 50]])
	def test_matrix_product_04(self): self.assertEqual(matrix_product([[1,0],[0,1]], [[5,6],[7,8]]),[[5, 6], [7, 8]])
	def test_matrix_product_05(self): self.assertEqual(matrix_product([[5, 6], [7, 8]], [[1,0],[0,1]]),[[5, 6], [7, 8]])
	def test_matrix_product_06(self): self.assertEqual(matrix_product([[1,2,3]],[[1],[1],[1]]),[[6]])
	def test_matrix_product_07(self): self.assertEqual(matrix_product([[1,2,3]],[[2],[2],[2]]),[[12]])
	def test_matrix_product_08(self): self.assertEqual(matrix_product([[1],[2],[3]],[[1,1,1]]),[[1, 1, 1], [2, 2, 2], [3, 3, 3]])
	def test_matrix_product_09(self): self.assertEqual(matrix_product([[1],[2],[3]],[[0,0,0]]),[[0, 0, 0], [0, 0, 0], [0, 0, 0]])
	def test_matrix_product_10(self): self.assertEqual(matrix_product([[1,2],[3,4]],[[1,0,0],[0,1,0]]),[[1, 2, 0], [3, 4, 0]])
	def test_matrix_product_11(self): self.assertEqual(matrix_product([[1,2],[3,4],[5,6]],[[1,0,0],[0,1,0]]),[[1, 2, 0], [3, 4, 0], [5, 6, 0]])
	def test_matrix_product_12(self): self.assertEqual(matrix_product([[1,2,3,4,5]],[[6,7,8]]),None)

	# --------------------------------------------------------------------
	
	############################################################################
	
# This class digs through AllTests, counts and builds all the tests,
# so that we have an entire test suite that can be run as a group.
class TheTestSuite (unittest.TestSuite):
	# constructor.
	def __init__(self,wants):
		self.num_req = 0
		self.num_ec = 0
		# find all methods that begin with "test".
		fs = []
		for w in wants:
			for func in AllTests.__dict__:
				# append regular tests
				# drop any digits from the end of str(func).
				dropnum = str(func)
				while dropnum[-1] in "1234567890":
					dropnum = dropnum[:-1]
				
				if dropnum==("test_"+w+"_") and (not (dropnum==("test_extra_credit_"+w+"_"))):
					fs.append(AllTests(str(func)))
				if dropnum==("test_extra_credit_"+w+"_") and not BATCH_MODE:
					fs.append(AllTests(str(func)))
		
#		print("TTS ====> ",list(map(lambda f: (f,id(f)),fs)))
		# call parent class's constructor.
		unittest.TestSuite.__init__(self,fs)

class TheExtraCreditTestSuite (unittest.TestSuite):
		# constructor.
		def __init__(self,wants):
			# find all methods that begin with "test_extra_credit_".
			fs = []
			for w in wants:
				for func in AllTests.__dict__:
					if str(func).startswith("test_extra_credit_"+w):
						fs.append(AllTests(str(func)))
		
#			print("TTS ====> ",list(map(lambda f: (f,id(f)),fs)))
			# call parent class's constructor.
			unittest.TestSuite.__init__(self,fs)

# all (non-directory) file names, regardless of folder depth,
# under the given directory 'dir'.
def files_list(dir):
	this_file = __file__
	if dir==".":
		dir = os.getcwd()
	info = os.walk(dir)
	filenames = []
	for (dirpath,dirnames,filez) in info:
#		print(dirpath,dirnames,filez)
		if dirpath==".":
			continue
		for file in filez:
			if file==this_file:
				continue
			filenames.append(os.path.join(dirpath,file))
#		print(dirpath,dirnames,filez,"\n")
	return filenames

def main():
	if len(sys.argv)<2:
		raise Exception("needed student's file name as command-line argument:"\
			+"\n\t\"python3 testerX.py gmason76_2xx_Px.py\"")
	
	if BATCH_MODE:
		print("BATCH MODE.\n")
		run_all()
		return
		
	else:
		want_all = len(sys.argv) <=2
		wants = []
		# remove batch_mode signifiers from want-candidates.
		want_candidates = sys.argv[2:]
		for i in range(len(want_candidates)-1,-1,-1):
			if want_candidates[i] in ['.'] or os.path.isdir(want_candidates[i]):
				del want_candidates[i]
	
		# set wants and extra_credits to either be the lists of things they want, or all of them when unspecified.
		wants = []
		extra_credits = []
		if not want_all:
			for w in want_candidates:
				if w in REQUIRED_DEFNS:
					wants.append(w)
				elif w in SUB_DEFNS:
					wants.append(w)
				elif w in EXTRA_CREDIT_DEFNS:
					extra_credits.append(w)
				else:
					raise Exception("asked to limit testing to unknown function '%s'."%w)
		else:
			wants = REQUIRED_DEFNS + SUB_DEFNS
			extra_credits = EXTRA_CREDIT_DEFNS
		
		# now that we have parsed the function names to test, run this one file.	
		run_one(wants,extra_credits)	
		return
	return # should be unreachable!	

# only used for non-batch mode, since it does the printing.
# it nicely prints less info when no extra credit was attempted.
def run_one(wants, extra_credits):
	
	has_reqs = len(wants)>0
	has_ec   = len(extra_credits)>0
	
	# make sure they exist.
	passed1 = 0
	passed2 = 0
	tried1 = 0
	tried2 = 0
	
	# only run tests if needed.
	if has_reqs:
		print("\nRunning required definitions:")
		(tag, passed1,tried1) = run_file(sys.argv[1],wants,False)
	if has_ec:
		print("\nRunning extra credit definitions:")
		(tag, passed2,tried2) = run_file(sys.argv[1],extra_credits,True)
	
	# print output based on what we ran.
	if has_reqs and not has_ec:
		print("\n%d/%d Required test cases passed (worth %d each)" % (passed1,tried1,weight_required) )
		print("\nScore based on test cases: %.2f/%d (%.2f*%d) " % (
																passed1*weight_required, 
																total_points_from_tests,
																passed1,
																weight_required
															 ))
	elif has_ec and not has_reqs:
		print("%d/%d Extra credit test cases passed (worth %d each)" % (passed2, tried2, weight_extra_credit))
	else: # has both, we assume.
		print("\n%d / %d Required test cases passed (worth %d each)" % (passed1,tried1,weight_required) )
		print("%d / %d Extra credit test cases passed (worth %d each)" % (passed2, tried2, weight_extra_credit))
		print("\nScore based on test cases: %.2f / %d ( %d * %d + %d * %d) " % (
																passed1*weight_required+passed2*weight_extra_credit, 
																total_points_from_tests,
																passed1,
																weight_required,
																passed2,
																weight_extra_credit
															 ))
	if CURRENTLY_GRADING:
		print("( %d %d %d %d )\n%s" % (passed1,tried1,passed2,tried2,tag))

# only used for batch mode.
def run_all():
		filenames = files_list(sys.argv[1])
		#print(filenames)
		
		wants = REQUIRED_DEFNS + SUB_DEFNS
		extra_credits = EXTRA_CREDIT_DEFNS
		
		results = []
		for filename in filenames:
			print(" Batching on : " +filename)
			# I'd like to use subprocess here, but I can't get it to give me the output when there's an error code returned... TODO for sure.
			lines = os.popen("python3 tester1p.py \""+filename+"\"").readlines()
			
			# delay of shame...
			time.sleep(DELAY_OF_SHAME)
			
			name = os.path.basename(lines[-1])
			stuff =lines[-2].split(" ")[1:-1]
			print("STUFF: ",stuff, "LINES: ", lines)
			(passed_req, tried_req, passed_ec, tried_ec) = stuff
			results.append((lines[-1],int(passed_req), int(tried_req), int(passed_ec), int(tried_ec)))
			continue
		
		print("\n\n\nGRAND RESULTS:\n")
		
			
		for (tag_req, passed_req, tried_req, passed_ec, tried_ec) in results:
			name = os.path.basename(tag_req).strip()
			earned   = passed_req*weight_required + passed_ec*weight_extra_credit
			possible = tried_req *weight_required # + tried_ec *weight_extra_credit
			print("%10s : %3d / %3d = %5.2d %% (%d/%d*%d + %d/%d*%d)" % (
															name,
															earned,
															possible, 
															(earned/possible)*100,
															passed_req,tried_req,weight_required,
															passed_ec,tried_ec,weight_extra_credit
														  ))
# only used for batch mode.
def run_all_orig():
		filenames = files_list(sys.argv[1])
		#print(filenames)
		
		wants = REQUIRED_DEFNS + SUB_DEFNS
		extra_credits = EXTRA_CREDIT_DEFNS
		
		results = []
		for filename in filenames:
			# wipe out all definitions between users.
			for fn in REQUIRED_DEFNS+EXTRA_CREDIT_DEFNS	:
				globals()[fn] = decoy(fn)
				fn = decoy(fn)
			try:
				name = os.path.basename(filename)
				print("\n\n\nRUNNING: "+name)
				(tag_req, passed_req, tried_req) = run_file(filename,wants,False)
				(tag_ec,  passed_ec,  tried_ec ) = run_file(filename,extra_credits,True)
				results.append((tag_req,passed_req,tried_req,tag_ec,passed_ec,tried_ec))
				print(" ###### ", results)
			except SyntaxError as e:
				tag = filename+"_SYNTAX_ERROR"
				results.append((tag,0,len(wants),tag,0,len(extra_credits)))
			except NameError as e:
				tag =filename+"_Name_ERROR"
				results.append((tag,0,len(wants),tag,0,len(extra_credits)))
			except ValueError as e:
				tag = filename+"_VALUE_ERROR"
				results.append((tag,0,len(wants),tag,0,len(extra_credits)))
			except TypeError as e:
				tag = filename+"_TYPE_ERROR"
				results.append((tag,0,len(wants),tag,0,len(extra_credits)))
			except ImportError as e:
				tag = filename+"_IMPORT_ERROR_TRY_AGAIN"
				results.append((tag,0,len(wants),tag,0,len(extra_credits)))
			except Exception as e:
				tag = filename+str(e.__reduce__()[0])
				results.append((tag,0,len(wants),tag,0,len(extra_credits)))
		
# 			try:
# 				print("\n |||||||||| scrupe: "+str(scruples))
# 			except Exception as e:
# 				print("NO SCRUPE.",e)
# 			scruples = None
		
		print("\n\n\nGRAND RESULTS:\n")
		for (tag_req, passed_req, tried_req, tag_ec, passed_ec, tried_ec) in results:
			name = os.path.basename(tag_req)
			earned   = passed_req*weight_required + passed_ec*weight_extra_credit
			possible = tried_req *weight_required # + tried_ec *weight_extra_credit
			print("%10s : %3d / %3d = %5.2d %% (%d/%d*%d + %d/%d*%d)" % (
															name,
															earned,
															possible, 
															(earned/possible)*100,
															passed_req,tried_req,weight_required,
															passed_ec,tried_ec,weight_extra_credit
														  ))

def try_copy(filename1, filename2, numTries):
	have_copy = False
	i = 0
	while (not have_copy) and (i < numTries):
		try:
			# move the student's code to a valid file.
			shutil.copy(filename1,filename2)
			
			# wait for file I/O to catch up...
			if(not wait_for_access(filename2, numTries)):
				return False
				
			have_copy = True
		except PermissionError:
			print("Trying to copy "+filename1+", may be locked...")
			i += 1
			time.sleep(1)
		except BaseException as e:
			print("\n\n\n\n\n\ntry-copy saw: "+e)
	
	if(i == numTries):
		return False
	return True

def try_remove(filename, numTries):
	removed = False
	i = 0
	while os.path.exists(filename) and (not removed) and (i < numTries):
		try:
			os.remove(filename)
			removed = True
		except OSError:
			print("Trying to remove "+filename+", may be locked...")
			i += 1
			time.sleep(1)
	if(i == numTries):
		return False
	return True

def wait_for_access(filename, numTries):
	i = 0
	while (not os.path.exists(filename) or not os.access(filename, os.R_OK)) and i < numTries:
		print("Waiting for access to "+filename+", may be locked...")
		time.sleep(1)
		i += 1
	if(i == numTries):
		return False
	return True

# this will group all the tests together, prepare them as 
# a test suite, and run them.
def run_file(filename,wants=None,checking_ec = False):
	if wants==None:
		wants = []
	
	# move the student's code to a valid file.
	if(not try_copy(filename,"student.py", 5)):
		print("Failed to copy " + filename + " to student.py.")
		quit()
		
	# import student's code, and *only* copy over the expected functions
	# for later use.
	import importlib
	count = 0
	while True:
		try:
# 			print("\n\n\nbegin attempt:")
			while True:
				try:
					f = open("student.py","a")
					f.close()
					break
				except:
					pass
# 			print ("\n\nSUCCESS!")
				
			import student
			importlib.reload(student)
			break
		except ImportError as e:
			print("import error getting student... trying again. "+os.getcwd(), os.path.exists("student.py"),e)
			time.sleep(0.5)
			while not os.path.exists("student.py"):
				time.sleep(0.5)
			count+=1
			if count>3:
				raise ImportError("too many attempts at importing!")
		except SyntaxError as e:
			print("SyntaxError in "+filename+":\n"+str(e))
			print("Run your file without the tester to see the details")
			return(filename+"_SYNTAX_ERROR",None, None, None)
		except NameError as e:
			print("NameError in "+filename+":\n"+str(e))
			print("Run your file without the tester to see the details")
			return((filename+"_Name_ERROR",0,1))	
		except ValueError as e:
			print("ValueError in "+filename+":\n"+str(e))
			print("Run your file without the tester to see the details")
			return(filename+"_VALUE_ERROR",0,1)
		except TypeError as e:
			print("TypeError in "+filename+":\n"+str(e))
			print("Run your file without the tester to see the details")
			return(filename+"_TYPE_ERROR",0,1)
		except ImportError as e:			
			print("ImportError in "+filename+":\n"+str(e))
			print("Run your file without the tester to see the details or try again")
			return((filename+"_IMPORT_ERROR_TRY_AGAIN	",0,1))	
		except Exception as e:
			print("Exception in loading"+filename+":\n"+str(e))
			print("Run your file without the tester to see the details")
			return(filename+str(e.__reduce__()[0]),0,1)
	
	# make a global for each expected definition.
	for fn in REQUIRED_DEFNS+EXTRA_CREDIT_DEFNS	:
		globals()[fn] = decoy(fn)
		try:
			globals()[fn] = getattr(student,fn)
		except:
			if fn in wants:
				print("\nNO DEFINITION FOR '%s'." % fn)	
	
	if not checking_ec:
		# create an object that can run tests.
		runner = unittest.TextTestRunner()
	
		# define the suite of tests that should be run.
		suite = TheTestSuite(wants)
	
	
		# let the runner run the suite of tests.
		ans = runner.run(suite)
		num_errors   = len(ans.__dict__['errors'])
		num_failures = len(ans.__dict__['failures'])
		num_tests    = ans.__dict__['testsRun']
		num_passed   = num_tests - num_errors - num_failures
		# print(ans)
	
	else:
		# do the same for the extra credit.
		runner = unittest.TextTestRunner()
		suite = TheExtraCreditTestSuite(wants)
		ans = runner.run(suite)
		num_errors   = len(ans.__dict__['errors'])
		num_failures = len(ans.__dict__['failures'])
		num_tests    = ans.__dict__['testsRun']
		num_passed   = num_tests - num_errors - num_failures
		#print(ans)
	
	# remove our temporary file.
	os.remove("student.py")
	if os.path.exists("__pycache__"):
		shutil.rmtree("__pycache__")
	if(not try_remove("student.py", 5)):
		print("Failed to remove " + filename + " to student.py.")
	
	tag = ".".join(filename.split(".")[:-1])
	
	
	return (tag, num_passed, num_tests)


# make a global for each expected definition.
def decoy(name):
		# this can accept any kind/amount of args, and will print a helpful message.
		def failyfail(*args, **kwargs):
			return ("<no '%s' definition was found - missing, or typo perhaps?>" % name)
		return failyfail

# this determines if we were imported (not __main__) or not;
# when we are the one file being run, perform the tests! :)
if __name__ == "__main__":
	main()
