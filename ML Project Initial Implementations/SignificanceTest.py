import pandas as pd
import numpy as np
from sklearn import metrics
from sklearn import tree
from sklearn.neighbors import KNeighborsClassifier
from sklearn.tree import DecisionTreeClassifier
from statsmodels.sandbox.stats.runs import mcnemar
import statsmodels.api as sm
from scipy import stats

from sklearn.model_selection import StratifiedKFold
from sklearn.model_selection import permutation_test_score

Train = pd.read_csv('NewTr.csv', delimiter=';', header=None)
Test = pd.read_csv('Newval.csv', delimiter=';')
trainM = Train.as_matrix()
testM = Test.as_matrix()
ytrain = trainM[:, 0]
Xtrain = trainM[:, 1:]
ytest = testM[:, 0]
Xtest = testM[:, 1:]
print("The mean of the dataset", Xtrain.mean())
print("The Variance of the dataset", Xtrain.var())
destree =     DecisionTreeClassifier()
destree.fit(Xtrain, ytrain)
ydecpred = destree.predict(Xtest)


knn = KNeighborsClassifier(n_neighbors=7)
knn.fit(Xtrain, ytrain)
ypred = knn.predict(Xtest)

X2 = sm.add_constant(Xtrain)
est = sm.OLS(ytrain, X2)
est2 = est.fit()

cv = StratifiedKFold(2)

score, permutation_scores, pvalueDT = permutation_test_score(
    destree, Xtrain, ytrain, scoring="accuracy", cv=cv, n_permutations=100, n_jobs=1)

score, permutation_scores, pvalueKnn = permutation_test_score(
    knn, Xtrain, ytrain, scoring="accuracy", cv=cv, n_permutations=100, n_jobs=1)	
	

print("Classification score for Decision tree algorithm %s (pvalue : %s)" % (score, pvalueDT))
print("Classification score for KNN algorithm %s (pvalue : %s)" % (score, pvalueKnn))


