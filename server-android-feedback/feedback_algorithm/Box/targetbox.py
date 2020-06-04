from point import Point
from boundary_info import BoundaryInfo

class TargetBox():
    def __init__(self,boundary_info:BoundaryInfo, frame_size):
        self.x_min = frame_size[0]*boundary_info.x_min
        self.x_max = frame_size[0]*boundary_info.x_max
        self.y_min = frame_size[1]*boundary_info.y_min
        self.y_max = frame_size[1]*boundary_info.y_max

        self.area = (self.x_max-self.x_min)*(self.y_max-self.y_min)
        self.center = Point(( self.x_min + self.x_max ) / 2, ( self.y_min + self.y_max ) / 2)
    
    def get_area(self):
        return self.area
    
    def is_contain(self,point):
        if self.x_max < point.x:
            return False
        elif self.x_min > point.x:
            return False
        elif self.y_min > point.y:
            return False
        elif self.y_max < point.y:
            return False
        else:
            return True
    
    def get_diff(self,point:Point):
        x_diff = point.x-self.center.x
        y_diff = point.y-self.center.y
        return Point(x_diff, y_diff)
    
    def get_center(self):
        return self.center
    
    #TODO:print string