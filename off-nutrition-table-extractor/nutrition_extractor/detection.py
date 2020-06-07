# from PIL import Image
import argparse
import time
import cv2
import sys
import os.path
from detect_table_class import NutritionTableDetector
from crop import crop
from text_detection import text_detection, load_text_model
from process import *
from regex import *
from nutrient_list import *
from spacial_map import *

# threshold to check if the image has nutrition facts table
threshold = 0.96
test = 1

def load_model():
    """
    load trained weights for the model
    """
    global obj
    obj = NutritionTableDetector()
    #print ("Weights Loaded!")

def detect(img_path, debug, eval_):
    """
    @param img_path: Pathto the image for which labels to be extracted
    """

    #Start the time
    start_time = time.time()
    #Make the table detector class and predict the score

    image = cv2.imread(img_path)
    #cv2.imshow('img_ori', image)
    #cv2.waitKey(0)

    boxes, scores, classes, num  = obj.get_classification(image)
    #Get the dimensions of the image
    width = image.shape[1]
    height = image.shape[0]

    if eval_:
        time_taken = time.time() - start_time


    #Select the bounding box with most confident output
    ymin = boxes[0][0][0]*height
    xmin = boxes[0][0][1]*width
    ymax = boxes[0][0][2]*height
    xmax = boxes[0][0][3]*width

    print(xmin, ymin, xmax, ymax, scores[0][0])
    if scores[0][0]<threshold:
        print("No table!!")
        sys.exit(0)
    coords = (xmin, ymin, xmax, ymax)

    #Crop the image with the given bounding box
    cropped_image = crop(image, coords, "./data/result/output.jpg", 0, True)

    print("size: "+str(cropped_image.shape))
    print("Cropped image and save it to ./data/result/cropped.jpg")
    cv2.imwrite("./data/result/cropped.jpg", cropped_image)

    if eval_:
        return time_taken

    return "crop and return!\n"


def eval(args):
    img_dir = os.path.dirname(args.image)
    total = 0
    count = 0
    for img in os.listdir(img_dir):
        if img.endswith(".jpg") or img.endswith(".jpeg"):
            time_taken=float(detect(args.image, args.debug, args.eval))
            total += time_taken
            count += 1
            print("TableDetection = %.5fs" % time_taken)
    print("Average: %.5fs" % (total / float(count)))


#main function to test different functions independently
def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("-i", "--image", required=True, help="path to the input image")
    ap.add_argument("-d", "--debug", action='store_true', help="print some debug info")
    ap.add_argument("-e", "--eval", action='store_true', help="print elapsed time info")
    args = ap.parse_args()

    load_model()


    if (args.eval):
        eval(args)
    else:
        print(detect(args.image, args.debug, args.eval))

if __name__ == '__main__':
    main()
