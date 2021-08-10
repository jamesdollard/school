import RecommendationModel
import DataLoader

rm = RecommendationModel.RecommendationModel(10)
rm.fit()
rm.recommend(13) # gives recommendations for lotr from least to most relevant