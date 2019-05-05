from recommendation import retrieve_most_similar_products


from flask import Flask
from flask_restful import reqparse,  Api, Resource

app = Flask(__name__)
api = Api(app)


parser = reqparse.RequestParser()

class Recommendation(Resource):
    def get(self):
        parser = reqparse.RequestParser()
        parser.add_argument('path', type=str)
        print(parser.parse_args())
        return retrieve_most_similar_products(parser.parse_args()['path'], 10)


api.add_resource(Recommendation, '/recommendation')

if __name__ == '__main__':
    app.run(debug=True)

