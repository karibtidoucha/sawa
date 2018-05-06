import random

def chunk(ngram,ngram2difficulty):
  arr = ngram.split()
  memo = [[-1.0 for i in range(len(arr) + 1)] for j in range(len(arr) + 1)]

  def dp(s, e): # get the minimum number of chunks of arr[s:e]
    if memo[s][e] != -1.0:
      return memo[s][e]
    if e == s + 1:# base case: only one word
      memo[s][e] = 1
    elif '_'.join(arr[s:e]) in ngram2difficulty:
      memo[s][e] = 1
    else:
      ans = len(arr)
      for j in range(s + 1, e):
        ans = min(ans, dp(s, j) + dp(j, e))
      memo[s][e] = ans
    return memo[s][e]
  minimum_number_of_chunks = dp(0, len(arr))
  # now, we need to get the chunking itself
  def trace(s, e): # chunk arr[s:e] into the smallest possible number of chunks
    if dp(s, e) == 1:
      #return [' '.join(arr[s:e])]
       return [[s:e]]
    else:
      for j in range(s + 1, e):
        if dp(s, e) == dp(s, j) + dp(j, e):
          return trace(s, j) + trace(j, e)
    raise ValueError("'dp' works differently from 'trace'")
  return trace(0, len(arr))
