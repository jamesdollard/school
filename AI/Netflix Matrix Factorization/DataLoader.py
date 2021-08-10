import os
import glob
import pandas as pd
import numpy as np

class DataLoader:

    def __init__(self):
        self.all_ratings = []
        self.all_users = []

        self.movies_rated = []
        self.user_ratings = []
        self.loadData()

        users_df = pd.read_csv("./data/user_list.txt")
        user_list = list(users_df.iloc[:, 1])
        self.users = {}
        for i in range(len(user_list)):
            self.users[user_list[i]] = i

    def getAverageMovieRatings(self):
        average_rating = []
        for movie in self.file_data:
            ratings = list(movie.iloc[:, 1])
            average_rating.append(np.mean(ratings))

        return average_rating

    def getFileName(self):
        return self.file_name

    def getFileData(self):
        return self.file_data

    def getUsers(self):
        return self.users

    def getAllRatings(self):
        return self.all_ratings

    def getAllUsers(self):
        return self.all_users

    def loadData(self):
        path = "./data/"
        file_paths = glob.glob(os.path.join(path, "*.txt"))
        file_paths.remove("./data/movie_titles.txt")
        file_paths.remove("./data/user_list.txt")
        file_paths.sort()

        self.file_name = []
        self.file_data = []
        for path in file_paths:
            name = os.path.splitext(os.path.basename(path))[0]
            data = pd.read_csv(path, sep=",", header=None, skiprows=1)
            self.file_name.append(name)
            self.file_data.append(data)

        for file in self.file_data:
            user_column = list(file.iloc[:, 0])
            rating_column = list(file.iloc[:, 1])
            self.all_users.append(user_column)
            self.all_ratings.append(rating_column)

