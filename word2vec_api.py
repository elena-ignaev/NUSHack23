print("Importing modules... ") 
import pandas as pd 
pd.options.mode.chained_assignment = None
import numpy as np
import nltk
nltk.download('stopwords')
nltk.download('punkt')
nltk.download('omw-1.4')
import gensim
from gensim.models import Word2Vec
from gensim.models import KeyedVectors 
#from sklearn.manifold import TSNE
#from sklearn.feature_extraction.text import TfidfVectorizer # if we're using word vectorization ig 
from nltk.tokenize import word_tokenize
from nltk.stem import WordNetLemmatizer
from nltk.corpus import stopwords

from flask import Flask, request 


#loading stuff

#initialize lemmatizer
print("Initializing lemmatizer and related functions... ") 
lemmatizer = WordNetLemmatizer()

letters = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'] 

#define filter text function using lemmatizer 
def filtertext(text): 
    new_tokens = [] 
    for token in word_tokenize(text): 
        new_tokens.append(lemmatizer.lemmatize(token))
    
    #assign to globally set stopwords to a local set
    stop_words = set(stopwords.words('english')+[''])
    
    #filter the stopwords and non-alphanumeric characters from the token
    filtered_tokens = [''.join(ch for ch in token if ch in letters) for token in new_tokens if not ''.join(ch for ch in token if ch in letters).lower() in stop_words]

    return filtered_tokens 


#Load pre-trained Word2Vec model
print("Loading vectorizer... (This is usually the longest step)") 
vectorizer = KeyedVectors.load_word2vec_format('GoogleNews-vectors-negative300.bin', binary=True)

print("Loading complete!") 

#define function to get word similarities 
def word_similarities(target_word, words): 
    distances = vectorizer.distances(target_word, words) #ordered based on orders of vocabulary it seems
    #return (distances-np.min(distances))/(np.max(distances)-np.min(distances))
    return distances 

def compare(a, b): # a and b are sentences
    a_words = filtertext(a)
    highest_similarities = np.array([0.0 for _ in range(len(a_words))]) 
    b_words = filtertext(b)
    print(a_words) 
    for i in range(len(b_words)):
        scores = word_similarities(b_words[i], a_words)
        highest_similarities[i] = np.max(scores) 
        print(b_words[i], scores)
        print(np.max(scores), highest_similarities[i]) 

    return np.median(highest_similarities) 


app = Flask(__name__) 

@app.route('/', methods=['GET', 'POST'])
def main():
    if (request.method == "GET"): 
        if not ('a' in request.args): return "Enter a", 400
        if not ('b' in request.args): return "Enter b", 400

        a = request.args.get('a')
        b = request.args.get('b')

        return str(compare(a, b)), 200 
    elif (request.method == "POST"): 
        args = request.json 
        if not ('a' in args.keys()): return "Enter a", 400 
        if not ('b' in args.keys()): return "Enter b", 400 

        a = args['a']
        b = args['b']

        return str(compare(a, b)), 200 

if __name__ == "__main__": 
    app.run(debug=True)
