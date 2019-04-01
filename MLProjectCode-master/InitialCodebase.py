import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn import metrics
from sklearn import tree
from sklearn.neighbors import KNeighborsClassifier
from sklearn.metrics import classification_report, confusion_matrix
from sklearn.ensemble import RandomForestClassifier
from sklearn import model_selection
from sklearn.linear_model import LogisticRegression
from sklearn.tree import DecisionTreeClassifier
from sklearn.neighbors import KNeighborsClassifier
from sklearn.discriminant_analysis import LinearDiscriminantAnalysis
from sklearn.naive_bayes import GaussianNB
from sklearn.svm import SVC
from sklearn.model_selection import cross_val_score


#The data intital scarce data is used to see the system perforamance 

Train = pd.read_csv('oldtrain.csv', delimiter=';', header=None)
Test = pd.read_csv('oldtest.csv', delimiter=';')

trainM = Train.as_matrix()
testM = Test.as_matrix()
ytrain = trainM[:, 0]
Xtrain = trainM[:, 1:]

ytest = testM[:, 0]
Xtest = testM[:, 1:]


#Decision tree model creation
destree = tree.DecisionTreeClassifier()
destree.fit(Xtrain, ytrain)
ydecpred = destree.predict(Xtest)


#Random forest  model creation
rfc = RandomForestClassifier(n_estimators=100)
rfc.fit(Xtrain, ytrain)
rfc_pred = rfc.predict(Xtest)


#Perameter tuning for checking better K value 
error_rate = []


for i in range(1, 40):
    knn = KNeighborsClassifier(n_neighbors=i)
    knn.fit(Xtrain, ytrain)
    pred_i = knn.predict(Xtest)
    error_rate.append(np.mean(pred_i != ytest))
	
#Plotting the response of the graph between error rate and k value 

plt.figure(figsize=(10, 6))
plt.plot(range(1, 40), error_rate, color='blue', linestyle='dashed', marker='o',
         markerfacecolor='red', markersize=10)
plt.title('Error Rate vs. K Value')
plt.xlabel('K')
plt.ylabel('Error Rate')
plt.show()

#Finally assigning best k value possible to model 
knn = KNeighborsClassifier(n_neighbors=4)
knn.fit(Xtrain, ytrain)
ypred = knn.predict(Xtest)

# Cross validation score to check for perameter tuning 

knnscores = cross_val_score(knn, Xtrain, ytrain, cv=10)
DTscores = cross_val_score(destree, Xtrain, ytrain, cv=10)
RFscores = cross_val_score(rfc, Xtrain, ytrain, cv=10)

print("-----------------------------------------------Initial evaluation with cross validation  ---------------------------------------------------------")
print("-----------------------------------------------//////////////////////////////////////---------------------------------------------------------")

print("KNN Accuracy with 95 percent  confidence interval: %0.2f (+/- %0.2f)" % (knnscores.mean(), knnscores.std() * 2))
print("Decision Tree Accuracy with 95 percent  confidence interval: %0.2f (+/- %0.2f)" % (DTscores.mean(), DTscores.std() * 2))
print("Random Forest Accuracy with 95 percent  confidence interval: %0.2f (+/- %0.2f)" % (RFscores.mean(), RFscores.std() * 2))


print("-----------------------------------------------//////////////////////////////////////---------------------------------------------------------")
print("-----------------------------------------------Final evaluation results on Test data ---------------------------------------------------------")

print("KNN Accuracy:%.3f%%" % (metrics.accuracy_score(ytest, ypred) * 100.0))
# print(confusion_matrix(ytest, ypred))
print(classification_report(ytest, ypred))

print("Decision Tree Accuracy:%.3f%%" % (metrics.accuracy_score(ytest, ydecpred) * 100.0))
print(classification_report(ytest, ydecpred))
print("Random Forest Accuracy:%.3f%%" % (metrics.accuracy_score(ytest, rfc_pred) * 100.0))
print(classification_report(ytest, rfc_pred))












