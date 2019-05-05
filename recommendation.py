'''
cited https://www.kaggle.com/jonathanoheix/product-recommendation-based-on-visual-similarity

It is also open source on github
'''
from keras.applications import vgg16
from keras.preprocessing.image import load_img,img_to_array
from keras.models import Model
from keras.applications.imagenet_utils import preprocess_input
import os
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity
import cv2
import pandas as pd
import redis
import zlib
def retrieve_most_similar_products(given_img, nb_closest_images):
    redisConn = redis.StrictRedis(host='localhost', port=6379, db=0)
    if redisConn.get(given_img) is not None:
        cos_similarities_df = pd.read_msgpack(redisConn.get(given_img))
    else:
        cos_similarities_df = pd.read_csv("/Users/hawkwood/PycharmProjects/styleclassification/cos_similarities_df.csv")
        cos_similarities_df = cos_similarities_df.loc[list(cos_similarities_df.columns).index(given_img)-1]
        redisConn.set(given_img, cos_similarities_df.to_msgpack(compress='zlib'))


    # print("-----------------------------------------------------------------------")
    # print("original product:")
    #
    # print(given_img)
    #

    # print("-----------------------------------------------------------------------")
    # print("most similar products:")
    tmp = dict(cos_similarities_df)
    del tmp['Unnamed: 0']
    closest_imgs = sorted(tmp.items(),key=lambda x:x[1])[-1-nb_closest_images:-1]
    result=[]
    for i in range(0, len(closest_imgs)):
        print(closest_imgs[i][0])
        result.append(closest_imgs[i][0])
    return result



def test_retrieve(given_img, nb_closest_images, cos_similarities_df):
    cos_similarities_df = cos_similarities_df.loc[list(cos_similarities_df.columns).index(given_img)-1]
    tmp = dict(cos_similarities_df)
    del tmp['Unnamed: 0']
    closest_imgs = sorted(tmp.items(), key=lambda x: x[1])[-1-nb_closest_images:-1]
    result = []
    for i in range(0, len(closest_imgs)):
        result.append(str(closest_imgs[i][0]).split('/')[-1])
    return result


if __name__ == '__main__':
    imgs_path = "/Users/hawkwood/Downloads/style-color-images/style/"
    imgs_model_width, imgs_model_height = 224, 224

    nb_closest_images = 10 # number of most similar images to retrieve

    vgg_model = vgg16.VGG16(weights='imagenet')

    feat_extractor = Model(inputs=vgg_model.input, outputs=vgg_model.get_layer("fc2").output)

    files = [imgs_path + x for x in os.listdir(imgs_path) if "png" in x]
    print (files[1])

    print("number of images:",len(files))

    original = cv2.imread(files[0])
    original = cv2.resize(original, (224, 224))
    numpy_image = np.array(original)

    image_batch = np.expand_dims(numpy_image, axis=0)
    print('image batch size', image_batch.shape)

    # prepare the image for the VGG model
    processed_image = preprocess_input(image_batch.copy())

    img_features = feat_extractor.predict(processed_image)

    print("features successfully extracted!")
    print("number of image features:",img_features.size)
    importedImages = []

    for f in files:
        filename = f
        original = cv2.imread(filename)
        original = cv2.resize(original, (224, 224))
        numpy_image = np.array(original)
        image_batch = np.expand_dims(numpy_image, axis=0)

        importedImages.append(image_batch)

    images = np.vstack(importedImages)

    processed_imgs = preprocess_input(images.copy())

    imgs_features = feat_extractor.predict(processed_imgs)

    cosSimilarities = cosine_similarity(imgs_features)

    cos_similarities_df = pd.DataFrame(cosSimilarities, columns=files, index=files)
    cos_similarities_df.to_csv("cos_similarities_df.csv")
    retrieve_most_similar_products(files[1], 10)