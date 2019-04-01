import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn import metrics
from sklearn.utils import class_weight
from sklearn.neighbors import KNeighborsClassifier
from sklearn.metrics import classification_report, confusion_matrix
from sklearn.feature_selection import RFE
from sklearn.grid_search import GridSearchCV
from sklearn import cross_validation
from sklearn import neighbors


Train = pd.read_csv('NewTr.csv', delimiter=';', header=None)
Test = pd.read_csv('NewVal.csv', delimiter=';')

trainM = Train.as_matrix()
testM = Test.as_matrix()
ytrain = trainM[:, 0]
Xtrain = trainM[:, 1:]

ytest = testM[:, 0]
Xtest = testM[:, 1:]
nFolds = 4
random_state  = 1234 
metrics       = ['minkowski','euclidean','manhattan'] 
weights       = ['uniform','distance'] #10.0**np.arange(-5,4)
numNeighbors  = np.arange(5,10)
param_grid    = dict(metric=metrics,weights=weights,n_neighbors=numNeighbors)
cv            = cross_validation.StratifiedKFold(ytrain, n_folds = 3)
grid = GridSearchCV(neighbors.KNeighborsClassifier(),param_grid=param_grid,cv=cv)
grid.fit(Xtrain,ytrain)



print("Best accuracy possible and best parameters to achieve them ")

print (grid.best_score_, grid.best_params_)

