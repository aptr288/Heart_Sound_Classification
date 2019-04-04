import pandas as pd
import numpy as np
from sklearn import metrics
from sklearn import tree
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import classification_report, confusion_matrix
from imblearn.over_sampling import RandomOverSampler
from imblearn.over_sampling import SMOTE, ADASYN
from sklearn.feature_selection import RFE
from sklearn.utils import resample

Train = pd.read_csv('NewTr.csv', delimiter=';', header=None)
MajorityClassdata = Train.iloc[0:38] #containing only normal instances 
MinorityClassdata = Train.iloc[39:106]#containing all other abnormal instances 
#Resampling data to  address selection bias
ResampledTrain1 =resample(MajorityClassdata , n_samples = 90, random_state = 1)
ResampledTrain2 =resample(MinorityClassdata, n_samples = 10, random_state = 1)

TrainUpSampled = pd.concat([ResampledTrain1, ResampledTrain2])
Test = pd.read_csv('NewVal.csv', delimiter=';')

trainM = TrainUpSampled.as_matrix()
testM = Test.as_matrix()
ytrain = trainM[:, 0]
Xtrain = trainM[:, 1:]

ytest = testM[:, 0]
Xtest = testM[:, 1:]

destree = tree.DecisionTreeClassifier()
destree.fit(Xtrain, ytrain)
ydecpred = destree.predict(Xtest)



rfc = RandomForestClassifier(n_estimators=100)
rfc.fit(Xtrain, ytrain)
rfc_pred = rfc.predict(Xtest)

print("Decision Tree Accuracy:%.3f%%" % (metrics.accuracy_score(ytest, ydecpred) * 100.0))
print(classification_report(ytest, ydecpred))
print("Random Forest Accuracy:%.3f%%" % (metrics.accuracy_score(ytest, rfc_pred) * 100.0))
print(classification_report(ytest, rfc_pred))

#####################################################################
#Upsampling all the classes manually


#Data with different label are divided manually as shown below 
#data from class 1 to 8 are divided respectively 
Train1 = Train.iloc[0:38]
Train2 = Train.iloc[39:48]
Train3 = Train.iloc[49:55]
Train4 = Train.iloc[56:69]
Train5 = Train.iloc[70:78]
Train6 = Train.iloc[79:93]
Train7 = Train.iloc[94:100]
Train8 = Train.iloc[101:106]

#Each data set representing particular minority class are upsapled to have 30 instances each
ResampledTrain2 =resample(Train2, n_samples = 30, random_state = 1)
ResampledTrain3 =resample(Train3, n_samples = 30, random_state = 1)
ResampledTrain4 =resample(Train4, n_samples = 30, random_state = 1)
ResampledTrain5 =resample(Train5, n_samples = 30, random_state = 1)
ResampledTrain6 =resample(Train6, n_samples = 30, random_state = 1)
ResampledTrain7 =resample(Train7, n_samples = 30, random_state = 1)
ResampledTrain8 =resample(Train8, n_samples = 30, random_state = 1)

#The upsampled data minority class files are concatinated with majority class file 
TrainUpSampled = pd.concat([Train1, ResampledTrain2, ResampledTrain3, ResampledTrain4, ResampledTrain5, ResampledTrain6, ResampledTrain7, ResampledTrain8])
Test = pd.read_csv('NewVal.csv', delimiter=';')



trainM = TrainUpSampled.as_matrix()
testM = Test.as_matrix()
ytrain = trainM[:, 0]
Xtrain = trainM[:, 1:]

ytest = testM[:, 0]
Xtest = testM[:, 1:]

destree = tree.DecisionTreeClassifier()
destree.fit(Xtrain, ytrain)
ydecpred = destree.predict(Xtest)

rfc = RandomForestClassifier(n_estimators=100)
rfc.fit(Xtrain, ytrain)
rfc_pred = rfc.predict(Xtest)

print("Decision Tree Accuracy:%.3f%%" % (metrics.accuracy_score(ytest, ydecpred) * 100.0))
print(classification_report(ytest, ydecpred))
print("Random Forest Accuracy:%.3f%%" % (metrics.accuracy_score(ytest, rfc_pred) * 100.0))
print(classification_report(ytest, rfc_pred))

