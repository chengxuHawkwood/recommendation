import redis
import os
import pandas as pd
if __name__ == '__main__':
    cos_similarities_df = pd.read_csv("/Users/hawkwood/PycharmProjects/styleclassification/cos_similarities_df.csv")
    imgs_path = "/Users/hawkwood/Downloads/style-color-images/style/"
    redisConn = redis.StrictRedis(host='localhost', port=6379, db=0)
    files = [imgs_path + x for x in os.listdir(imgs_path) if "png" in x]
    for f in files:
        tmp = cos_similarities_df.loc[list(cos_similarities_df.columns).index(f) - 1]
        redisConn.set(f, tmp.to_msgpack(compress='zlib'))