import pandas as pd
from recommendation import test_retrieve
import os
import numpy as np
if __name__ == '__main__':
    mat = np.zeros((10, 7))
    table = pd.read_csv("/Users/hawkwood/Downloads/style-color-images/style/style.csv")
    brand = {}
    kind = {}
    table = pd.DataFrame(table)
    result=[]
    brandsize = {"0": 0, "1": 0, "2": 0, "3": 0, "4": 0, "5": 0, "6": 0}
    brandrightsize = {"0": 0, "1": 0, "2": 0, "3": 0, "4": 0, "5": 0, "6": 0}
    kindsize = {"0": 0, "1": 0, "2": 0, "3": 0, "4": 0, "5": 0, "6": 0, "7": 0, "8": 0, "9": 0}
    kindrightsize = {"0": 0, "1": 0, "2": 0, "3": 0, "4": 0, "5": 0, "6": 0, "7": 0, "8": 0, "9": 0}
    imgs_path = "/Users/hawkwood/Downloads/style-color-images/style/"
    files = [imgs_path + x for x in os.listdir(imgs_path) if "png" in x]
    cos_similarities_df = pd.read_csv("/Users/hawkwood/PycharmProjects/styleclassification/cos_similarities_df.csv")
    totalrightbrand = 0
    totalrightkind = 0
    for img in files:
        res = test_retrieve(img, 10, cos_similarities_df)
        tmp = img.split('/')[-1]
        rightBrand = 0
        rightkind = 0
        brandsize[tmp.split('_')[0]] = brandsize[tmp.split('_')[0]] + 1
        kindsize[tmp.split('_')[1]] = kindsize[tmp.split('_')[1]] + 1
        mat[int(tmp.split('_')[1])][int(tmp.split('_')[0])] = mat[int(tmp.split('_')[1])][int(tmp.split('_')[0])]+1
        for r in res:

            #print(r.split('_')[0], tmp.split('_')[0], r.split('_')[1], tmp.split('_')[1])
            if r.split('_')[0] == tmp.split('_')[0]:
                brandrightsize[tmp.split('_')[0]] = brandrightsize[tmp.split('_')[0]] + 1
                rightBrand = rightBrand+1
                totalrightbrand = totalrightbrand+1
            if r.split('_')[1] == tmp.split('_')[1]:
                kindrightsize[tmp.split('_')[1]] = kindrightsize[tmp.split('_')[1]] + 1
                rightkind = rightkind+1
                totalrightkind = totalrightkind+1
        result.append([rightBrand, rightkind])
    print(totalrightkind/(10*len(files)), totalrightbrand/(10*len(files)))
    print(brandsize)
    print(brandrightsize)
    print(kindsize)
    print(kindrightsize)
    print(mat)





