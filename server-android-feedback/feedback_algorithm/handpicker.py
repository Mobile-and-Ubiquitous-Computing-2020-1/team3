class HandPicker():

    def __init__(self,confidence):
        self.confidence_hand = confidence

    def find_hand_box(self,stable_results):
        area_list = [self.get_area(result) for result in stable_results]
        max_box_idx = area_list.index(max(area_list))
        hand_box = stable_results[max_box_idx] # pick first if redundant
        max_area = area_list[max_box_idx]
        return hand_box, max_area
    
    def get_area(self,result_box):
        w = int(result_box[3])
        h = int(result_box[4])
        return w*h

    def filter_uncertainty(self,results):
        filtered_results = [result for result in results if result[5] > self.confidence_hand]
        return filtered_results