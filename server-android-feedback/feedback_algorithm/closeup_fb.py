# stage 1 feedback

# hand find
# hand size control

import sys
sys.path.append('/root/Edwin/server-android-feedback/feedback_algorithm')
from handpicker import HandPicker

CLOSE = 'close'
FAR = 'far'
STAGE_CLEAR = 'Stage 1 clear'

CONFIDENCE_HAND = 0.6 # 데이터 실험해보고 바꿔야 함.
SIZE_TH = 0.05 # 실제 데이터 보거나 실험 보고 적당히 조절 해야함
SIZE_UPPER_BOUND = 0.1 + SIZE_TH 
SIZE_LOWER_BOUND = 0.1 - SIZE_TH

HAND_PICKER = HandPicker(CONFIDENCE_HAND)

def generate_closeup_fb(results, frame_size):
    assert isinstance(results, list)
    # frame_size assert 추가
    frame_area = frame_size[0]*frame_size[1]

    # confidence 기준으로 0.6 넘는 result 만 일단 걸러내야함.
    stable_results = HAND_PICKER.filter_uncertainty(results)
    
    if len(stable_results) == 0:
        return FAR
    else:
        return generate_sizecontrol_fb(stable_results, frame_area)

def generate_closeup_fb_wb(results, frame_size):
    assert isinstance(results, list)
    # frame_size assert 추가
    frame_area = frame_size[0]*frame_size[1]

    # confidence 기준으로 0.6 넘는 result 만 일단 걸러내야함.
    stable_results = HAND_PICKER.filter_uncertainty(results)
    
    if len(stable_results) == 0:
        return FAR, stable_results
    else:
        return generate_sizecontrol_fb(stable_results, frame_area), stable_results


def generate_sizecontrol_fb(stable_results, frame_area):
    hand_box, max_area = HAND_PICKER.find_hand_box(stable_results)
    upper_bound = frame_area*SIZE_UPPER_BOUND 
    lower_bound = frame_area*SIZE_LOWER_BOUND
    
    if max_area > upper_bound:
        return FAR
    elif max_area <  lower_bound:
        return CLOSE
    else:
        return STAGE_CLEAR # go to next stage # 바로 처리해도 될듯