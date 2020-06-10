import os, json
from pprint import pprint
from flask import Flask, request, jsonify
import tensorflow as tf

import sys
sys.path.append('/root/Edwin/server-android-feedback')
from feedback_algorithm.closeup_fb import *
from feedback_algorithm.locateframe_fb import *
sys.path.append('/root/Edwin/hand-detection-YoloKeras')
from load_hdmodel import *
from get_interpretation import *
from feedback_algorithm.closeup_fb import *
sys.path.append('/root/Edwin/server')
from server_breakdown import *
import cv2

sys.path.append('/root/team3/off-nutrition-table-extractor/nutrition_extractor')
from detection_server import *
sys.path.append('/root/team3/OCR')
from ocr_server import *
import matplotlib

app = Flask(__name__)
model_hand = None

def load_model():
    global model_hand
    model_hand = load_hdmodel()
    global graph
    graph = tf.get_default_graph()
    
@app.before_first_request
def init():
    #global model_hand
    #model_hand = load_hdmodel()

    # model load check code
    # app.run() 실행 전에 필요한 코드 여기서 작성하기
    # 필요하다면 global variable
    load_model()
    table_load_model()

    return None

def get_controlHand(preprocessed_img,retVal,open_cv_image, img_cv):
    with graph.as_default():
        start = time.time()
        out2 = model_hand.predict(preprocessed_img)[0]
        end = time.time()
        print('prediction time:\t', round(end-start, 3))
    results = interpret_output_yolov2(out2, open_cv_image.shape[1], open_cv_image.shape[0])
    frame_size = img_cv.shape
    fb_str = generate_closeup_fb(results,frame_size) # 'CLOSE', 'FAR' 
    if fb_str == 'Stage 1 clear':
        fb_str = generate_locateframe_fb(results,frame_size) # 'LEFT', 'RIGHT' , 'DOWN' ,'UP', 'location clear'
    print('stage 1 fb_str:\t',fb_str)        
    retVal = {'feedback': fb_str, 'stage': 'DETECT_HAND'}
    if fb_str == 'location clear':
        retVal['stage'] = "ROTATE"
    print('Final response in contorl Hand:\t',retVal)
    return retVal
    
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
    bd = ServerBreakDown()
    bd.start_preprocessing()
    print('DETECT HAND ---------------------')
    retVal = {'feedback': 'NONE', 'stage': 'DETECT_HAND' , 'breakdown_dict': bd}
    start =0
    if request.method == 'POST':
        
        params = request.json
        if (params == None ):
            params = request.args
        
        if (params != None):
            f = request.files['files']
            img_path = 'captured_img/det_'+str(time.time())+f.filename 
            f.save(img_path)
            img = Image.open(f.stream)
            open_cv_image = numpy.array(img)
            open_cv_image = np.rot90(open_cv_image,3)
            open_cv_image = cv2.imread(img_path, cv2.IMREAD_COLOR)
            preprocessed_img , img_cv = preprocess_img(open_cv_image)
            bd.end_preprocessing()
            bd.start_detHand()
            retVal = get_controlHand(preprocessed_img,retVal, open_cv_image, img_cv)
            bd.end_detHand()
            if retVal['stage'] == 'ROTATE':
                bd.start_getNutrition()
                opencv_img = cv2.cvtColor(numpy.array(img), cv2.COLOR_RGB2BGR)
                print('final response in detHand:\t',retVal)
                retVal = getNutrition(opencv_img, 'ROTATE')
                bd.end_getNutrition()
    bd.end_request()
    retVal['breakdown_dict'] = json.dumps(bd.breakdown_dict)
    print(bd.breakdown_dict)
    print('final response in detHand:\t',retVal)
    
    return jsonify(retVal)

@app.route('/rotate', methods=['POST'])
def rotate():
    bd = ServerBreakDown()
    bd.start_preprocessing()
    print("ROTATE ---------------------")

    request_start = time.time()
    f = request.files['files']
    img = Image.open(f.stream)
    img = img.rotate(-90)
    img.save('pic.jpg')
    opencv_img = cv2.cvtColor(numpy.array(img), cv2.COLOR_RGB2BGR)
    bd.end_preprocessing()
    
    bd.start_getNutrition()
    retVal = getNutrition(opencv_img, 'ROTATE')
    bd.end_getNutrition()
    
    bd.end_request()
    retVal['breakdown_dict'] = json.dumps(bd.breakdown_dict)
    print(bd.breakdown_dict)    
    
    return jsonify(retVal)

@app.route('/flip', methods=['POST'])
def flip():
    bd = ServerBreakDown()
    bd.start_preprocessing()
    print("FLIP ---------------------")
    request_start = time.time()
    f = request.files['files']
    img = Image.open(f.stream)
    img = img.rotate(-90)
    img.save('pic.jpg')
    opencv_img = cv2.cvtColor(numpy.array(img), cv2.COLOR_RGB2BGR)
    bd.end_preprocessing()
    
    bd.start_getNutrition()
    retVal = getNutrition(opencv_img, 'FLIP')
    bd.end_getNutrition()
    
    bd.end_request()
    retVal['breakdown_dict'] = json.dumps(bd.breakdown_dict)
    print(bd.breakdown_dict)
    
    return jsonify(retVal)

def getNutrition(image, curr_state):
    print("GET NUTRITION ---------------------")
    feedback_string = ""
    stage_name = ""
    start = time.time()
    found, cropped_image = detect_server(image)
    end = time.time()
    print('table detection time:\t', round(end-start, 3))

    if not found:
        print("No table")
        feedback_string = "No table"
        retVal = {'feedback': feedback_string, 'stage': curr_state}
        return retVal

    print("Found Table ---------")
    cropped_path = 'crop.jpg' # TODO: random name
    cv2.imwrite(cropped_path, cropped_image)
    
    start = time.time()
    success, resdict = ocr_server(cropped_path)
    end = time.time()
    
    print('OCR time:\t', round(end-start, 3))
    if success:
        print(resdict)
        retVal = {'feedback': json.dumps(resdict), 'stage': "DONE"}
    else:
        print("No nutrition facts table")
        retVal = {'feedback': "No nutrition facts table", 'stage': curr_state} 
    return retVal

if __name__=='__main__':
    # app.run() # production
    # app.run(debug=True) # for debugging purpose
    app.run(host='0.0.0.0', port=int(os.getenv('PORT', 8080)) ,debug = False, threaded = False)
