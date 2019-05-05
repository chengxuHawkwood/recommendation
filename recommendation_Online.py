from recommendation import retrieve_most_similar_products
import sys
if __name__ == '__main__':
    retrieve_most_similar_products(sys.argv[1], 10)