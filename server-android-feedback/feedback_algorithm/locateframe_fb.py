# stage 2


import sys
sys.path.append('/root/Edwin/server-android-feedback/feedback_algorithm')
from handpicker import HandPicker
sys.path.append('/root/Edwin/server-android-feedback/feedback_algorithm/Box')
from Box.point import Point
from Box.boundary_info import BoundaryInfo
from handpicker import HandPicker
from Box.targetbox import TargetBox

CONFIDENCE_HAND = 0.6 # 데이터 실험해보고 바꿔야 함.

X_UPPER_BOUND = 0.5
X_LOWER_BOUND = 0.17
Y_UPPER_BOUND = 0.8
Y_LOWER_BOUND = 0.6

STAGE_CLEAR = 'location clear'
STAGE_BACK = 'go stage 1'
LEFT = 'LEFT'
RIGHT ='RIGHT'
SOUTH = 'DOWN'
NORTH = 'UP'

BOUNDARIES = BoundaryInfo(X_LOWER_BOUND, Y_LOWER_BOUND, X_UPPER_BOUND, Y_UPPER_BOUND)
HAND_PICKER = HandPicker(CONFIDENCE_HAND)

# need to check area of the target box :TODO

def generate_locateframe_fb(results, frame_size):
    assert isinstance(results, list)

    TARGET_BOX = TargetBox(BOUNDARIES,frame_size)
    # frame_size assert 추가

    # confidence 기준으로 0.6 넘는 result 만 일단 걸러내야함.
    stable_results = HAND_PICKER.filter_uncertainty(results)
    hand_box, max_area = HAND_PICKER.find_hand_box(stable_results)
    
    if len(stable_results) == 0:
        return STAGE_BACK
    elif islocate(hand_box, TARGET_BOX):
        return STAGE_CLEAR
    else:
        return get_NEWS_fb(hand_box, TARGET_BOX)

def islocate(handbox, targetbox:TargetBox):
    hb_center = Point( handbox[1], handbox[2] )
    return targetbox.is_contain(hb_center)

def get_NEWS_fb(handbox, targetbox:TargetBox):
        hb_center = Point( handbox[1], handbox[2])
        diff = targetbox.get_diff(hb_center)
        if abs(diff.x) > abs(diff.y):
            return get_horizontal_fb(diff.x)
        return get_vertical_fb(diff.y)

def get_horizontal_fb(diff_x):
    if diff_x > 0:
        return RIGHT
    return LEFT

def get_vertical_fb(diff_y):
    if diff_y > 0:
        return NORTH
    return SOUTH
