import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn import metrics
from sklearn.utils import class_weight
from sklearn.neighbors import KNeighborsClassifier
from sklearn.metrics import classification_report, confusion_matrix
from imblearn.over_sampling import RandomOverSampler
from imblearn.over_sampling import SMOTE, ADASYN
from sklearn.feature_selection import RFE
from sklearn.dummy import DummyClassifier


Train = pd.read_csv('FinalTrain.csv', delimiter=';', header=None)
Test = pd.read_csv('Newtes.csv', delimiter=';')

trainM = Train.as_matrix()
testM = Test.as_matrix()
ytrain = trainM[:, 0]
Xtrain = trainM[:, 1:]
ytest = testM[:, 0]
Xtest = testM[:, 1:]


#Grid search knn
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



#Class weights are used to resassign the weightage of each class which is being oversampled data 
class_weight = class_weight.compute_class_weight('balanced', np.unique(ytrain), ytrain)

cw ={1: class_weight[0], 2: class_weight[1], 3: class_weight[2], 4: class_weight[3], 5: class_weight[4], 6 : class_weight[5],
  7: class_weight[6], 8:class_weight[7]}

X_resampled, y_resampled = SMOTE(ratio='minority').fit_sample(Xtrain, ytrain)

# Dummy classifier is used for getting baseline accuracy
baseline = DummyClassifier(strategy='stratified', random_state=None, constant=None)
baseline.fit(Xtrain, ytrain)
ybaselinepred = baseline.predict(Xtest)


print("Baseline accuracy Accuracy:%.3f%%" % (metrics.accuracy_score(ytest, ybaselinepred) * 100.0))
print(classification_report(ytest, ybaselinepred))

#Used the best combination of parameters got through grid search 
knn = KNeighborsClassifier(metric= 'minkowski', n_neighbors= 2, weights= 'uniform')
knn.fit(X_resampled, y_resampled)
ypred = knn.predict(Xtest)
print("KNN Accuracy:%.3f%%" % (metrics.accuracy_score(ytest, ypred) * 100.0))
print(classification_report(ytest, ypred))

