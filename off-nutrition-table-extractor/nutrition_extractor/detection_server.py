import argparse
import cv2
import sys
import os.path
from detect_table_class import NutritionTableDetector
from crop import crop
from nutrient_list import *

# threshold to check if the image has nutrition facts table
threshold = 0.96
test = 1

def table_load_model():
    """
    load trained weights for the model
    """
    global obj
    obj = NutritionTableDetector()
    print ("Weights Loaded!")

def detect_server(image):
    """
    @param img_path: Pathto the image for which labels to be extracted
    """

    #Start the time
    #start_time = time.time()

    #image = cv2.imread(img_path)
    #cv2.imshow('img_ori', image)
    #cv2.waitKey(0)

    boxes, scores, classes, num  = obj.get_classification(image)
    #Get the dimensions of the image
    width = image.shape[1]
    height = image.shape[0]



    #Select the bounding box with most confident output
    ymin = boxes[0][0][0]*height
    xmin = boxes[0][0][1]*width
    ymax = boxes[0][0][2]*height
    xmax = boxes[0][0][3]*width

    print(xmin, ymin, xmax, ymax, scores[0][0])
    if scores[0][0]<threshold:
        print("No table!!")
        return False, 0
    coords = (xmin, ymin, xmax, ymax)

    #Crop the image with the given bounding box
    cropped_image = crop(image, coords, "", 0, False)

    print("size: "+str(cropped_image.shape))
    print("Cropped image and save it to ./data/result/cropped.jpg")


    return True, cropped_image

