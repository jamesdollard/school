import os
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import NeighborModel

url = os.path.join('https://archive.ics.uci.edu', 'ml', 'machine-learning-databases', 'iris', 'iris.data')

data = pd.read_csv(url, header=None, encoding='utf-8')

dataBin1 = data.iloc[0:15, 0:5]
dataBin2 = data.iloc[15:30, 0:5]
dataBin3 = data.iloc[30:45, 0:5]
dataBin4 = data.iloc[45:60, 0:5]
dataBin5 = data.iloc[60:75, 0:5]
dataBin6 = data.iloc[75:90, 0:5]
dataBin7 = data.iloc[90:105, 0:5]
dataBin8 = data.iloc[105:120, 0:5]
dataBin9 = data.iloc[120:135, 0:5]
dataBin10 = data.iloc[135:150, 0:5]

dataList = [dataBin1, dataBin2, dataBin3, dataBin4, dataBin5, dataBin6,
            dataBin7, dataBin8, dataBin9, dataBin10]

# tenfold cross-validation
for hparameter in range(20):

    neighbors = 1 + hparameter
    success = 0
    total = 0
    nm = NeighborModel.NeighborModel(neighbors)

    for v in range(10):

        testBin = dataList[v]
        trainBin = data.copy()
        for i in range(15):
            trainBin = trainBin.drop(index=(v*15 + i))

        testData = testBin.iloc[:15, 0:4].values
        testLabels = testBin.iloc[:15, 4].values
        trainData = trainBin.iloc[:, 0:4].values
        trainLabels = trainBin.iloc[:, 4].values

        testL = np.where(testLabels == 'Iris-setosa', 1, -1)   # setosa
        trainL = np.where(trainLabels == 'Iris-setosa', 1, -1)
        nm.fit(trainData, trainL)
        predictions = nm.predict(testData)
        if(predictions == list(testL)):
            success += 1
        total += 1

        testL = np.where(testLabels == 'Iris-versicolor', 1, -1)   # versicolor
        trainL = np.where(trainLabels == 'Iris-versicolor', 1, -1)
        nm.fit(trainData, trainL)
        predictions = nm.predict(testData)

        if(predictions == list(testL)):
            success += 1
        total += 1

        testL = np.where(testLabels == 'Iris-virginica', 1, -1)   # virginica
        trainL = np.where(trainLabels == 'Iris-virginica', 1, -1)
        nm.fit(trainData, trainL)
        predictions = nm.predict(testData)
        if(predictions == list(testL)):
            success += 1
        total += 1

    print("Successful tests checking " + str(neighbors) + " neighbors: " + str(success) + "/" + str(total))

