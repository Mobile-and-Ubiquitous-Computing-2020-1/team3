import time

class ServerBreakDown():
    def __init__(self):
        self.request_start = time.time()
        self.request_end = 0
        self.detHand_start = 0
        self.detHand_end = 0
        self.getNutrition_start = 0
        self.getNutrition_end = 0
        self.preprocessing_start = 0
        self.preprocessing_end = 0
        self.breakdown_dict = {
            'full_request':0,
            'detHand':0,
            'getNutrition':0,
            'preprocessing':0            
        }
    
    def end_request(self):
        self.request_end = time.time()
        self.breakdown_dict['full_request'] = self.request_end - self.request_start
    
    def start_detHand(self):
        self.detHand_start = time.time()
    
    def end_detHand(self):
        self.detHand_end = time.time()
        self.breakdown_dict['detHand'] = self.detHand_end - self.detHand_start
    
    def start_getNutrition(self):
        self.getNutrition_start = time.time()
    
    def end_getNutrition(self):
        self.getNutrition_end = time.time()
        self.breakdown_dict['getNutrition'] = self.getNutrition_end - self.getNutrition_start
    
    def start_preprocessing(self):
        self.preprocessing_start = time.time()
    
    def end_preprocessing(self):
        self.preprocessing_end = time.time()
        self.breakdown_dict['preprocessing'] = self.preprocessing_end - self.preprocessing_start
       
    def get_breakdown_dict(self):
        return self.breakdown_dict
        
        
        
        