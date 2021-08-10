from functools import reduce
import numpy as np
import collections

class NeighborModel:

    def __init__(self, numOfNeighbors):
        self.numOfNeighbors = numOfNeighbors

    def fit(self, data, labels):
        self.data = data
        self.labels = labels
        self.nexamples = len(data)
        self.nfeatures = len(data[0])


    def predict(self, testData):
        nearestNeighbors = self.nearestNeighbors(testData)

        predictions = []
        for i in range(len(testData)):
            r = reduce(lambda x,y : x+y, self.labels[nearestNeighbors[i]])

            if r > 0:
                predictions.append(1)
            else:
                predictions.append(-1)

        return predictions

    def nearestNeighbors(self, testData):

        neighborsMatrix = []
        for i in range(len(testData)):
            row = testData[i]
            pointDistance = {}
            for j in range(self.nexamples):
                distance = np.linalg.norm(row - self.data[j])
                pointDistance[distance] = j
                pointDistance = collections.OrderedDict(sorted(pointDistance.items()))
            closestNeighbors = []
            for n in range(self.numOfNeighbors):
                neighbor = pointDistance.popitem(last=False)
                closestNeighbors.append(neighbor[1])
            neighborsMatrix.append(closestNeighbors)
        np.delete(neighborsMatrix, 0)

        return neighborsMatrix