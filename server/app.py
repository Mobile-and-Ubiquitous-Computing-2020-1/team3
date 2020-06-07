import os, json
from pprint import pprint
from flask import Flask, request, jsonify

import sys
sys.path.append('/root/Edwin/server-android-feedback')
from feedback_algorithm.closeup_fb import *
sys.path.append('/root/Edwin/hand-detection-YoloKeras')
from load_hdmodel import *
from get_interpretation import *
import cv2

app = Flask(__name__)
model_hand = None

@app.before_first_request
def init():
    global model_hand
    model_hand = load_hdmodel()
    
    # model load check code
    '''
    img_path = '/root/Edwin/hand-detection-YoloKeras/hand/5.jpg'
    save_path = './result.png'
    img=cv2.imread(img_path, cv2.IMREAD_COLOR)
    plt.imshow(cv2.cvtColor(img, cv2.COLOR_BGR2RGB))
    preporcessed_img , img_cv = preprocess_img(img)
    plt.imshow(img_cv)
    start = time.time()
    out2 = model_hand.predict(preporcessed_img)[0]
    end = time.time()
    print('prediction time:',end-start)
    print(out2)
    '''
    
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
        f.save('det_'+f.filename)
        
        print('*********file type from app:',type(f))
        print(f)
        img = Image.open(f.stream)
        oven_cv_image = numpy.array(img)
        preprocessed_img , img_cv = preprocess_img(oven_cv_image)
        out2 = model_hand.predict(preprocessed_img)[0]
        results = interpret_output_yolov2(out2, oven_cv_image.shape[1], oven_cv_image.shape[0])
        frame_size = img_cv.shape
        fb_str = generate_closeup_fb(results,frame_size)
        print(results)
        print('*********file type from app:',type(f))
        print(f)
        print(type(fb_str))
        print('fb_str:',fb_str)
    retVal = {'feedback': fb_str, 'stage': 'DETECT_HAND'}
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