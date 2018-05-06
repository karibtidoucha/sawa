from __future__ import division

import ast
#import scripts.sawaFunctions

#import gensim
#from gensim import *
#from gensim.models.word2vec import Word2Vec

import pickle
import argparse
import time
import random
import sys
import os

#from gensim.models.keyedvectors import KeyedVectors, Vocab

#from numpy import array
#from scipy.cluster.vq import vq, kmeans, whiten
#from scipy.spatial.distance import cosine, euclidean
#from scipy.cluster import hierarchy
#from scipy.cluster.hierarchy import linkage
#from sklearn import cluster
#from sklearn.cluster import KMeans 
#import numpy as np
#import scipy
import fileinput
import string
import itertools
from itertools import permutations
import math
#import argparse
#import copy
#from multiprocessing import Pool
#import fastcluster
#from sklearn.neighbors import NearestNeighbors

histFile = 'histograms/hist.RP.lev'

def pickleInDict(fileName):
	pklFile = open(fileName, 'rb')
	dic = pickle.load(pklFile)
	pklFile.close()
	return dic

def pickleOutDict(fileName,dic):
        pklOut = open(fileName, 'wb')
        pickle.dump(dic, pklOut)

#####
#pruned_dic = pickleInDict('models/prunedW2V3.pkl')
#print(pruned_dic['AnA'])
#exit()
####


#w2v = gensim.models.KeyedVectors.load_word2vec_format('models/lev.bin', fvocab=None, binary=True, encoding='utf8', unicode_errors='strict', limit=None)
#morph2freq = pickleInDict('models/morph2freq3.pkl')
#ngram2difficulty = pickleInDict('models/ngram2difficulty3.pkl')

#print(morph2freq['AnA'])
#print(ngram2difficulty['mA'])
#exit()

def python2_export_as_str(fileName):
	dic = pickleInDict(fileName)
	f = open(fileName + '.txt', 'w')
	f.write(str(dic))
#ngram2difficulty = pickleInDict('models/ngram2difficulty.pkl')
#f = open('models/ngram2difficulty.pkl.txt', 'w')
#f.write(str(ngram2difficulty))
#exit()

def python3_import_str(fileName):
	print(fileName)
	f = open(fileName, 'r')
	dic = ast.literal_eval(f.read())
	pickleOutDict(fileName[0:len(fileName) - 8] + '3.pkl', dic)
	f.close()
	del dic
	print(fileName)
#f = open('models/ngram2difficulty.pkl.txt', 'r')
#ngram2difficulty = ast.literal_eval(f.read())
#pickleOutDict('models/ngram2difficulty3.pkl', ngram2difficulty)
#exit()

#for dic in ['definitions.pkl', 'ngrams2sents.pkl', 'short_phrase_occurences.pkl', 'word2stem.pkl']:
for dic in ['word2stem.pkl']:
	python3_import_str('models/' + dic + '.txt')
	del dic
exit()



def getStem(nGram):
	nGram = nGram.replace(' ', '_')
	if len(nGram.split()) > 1:
		return None
	else:
		try:
			return word2stem[nGram]
		except:
			minDiff = 1
			stem = None
			for n in range(len(nGram)-1,0,-1): # length of ngram
				for i in range(0,len(nGram)): # starting point of ngram
					if i + n < len(nGram):
						potMorf = nGram[i:i+n]

						# look for a substring as a word
						if potMorf in morph2freq:
							diff = 1/morph2freq[potMorf]
							if diff < minDiff:
								minDiff = diff
								stem = potMorf
						# look for a suffix
						if i > 0 and '+'+potMorf in morph2freq:
							diff = 1/morph2freq['+'+potMorf]
							if diff < minDiff:
								minDiff = diff
								stem = potMorf
							# look for a medial morpheme
							if i + n < len(nGram) -1 and '+'+potMorf+'+' in morph2freq:
								diff = 1/morph2freq['+'+potMorf+'+']
								if diff < minDiff:
									minDiff = diff
									stem = potMorf
						# look for a prefix
						elif i + n < len(nGram) -1 and potMorf+'+' in morph2freq:
							diff = 1/morph2freq[potMorf+'+']
							if diff < minDiff:
								minDiff = diff
								stem = potMorf

				if minDiff != 1:
					return stem

			return stem
no_nn_counter = 0
def getNNs(s):
	stem = getStem(s)
	mostSimilarWords = []
	if s not in w2v.vocab:
		no_nn_counter += 1
		print(str(no_nn_counter), s)
		return ['SORRY, NO KNOWN SIMILAR WORDS']
	else:
		ms = w2v.similar_by_word(s,5)
		for x in ms:
			NN = x[0].replace('_',' ')
			if len(NN.split()) > 1:
				mostSimilarWords.append(NN)
			else:
				NNstem = getStem(NN)
				if NNstem != stem or NNstem == None:
					mostSimilarWords.append(NN)
			if len(mostSimilarWords) == 3:
				break
		return mostSimilarWords

prunedW2V = {}
counter = 0
for line in fileinput.input(histFile):
	counter += 1
	print(counter)
	count = float(line.split()[0])
	word = line.split()[1]
	outList = getNNs(word)
	prunedW2V[word] = outList
	if count <= 15:
		break
fileinput.close()
pickleOutDict('prunedW2V3.pkl',prunedW2V)

