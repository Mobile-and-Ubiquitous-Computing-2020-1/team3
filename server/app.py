import os, json
from pprint import pprint
from flask import Flask, request, jsonify


app = Flask(__name__)

@app.before_first_request
def init():
    # app.run() 실행 전에 필요한 코드 여기서 작성하기
    # 필요하다면 global variable
    return None

# @app.route('/image', methods=['POST'])
# def image():
#     if request.method == 'POST':
#         f = request.files['files']
#         f.save(f.filename)
#         print("Saving...")
#         return "SAVED"
#     req = request.files.get('imagefile', '')
#     print(request.get_data())
#     print(request.files)
#     request.file
#     return ("Feedback content", "STAGE_NAME")

@app.route('/detHand', methods=['POST'])
def detHand():
    if request.method == 'POST':
        f = request.files['files']
    retVal = {'feedback': 'feedback_string', 'stage': 'stage_name'}
    return jsonify(retVal)

@app.route('/locHand', methods=['POST'])
def locHand():

    retVal = {'feedback': 'feedback_string', 'stage': 'stage_name'}
    return jsonify(retVal)

@app.route('/rotate', methods=['POST'])
def rotate():

    retVal = {'feedback': 'feedback_string', 'stage': 'stage_name'}
    return jsonify(retVal)

@app.route('/flip', methods=['POST'])
def flip():
    
    retVal = {'feedback': 'feedback_string', 'stage': 'stage_name'}
    return jsonify(retVal)

if __name__=='__main__':
    # app.run() # production
    # app.run(debug=True) # for debugging purpose
    app.run(host='0.0.0.0', port=int(os.getenv('PORT', 8080)))