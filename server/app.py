import os, json
from pprint import pprint
from flask import Flask, request, jsonify


app = Flask(__name__)

@app.before_first_request
def init():
    # app.run() 실행 전에 필요한 코드 여기서 작성하기
    # 필요하다면 global variable

# TODO: IMAGE CONNECT
# input: image
# output: text

# 나중에 아래 주석처리한 걸로 세개로 나누어 질 예정
@app.route('/image', methods=['POST'])
def image():
    if request.method == 'POST':
        f = request.files['files']
        f.save(f.filename)
        print("Saving...")
        return "SAVED"
    req = request.files.get('imagefile', '')
    print(request.get_data())
    print(request.files)
    request.file
    return ("DATA: ?")

"""
@app.route('/ObjDet', methods=['POST'])
def objDet():
    # 사진 받아 오브젝트 디텍션 후 결과 리턴 (성공 결과 혹은 피드백 결과)
    return "objDet"

@app.route('/TableDet', methods=['POST'])
def image():
    # 사진 받아 테이블 디텍션 후 결과 리턴 (성공 결과 혹은 피드백 결과)
    return "TableDet"

@app.route('/OCR', methods=['POST'])
def OCR():
    # 사진 받아 OCR 후 요약 결과 리턴
    return "OCR"
"""

if __name__=='__main__':
    # app.run() # production
    # app.run(debug=True) # for debugging purpose
    app.run(host='0.0.0.0', port=int(os.getenv('PORT', 8080)))