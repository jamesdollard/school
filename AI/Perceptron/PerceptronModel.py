import numpy as np


class PerceptronModel:

    def __init__(self):
        self.random_state = 1


    def fit(self, trainingSet, trainingLabels):
        self.trainingSet = trainingSet
        self.trainingLabels = trainingLabels
        self.nexamples = len(trainingSet)
        self.nfeatures = len(trainingSet[0])

        #find largest vector
        self.r = 0

        for i in range(self.nexamples):

            vectorLength = np.linalg.norm(trainingSet[i])
            if (self.r < vectorLength):
                self.r = vectorLength


        rgen = np.random.RandomState(self.random_state)
        self.w = rgen.normal(loc=0.0, scale = 0.1, size=(trainingSet.shape[1]))
        self.b = rgen.normal(loc=0.0, scale = 0.1, size=1)

        self.learningRate = 1

        self.setHyperplane()

    def setHyperplane(self):

        count = 0
        errors = True
        while(errors):
            errors = False
            for i in range(self.nexamples):

                if (self.trainingLabels[i] * (np.dot(self.w,self.trainingSet[i]) + self.b)) < 0:

                    self.w += self.learningRate * np.dot(self.trainingLabels[i], self.trainingSet[i])
                    self.b += self.learningRate * self.trainingLabels[i] * np.square(self.r)
                    errors = True
                    count += 1

                if (count > 10000):
                    return

    def predictionAccuracy(self, testSet, testLabels):
        success = 0
        failure = 0
        for i in range(len(testSet)):
            if (testLabels[i] * (np.dot(self.w, testSet[i]) + self.b)) < 0:
                failure += 1
            else:
                success += 1

        return success