import os, json
from pprint import pprint
from flask import Flask, request, jsonify


app = Flask(__name__)


@app.route('/')
def hello():
    return 'API Server Test'
    
@app.route('/api/return')
def hello2():
    return 'API Server Test2'

# TODO: OCR CONNECT
# input: image
# output: text

# @app.route('/ocr/', methods=['POST'])
# def ocr():
#     req = request.get_json()

# TODO: OBJECT SEGMENTATION CONNECT
# input: image
# output: location information

if __name__=='__main__':
    # app.run() # production
    # app.run(debug=True) # for debugging purpose
    app.run(host='0.0.0.0', port=int(os.getenv('PORT', 8080)))