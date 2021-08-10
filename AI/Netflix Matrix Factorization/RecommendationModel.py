import pandas as pd
import numpy as np
import DataLoader

class RecommendationModel:

    def __init__(self, features):
        self.features = features
        self.dl = DataLoader.DataLoader()
        self.users = self.dl.getUsers()

        self.lr = 0.01
        self.epochs = 5

        random_state = 1         #randomize user and movie vectors
        rgen = np.random.RandomState(random_state)
        self.user_vector = []
        self.movie_vector = []
        for user in self.users:
            random_vector = rgen.normal(loc=1, scale=1, size=(features))
            self.user_vector.append(random_vector)

        for movie in range(1000):
            random_vector = rgen.normal(loc=1, scale=1, size=(features))
            self.movie_vector.append(random_vector)

        self.file_name = self.dl.getFileName()
        self.file_data = self.dl.getFileData()

        self.average_rating = self.dl.getAverageMovieRatings()

        self.all_users = self.dl.getAllUsers()
        self.all_ratings = self.dl.getAllRatings()




    def fit(self):
        print("fitting...\n")
        print("Err: ")

        for i in range(self.epochs):
            print(self.calculateErr())


            for movie_index in range(1000):
                m_vector = self.movie_vector[movie_index]
                average_rating = self.average_rating[movie_index]
                user_column = self.all_users[movie_index]
                rating_column = self.all_ratings[movie_index]
                row_index = 0
                for user in user_column:
                    user_rating = rating_column[row_index]
                    user_index = self.users[user]
                    u_vector = self.user_vector[user_index]

                    vector_dot = np.dot(u_vector, m_vector)
                    movie_weight_change = np.dot(vector_dot + average_rating - user_rating, u_vector)
                    user_weight_change = np.dot(vector_dot + average_rating - user_rating, m_vector)

                    movie_gradient = np.multiply(movie_weight_change, 2)
                    movie_gradient = np.divide(movie_gradient, len(user_column))
                    self.movie_vector[movie_index] = np.subtract(self.movie_vector[movie_index], np.multiply(self.lr, movie_gradient))

                    user_gradient = np.multiply(user_weight_change, 2)
                    user_gradient = np.divide(user_gradient, 1000)
                    self.user_vector[user_index] = np.subtract(self.user_vector[user_index], np.multiply(self.lr, user_gradient))

                    row_index += 1


            self.lr = self.lr * 0.9

    def recommend(self, movie):
        movie_index = movie - 1
        input_vector = self.movie_vector[movie_index]

        vector_angles = []
        for i in range(1000):
            compare_vector = self.movie_vector[i]
            cos = np.divide(np.dot(input_vector, compare_vector), np.multiply(np.linalg.norm(input_vector), np.linalg.norm(compare_vector)))
            vector_angles.append(cos)

        ordered_angles = sorted(vector_angles)

        recommendations = []
        for i in range(1000):
            angle_value = ordered_angles[i]
            index = vector_angles.index(angle_value)
            movie_title = index + 1
            recommendations.append(movie_title)

        return recommendations

    def calculateErr(self):
        err = 0
        for movie_index in range(1000):
            err += self.calculateMovieError(movie_index)
        err = np.sqrt(np.divide(err, 1000))

        return err

    def calculateMovieError(self, movie):
        movie_index = movie - 1
        user_category = self.all_users[movie_index]
        rating_category = self.all_ratings[movie_index]

        iterations = (len(user_category))

        error_total = 0
        for i in range(iterations):
            user_index = self.users[user_category[i]]
            u_vector = self.user_vector[user_index]
            m_vector = self.movie_vector[movie_index]

            vector_dot = np.dot(u_vector, m_vector)
            bias = self.average_rating[movie_index]
            actual_rating = rating_category[i]

            error = np.square(vector_dot - bias - actual_rating)
            error_total += error

        error_total = np.divide(error_total, iterations)
        return error_total